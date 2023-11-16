package com.test.schemaTest.repository;

import com.test.schemaTest.models.BankCustomers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankCustomersRepository extends JpaRepository<BankCustomers, Integer> {
    @Query("select bc from BankCustomers bc where bc.bank.id = :bankId")
    List<BankCustomers> findBankCustomersByBankId(@Param("bankId") int bankId);
}