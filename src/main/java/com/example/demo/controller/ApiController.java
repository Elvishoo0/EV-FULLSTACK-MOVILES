package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Ruta base para todos los endpoints de este controlador
public class ApiController {

    // --- Inyección de dependencias por constructor (práctica recomendada) ---
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public ApiController(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    // --- Endpoints para obtener listas ---

    /**
     * Devuelve una lista de todos los usuarios registrados.
     * Se accede a través de GET /api/usuarios
     */
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Devuelve una lista de todos los productos registrados.
     * Se accede a través de GET /api/productos
     */
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return ResponseEntity.ok(productos);
    }

    // --- Endpoints de Autenticación ---

    /**
     * Registra un nuevo usuario en la base de datos.
     * Se accede a través de POST /api/register
     */
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(usuarioGuardado);
    }

    /**
     * Simula un login buscando un usuario por su correo electrónico.
     * IMPORTANTE: No verifica la contraseña.
     * Se accede a través de POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuarioLogin) {
        // Usa el método findByCorreo que definimos en el repositorio
        return usuarioRepository.findByCorreo(usuarioLogin.getCorreo())
                .map(usuarioEncontrado -> ResponseEntity.ok(usuarioEncontrado)) // Si lo encuentra, devuelve 200 OK con el usuario
                .orElse(ResponseEntity.status(404).body(null)); // Si no, devuelve 404 Not Found
    }
}