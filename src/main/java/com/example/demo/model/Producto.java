package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "DB_CATALOGO") // Mapea a la colección "productos"
public class Producto {

    @Id
    private String id;

    @Field("nombre_inv")
    private String nombre;

    @Field("codigo_inv")
    private String codigo;

    @Field("precio_inv")
    private double precio;

    @Field("stock_inv")
    private int stock;

    @Field("tipoStock_inv")
    private String tipoStock;

    @Field("descripcion_inv")
    private String descripcion;
}