package com.example.CenaClientes.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private Boolean male;

    private Integer type;

    private Long location;

    private Long company;

    private Boolean encrypt;

    @OneToMany(mappedBy = "client", fetch= FetchType.EAGER)
    private List<Account> accounts;

    private Long totalBalance;

}
