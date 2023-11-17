package com.test.schemaTest.service;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.BankCustomers;
import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.repository.BankCustomersRepository;
import com.test.schemaTest.repository.BankRepository;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.utils.CompanyDataUtils;
import com.test.schemaTest.views.CompanyDataView;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final CompanyDataRepository companyDataRepository;
    private final BankRepository bankRepository;
    private final BankCustomersRepository bankCustomersRepository;
    private final CompanyDataService companyDataService;
    private static final CompanyDataUtils companyDataUtils = new CompanyDataUtils();

    public BankService(final CompanyDataRepository companyDataRepository,
                       final BankRepository bankRepository,
                       final BankCustomersRepository bankCustomersRepository,
                       final CompanyDataService companyDataService) {
        this.companyDataRepository = companyDataRepository;
        this.bankRepository = bankRepository;
        this.bankCustomersRepository = bankCustomersRepository;
        this.companyDataService = companyDataService;
    }

    public List<CompanyData> generateCreditRatingScoreForBank(final int bankId) {
        Bank bank = bankRepository.findBankById(bankId);
        List<BankCustomers> customersList = bankCustomersRepository.findBankCustomersByBankId(bankId);
        List<CompanyData> companyDataList = new ArrayList<>();
        for (BankCustomers customer : customersList) {
            CompanyData companyData = companyDataUtils.getCompanyDataFromCustomer(customer);
            companyDataList.add(companyData);
            companyDataRepository.save(companyData); // each pruned company data will be stored in the DB
        }

        // Generate a rating score for each pruned company for each factor
        return companyDataList;
    }

    public Map<String, List<CompanyDataView>> getCreditRatingForBankCustomers(final int bankId) {
        Bank bank = bankRepository.findBankById(bankId);
        List<BankCustomers> bankCustomersList = bankCustomersRepository.findBankCustomersByBankId(bankId);
        List<CompanyData> companyDataList = bankCustomersList.stream()
                .map(bankCustomer -> companyDataRepository.findCompanyDataByHashId(DigestUtils.sha256Hex(bankCustomer.getCompany().getPanNumber().getBytes(StandardCharsets.UTF_8))))
                .toList();

        Map<String, List<CompanyData>> categorizedData = new HashMap<>();
        for (CompanyData companyData : companyDataList) {
            String industryType = companyData.getIndustryType();
            String annualSales = companyData.getAnnualSales();

            String key = industryType + "_" + annualSales;
            List<CompanyData> dataList = categorizedData.computeIfAbsent(key, k -> new ArrayList<>());
            dataList.add(companyData);
        }
        Map<String, List<CompanyDataView>> companyDataViewMap = new HashMap<>();
        for (Map.Entry<String, List<CompanyData>> entry : categorizedData.entrySet()) {
            String key = entry.getKey();
            List<CompanyData> companyDataListForKey = entry.getValue();
            List<CompanyDataView> companyDataViewList = companyDataService.getBankCustomersDataView(companyDataListForKey);
            companyDataViewMap.put(key, companyDataViewList);
        }
        return companyDataViewMap;
    }
}
