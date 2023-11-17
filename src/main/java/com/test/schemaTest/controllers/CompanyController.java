package com.test.schemaTest.controllers;

import com.test.schemaTest.service.CompanyDataService;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final CompanyDataService companyDataService;

    public CompanyController(final CompanyDataService companyDataService) {
        this.companyDataService = companyDataService;
    }

    @GetMapping("/company/{panNumber}")
    public ResponseEntity<CompanyDataView> getCompanyDataView(@PathVariable String panNumber) {
        CompanyDataView companyDataView = companyDataService.getCompanyDataView(panNumber);
        return ResponseEntity.ok().body(companyDataView);
    }


    @GetMapping("/company/sql/{panNumber}")
    public ResponseEntity<CompanyDataView> getCompanyDataViewFromSQL(@PathVariable String panNumber) {
        CompanyDataView companyDataView = companyDataService.getCompanyDataViewFromSQL(panNumber);
        return ResponseEntity.ok().body(companyDataView);
    }
}
