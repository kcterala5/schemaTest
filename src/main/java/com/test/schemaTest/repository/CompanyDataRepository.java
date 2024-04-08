package com.test.schemaTest.repository;

import com.test.schemaTest.models.CompanyData;
import com.test.schemaTest.views.CompanyDataView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyData, Integer> {

    @Query("select c from CompanyData c where c.industryType = :industry and c.annualSales = :sales")
    List<CompanyData> findCompaniesByIndustryAndSales(@Param("industry") String industry, @Param("sales") String sales);

    @Query("select c from CompanyData c where c.hashId = :hash")
    CompanyData findCompanyDataByHashId(@Param("hash") String hash);


    @Query(value = "WITH RankedSales AS ( " +
            "    SELECT id, " +
            "           hash_id, " +
            "           industry_type, " +
            "           annual_sales, " +
            "           PERCENT_RANK() OVER (ORDER BY sales_parameter_score) * 100 AS approximate_percentile " +
            "    FROM company_data " +
            ") SELECT * FROM RankedSales WHERE hash_id = :hashId", nativeQuery = true)


        // still in progress
//    @Query(value = "SELECT cd.id, cd.hash_id, cd.industry_type, " +
//            "cd.annual_sales, " +
//            "RANK() OVER (PARTITION BY cd.industry_type, cd.annual_sales ORDER BY cd.sales_parameter_score)" +
//            "FROM company_data cd " +
//            "WHERE cd.hash_id = :hashId", nativeQuery = true)
    List<Object[]> getCompanyDataViewByHashId(@Param("hashId") String hashId);


    @Modifying
    @Transactional
    @Query(value = """
                WITH PercentileRanks AS (
                    SELECT
                        id,
                        sales_parameter_score,
                        quarterly_sales_growth_score,
                        PERCENT_RANK() OVER (
                            PARTITION BY annual_sales, industry_type
                            ORDER BY sales_parameter_score DESC
                        ) AS new_sales_score_percentile,
                        PERCENT_RANK() OVER (
                            PARTITION BY annual_sales, industry_type
                            ORDER BY quarterly_sales_growth_score DESC
                        ) AS new_growth_score_percentile
                    FROM
                        company_data
                    WHERE
                        annual_sales = ?1 AND industry_type = ?2
                )
                UPDATE company_data
                SET
                    score_map = jsonb_build_object(
                        'sales_percentile', PercentileRanks.new_sales_score_percentile,
                        'growth_percentile', PercentileRanks.new_growth_score_percentile
                    )
                FROM
                    PercentileRanks
                WHERE
                    company_data.id = PercentileRanks.id;
                """,
            nativeQuery = true)
    void updateScoreMapBySalesAndIndustryType(String annualSales, String industryType);



    /*
    id, hashid, industrytype, annual sales, sales parameter percentile
     */
}
