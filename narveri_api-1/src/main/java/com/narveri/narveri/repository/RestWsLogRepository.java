package com.narveri.narveri.repository;

import com.narveri.narveri.model.RestWsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestWsLogRepository extends JpaRepository<RestWsLog, Long> {
}
