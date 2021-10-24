package com.example.CenaClientes.repository;

import com.example.CenaClientes.entities.Client;

import java.util.List;

public interface ClientRepositoryCustom {

    List<Client> findClientByCriteria(Integer TC, String UG, String RI, String RF);

}
