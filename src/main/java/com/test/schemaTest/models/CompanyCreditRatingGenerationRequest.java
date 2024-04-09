package com.test.schemaTest.models;

import com.test.schemaTest.pojos.CompanyCreditRatingGenerationFactor;
import com.test.schemaTest.pojos.CompanyCreditRatingGenerationStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class CompanyCreditRatingGenerationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

    @Column
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Enumerated(EnumType.STRING)
    private CompanyCreditRatingGenerationStatus companyCreditRatingGenerationStatus;

    @Enumerated(EnumType.STRING)
    private CompanyCreditRatingGenerationFactor companyCreditRatingGenerationFactor;

    @Column
    private int retryCount;

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public CompanyCreditRatingGenerationStatus getStatus() {
        return companyCreditRatingGenerationStatus;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

    public void setCompanyCreditRatingGenerationFactor(final CompanyCreditRatingGenerationFactor companyCreditRatingGenerationFactor) {
        this.companyCreditRatingGenerationFactor = companyCreditRatingGenerationFactor;
    }

    public void setStatus(final CompanyCreditRatingGenerationStatus status) {
        this.companyCreditRatingGenerationStatus = status;
    }

    public void setRetryCount(final int retryCount) {
        this.retryCount = retryCount;
    }

    public CompanyCreditRatingGenerationRequest(final Company company,
                                                final CompanyCreditRatingGenerationFactor companyCreditRatingGenerationFactor) {
        this.company = company;
        this.companyCreditRatingGenerationFactor = companyCreditRatingGenerationFactor;
        this.companyCreditRatingGenerationStatus = CompanyCreditRatingGenerationStatus.PENDING;
    }
}
