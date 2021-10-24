package com.example.CenaClientes.repository;

import com.example.CenaClientes.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Integer>, ClientRepositoryCustom {
}
