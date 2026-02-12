package com.narveri.narveri;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT1H")
@EnableAsync
public class NarveriApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NarveriApiApplication.class, args);
    }

}

