package com.narveri.narveri.service.impl;

import com.narveri.narveri.model.RagDocuments;
import com.narveri.narveri.repository.DocumentEmbeddingRepository;

import com.narveri.narveri.service.EmbeddingService;
import com.narveri.narveri.service.RagChatService;
import com.narveri.narveri.service.RagDocumentService;
import com.narveri.narveri.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
@Slf4j
@AllArgsConstructor
@Service
public class RagChatServiceImpl implements RagChatService {


    private final ChatModel chatModel;

    private final DocumentEmbeddingRepository embeddingRepository;
    private final EmbeddingService embeddingService;
    private RagDocumentService ragDocumentService;
    private UserService userService;


    @Override
    public String answerQuestion(String question, int topK) {

        Long userId = userService.getCurrentUser().getId();
        // 1) Embed question
        float[] qEmb = embeddingService.generateEmbedding(question);
        log.info("Embedding size = {}", qEmb.length);

        String vectorString = toPgvectorString(qEmb);

        // 2) Vector search
        List<Object[]> results =
                embeddingRepository.findNearestByEmbedding(userId, vectorString, topK);

        // 3) Build context
        StringBuilder context = new StringBuilder();

        for (Object[] row : results) {
            String chunk = (String) row[0]; // ✅ now correct
            String title = (String) row[1]; // ✅ now correct

            context.append("Title: ")
                    .append(title != null ? title : "Untitled")
                    .append("\n")
                    .append(chunk)
                    .append("\n---\n");
        }

        // 4) Prompt
        String systemText = """
        You are a helpful assistant.
        Use ONLY the information in the given context.
        If the answer is not in the context, say you don't know.
        """;

        String userText = "Context:\n" + context + "\n\nQuestion:\n" + question;

        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage(systemText),
                        new UserMessage(userText)
                )
        );

        // 5) Chat
        ChatResponse response = chatModel.call(prompt);

        return response
                .getResult()
                .getOutput()
                .getText();
    }


    @Override
    public Flux<String> answerQuestionStream(Long userId, String question, int topK) {

        return Mono.fromCallable(() -> {
                    // 1) DB + embedding işlemleri BLOCKING, o yüzden boundedElastic'te
                    float[] qEmb = embeddingService.generateEmbedding(question);
                    String vectorString = toPgvectorString(qEmb);

                    List<Object[]> results =
                            embeddingRepository.findNearestByEmbedding(userId, vectorString, topK);

                    StringBuilder context = new StringBuilder();
                    for (Object[] row : results) {
                        String chunk = (String) row[0];
                        String title = (String) row[1];

                        context.append("Title: ")
                                .append(title == null ? "Untitled" : title)
                                .append("\n");
                        context.append(chunk).append("\n---\n");
                    }

                    return context.toString();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(ctx -> {

                    String systemText = """
                            You are a helpful assistant.
                            Use ONLY the information in the given context.
                            If the answer is not in the context, say you don't know.
                            """;

                    String userText = "Context:\n" + ctx + "\n\nQuestion:\n" + question;

                    // ❗ ChatModel, StreamingChatModel'i de implement ettiği için
                    // stream(Message...) ile direkt Flux<String> alabiliyoruz.
                    return chatModel.stream(
                            new SystemMessage(systemText),
                            new UserMessage(userText)
                    );
                });
    }

    @Override
    public void embedAndStoreDocument(Long docID) {
        RagDocuments ragDocuments = ragDocumentService.findByCurrentUserId(docID);
        embeddingService.saveEmbedding(ragDocuments);
    }

    private String toPgvectorString(float[] emb) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < emb.length; i++) {
            sb.append(emb[i]);
            if (i < emb.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}