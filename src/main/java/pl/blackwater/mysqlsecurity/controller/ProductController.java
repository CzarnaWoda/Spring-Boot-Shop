package pl.blackwater.mysqlsecurity.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.blackwater.mysqlsecurity.data.HttpResponse;
import pl.blackwater.mysqlsecurity.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.*;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ObjectMapper mapper;


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpResponse> getAllProducts(){
        return ResponseEntity.status(ACCEPTED).body(HttpResponse
                .builder()
                .timeStamp(now().toString())
                        .message("all products")
                        .statusCode(ACCEPTED.value())
                        .status(ACCEPTED)
                        .data(Map.of("Products",productRepository.findAll()))
                .build());
    }
    @GetMapping(value = "entity", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<HttpResponse> getProductEntity(){
        return ResponseEntity.status(ACCEPTED).body(HttpResponse.builder().timeStamp(now().toString()).build());
    }


}
