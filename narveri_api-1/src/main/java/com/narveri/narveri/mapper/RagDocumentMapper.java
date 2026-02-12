package com.narveri.narveri.mapper;

import com.narveri.narveri.dto.RagDocumentDto;
import com.narveri.narveri.model.RagDocuments;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface RagDocumentMapper extends EntityMapper<RagDocumentDto, RagDocuments> {


}
