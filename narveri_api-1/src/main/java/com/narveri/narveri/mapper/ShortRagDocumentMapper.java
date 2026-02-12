package com.narveri.narveri.mapper;

import com.narveri.narveri.dto.ShortRagDocumentDto;
import com.narveri.narveri.model.RagDocuments;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "Spring")
public interface ShortRagDocumentMapper extends EntityMapper<ShortRagDocumentDto, RagDocuments >{

}
