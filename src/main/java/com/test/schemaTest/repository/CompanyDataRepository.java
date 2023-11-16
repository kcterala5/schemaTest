package com.test.schemaTest.repository;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyData, Integer> {

    @Query("select c from CompanyData c where c.industryType = :industry and c.annualSales = :sales")
    List<CompanyData> findCompaniesByIndustryAndSales(@Param("industry") String industry, @Param("sales") String sales);

    @Query("select c from CompanyData c where c.hashId = :hash")
    CompanyData findCompanyDataByHashId(@Param("hash") String hash);

    @Query("select new com.test.schemaTest.views.CompanyDataView(c.id, c.hashId, c.industryType, c.annualSales, c.scoreMap")
    List<CompanyDataView> findCreditRatingForBankCustomers(@Param("bankId") int bankId);

}
