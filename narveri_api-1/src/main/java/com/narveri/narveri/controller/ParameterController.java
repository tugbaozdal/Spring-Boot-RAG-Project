package com.narveri.narveri.controller;


import com.narveri.narveri.dto.BaseResponseDto;
import com.narveri.narveri.dto.ParameterDto;
import com.narveri.narveri.enums.ResponseMessageEnum;
import com.narveri.narveri.mapper.ParameterMapper;
import com.narveri.narveri.model.Parameter;
import com.narveri.narveri.service.ParameterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Parameter-Api")
@Slf4j
@RestController
@RequestMapping("/api")
public class ParameterController {

    @Autowired
    private ParameterService service;

    @Autowired
    private ParameterMapper mapper;

    @GetMapping("/admin/parameter")
    public ResponseEntity<Page<ParameterDto>> search(@RequestParam(value = "searchKey", required = false) String searchKey, Pageable pageable) {
        Page<Parameter> searchResult = service.search(searchKey, pageable);
        return ResponseEntity.ok(new PageImpl<>(mapper.toDto(searchResult.getContent()), pageable, searchResult.getTotalElements()));
    }

    @PostMapping("/admin/parameter")
    public ResponseEntity<ParameterDto> create(@RequestBody ParameterDto parameterDto) {
        return ResponseEntity.ok(mapper.toDto(service.create(parameterDto)));
    }

    @GetMapping("/admin/parameter/{id}")
    public ResponseEntity<ParameterDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @PutMapping("/admin/parameter/{id}")
    public ResponseEntity<ParameterDto> update(@PathVariable Long id, @RequestBody ParameterDto parameterDto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, parameterDto)));
    }

    @DeleteMapping("/admin/parameter/{id}")
    public ResponseEntity<BaseResponseDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponseDto.builder().message(ResponseMessageEnum.BACK_PARAMETER_MSG_004.messageDetail()).build());
    }
}