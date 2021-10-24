package com.example.CenaClientes.classes;

import com.example.CenaClientes.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class that defines the table
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {

    private String tableName;
    private List<Client> clients;

}
