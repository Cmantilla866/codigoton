package com.example.CenaClientes.classes;

import lombok.Data;

import java.util.List;

@Data
public class Table {

    String tableName;
    List<String> filters;

}
