package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "DB_PEDIDOS") // Mapea a la colección DB_PEDIDOS
public class Pedido {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId; // Guarda el ID del usuario que hizo el pedido

    @Field("producto_ids")
    private List<String> productoIds; // Lista de IDs de los productos comprados

    @Field("fecha_pedido")
    private Date fechaPedido; // Fecha en que se realizó el pedido

    @Field("monto_total")
    private double montoTotal;

    @Field("estado") // Ej: "Pendiente", "Enviado", "Entregado"
    private String estado;

}