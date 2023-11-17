package com.test.schemaTest.models;

import com.test.schemaTest.pojos.Parameter;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class CompanyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String hashId;

    @Column
    private String industryType;

    @Column
    private String annualSales;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Map<Parameter, Integer> scoreMap;


    // flat map fields
    @Column
    private int salesParameterScore;

    @Column
    private int quarterlySalesGrowthScore;

    public int getId() {
        return id;
    }

    public String getHashId() {
        return hashId;
    }

    public String getIndustryType() {
        return industryType;
    }

    public String getAnnualSales() {
        return annualSales;
    }

    public Map<Parameter, Integer> getScoreMap() {
        return scoreMap;
    }


    public CompanyData() {
    }

    public CompanyData(final String hashId,
                       final String industryType,
                       final String annualSales,
                       final Map<Parameter, Integer> scoreMap,
                       final int salesParameterScore,
                       final int quarterlySalesGrowthScore) {
        this.hashId = hashId;
        this.industryType = industryType;
        this.annualSales = annualSales;
        this.scoreMap = scoreMap;
        this.salesParameterScore = salesParameterScore;
        this.quarterlySalesGrowthScore = quarterlySalesGrowthScore;
    }
}
