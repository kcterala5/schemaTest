package com.test.schemaTest.models;

import com.test.schemaTest.pojos.Parameter;
import com.vladmihalcea.hibernate.type.json.JsonType;
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

    public CompanyData(final String hashId, final String industryType, final String annualSales, final Map<Parameter, Integer> scoreMap) {
        this.hashId = hashId;
        this.industryType = industryType;
        this.annualSales = annualSales;
        this.scoreMap = scoreMap;
    }
}
