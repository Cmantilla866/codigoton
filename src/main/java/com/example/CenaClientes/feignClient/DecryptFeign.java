package com.example.CenaClientes.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for using the web service for decryption
 * */
@FeignClient(name = "code-decrypt", url = "https://test.evalartapp.com/extapiquest/code_decrypt")
public interface DecryptFeign {

    @GetMapping(value = "/{code}")
    public String getUncryptedCode(@PathVariable("code") String code);
}
