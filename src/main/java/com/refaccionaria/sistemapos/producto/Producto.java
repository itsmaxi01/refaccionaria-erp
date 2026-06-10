package com.refaccionaria.sistemapos.producto;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
public class Producto{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id_producto;
    String codigo_barras;
    String nombre;
    String tipo;
    BigDecimal precio_mayoreo;
    BigDecimal precio_menudo;
    boolean activo;


    public Producto(){

    }







}