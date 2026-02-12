package com.narveri.narveri.service;


import com.narveri.narveri.dto.ParameterDto;
import com.narveri.narveri.model.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParameterService {

    List<Parameter> getAllParameters();

    Page<Parameter> search(String searchKey, Pageable pageable);

    List<Parameter> findParameterByKeyContains(String searchKey);

    Parameter create(ParameterDto parameterDto);

    Parameter findById(Long id);

    String getValueByKey(String key);

    Parameter update(Long id, ParameterDto parameterDto);

    void delete(Long id);


}
