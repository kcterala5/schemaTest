package com.test.schemaTest.controllers;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.service.BankService;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BankController {

    private final BankService bankService;

    public BankController(final BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/bank/{bankId}/generate")
    public ResponseEntity<List<CompanyData>> generateCreditRatingScoreForBank(@PathVariable int bankId) {
        List<CompanyData> companyDataViewList = bankService.generateCreditRatingScoreForBank(bankId);
        return ResponseEntity.ok().body(companyDataViewList);
    }

    @GetMapping("/bank/{bankId}")
    public ResponseEntity<Map<String, List<CompanyDataView>>> getCreditRatingForBank(@PathVariable int bankId) {
        Map<String, List<CompanyDataView>> companyDataViewList = bankService.getCreditRatingForBankCustomers(bankId);
        return ResponseEntity.ok().body(companyDataViewList);
    }
}
