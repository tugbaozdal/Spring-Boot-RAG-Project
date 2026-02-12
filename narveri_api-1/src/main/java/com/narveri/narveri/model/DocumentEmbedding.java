package com.narveri.narveri.model;

import com.narveri.narveri.config.FloatArrayToVectorConverter;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "rag_documents_embeddings")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SQLDelete(sql = "UPDATE rag_documents_embeddings SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class DocumentEmbedding extends AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "embedding",
            columnDefinition = "vector(768)"
    )
    @ColumnTransformer(
            write = "?::vector"
    )
    @Convert(converter = FloatArrayToVectorConverter.class)
    @JdbcTypeCode(SqlTypes.OTHER)
    private float[] embedding;


    @Column(name = "start_index")
    private int startIndex;

    @Column(name = "end_index")
    private int endIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rag_document_id")
    private RagDocuments ragDocument;
}
