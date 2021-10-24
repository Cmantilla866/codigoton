package com.example.CenaClientes.service;

import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.feignClient.DecryptFeign;
import com.example.CenaClientes.repository.AccountRepository;
import com.example.CenaClientes.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CenaServiceImpl implements CenaService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    DecryptFeign decryptFeign;

    @Override
    public List<Table> getTables(MultipartFile multipartFile) throws IOException {
        String test = new String(multipartFile.getBytes());
        String[] lines = test.split("\r\n");
        Table table = null;
        List<String> filters = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        for (int i=0; i<lines.length; i++){
            if (lines[i].contains("<")){
                if (table!=null){
                    table.setFilters(filters);
                    tables.add(table);
                    filters = new ArrayList<>();
                }
                table = new Table();
                table.setTableName(lines[i]);
            }
            else{
                filters.add(lines[i]);
                if (i+1== lines.length){
                    table.setFilters(filters);
                    tables.add(table);
                }
            }
        }
        return tables;
    }

    @Override
    public List<Client> listAllClients() {
        //List<Client> clients = clientRepository.findAll();
        List<Client> clients = clientRepository.findClientByCriteria(null,null,"1074049",null);
        for (Client client : clients){
            if (client.getEncrypt()){
                client.setCode(decryptFeign.getUncryptedCode(client.getCode()).replace("\"",""));
            }
        }
        return clients;
    }

    @Override
    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }
}
