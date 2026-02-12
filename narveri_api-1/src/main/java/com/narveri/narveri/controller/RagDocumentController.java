package com.narveri.narveri.controller;
import com.narveri.narveri.dto.BaseResponseDto;
import com.narveri.narveri.dto.RagDocumentDto;
import com.narveri.narveri.dto.ShortRagDocumentDto;
import com.narveri.narveri.model.RagDocuments;
import com.narveri.narveri.model.User;
import com.narveri.narveri.service.RagDocumentService;
import com.narveri.narveri.mapper.RagDocumentMapper;
import com.narveri.narveri.mapper.ShortRagDocumentMapper;
import com.narveri.narveri.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
public class RagDocumentController {

    @Autowired
    private RagDocumentService service;


    @Autowired
    private RagDocumentMapper mapper;

    @Autowired
    private ShortRagDocumentMapper shortRagDocumentMapper;

    @PostMapping("/rag-document")
    public ResponseEntity<RagDocumentDto> save(@RequestParam MultipartFile file, @RequestParam String title) {
        return ResponseEntity.ok(mapper.toDto(service.save(file, title)));
    }

    @GetMapping("/rag-document/current-user")
    public ResponseEntity<List<ShortRagDocumentDto>> getForCurrentUser() {
        return ResponseEntity.ok(shortRagDocumentMapper.toDto(service.getForCurrentUser()));
    }


    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity<BaseResponseDto> deleteByAdmin(@PathVariable Long id) {
        service.deleteByAdmin(id);
        return ResponseEntity.ok(BaseResponseDto.builder().message("Döküman silme işlemi başarılı olarak tamamlanmıştır.").build());
    }

    @DeleteMapping(path = "/rag-document/{id}")
    public ResponseEntity<BaseResponseDto> deleteByUser(@PathVariable Long id) {
        service.deleteByUser(id);
        return ResponseEntity.ok(BaseResponseDto.builder().message("Döküman silme işlemi başarılı olarak tamamlanmıştır.").build());
    }


    @GetMapping(path = "/admin/rag-document/{id}")
    public ResponseEntity<RagDocumentDto> findByIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @GetMapping(path = "/rag-document/{id}")
    public ResponseEntity<RagDocumentDto> findByIdUser(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findByCurrentUserId(id)));
    }
    @GetMapping("/admin/rag-document/search")
    public Page<RagDocumentDto> search(@RequestParam(value = "searchText", required = false) String searchText, Pageable pageable) {
        Page<RagDocuments> result = service.search(searchText, pageable);
        return new PageImpl<>(mapper.toDto(result.getContent()), pageable, result.getTotalElements()
        );
    }
    @GetMapping("/user/rag-document/search")
    public Page<RagDocumentDto> searchByUser(@RequestParam(value="searchText",required = false) String searchText, Pageable pageable) {
        // 2️⃣ Service aracılığıyla sadece bu kullanıcıya ait belgelerde arama yap
        Page<RagDocuments> result = service.searchByUser(searchText, pageable);
        return new PageImpl<>(mapper.toDto(result.getContent()),pageable, result.getTotalElements());
    }
}

