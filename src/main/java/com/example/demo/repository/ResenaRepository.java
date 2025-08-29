package com.example.demo.repository;

import com.example.demo.model.Resena;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResenaRepository extends MongoRepository<Resena, String> {
}