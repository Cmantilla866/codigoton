package com.example.CenaClientes.classes;

import com.example.CenaClientes.entities.Client;
import lombok.Data;

import java.util.List;

@Data
public class Filters {

    private Long TC;
    private Long UG;
    private Long RI;
    private Long RF;
    private Integer limit;
    private Boolean male;
    private List<Client> usedClients;

}
