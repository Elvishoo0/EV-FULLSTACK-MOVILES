package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // Todas las rutas en este controlador empezarán con /api
public class ApiController {

    @Autowired // Inyección de dependencias para usar el repositorio de usuarios
    private UsuarioRepository usuarioRepository;

    @Autowired // Inyección de dependencias para el repositorio de productos
    private ProductoRepository productoRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios); // Devuelve la lista con un código 200 OK
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return ResponseEntity.ok(productos); // Devuelve la lista de productos
    }
}