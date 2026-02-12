package com.narveri.narveri.mapper;

import com.narveri.narveri.dto.ParameterDto;
import com.narveri.narveri.model.Parameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParameterMapper extends EntityMapper<ParameterDto, Parameter> {


}
