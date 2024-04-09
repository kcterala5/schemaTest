package com.test.schemaTest.service;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.BankCustomers;
import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import com.test.schemaTest.repository.*;
import com.test.schemaTest.utils.CompanyDataUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class InitService {

    private static final Logger logger = LoggerFactory.getLogger(InitService.class);
    public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // industries
    public static final String MANUFACTURING = "manufacturing";
    public static final String STEEL = "steel";
    public static final String REAL_ESTATE = "real-estate";

    // sales
    public static final String MICRO = "micro"; // just for simplicity
    public static final String SMALL = "small";
    public static final String MEDIUM = "medium";
    private final CompanyDataRepository companyDataRepository;

    private final EntityManager entityManager;

    private final PlatformTransactionManager transactionManager;

    private final CompanyRepository companyRepository;

    private final BankRepository bankRepository;
    private final BankCustomersRepository bankCustomersRepository;
    private final CompanyCreditRatingGenerationRequestRepository companyCreditRatingGenerationRequestRepository;
    private static final CompanyDataUtils companyDataUtils = new CompanyDataUtils();

    public InitService(final CompanyDataRepository companyDataRepository,
                       final EntityManager entityManager,
                       final PlatformTransactionManager transactionManager,
                       final CompanyRepository companyRepository,
                       final BankRepository bankRepository,
                       final BankCustomersRepository bankCustomersRepository,
                       final CompanyCreditRatingGenerationRequestRepository companyCreditRatingGenerationRequestRepository) {
        this.companyDataRepository = companyDataRepository;
        this.entityManager = entityManager;
        this.transactionManager = transactionManager;
        this.companyRepository = companyRepository;
        this.bankRepository = bankRepository;
        this.bankCustomersRepository = bankCustomersRepository;
        this.companyCreditRatingGenerationRequestRepository = companyCreditRatingGenerationRequestRepository;
    }

    public void teardown() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        try {
            transactionTemplate.execute(transactionStatus -> {
                entityManager.createNativeQuery("Truncate " +
                                "company," +
                                "company_data," +
                                "bank," +
                                "bank_customers," +
                                "company_credit_rating_generation_request," +
                                "credit_score"
                        )
                        .executeUpdate();
                transactionStatus.flush();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise() {
        Bank bank1 = new Bank("SBI", "Street 1", "Street 2", "City1", "MH", "560123", "9898989898");
        Bank bank2 = new Bank("HDFC", "Street 3", "Street 4", "City2", "KA", "660784", "9090909090");
        Bank bank3 = new Bank("RXIL", "Street 1", "Street 2", "City1", "MH", "560123", "9898989898");
        bankRepository.save(bank3);
        bankRepository.save(bank1);
        bankRepository.save(bank2);

        Company company1 = new Company("Fintheon", "street 1", "area", "city", "KN", "560324", "support@fintheon.com", "9090909090", "AAECF6622N");
        Company company2 = new Company("KSG Autoworks", "street 10", "area", "city", "MH", "100001", "support@ksg.com", "9879879876", "AAACG3995M");
        Company company3 = new Company("ST Casting", "street 10", "area", "city", "MH", "100001", "support@stc.com", "8090706090", "AAAKG3995M");
        companyRepository.save(company1);
        companyRepository.save(company2);
        companyRepository.save(company3);


        String hashId1 = DigestUtils.sha256Hex(company1.getPanNumber().getBytes(StandardCharsets.UTF_8));
        int salesTrendParameter1 = companyDataUtils.generateRandomNumber(100);
        int quarterlySalesGrowthParameter1 = companyDataUtils.generateRandomNumber(100);
        Map<Parameter, Integer> scoreMap1 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, quarterlySalesGrowthParameter1,
                Parameter.SALES_GROWTH_MOMENTUM, companyDataUtils.generateRandomNumber(100),
                Parameter.SALES_TREND, salesTrendParameter1
        );
        CompanyData companyData1 = new CompanyData(hashId1, MANUFACTURING, MICRO, scoreMap1, salesTrendParameter1, quarterlySalesGrowthParameter1);
        companyDataRepository.save(companyData1);

        String hashId2 = DigestUtils.sha256Hex(company2.getPanNumber().getBytes(StandardCharsets.UTF_8));
        int salesTrendParameter2 = companyDataUtils.generateRandomNumber(100);
        int quarterlySalesGrowthParameter2 = companyDataUtils.generateRandomNumber(100);
        Map<Parameter, Integer> scoreMap2 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, quarterlySalesGrowthParameter2,
                Parameter.SALES_GROWTH_MOMENTUM, companyDataUtils.generateRandomNumber(100),
                Parameter.SALES_TREND, salesTrendParameter2
        );
        CompanyData companyData2 = new CompanyData(hashId2, STEEL, MEDIUM, scoreMap2, salesTrendParameter2, quarterlySalesGrowthParameter2);
        companyDataRepository.save(companyData2);

        String hashId3 = DigestUtils.sha256Hex(company3.getPanNumber().getBytes(StandardCharsets.UTF_8));
        int salesTrendParameter3 = companyDataUtils.generateRandomNumber(100);
        int quarterlySalesGrowthParameter3 = companyDataUtils.generateRandomNumber(100);
        Map<Parameter, Integer> scoreMap3 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, quarterlySalesGrowthParameter3,
                Parameter.SALES_GROWTH_MOMENTUM, companyDataUtils.generateRandomNumber(100),
                Parameter.SALES_TREND, salesTrendParameter3
        );
        CompanyData companyData3 = new CompanyData(hashId3, REAL_ESTATE, SMALL, scoreMap3, salesTrendParameter3, quarterlySalesGrowthParameter3);
        companyDataRepository.save(companyData3);

        BankCustomers bankCustomers1 = new BankCustomers(bank1, company1);
        BankCustomers bankCustomers2 = new BankCustomers(bank2, company2);
        BankCustomers bankCustomers3 = new BankCustomers(bank3, company3);
        bankCustomersRepository.save(bankCustomers1);
        bankCustomersRepository.save(bankCustomers2);
        bankCustomersRepository.save(bankCustomers3);


        logger.info("Storing random data in the database");
        long start = System.currentTimeMillis();
//        for (int i = 1; i <= 100; i++) {
//            String panNumber = companyDataUtils.generateRandomString(10);
//            Company randomCompany = new Company(panNumber, "street 1", "area", "city", "KN", "560324", "support@fintheon.com", "9090909090", panNumber);
//            BankCustomers bankCustomers = new BankCustomers(companyDataUtils.getRandomBank(bank1, bank2, bank3), randomCompany);
//            String hashId = DigestUtils.sha256Hex(randomCompany.getPanNumber().getBytes(StandardCharsets.UTF_8));
//            int salesTrendParameter = companyDataUtils.generateRandomNumber(100);
//            int quaterlySalesGrowthParameter = companyDataUtils.generateRandomNumber(100);
//            Map<Parameter, Integer> scoreMap = Map.of(
//                    Parameter.QUARTERLY_SALES_GROWTH, quaterlySalesGrowthParameter,
//                    Parameter.SALES_GROWTH_MOMENTUM, companyDataUtils.generateRandomNumber(100),
//                    Parameter.SALES_TREND, salesTrendParameter
//            );
//            CompanyData companyData = new CompanyData(hashId, companyDataUtils.getRandomIndustry(), companyDataUtils.getRandomSales(), scoreMap, salesTrendParameter, quaterlySalesGrowthParameter);
//
//            companyRepository.save(randomCompany);
//            bankCustomersRepository.save(bankCustomers);
//            companyDataRepository.save(companyData);
//        }

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            String panNumber = companyDataUtils.generateRandomString(10);
            Company randomCompany = new Company(panNumber, "street 1", "area", "city", "KN", "560324", "support@fintheon.com", "9090909090", panNumber);
            String hashId = DigestUtils.sha256Hex(randomCompany.getPanNumber().getBytes(StandardCharsets.UTF_8));
            String[] industryTypes = {"MANUFACTURING", "CONSTRUCTION"};
            String industryType = industryTypes[random.nextInt(industryTypes.length)];

            Map<Parameter, Integer> scoreMap = new HashMap<>();

            CompanyData companyData = new CompanyData(hashId,
                    industryType,
                    "SMALL",
                    scoreMap,
                    10,
                    random.nextInt(100));

            companyDataRepository.save(companyData);
        }
        long latency = System.currentTimeMillis() - start;
        logger.info("Stored Random Data in {} sec", (latency / 1000));
    }
}
