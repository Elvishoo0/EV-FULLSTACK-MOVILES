package com.example.demo.repository;

import com.example.demo.model.Pedido;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findByUsuarioId(String usuarioId);
}