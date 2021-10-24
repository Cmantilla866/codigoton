package com.example.CenaClientes.classes;

import com.example.CenaClientes.entities.Client;

import java.util.Comparator;

public class ClientComparator implements Comparator<Client> {

    @Override
    public int compare(Client client1, Client client2) {
        int value1 = client2.getTotalBalance().compareTo(client1.getTotalBalance());
        if (value1 == 0){
            return client2.getCode().compareTo(client1.getCode());
        }
        return value1;
    }
}
