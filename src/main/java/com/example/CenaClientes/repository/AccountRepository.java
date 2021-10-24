package com.example.CenaClientes.repository;

import com.example.CenaClientes.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the account entity
 * */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
