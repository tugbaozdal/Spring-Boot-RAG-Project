package com.narveri.narveri.repository;

import com.narveri.narveri.model.DocumentEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentEmbeddingRepository extends JpaRepository<DocumentEmbedding, Long> {
    @Query(value = """
        SELECT * FROM rag_documents_embeddings e
        JOIN rag_documents d ON d.id = e.rag_document_id
        WHERE d.user_id = :userId
        ORDER BY e.embedding <-> CAST(:queryEmbedding AS vector)
        LIMIT 5
    """, nativeQuery = true)
    List<DocumentEmbedding> semanticSearch(Long userId, String queryEmbedding);

    @Query(
            value = """
    SELECT
      SUBSTRING(d.content FROM e.start_index + 1 FOR (e.end_index - e.start_index)) AS chunk,
      d.title
    FROM rag_documents_embeddings e
    JOIN rag_documents d ON d.id = e.rag_document_id
    WHERE d.user_id = :userId
    ORDER BY e.embedding <-> CAST(:queryEmbedding AS vector)
    LIMIT :topK
    """,
            nativeQuery = true
    )
    List<Object[]> findNearestByEmbedding(
            @Param("userId") Long userId,
            @Param("queryEmbedding") String queryEmbedding,
            @Param("topK") int topK
    );


    @Query(
            value = """
  INSERT INTO document (embedding)
  VALUES (CAST(:embedding AS vector))
  """,
            nativeQuery = true
    )
    void saveEmbedding(@Param("embedding") String embedding);


    @Modifying
    @Query(
            value = """
        INSERT INTO rag_documents_embeddings
        (embedding, start_index, end_index, rag_document_id)
        VALUES (CAST(:embedding AS vector), :start, :end, :docId)
        """,
            nativeQuery = true
    )
    void insertEmbedding(
            @Param("embedding") String embedding,
            @Param("start") int start,
            @Param("end") int end,
            @Param("docId") Long docId
    );

}