package com.test.schemaTest.service;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import com.test.schemaTest.repository.BankRepository;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.DigestUtils;

import javax.persistence.EntityManager;
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
    private static final String SMALL = "small";
    private static final String MEDIUM = "medium";
    private final CompanyDataRepository companyDataRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BankRepository bankRepository;

    public InitService(final CompanyDataRepository companyDataRepository) {
        this.companyDataRepository = companyDataRepository;
    }

    public void teardown() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        try {
            transactionTemplate.execute(transactionStatus -> {
                entityManager.createNativeQuery("Truncate " +
                                "company," +
                                "company_data," +
                                "bank," +
                                "bank_customer_data," +
                                "bank_customers"
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
        Company company = new Company("Fintheon", "street 1", "area", "city", "KN", "560324", "support@fintheon.com", "9090909090", "AAECF6622N");
        Company company1 = new Company("KSG Autoworks", "street 10", "area", "city", "MH", "100001", "support@ksg.com", "9879879876", "AAACG3995M");
        Company company2 = new Company("ST Casting", "street 10", "area", "city", "MH", "100001", "support@stc.com", "8090706090", "AAAKG3995M");
        Company company3 = new Company("Test", "street 2", "street 4", "city", "state", "110087", "support@test.com", "1234567890", "AAALG3995M");
        companyRepository.save(company);
        companyRepository.save(company1);
        companyRepository.save(company2);

        String hashId = DigestUtils.md5DigestAsHex(company.getName().getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 34,
                Parameter.SALES_GROWTH_MOMENTUM, 50,
                Parameter.SALES_TREND, 89
        );
        CompanyData companyData = new CompanyData(hashId, MANUFACTURING, MICRO, scoreMap);
        companyDataRepository.save(companyData);



        String hashId1 = DigestUtils.md5DigestAsHex(company1.getName().getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap1 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 89,
                Parameter.SALES_GROWTH_MOMENTUM, 30,
                Parameter.SALES_TREND, 29
        );
        CompanyData companyData1 = new CompanyData(hashId1, STEEL, SMALL, scoreMap1);
        companyDataRepository.save(companyData1);


        String hashId2 = DigestUtils.md5DigestAsHex(company2.getName().getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap2 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 64,
                Parameter.SALES_GROWTH_MOMENTUM, 20,
                Parameter.SALES_TREND, 89
        );
        CompanyData companyData2 = new CompanyData(hashId2, REAL_ESTATE, MICRO, scoreMap2);
        companyDataRepository.save(companyData2);


        String hashId3 = DigestUtils.md5DigestAsHex(company3.getName().getBytes(StandardCharsets.UTF_8));
        Map<Parameter, Integer> scoreMap3 = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, 94,
                Parameter.SALES_GROWTH_MOMENTUM, 20,
                Parameter.SALES_TREND, 19
        );
        CompanyData companyData3 = new CompanyData(hashId3, MANUFACTURING, MEDIUM, scoreMap3);
        companyDataRepository.save(companyData3);
    }
}
