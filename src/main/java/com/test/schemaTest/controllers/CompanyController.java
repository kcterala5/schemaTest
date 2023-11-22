package com.test.schemaTest.controllers;

import com.test.schemaTest.service.CompanyDataService;
import com.test.schemaTest.service.InitService;
import com.test.schemaTest.views.CompanyDataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyDataService companyDataService;

    public CompanyController(final CompanyDataService companyDataService) {
        this.companyDataService = companyDataService;
    }

    @GetMapping("/company/{panNumber}")
    public ResponseEntity<CompanyDataView> getCompanyDataView(@PathVariable String panNumber) {
        long start = System.currentTimeMillis();
        CompanyDataView companyDataView = companyDataService.getCompanyDataView(panNumber);
        long latency = System.currentTimeMillis() - start;
        logger.info("Java Latency : {}", latency);

        return ResponseEntity.ok().body(companyDataView);
    }


    @GetMapping("/company/sql/{panNumber}")
    public ResponseEntity<CompanyDataView> getCompanyDataViewFromSQL(@PathVariable String panNumber) {
        long start = System.currentTimeMillis();
        CompanyDataView companyDataView = companyDataService.getCompanyDataViewFromSQL(panNumber);
        long latency = System.currentTimeMillis() - start;
        logger.info("SQL Latency : {}", latency);
        return ResponseEntity.ok().body(companyDataView);
    }
}
