package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data 
@Document(collection = "DB_USERS") 
public class Usuario {

    @Id
    private String id;

    private String correo;
    private String contrasena;

    @Field("tipo_usuario") 
    private String tipoUsuario;

    private String nombre;
    private String direccion;
    private String rut;

    @Field("numero_de_telefono")
    private String numeroDeTelefono;
}