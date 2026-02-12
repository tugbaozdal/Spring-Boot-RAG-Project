package com.narveri.narveri.service.impl;
import com.narveri.narveri.constant.PrivilegeConstant;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.model.RagDocuments;
import com.narveri.narveri.model.User;
import com.narveri.narveri.repository.RagDocumentRepository;
import com.narveri.narveri.service.FileUploadService;
import com.narveri.narveri.service.RagDocumentService;
import com.narveri.narveri.service.UserService;
import com.uploadcare.upload.UploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RagDocumentServiceImpl implements RagDocumentService {

    @Autowired
   private RagDocumentRepository  repository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService  fileUploadService;

    @Override
    public RagDocuments getRagDocumentDtoById(Long id) {
        return null;
    }

    @Override
    public RagDocuments save(MultipartFile file,String title) {
        try {
            User user = userService.getCurrentUser();

            if (ObjectUtils.isEmpty(file)) {
                throw new BusinessException(ResponseMessageEnum.BACK_RAGDOCUMENT_MSG_001);
            }
            String url=fileUploadService.uploadFile(file);

            String text=extractText(file);

            if(StringUtils.isEmpty(text)){
                throw new BusinessException(ResponseMessageEnum.BACK_RAGDOCUMENT_MSG_002);
            }
            log.info("tugba sena{}",url);


            RagDocuments ragDocuments = new RagDocuments();
            ragDocuments.setTitle(title);
            ragDocuments.setDocumentUrl(url);
            ragDocuments.setContent(text);
            ragDocuments.setUser(user);
          return   repository.save(ragDocuments);


        } catch (UploadFailureException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<RagDocuments> getForCurrentUser() {
        User user = userService.getCurrentUser();
        return repository.getForCurrentUser(user.getId());
    }

    @Override
    public RagDocuments findById(Long id) {
        return repository.findById(id).orElseThrow(()->new BusinessException(ResponseMessageEnum.BACK_RAGDOCUMENT_MSG_003));
    }

    @Override
    public RagDocuments findByCurrentUserId(Long id) {
        User user = userService.getCurrentUser();

        return repository.findByDocumentIdAndUserId(id,user.getId()).orElseThrow(()->new BusinessException(ResponseMessageEnum.BACK_USER_MSG_009));
    }



    @Override
    public void deleteByUser(Long id) {
        RagDocuments ragDocuments = findById(id);
        deleteFromUploadcare(ragDocuments.getDocumentUrl());
        repository.deleteById(ragDocuments.getId());
    }

    @Override
    public void deleteByAdmin(Long id) {
        RagDocuments ragDocuments = findById(id);
        deleteFromUploadcare(ragDocuments.getDocumentUrl());
        repository.deleteById(ragDocuments.getId());
    }

    @Override
    public Page<RagDocuments> search(String searchText, Pageable pageable) {
        return repository.search(null,searchText, pageable);
    }

    @Override
    public Page<RagDocuments> searchByUser(String searchText, Pageable pageable) {
        Long userId = userService.getCurrentUser().getId();
        return repository.search(userId, searchText, pageable);
    }


    private String extractText(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }


    private void deleteFromUploadcare(String url){
        try {
            fileUploadService.deleteFile(url);
        }catch (Exception e){

        }

    }
    }

