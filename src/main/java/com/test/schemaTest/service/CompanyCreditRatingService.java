package com.test.schemaTest.service;

import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyCreditRatingGenerationRequest;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.models.CreditScore;
import com.test.schemaTest.pojos.CompanyCreditRatingGenerationFactor;
import com.test.schemaTest.pojos.CompanyCreditRatingGenerationStatus;
import com.test.schemaTest.repository.CompanyCreditRatingGenerationRequestRepository;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.repository.CompanyRepository;
import com.test.schemaTest.utils.CompanyDataUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CompanyCreditRatingService {

    private final CompanyCreditRatingGenerationRequestRepository companyCreditGenerationRequestRepository;
    private final CompanyDataRepository companyDataRepository;
    private final CompanyRepository companyRepository;
    private final ExecutorService executorService;
    private static final CompanyDataUtils companyDataUtils = new CompanyDataUtils();

    public CompanyCreditRatingService(final CompanyCreditRatingGenerationRequestRepository companyCreditGenerationRequestRepository,
                                      final CompanyRepository companyRepository,
                                      final CompanyDataRepository companyDataRepository) {
        this.companyCreditGenerationRequestRepository = companyCreditGenerationRequestRepository;
        this.companyRepository = companyRepository;
        this.companyDataRepository = companyDataRepository;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public void triggerAsyncCreditRatingGeneration(final int companyId) {
        final CompanyCreditRatingGenerationRequest companyCreditRatingGenerationRequest = companyCreditGenerationRequestRepository.findRequestByCompanyId(companyId).get();
        companyCreditRatingGenerationRequest.setStatus(CompanyCreditRatingGenerationStatus.IN_PROGRESS);
        companyCreditGenerationRequestRepository.save(companyCreditRatingGenerationRequest);
        executorService.submit(() -> generateCompanyCreditRating(companyCreditRatingGenerationRequest));
    }

    public void generateCompanyCreditRating(final CompanyCreditRatingGenerationRequest companyCreditRatingGenerationRequest) {
        final CreditScore creditScore = generateCreditScore(companyCreditRatingGenerationRequest.getCompany());
        final CompanyData companyData = companyDataUtils.getCompanyDataFromCreditScore(creditScore);
        final String annualSales = companyData.getAnnualSales();
        final String industryType = companyData.getIndustryType();
        companyDataRepository.save(companyData);
        companyDataRepository.updateScoreMapBySalesAndIndustryType(annualSales, industryType);
    }

    public void saveCompanyCreditRatingRequest(final int companyId, final CompanyCreditRatingGenerationFactor companyCreditRatingGenerationFactor) {
        final Company company = companyRepository.findById(companyId).get();
        final CompanyCreditRatingGenerationRequest companyCreditRatingGenerationRequest = new CompanyCreditRatingGenerationRequest(company, companyCreditRatingGenerationFactor);
        companyCreditGenerationRequestRepository.save(companyCreditRatingGenerationRequest);
    }

    public CreditScore generateCreditScore(Company company) {
        return new CreditScore(company);
    }
}
