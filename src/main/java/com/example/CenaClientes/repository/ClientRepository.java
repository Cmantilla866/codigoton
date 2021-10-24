package com.example.CenaClientes.repository;

import com.example.CenaClientes.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Client class
 * */
@Repository
public interface ClientRepository extends JpaRepository <Client, Integer>, ClientRepositoryCustom {

    /**
     * Stored procedure for creating the total balance of each client in every account
     * */
    @Procedure("ROWPERROW")
    void addTotalBalancePerClient();
}
