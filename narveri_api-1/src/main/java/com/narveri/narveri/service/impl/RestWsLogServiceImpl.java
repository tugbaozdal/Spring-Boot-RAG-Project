package com.narveri.narveri.service.impl;

import com.narveri.narveri.model.RestWsLog;
import com.narveri.narveri.repository.RestWsLogRepository;
import com.narveri.narveri.service.RestWsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("restWsLogService")
public class RestWsLogServiceImpl implements RestWsLogService {

    @Autowired
    private RestWsLogRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(RestWsLog restWsLog) {
        repository.save(restWsLog);
    }
}
