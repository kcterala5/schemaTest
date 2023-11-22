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


    @Query(value = "SELECT * FROM (SELECT \n" +
            "    ID,\n" +
            "    HASH_ID,\n" +
            "    INDUSTRY_TYPE,\n" +
            "    ANNUAL_SALES,\n" +
            "    PERCENT_RANK() OVER (PARTITION BY INDUSTRY_TYPE, ANNUAL_SALES ORDER BY SALES_PARAMETER_SCORE) AS SALES_PERCENTILE\n" +
            "FROM \n" +
            "    COMPANY_DATA) as derivedTable\n" +
            "WHERE hash_id = :hashId", nativeQuery = true)
    List<Object[]> getCompanyDataViewByHashId(@Param("hashId") String hashId);



    /*
    id, hashid, industrytype, annual sales, sales parameter percentile
     */
}
