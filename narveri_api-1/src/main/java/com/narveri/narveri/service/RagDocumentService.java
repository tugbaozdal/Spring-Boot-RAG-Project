package com.narveri.narveri.service;


import com.narveri.narveri.dto.RagDocumentDto;
import com.narveri.narveri.model.RagDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RagDocumentService {

    RagDocuments getRagDocumentDtoById(Long id);

    RagDocuments save(MultipartFile file,   String title);
    List<RagDocuments> getForCurrentUser( );


    RagDocuments findById(Long id);
    RagDocuments findByCurrentUserId(Long id);

    void deleteByUser(Long id);
    void deleteByAdmin(Long id);

    Page<RagDocuments> search(String searchText, Pageable pageable);
    Page<RagDocuments> searchByUser(String searchText, Pageable pageable);  // user

}
