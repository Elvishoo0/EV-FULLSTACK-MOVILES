package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api") // Ruta base para todos los endpoints
public class ApiController {

    // Inyección de todos los repositorios que vamos a necesitar
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    // private final ResenaRepository resenaRepository; // Descomenta cuando lo necesites

    public ApiController(UsuarioRepository usuarioRepository, ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // =====================================================================================
    // ================== ENDPOINTS DE ADMINISTRACIÓN (ADMIN) ==============================
    // =====================================================================================

    // --- CRUD USUARIOS (ADMIN) ---

    @PostMapping("/admin/usuarios") // Crear un nuevo usuario
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        // TODO: En un caso real, la contraseña debería ser cifrada antes de guardar
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/admin/usuarios") // Obtener todos los usuarios
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
    
    @GetMapping("/admin/usuarios/{id}") // Obtener un usuario por ID
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable String id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/admin/usuarios/{id}") // Actualizar un usuario por ID
    public ResponseEntity<Usuario> updateUsuario(@PathVariable String id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setCorreo(usuarioActualizado.getCorreo());
                    usuario.setDireccion(usuarioActualizado.getDireccion());
                    usuario.setTipoUsuario(usuarioActualizado.getTipoUsuario()); // Admin puede cambiar el rol
                    // NO actualizamos la contraseña aquí por seguridad. Se hace en un endpoint aparte.
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/usuarios/{id}") // Eliminar un usuario por ID
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // --- CRUD PRODUCTOS (ADMIN) ---

    @PostMapping("/admin/productos") // Crear un nuevo producto
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto nuevoProducto) {
        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return new ResponseEntity<>(productoGuardado, HttpStatus.CREATED);
    }

    @PutMapping("/admin/productos/{id}") // Actualizar un producto existente
    public ResponseEntity<Producto> updateProducto(@PathVariable String id, @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    producto.setCodigo(productoActualizado.getCodigo());
                    return ResponseEntity.ok(productoRepository.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/productos/{id}") // Eliminar un producto
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================================================
    // ================== ENDPOINTS PÚBLICOS Y DE CLIENTES =================================
    // =====================================================================================

    // --- VISTA DE PRODUCTOS (PÚBLICO) ---

    @GetMapping("/productos") // Obtener todos los productos para el catálogo
    public ResponseEntity<List<Producto>> getAllProductosPublic() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @GetMapping("/productos/{id}") // Ver el detalle de un producto
    public ResponseEntity<Producto> getProductoByIdPublic(@PathVariable String id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // --- PERFIL DE USUARIO (CLIENTE) ---

    @GetMapping("/perfil/{usuarioId}") // Ver mis datos personales
    public ResponseEntity<Usuario> getMiPerfil(@PathVariable String usuarioId) {
        // TODO: En un sistema real, el ID del usuario se obtendría del token de seguridad, no de la URL.
        return usuarioRepository.findById(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/perfil/{usuarioId}") // Actualizar mis datos personales
    public ResponseEntity<Usuario> updateMiPerfil(@PathVariable String usuarioId, @RequestBody Usuario datosActualizados) {
        // TODO: Aquí también, el ID vendría del token.
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    usuario.setNombre(datosActualizados.getNombre());
                    usuario.setDireccion(datosActualizados.getDireccion());
                    usuario.setNumeroDeTelefono(datosActualizados.getNumeroDeTelefono());
                    // Un cliente NO puede cambiar su rol (tipoUsuario) ni su correo por esta vía.
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // --- GESTIÓN DE PEDIDOS (CLIENTE) ---

    @PostMapping("/pedidos") // Crear un nuevo pedido (simula el "checkout" del carrito)
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido nuevoPedido) {
        // TODO: Se debería validar que el usuario y los productos existen, y que hay stock.
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        return new ResponseEntity<>(pedidoGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/pedidos/usuario/{usuarioId}") // Ver mi historial de pedidos
    public ResponseEntity<List<Pedido>> getMisPedidos(@PathVariable String usuarioId) {
        // TODO: Proteger para que un usuario solo pueda ver sus propios pedidos.
        return ResponseEntity.ok(pedidoRepository.findByUsuarioId(usuarioId));
    }

    @GetMapping("/pedidos/{pedidoId}") // Ver el detalle de un pedido específico
    public ResponseEntity<Pedido> getDetallePedido(@PathVariable String pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}