package com.test.schemaTest.utils;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.BankCustomers;
import com.test.schemaTest.models.Company;
import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

import static com.test.schemaTest.service.InitService.*;

public class CompanyDataUtils {
    public String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public int generateRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max) + 1;
    }

    public Bank getRandomBank(Bank bank1, Bank bank2, Bank bank3) {
        int number = generateRandomNumber(100) % 3;
        return switch (number) {
            case 0 -> bank1;
            case 1 -> bank2;
            case 2 -> bank3;
            default -> throw new IllegalStateException("Unexpected value: " + number);
        };
    }

    public String getRandomIndustry() {
        int number = generateRandomNumber(100) % 3;
        return switch (number) {
            case 0 -> MANUFACTURING;
            case 1 -> STEEL;
            case 2 -> REAL_ESTATE;
            default -> throw new IllegalStateException("Unexpected value: " + number);
        };
    }

    public String getRandomSales() {
        int number = generateRandomNumber(100) % 3;
        return switch (number) {
            case 0 -> MICRO;
            case 1 -> MEDIUM;
            case 2 -> SMALL;
            default -> throw new IllegalStateException("Unexpected value: " + number);
        };
    }

    public CompanyData getCompanyDataFromCustomer(BankCustomers customer) {
        String hashId = DigestUtils.sha256Hex(customer.getCompany().getPanNumber().getBytes(StandardCharsets.UTF_8));
        String industry = getRandomIndustry();
        String sales = getRandomSales();
        Map<Parameter, Integer> scoreMap = Map.of(
                Parameter.QUARTERLY_SALES_GROWTH, generateRandomNumber(100),
                Parameter.SALES_GROWTH_MOMENTUM, generateRandomNumber(100),
                Parameter.SALES_TREND, generateRandomNumber(100)
        );
        return new CompanyData(hashId, industry, sales, scoreMap, generateRandomNumber(100), generateRandomNumber(100));
    }
}
