package com.narveri.narveri.config;

import com.uploadcare.api.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadcareConfig {

    // application.yml'den "uploadcare" altındaki "public-key" değerini alır
    @Value("${uploadcare.public-key}")
    private String publicKey;

    // application.yml'den "uploadcare" altındaki "secret-key" değerini alır
    @Value("${uploadcare.secret-key}")
    private String secretKey;


    public Client UploadFileConfig() {
        // Bu Client nesnesi artık yml dosyasındaki anahtarları kullanacaktır.
        return new Client(publicKey, secretKey);
    }
}

