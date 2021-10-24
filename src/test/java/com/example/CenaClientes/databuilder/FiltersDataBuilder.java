package com.example.CenaClientes.databuilder;

import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.entities.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class that defines the filters used in the consult
 * */
@Data
public class FiltersDataBuilder {

    private Long tc;
    private Long ug;
    private Long ri;
    private Long rf;
    private Integer limit;
    private Boolean male;
    private List<Client> usedClients;

    private Long TC = 1L;
    private Long UG = 1L;
    private Long RI = 1L;
    private Long RF = 1L;
    private Integer LIMIT = 4;
    private Boolean MALE = true;
    private List<Client> USEDCLIENTS = new ArrayList<>();

    public FiltersDataBuilder() {
        this.tc = TC;
        this.ug = UG;
        this.ri = RI;
        this.rf = RF;
        this.limit = LIMIT;
        this.male = MALE;
        this.usedClients = USEDCLIENTS;
    }

    public Filters buildFilters(){
        return new Filters(
                this.tc,
                this.ug,
                this.ri,
                this.rf,
                this.limit,
                this.male,
                this.usedClients);
    }
}
