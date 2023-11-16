package com.test.schemaTest.repository;

import com.test.schemaTest.models.Bank;
import com.test.schemaTest.models.CompanyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    @Query("select b from Bank b where b.id = :bankId")
    Bank findBankById(@Param("bankId") int bankId);
}
