package com.example.CenaClientes.controller;

import com.example.CenaClientes.classes.Table;
import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import com.example.CenaClientes.exception.CustomException;
import com.example.CenaClientes.service.CenaService;
import io.swagger.annotations.*;
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
@SwaggerDefinition(tags = {@Tag(name = "General", description = "RestController for the assignment of clients to tables")})
public class CenaController {

    @Autowired
    private CenaService cenaService;

    @ApiOperation(value = "Assigns clients to tables", notes = "Assign different clients to different tables based on the defined criteria")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful assignment fo clients", response = String.class),
            @ApiResponse(code = 400, message = "Transaction error, due to and error in the input", response = CustomException.class)})
    @PostMapping
    public ResponseEntity<String> getTables(@RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(cenaService.getTables(multipartFile));
    }

}
