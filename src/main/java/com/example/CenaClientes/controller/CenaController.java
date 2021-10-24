package com.example.CenaClientes.controller;

import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.service.CenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CenaController {

    @Autowired
    private CenaService cenaService;

    @PostMapping
    public ResponseEntity<String> getTables(@RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(cenaService.getTables(multipartFile));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients(){
        return ResponseEntity.ok(cenaService.listAllClients());
    }

    @GetMapping(value = "/account")
    public ResponseEntity<List<Account>> getAccount(){
        return ResponseEntity.ok(cenaService.listAllAccounts());
    }

}
