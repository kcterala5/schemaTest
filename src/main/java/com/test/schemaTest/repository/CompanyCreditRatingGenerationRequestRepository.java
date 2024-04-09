package com.test.schemaTest.repository;

import com.test.schemaTest.models.CompanyCreditRatingGenerationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyCreditRatingGenerationRequestRepository extends JpaRepository<CompanyCreditRatingGenerationRequest, Integer> {

    @Query("select c from CompanyCreditRatingGenerationRequest c where c.company.id = :companyId")
    Optional<CompanyCreditRatingGenerationRequest> findRequestByCompanyId(@Param("companyId") int companyId);

}
