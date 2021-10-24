package com.example.CenaClientes.classes;

import com.example.CenaClientes.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  Class that defines the filters used in the consult
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filters {

    private Long tc;
    private Long ug;
    private Long ri;
    private Long rf;
    private Integer limit;
    private Boolean male;
    private List<Client> usedClients;

}
