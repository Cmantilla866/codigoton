package com.example.CenaClientes.databuilder;

import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


public class ClientDataBuilder {

    private Integer id;
    private String code;
    private Boolean male;
    private Integer type;
    private Long location;
    private Long company;
    private Boolean encrypt;
    private List<Account> accounts;
    private Long totalBalance;


    private Integer ID = 1;
    private String CODE = "COD1";
    private Boolean MALE = true;
    private Integer TYPE = 1;
    private Long LOCATION = 1L;
    private Long COMPANY = 1L;
    private Boolean ENCRYPT = false;
    private List<Account> ACCOUNTS = new ArrayList<>();
    private Long TOTALBALANCE = 1L;

    public ClientDataBuilder() {
        this.id = ID;
        this.code = CODE;
        this.male = MALE;
        this.type = TYPE;
        this.location = LOCATION;
        this.company = COMPANY;
        this.encrypt = ENCRYPT;
        this.accounts = ACCOUNTS;
        this.totalBalance = TOTALBALANCE;
    }

    public Client buildClient(){
        return new Client(this.id,
                this.code,
                this.male,
                this.type,
                this.location,
                this.company,
                this.encrypt,
                this.accounts,
                this.totalBalance);
    }
}
