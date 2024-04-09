package com.test.schemaTest.service;

import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.CompanyCreditRatingGenerationFactor;
import com.test.schemaTest.pojos.Parameter;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.repository.CompanyRepository;
import com.test.schemaTest.utils.CompanyDataUtils;
import com.test.schemaTest.views.CompanyDataView;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyDataService {

    private final CompanyDataRepository companyDataRepository;
    private final CompanyRepository companyRepository;
    private final CompanyCreditRatingService companyCreditRatingService;

    private static final CompanyDataUtils companyDataUtils = new CompanyDataUtils();

    public CompanyDataService(final CompanyDataRepository companyDataRepository,
                              final CompanyRepository companyRepository,
                              final CompanyCreditRatingService companyCreditRatingService) {
        this.companyDataRepository = companyDataRepository;
        this.companyRepository = companyRepository;
        this.companyCreditRatingService = companyCreditRatingService;
    }

    public List<CompanyDataView> getBankCustomersDataView(final List<CompanyData> sameStackCompanies) {
        return sameStackCompanies.stream()
                .map(companyData -> getCompanyDataView(companyData, sameStackCompanies))
                .collect(Collectors.toList());
    }


    public CompanyDataView getCompanyDataViewFromSQL(final String panNumber) {

        String hashId = DigestUtils.sha256Hex(panNumber.getBytes(StandardCharsets.UTF_8));
        List<Object[]> results =  companyDataRepository.getCompanyDataViewByHashId(hashId);
        if (results.isEmpty()) {
            return null; // Handle case where no data is found
        }

        Object[] row = results.get(0); // Assuming single result

        // Map the values to construct CompanyDataView
        return new CompanyDataView(
                (int) row[0],           // id
                (String) row[1],        // hashId
                (String) row[2],        // industryType
                (String) row[3],        // annualSales
                (Double) row[4],        // salesParameterPercentile
                0.0         // quarterlySalesGrowthPercentile
        );
    }

    public CompanyDataView getCompanyDataView(final String toBeHashed) {
        String hashedId = DigestUtils.sha256Hex(toBeHashed.getBytes(StandardCharsets.UTF_8));
        CompanyData companyData = companyDataRepository.findCompanyDataByHashId(hashedId);
        List<CompanyData> sameStackCompanies = companyDataRepository.findCompaniesByIndustryAndSales(companyData.getIndustryType(), companyData.getAnnualSales());
        if (sameStackCompanies == null) {
            return CompanyDataView.from(companyData);
        }
        return getCompanyDataView(companyData, sameStackCompanies);
    }

    public void addCompany() {
        String panNumber = companyDataUtils.generateRandomString(10);
        Company randomCompany = new Company(panNumber, "street 1", "area", "city", "KN", "560324", "support@fintheon.com", "9090909090", panNumber);
        companyRepository.save(randomCompany);

        companyCreditRatingService.saveCompanyCreditRatingRequest(randomCompany.getId(), CompanyCreditRatingGenerationFactor.GST);
        companyCreditRatingService.triggerAsyncCreditRatingGeneration(randomCompany.getId());
    }

    private CompanyDataView getCompanyDataView(final CompanyData companyData, final List<CompanyData> sameStackCompanies) {
        Map<Parameter, Integer> givenScoreMap = companyData.getScoreMap();

        List<Map<Parameter, Integer>> allScoreMaps = sameStackCompanies
                .stream()
                .map(CompanyData::getScoreMap)
                .toList();

        Map<Parameter, Double> percentileMap = givenScoreMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> calculatePercentile(entry.getValue(), allScoreMaps.stream()
                                        .map(mp -> mp.get(entry.getKey())).toList())
                        )
                );

        CompanyDataView companyDataView = CompanyDataView.from(companyData);
        companyDataView.percentileMap = percentileMap;
        return companyDataView;
    }

    private Double calculatePercentile(final Integer value, final List<Integer> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List of values is empty");
        }

        List<Integer> mutableList = new java.util.ArrayList<>(List.copyOf(list));
        Collections.sort(mutableList);
        int index = Collections.binarySearch(mutableList, value);

        int position;
        if (index < 0) {
            position = -index - 1;
        } else {
            position = index + 1;
        }

        // Calculate the percentile based on the position
        return  (double) position / list.size() * 100.0;
    }
}
