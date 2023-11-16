package com.test.schemaTest.service;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyDataService {

    private final CompanyDataRepository companyDataRepository;

    public CompanyDataService(final CompanyDataRepository companyDataRepository) {
        this.companyDataRepository = companyDataRepository;
    }

    public CompanyDataView getCompanyDataView(final String toBeHashed) {
        String hashedId = DigestUtils.md5DigestAsHex(toBeHashed.getBytes(StandardCharsets.UTF_8));
        CompanyData companyData = companyDataRepository.findCompanyDataByHashId(hashedId);
        List<CompanyData> sameStackCompanies = companyDataRepository.findCompaniesByIndustryAndSales(companyData.getIndustryType(), companyData.getAnnualSales());
        if (sameStackCompanies == null) {
            return CompanyDataView.from(companyData);
        }
        return getCompanyDataView(companyData, sameStackCompanies);
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
