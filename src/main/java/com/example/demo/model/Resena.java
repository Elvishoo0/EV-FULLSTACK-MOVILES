package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "DB_RESENAS") // Mapea a la colección DB_RESENAS
public class Resena {

    @Id
    private String id;

    @Field("producto_id")
    private String productoId; // ID del producto que se está reseñando

    @Field("usuario_id")
    private String usuarioId; // ID del usuario que escribe la reseña

    @Field("calificacion") // Ej: un número del 1 al 5
    private int calificacion;

    @Field("comentario")
    private String comentario;

    @Field("fecha_resena")
    private Date fechaResena;

}