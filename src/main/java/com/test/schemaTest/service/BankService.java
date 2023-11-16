package com.test.schemaTest.service;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.BankCustomers;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.repository.BankCustomersRepository;
import com.test.schemaTest.repository.BankRepository;
import com.test.schemaTest.repository.CompanyDataRepository;
import com.test.schemaTest.utils.CompanyDataUtils;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {

    private final CompanyDataRepository companyDataRepository;
    private final BankRepository bankRepository;
    private final BankCustomersRepository bankCustomersRepository;
    private static final CompanyDataUtils companyDataUtils = new CompanyDataUtils();

    public BankService(final CompanyDataRepository companyDataRepository,
                       final BankRepository bankRepository,
                       final BankCustomersRepository bankCustomersRepository) {
        this.companyDataRepository = companyDataRepository;
        this.bankRepository = bankRepository;
        this.bankCustomersRepository = bankCustomersRepository;
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

    public List<CompanyDataView> getCreditRatingForBank(final int bankId) {
        Bank bank = bankRepository.findBankById(bankId);
        List<CompanyDataView> companyDataList = new ArrayList<>();
        return companyDataList;
    }
}
