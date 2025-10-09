package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ApiController {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public ApiController(UsuarioRepository usuarioRepository, ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // =====================================================================================
    // ================== ENDPOINTS DE ADMINISTRACIÓN (ADMIN) =============================D
    // =====================================================================================

    // --- CRUD USUARIOS (ADMIN) ---

    @Tag(name = "Admin: Gestión de Usuarios")
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema. Requiere rol de Administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    @PostMapping("/admin/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @Tag(name = "Admin: Gestión de Usuarios")
    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping("/admin/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @Tag(name = "Admin: Gestión de Usuarios")
    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/admin/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@Parameter(description = "ID del usuario a buscar") @PathVariable String id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Tag(name = "Admin: Gestión de Usuarios")
    @Operation(summary = "Actualizar un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/admin/usuarios/{id}")
    public ResponseEntity<Usuario> updateUsuario(@Parameter(description = "ID del usuario a actualizar") @PathVariable String id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setCorreo(usuarioActualizado.getCorreo());
                    usuario.setDireccion(usuarioActualizado.getDireccion());
                    usuario.setTipoUsuario(usuarioActualizado.getTipoUsuario());
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Tag(name = "Admin: Gestión de Usuarios")
    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/admin/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@Parameter(description = "ID del usuario a eliminar") @PathVariable String id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // --- CRUD PRODUCTOS (ADMIN) ---

    @Tag(name = "Admin: Gestión de Productos")
    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Producto creado")})
    @PostMapping("/admin/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto nuevoProducto) {
        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return new ResponseEntity<>(productoGuardado, HttpStatus.CREATED);
    }

    @Tag(name = "Admin: Gestión de Productos")
    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/admin/productos/{id}")
    public ResponseEntity<Producto> updateProducto(@Parameter(description = "ID del producto a actualizar") @PathVariable String id, @RequestBody Producto productoActualizado) {
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

    @Tag(name = "Admin: Gestión de Productos")
    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/admin/productos/{id}")
    public ResponseEntity<Void> deleteProducto(@Parameter(description = "ID del producto a eliminar") @PathVariable String id) {
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

    @Tag(name = "Cliente: Productos")
    @Operation(summary = "Obtener todos los productos del catálogo")
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getAllProductosPublic() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @Tag(name = "Cliente: Productos")
    @Operation(summary = "Ver el detalle de un producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> getProductoByIdPublic(@Parameter(description = "ID del producto a buscar") @PathVariable String id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // --- PERFIL DE USUARIO (CLIENTE) ---

    @Tag(name = "Cliente: Perfil")
    @Operation(summary = "Ver mis datos personales", description = "Obtiene los datos del perfil del usuario logueado. En esta versión de prueba, se pasa el ID por la URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/perfil/{usuarioId}")
    public ResponseEntity<Usuario> getMiPerfil(@Parameter(description = "ID del usuario cuyo perfil se desea ver") @PathVariable String usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Tag(name = "Cliente: Perfil")
    @Operation(summary = "Actualizar mis datos personales", description = "Permite a un usuario actualizar su nombre, dirección y teléfono.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/perfil/{usuarioId}")
    public ResponseEntity<Usuario> updateMiPerfil(@Parameter(description = "ID del usuario a actualizar") @PathVariable String usuarioId, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    usuario.setNombre(datosActualizados.getNombre());
                    usuario.setDireccion(datosActualizados.getDireccion());
                    usuario.setNumeroDeTelefono(datosActualizados.getNumeroDeTelefono());
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // --- GESTIÓN DE PEDIDOS (CLIENTE) ---

    @Tag(name = "Cliente: Pedidos")
    @Operation(summary = "Crear un nuevo pedido", description = "Simula el checkout de un carrito de compras para crear un registro de pedido.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Pedido creado exitosamente")})
    @PostMapping("/pedidos")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido nuevoPedido) {
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        return new ResponseEntity<>(pedidoGuardado, HttpStatus.CREATED);
    }

    @Tag(name = "Cliente: Pedidos")
    @Operation(summary = "Ver mi historial de pedidos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listado de pedidos del usuario")})
    @GetMapping("/pedidos/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> getMisPedidos(@Parameter(description = "ID del usuario para buscar sus pedidos") @PathVariable String usuarioId) {
        return ResponseEntity.ok(pedidoRepository.findByUsuarioId(usuarioId));
    }

    @Tag(name = "Cliente: Pedidos")
    @Operation(summary = "Ver el detalle de un pedido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/pedidos/{pedidoId}")
    public ResponseEntity<Pedido> getDetallePedido(@Parameter(description = "ID del pedido a buscar") @PathVariable String pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
