package com.narveri.narveri.service;

import com.narveri.narveri.model.RagDocuments;

public interface EmbeddingService {
    void saveEmbedding(RagDocuments doc);
   float[] generateEmbedding(String text);
}
