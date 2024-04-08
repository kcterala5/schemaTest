package com.test.schemaTest.controllers;

import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.service.CompanyDataService;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final CompanyDataService companyDataService;
    private final CompanyDataRepository companyDataRepository;

    public CompanyController(final CompanyDataService companyDataService, final CompanyDataRepository companyDataRepository) {
        this.companyDataService = companyDataService;
        this.companyDataRepository = companyDataRepository;
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

    @GetMapping("/company/add/{annual_sales}/{industry_type}")
    public ResponseEntity<Void> updateMappings(@PathVariable String annual_sales, @PathVariable String industry_type) {
        companyDataRepository.updateScoreMapBySalesAndIndustryType(annual_sales, industry_type);
        return ResponseEntity.ok().build();
    }
}
