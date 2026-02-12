package com.narveri.narveri.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

import javax.jws.soap.SOAPBinding;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "rag_documents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@SQLDelete(sql = "UPDATE rag_documents SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class RagDocuments  extends AbstractModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", name = "document_url",nullable = false)
    private String documentUrl;

    @Column(columnDefinition = "TEXT", name = "content",nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", name = "title",nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


}
