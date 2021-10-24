package com.example.CenaClientes.databuilder;

import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Client;
import lombok.Data;

import java.util.List;

/**
 * Class that defines the table
 * */
@Data
public class TableDataBuilder {

    private String tableName;
    private List<Client> clients;

    private String TABLENAME;
    private List<Client> CLIENTS;

    public TableDataBuilder() {
        this.tableName = TABLENAME;
        this.clients = CLIENTS;
    }

    public Table buildTable(){
        return new Table(
                this.tableName,
                this.clients
        );
    }
}
