package com.example.CenaClientes.classes;

import com.example.CenaClientes.entities.Client;
import lombok.Data;

import java.util.List;

@Data
public class Table {

    String tableName;
    List<Client> clients;

}
