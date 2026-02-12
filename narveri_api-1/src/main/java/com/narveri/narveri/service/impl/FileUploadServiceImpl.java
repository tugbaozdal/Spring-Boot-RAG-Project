package com.narveri.narveri.service.impl;

import com.narveri.narveri.config.UploadcareConfig;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.service.FileUploadService;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private UploadcareConfig fileConfig;

    @Value("${app.upload.max-size-mb}")
    private int maxFileSizeMb;

    public  String uploadFile(MultipartFile file) {
        log.info("Uploading file: {}",file.getOriginalFilename());
        if(file.isEmpty()){
            throw  new BusinessException(ResponseMessageEnum.BACK_FILE_MSG_003);
        }
        try {
            checkFileSize(file);
            Uploader uploader = new FileUploader(fileConfig.UploadFileConfig(), file.getBytes(), file.getOriginalFilename());
            return uploader.upload().getOriginalFileUrl().toString();
        }catch (UploadFailureException e){
            throw  new BusinessException(ResponseMessageEnum.BACK_FILE_MSG_001+e.getMessage());
        } catch (IOException e) {
            throw  new BusinessException(ResponseMessageEnum.BACK_FILE_MSG_001+e.getMessage());
        }
    }


    public void deleteFile(String url) {
        try {
            String fileId = getFileId(url);
            log.info("Deleting file, fileId: {}", fileId);
            fileConfig.UploadFileConfig().deleteFile(fileId);
            log.info("File deleted successfully, fileId: {}", fileId);
        } catch (Exception e) {
            if (e.getMessage().contains("Page not found")) {
                log.warn("File already deleted or not found, URL: {}", url);
            } else {
                throw new BusinessException(ResponseMessageEnum.BACK_FILE_MSG_002 + e.getMessage());
            }
        }
    }


    private String getFileId(String url){
        try {
            // URL: https://ucarecdn.com/<ID>/filename.pdf
            String[] parts = url.split("/");
            return parts[3]; // <ID> burada
        } catch (Exception e) {
            log.error("Dosya ID çıkarılamadı, URL: {}", url, e);
            throw new BusinessException("Dosya ID alınamadı.");
        }
    }

    private  void checkFileSize(MultipartFile file){
        BigDecimal bigDecimal=BigDecimal.valueOf(file.getSize())
                .divide(BigDecimal.valueOf(1024)).divide(BigDecimal.valueOf(1024));
        log.info("fileSize: " + bigDecimal);
        if(bigDecimal.compareTo(BigDecimal.valueOf(maxFileSizeMb)) > 0){
            throw  new BusinessException(ResponseMessageEnum.BACK_FILE_MSG_003);
        }

    }
}
