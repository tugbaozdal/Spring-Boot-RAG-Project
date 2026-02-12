package com.narveri.narveri.service.impl;

import com.narveri.narveri.dto.Chunk;

import com.narveri.narveri.model.RagDocuments;
import com.narveri.narveri.repository.DocumentEmbeddingRepository;
import com.narveri.narveri.service.EmbeddingService;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel; // Kritik import bu

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class EmbeddingServiceImpl implements EmbeddingService {

//    private final OpenAiApi openAiApi;
// Artık OpenAiApi yok! Spring AI'nın genel EmbeddingModel'ini kullanıyoruz.
// application.yml'daki ayarlara göre otomatik olarak Gemini çalışacak.
    private final EmbeddingModel embeddingModel;
    private final DocumentEmbeddingRepository repository;

//    @Override
//    public void saveEmbedding(RagDocuments doc) {
//        float[] emb = generateEmbedding(doc.getContent());
//
//        DocumentEmbedding de = new DocumentEmbedding();
//        de.setEmbedding(emb);
//        de.setRagDocument(doc);
//        repository.save(de);
//    }
    @Transactional
    @Modifying
    @Override
    public void saveEmbedding(RagDocuments doc) {
        List<Chunk> chunks = chunkText(doc.getContent(), 2000, 200);

        for (Chunk c : chunks) {
//            float[] emb = generateEmbedding(c.getText());
//
//            DocumentEmbedding e = new DocumentEmbedding();
//            e.setEmbedding(emb);
//            e.setStartIndex(c.getStart());
//            e.setEndIndex(c.getEnd());
//            e.setRagDocument(doc);
//
//           repository.save(e);

            float[] emb = generateEmbedding(c.getText());
            log.info("Infor {}",emb.length);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < emb.length; i++) {
                sb.append(emb[i]);
                if (i < emb.length - 1) sb.append(",");
            }
            sb.append("]");

            String vector = sb.toString();

            repository.insertEmbedding(
                    vector,
                    c.getStart(),
                    c.getEnd(),
                    doc.getId()
            );


        }

    }


    public List<Chunk> chunkText(String text, int chunkSize, int overlap) {
        List<Chunk> chunks = new ArrayList<>();

        int length = text.length();
        int step = chunkSize - overlap;

        for (int i = 0; i < length; i += step) {
            int end = Math.min(length, i + chunkSize);
            String part = text.substring(i, end);

            chunks.add(new Chunk(part, i, end));
        }

        return chunks;
    }

    // İŞTE EN BÜYÜK DEĞİŞİKLİK BURADA:
    public float[] generateEmbedding(String text) {
        // Eski kodundaki Request/Response/Body kontrollerine gerek yok.
        // Spring AI bunu tek satırda halleder.
        // application.yml dosyamızdaki 'text-embedding-004' modelini kullanır.
        return embeddingModel.embed(text);
    }


//    public float[] generateEmbedding(String text) {
//        EmbeddingRequest<String> request = new EmbeddingRequest<>(text);
//        ResponseEntity<EmbeddingList<Embedding>> resp = openAiApi.embeddings(request);
//        EmbeddingList<Embedding> embeddingList = resp.getBody();
//
//        if (embeddingList == null || embeddingList.data().isEmpty()) {
//            throw  new BusinessException("");
//        }
//
//        Embedding embedding = embeddingList.data().getFirst();
//
//        // Spring AI Embedding returns float[] as output
//        return embedding.embedding();
//    }

}
