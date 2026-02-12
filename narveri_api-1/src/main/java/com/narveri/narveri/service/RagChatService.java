package com.narveri.narveri.service;

import reactor.core.publisher.Flux;

public interface RagChatService {

    String answerQuestion( String question, int topK);
    Flux<String> answerQuestionStream(Long userId, String question, int topK);

    void  embedAndStoreDocument(Long docID);
}
