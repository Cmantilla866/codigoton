package com.example.CenaClientes.service;

import com.example.CenaClientes.classes.ClientComparator;
import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.exception.CustomException;
import com.example.CenaClientes.feignClient.DecryptFeign;
import com.example.CenaClientes.repository.AccountRepository;
import com.example.CenaClientes.repository.ClientRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service with the program's logic
 * */
@Service
public class CenaServiceImpl implements CenaService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    DecryptFeign decryptFeign;

    List<Client> usedClients = new ArrayList<>();

    /**
     * Method that calculates the clients in every table
     * @return A String with the clients in every table
     * */
    @Override
    public String getTables(MultipartFile multipartFile) throws IOException{
        // Runs the stored procedure to create the total_balance column in the DB
        clientRepository.addTotalBalancePerClient();

        //Reads the input file
        String test = new String(multipartFile.getBytes());
        String[] lines = test.split("\r\n");

        //Setup for the process
        Table table = null;
        List<Table> tables = new ArrayList<>();
        Filters filters = new Filters();


        for (int i=0; i<lines.length; i++){
            // If the line contains a table
            if (lines[i].contains("<")){
                if (table!=null){
                    table = runFilters(filters,table);
                    tables.add(table);
                    filters = new Filters();
                }
                table = new Table();
                table.setTableName(lines[i]);
            }
            // If the line contains a filter
            else{
                try{
                    String[] filter = lines[i].split(":");
                    switch (filter[0].trim()){
                        case "TC":
                            filters.setTc(Long.parseLong(filter[1].trim()));
                            break;
                        case "UG":
                            filters.setUg(Long.parseLong(filter[1].trim()));
                            break;
                        case "RI":
                            filters.setRi(Long.parseLong(filter[1].trim()));
                            break;
                        case "RF":
                            filters.setRf(Long.parseLong(filter[1].trim()));
                            break;
                        default:
                            throw new CustomException("Error processing filters","The filter "+filter[0].trim()+" wasn't found",400);
                    }
                }catch (CustomException e){
                    return e.getUserMessage();
                }

                // If the line it's the last one it will add the last table
                if (i+1== lines.length){
                    table = runFilters(filters,table);
                    tables.add(table);
                }
            }
        }

        // Sets up the resulting String
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
        // Resets the used clients for the configuration
        usedClients = new ArrayList<>();
        return result;
    }

    /**
     * Method that runs the added filters
     * @return A table with the filtered clients
     * */
    public Table runFilters(Filters filters, Table table){
        List<Client> clients = new ArrayList<>();
        // Searches for male clients with the filters
        filters.setMale(true);
        filters.setLimit(4);
        filters.setUsedClients(usedClients);
        List<Client> maleClients = clientRepository.findClientByCriteria(filters);
        // If there are less than 2 male clients the table is cancelled
        if (maleClients.size() < 2){
            table.setClients(clients);
        }
        else{
            // Searches for female clients with the filters
            filters.setLimit(maleClients.size());
            filters.setMale(false);
            List<Client> femaleClients = clientRepository.findClientByCriteria(filters);
            // If there are different amounts of male and female clients
            if (femaleClients.size() != maleClients.size()){
                int diff = maleClients.size() - femaleClients.size();
                // Removes the difference
                for (int j=0;j < diff; j++){
                    maleClients.remove(maleClients.size()-1);
                }
            }
            // Joins the clients
            maleClients.addAll(femaleClients);
            // If there are less than 4 clients the table is cancelled
            if (maleClients.size() < 4){
                table.setClients(clients);
            }
            else{
                // Decrypt the corresponding clients
                for (Client client : maleClients){
                    if (client.getEncrypt()){
                        client.setCode(decryptFeign.getUncryptedCode(client.getCode()).replace("\"",""));
                    }
                    clients.add(client);
                }

                // Sorts the clients by total_balance and code
                ClientComparator clientComparator = new ClientComparator();
                Collections.sort(clients,clientComparator);

                table.setClients(clients);

                // Adds clients who already belong to a table
                usedClients.addAll(clients);
            }
        }
        return table;
    }

}
