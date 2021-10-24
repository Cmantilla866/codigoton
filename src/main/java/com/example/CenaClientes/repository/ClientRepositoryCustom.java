package com.example.CenaClientes.repository;

import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.entities.Client;

import java.util.List;

public interface ClientRepositoryCustom {

    List<Client> findClientByCriteria(Filters filters);

}
