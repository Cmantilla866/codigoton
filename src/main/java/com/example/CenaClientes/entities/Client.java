package com.example.CenaClientes.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Client entity
 * */
@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
