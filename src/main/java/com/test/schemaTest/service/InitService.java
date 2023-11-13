package com.test.schemaTest.service;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import com.test.schemaTest.repository.CompanyDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class InitService {

    // industries
    private static final String MANUFACTURING = "manufacturing";
    private static final String STEEL = "steel";
    private static final String REAL_ESTATE = "real-estate";

    // sales
    private static final String MICRO = "micro"; // just for simplicity
    private static final String MACRO = "macro";
    private static final String MEDIUM = "medium";
    private final CompanyDataRepository companyDataRepository;

    public InitService(final CompanyDataRepository companyDataRepository) {
        this.companyDataRepository = companyDataRepository;
    }


    public void initialise() {
        companyDataRepository.deleteAll();
        String hashId = DigestUtils.md5DigestAsHex("Test".getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 34,
                Parameter.SALES_GROWTH_MOMENTUM, 50,
                Parameter.SALES_TREND, 89
        );
        CompanyData companyData = new CompanyData(hashId, MANUFACTURING, MICRO, scoreMap);
        companyDataRepository.save(companyData);



        String hashId1 = DigestUtils.md5DigestAsHex("Test1".getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap1 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 89,
                Parameter.SALES_GROWTH_MOMENTUM, 30,
                Parameter.SALES_TREND, 29
        );
        CompanyData companyData1 = new CompanyData(hashId1, STEEL, MACRO, scoreMap1);
        companyDataRepository.save(companyData1);


        String hashId2 = DigestUtils.md5DigestAsHex("Test2".getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap2 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 64,
                Parameter.SALES_GROWTH_MOMENTUM, 20,
                Parameter.SALES_TREND, 89
        );
        CompanyData companyData2 = new CompanyData(hashId2, REAL_ESTATE, MICRO, scoreMap2);
        companyDataRepository.save(companyData2);


        String hashId3 = DigestUtils.md5DigestAsHex("Test3".getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap3 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 94,
                Parameter.SALES_GROWTH_MOMENTUM, 20,
                Parameter.SALES_TREND, 19
        );
        CompanyData companyData3 = new CompanyData(hashId3, MANUFACTURING, MEDIUM, scoreMap3);
        companyDataRepository.save(companyData3);
    }
}
