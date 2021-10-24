package com.example.CenaClientes.service;

import com.example.CenaClientes.classes.ClientComparator;
import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.feignClient.DecryptFeign;
import com.example.CenaClientes.repository.AccountRepository;
import com.example.CenaClientes.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    public String getTables(MultipartFile multipartFile) throws IOException {
        String test = new String(multipartFile.getBytes());
        String[] lines = test.split("\r\n");
        Table table = null;
        List<Table> tables = new ArrayList<>();
        Filters filters = new Filters();
        List<Client> usedClients = new ArrayList<>();


        for (int i=0; i<lines.length; i++){

            if (lines[i].contains("<")){

                if (table!=null){

                    List<Client> clients = new ArrayList<>();
                    filters.setMale(true);
                    filters.setLimit(4);
                    filters.setUsedClients(usedClients);
                    List<Client> maleClients = clientRepository.findClientByCriteria(filters);
                    if (maleClients.size() < 2){
                        table.setClients(clients);
                    }
                    else{
                        filters.setLimit(maleClients.size());
                        filters.setMale(false);
                        List<Client> femaleClients = clientRepository.findClientByCriteria(filters);
                        if (femaleClients.size() != maleClients.size()){
                            int diff = maleClients.size() - femaleClients.size();
                            for (int j=0;j < diff; j++){
                                maleClients.remove(maleClients.size()-1);
                            }

                        }
                        maleClients.addAll(femaleClients);
                        if (maleClients.size() < 4){
                            table.setClients(clients);
                        }
                        else{
                            for (Client client : maleClients){
                                if (client.getEncrypt()){
                                    client.setCode(decryptFeign.getUncryptedCode(client.getCode()).replace("\"",""));
                                }
                                clients.add(client);
                            }

                            ClientComparator clientComparator = new ClientComparator();
                            Collections.sort(clients,clientComparator);

                            table.setClients(clients);

                            usedClients.addAll(clients);
                        }
                    }
                    tables.add(table);
                    filters = new Filters();
                }

                table = new Table();
                table.setTableName(lines[i]);

            }
            else{

                String[] filter = lines[i].split(":");

                switch (filter[0].trim()){
                    case "TC":
                        filters.setTC(Long.parseLong(filter[1].trim()));
                        break;
                    case "UG":
                        filters.setUG(Long.parseLong(filter[1].trim()));
                        break;
                    case "RI":
                        filters.setRI(Long.parseLong(filter[1].trim()));
                        break;
                    case "RF":
                        filters.setRF(Long.parseLong(filter[1].trim()));
                        break;
                }

                if (i+1== lines.length){
                    List<Client> clients = new ArrayList<>();
                    filters.setMale(true);
                    filters.setLimit(4);
                    filters.setUsedClients(usedClients);
                    List<Client> maleClients = clientRepository.findClientByCriteria(filters);
                    if (maleClients.size() < 2){
                        table.setClients(clients);
                    }
                    else{
                        filters.setLimit(maleClients.size());
                        filters.setMale(false);
                        List<Client> femaleClients = clientRepository.findClientByCriteria(filters);
                        if (femaleClients.size() != maleClients.size()){
                            int diff = maleClients.size() - femaleClients.size();
                            for (int j=0;j < diff; j++){
                                maleClients.remove(maleClients.size()-1);
                            }

                        }
                        maleClients.addAll(femaleClients);
                        if (maleClients.size() < 4){
                            table.setClients(clients);
                        }
                        else{
                            for (Client client : maleClients){
                                if (client.getEncrypt()){
                                    client.setCode(decryptFeign.getUncryptedCode(client.getCode()).replace("\"",""));
                                }
                                clients.add(client);
                            }
                            ClientComparator clientComparator = new ClientComparator();
                            Collections.sort(clients,clientComparator);

                            table.setClients(clients);

                        }
                    }
                    tables.add(table);
                }
            }
        }
        String result = "";
        int index = 0;
        for (Table table1 : tables){
            if (index > 0){
                result = result + "\r\n" + table1.getTableName();
            }
            else{
                result = result + table1.getTableName();
            }
            if (table1.getClients().size()==0){
                result = result + "\r\nCANCELADA";
            }
            else{
                result = result + "\r\n";
                for (Client client : table1.getClients()){
                    result = result + client.getCode() + ",";
                }
                result = result.substring(0,result.length()-1);
            }
            index++;
        }
        return result;
    }

    @Override
    public List<Client> listAllClients() {
        //List<Client> clients = clientRepository.findAll();
        Filters filters = new Filters();
        filters.setLimit(4);
        filters.setMale(true);
        List<Client> clients = clientRepository.findClientByCriteria(filters);
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
