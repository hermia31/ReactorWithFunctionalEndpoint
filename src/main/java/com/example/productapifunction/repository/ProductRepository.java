package com.example.productapifunction.repository;

import com.example.productapifunction.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository
extends ReactiveMongoRepository<Product, String> {
}
