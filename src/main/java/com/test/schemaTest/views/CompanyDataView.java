package com.test.schemaTest.views;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.pojos.Parameter;
import java.util.Map;

public class CompanyDataView {
    public int id;
    public String hashId;
    public String industryType;
    public String annualSales;
    public Map<Parameter, Integer> scoreMap;
    public Map<Parameter, Double> percentileMap;


    public CompanyDataView(final int id, final String hashId, final String industryType, final String annualSales, final Map<Parameter, Integer> scoreMap) {
        this.id = id;
        this.hashId = hashId;
        this.industryType = industryType;
        this.annualSales = annualSales;
        this.scoreMap = scoreMap;
    }

    public static CompanyDataView from(CompanyData companyData) {
        return new CompanyDataView(
                companyData.getId(),
                companyData.getHashId(),
                companyData.getIndustryType(),
                companyData.getAnnualSales(),
                companyData.getScoreMap()
        );
    }


}
