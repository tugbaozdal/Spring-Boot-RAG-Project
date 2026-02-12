package com.narveri.narveri.service.impl;

import com.narveri.narveri.dto.ParameterDto;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.exception.BusinessException;
import com.narveri.narveri.mapper.ParameterMapper;
import com.narveri.narveri.model.Parameter;
import com.narveri.narveri.repository.ParameterRepository;
import com.narveri.narveri.service.ParameterService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Transactional
@Service("parameterService")
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterRepository repository;

    @Autowired
    private ParameterMapper mapper;

    @Override
    public List<Parameter> getAllParameters() {
        return repository.findAll();
    }

    @Override
    public Page<Parameter> search(String searchKey, Pageable pageable) {
        return repository.search(searchKey, pageable);
    }

    @Override
    public List<Parameter> findParameterByKeyContains(String searchKey) {
        return repository.findParameterByKeyContainsOrderByIdAsc(searchKey);
    }

    @Override
    public Parameter create(ParameterDto parameterDto) {
        Parameter parameter = repository.findByKey(parameterDto.getKey());
        if (parameter != null) {
            throw new BusinessException(ResponseMessageEnum.BACK_PARAMETER_MSG_005);
        }
        parameter = repository.save(mapper.toEntity(parameterDto));
        return parameter;
    }

    @Override
    public Parameter findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ResponseMessageEnum.BACK_PARAMETER_MSG_001));
    }

    @Override

    public String getValueByKey(String key) {
        Parameter parameter = repository.findByKey(key);
        return parameter != null ? parameter.getValue() : null;
    }

    @Override
    public Parameter update(Long id, ParameterDto parameterDto) {
        Parameter parameter = findById(id);
        if (StringUtils.isNotEmpty(parameterDto.getKey())) {
            parameter.setKey(parameterDto.getKey());
        }
        if (StringUtils.isNotEmpty(parameterDto.getValue())) {
            parameter.setValue(parameterDto.getValue());
        }
        if (StringUtils.isNotEmpty(parameterDto.getDescription())) {
            parameter.setDescription(parameterDto.getDescription());
        }
        repository.save(parameter);
        return parameter;
    }

    @Override
    public void delete(Long id) {
        Parameter parameter = findById(id);
        repository.deleteById(parameter.getId());
    }
}