package com.test.schemaTest.repository;

import com.test.schemaTest.models.BankCustomers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCustomersRepository extends JpaRepository<BankCustomers, Integer> {

}
