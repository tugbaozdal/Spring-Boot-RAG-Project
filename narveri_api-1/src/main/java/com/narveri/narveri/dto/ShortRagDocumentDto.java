package com.narveri.narveri.dto;

import lombok.Data;

@Data
public class ShortRagDocumentDto extends BaseDto {
    private Long id;

    private String documentUrl;

    private String content;

    private String title;
}
