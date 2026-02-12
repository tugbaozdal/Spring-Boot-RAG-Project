package com.narveri.narveri.repository;

import com.narveri.narveri.model.RagDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RagDocumentRepository extends JpaRepository <RagDocuments, Long> {

    @Query("select rag from RagDocuments rag where rag.user.id=:userId and rag.deleted=false ")
    List<RagDocuments> getForCurrentUser(Long userId);

    @Query("select rag from RagDocuments rag where rag.id=:documentId and rag.user.id=:userId and rag.deleted=false")
    Optional<RagDocuments> findByDocumentIdAndUserId(Long documentId, Long userId);


//        @Query("""
//        select d from RagDocuments d
//        where ('' = :searchText or :searchText is null
//               or lower(d.title) like lower(concat('%', :searchText, '%'))
//               or lower(d.content) like lower(concat('%', :searchText, '%'))
//        )
//    """)
//        Page<RagDocuments> search(String searchText, Pageable pageable);

    @Query("""
    select d from RagDocuments d
    where (:userId is null or d.user.id = :userId)
      and ('' = :searchText or :searchText is null
           or lower(d.title) like lower(concat('%', :searchText, '%'))
           or lower(d.content) like lower(concat('%', :searchText, '%'))
      )
""")
    Page<RagDocuments> search(Long userId, String searchText, Pageable pageable);

}


