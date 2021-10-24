package com.example.CenaClientes.service;

import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.databuilder.ClientDataBuilder;
import com.example.CenaClientes.databuilder.FiltersDataBuilder;
import com.example.CenaClientes.databuilder.TableDataBuilder;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.feignClient.DecryptFeign;
import com.example.CenaClientes.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CenaServiceImplTest {

    @InjectMocks
    CenaServiceImpl cenaService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    DecryptFeign decryptFeign;

    @Mock
    MultipartFile multipartFile;


    @Test
    void getTablesTestCancelled() throws IOException {
        String wenas = "<General>\r\nTC:1\r\n<Mesa 1>\r\nUG:2\r\nRI:500000\r\nRF:10000\r\nTC:5";
        List<Client> clients = new ArrayList<>();
        clients.add(new ClientDataBuilder().buildClient());

        Mockito.when(multipartFile.getBytes()).thenReturn(wenas.getBytes(StandardCharsets.UTF_8));
        Mockito.when(clientRepository.findClientByCriteria(any())).thenReturn(clients);

        Assertions.assertEquals("<General>\r\nCANCELADA\r\n<Mesa 1>\r\nCANCELADA",cenaService.getTables(multipartFile));
    }

    @Test
    void getTablesTest() throws IOException {
        String wenas = "<General>\r\nTC:1";

        Filters filters = new FiltersDataBuilder().buildFilters();
        filters.setUg(null);
        filters.setRf(null);
        filters.setRi(null);


        List<Client> male = new ArrayList<>();
        Client client1 = new ClientDataBuilder().buildClient();
        Client client2 = new ClientDataBuilder().buildClient();
        Client client3 = new ClientDataBuilder().buildClient();

        male.add(client1);
        male.add(client2);
        male.add(client3);

        List<Client> female = new ArrayList<>();
        female.add(client1);
        female.add(client2);
        female.add(client3);

        Mockito.when(clientRepository.findClientByCriteria(filters)).thenReturn(male);
        Filters femaleFilters = new FiltersDataBuilder().buildFilters();
        femaleFilters.setLimit(male.size());
        femaleFilters.setMale(false);
        femaleFilters.setUg(null);
        femaleFilters.setRf(null);
        femaleFilters.setRi(null);
        Mockito.when(clientRepository.findClientByCriteria(femaleFilters)).thenReturn(female);

        Mockito.when(multipartFile.getBytes()).thenReturn(wenas.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals("<General>\r\nCOD1,COD1,COD1,COD1,COD1,COD1",cenaService.getTables(multipartFile));
    }

    @Test
    void getTablesTestException() throws IOException {
        String wenas = "<General>\r\nTC:1\r\n<Mesa 1>\r\nUG:2\r\nRI:500000\r\nRF:10000\r\nKappa:5";
        List<Client> clients = new ArrayList<>();
        clients.add(new ClientDataBuilder().buildClient());

        Mockito.when(multipartFile.getBytes()).thenReturn(wenas.getBytes(StandardCharsets.UTF_8));
        Mockito.when(clientRepository.findClientByCriteria(any())).thenReturn(clients);

        Assertions.assertEquals("The filter Kappa wasn't found",cenaService.getTables(multipartFile));
    }

    @Test
    void runFiltersTestNotEnoughMaleClients() {
        Filters filters = new FiltersDataBuilder().buildFilters();
        Table table = new TableDataBuilder().buildTable();


        List<Client> male = new ArrayList<>();
        Client client1 = new ClientDataBuilder().buildClient();

        male.add(client1);

        Mockito.when(clientRepository.findClientByCriteria(filters)).thenReturn(male);

        Assertions.assertEquals(0,cenaService.runFilters(filters,table).getClients().size());
    }

    @Test
    void runFiltersTestFemaleSizeIsDifferent() {
        Filters filters = new FiltersDataBuilder().buildFilters();
        Table table = new TableDataBuilder().buildTable();


        List<Client> male = new ArrayList<>();
        Client client1 = new ClientDataBuilder().buildClient();
        Client client2 = new ClientDataBuilder().buildClient();
        Client client3 = new ClientDataBuilder().buildClient();

        male.add(client1);
        male.add(client2);
        male.add(client3);

        List<Client> female = new ArrayList<>();
        female.add(client1);

        Mockito.when(clientRepository.findClientByCriteria(filters)).thenReturn(male);
        Filters femaleFilters = new FiltersDataBuilder().buildFilters();
        femaleFilters.setLimit(male.size());
        femaleFilters.setMale(false);
        Mockito.when(clientRepository.findClientByCriteria(femaleFilters)).thenReturn(female);

        Assertions.assertEquals(0,cenaService.runFilters(filters,table).getClients().size());
    }

    @Test
    void runFiltersTestNotEcryptedCodes() {
        Filters filters = new FiltersDataBuilder().buildFilters();
        Table table = new TableDataBuilder().buildTable();


        List<Client> male = new ArrayList<>();
        Client client1 = new ClientDataBuilder().buildClient();
        Client client2 = new ClientDataBuilder().buildClient();
        Client client3 = new ClientDataBuilder().buildClient();

        male.add(client1);
        male.add(client2);
        male.add(client3);

        List<Client> female = new ArrayList<>();
        female.add(client1);
        female.add(client2);
        female.add(client3);

        Mockito.when(clientRepository.findClientByCriteria(filters)).thenReturn(male);
        Filters femaleFilters = new FiltersDataBuilder().buildFilters();
        femaleFilters.setLimit(male.size());
        femaleFilters.setMale(false);
        Mockito.when(clientRepository.findClientByCriteria(femaleFilters)).thenReturn(female);

        Assertions.assertEquals(6,cenaService.runFilters(filters,table).getClients().size());
    }

    @Test
    void runFiltersTestEcryptedCodes() {
        Filters filters = new FiltersDataBuilder().buildFilters();
        Table table = new TableDataBuilder().buildTable();


        List<Client> male = new ArrayList<>();
        Client client1 = new ClientDataBuilder().buildClient();
        Client client2 = new ClientDataBuilder().buildClient();
        Client client3 = new ClientDataBuilder().buildClient();
        client3.setEncrypt(true);

        male.add(client1);
        male.add(client2);
        male.add(client3);

        List<Client> female = new ArrayList<>();
        female.add(client1);
        female.add(client2);
        female.add(client3);

        Mockito.when(clientRepository.findClientByCriteria(filters)).thenReturn(male);
        Filters femaleFilters = new FiltersDataBuilder().buildFilters();
        femaleFilters.setLimit(male.size());
        femaleFilters.setMale(false);
        Mockito.when(clientRepository.findClientByCriteria(femaleFilters)).thenReturn(female);

        Mockito.when(decryptFeign.getUncryptedCode(any())).thenReturn("Kappa");

        Assertions.assertEquals(6,cenaService.runFilters(filters,table).getClients().size());
    }
}