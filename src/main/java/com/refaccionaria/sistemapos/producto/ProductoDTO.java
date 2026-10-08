package com.refaccionaria.sistemapos.producto;

import java.math.BigDecimal;

public class ProductoDTO {
    private Integer idproducto;
    private String codigo_barras;
    private String nombre;
    private String tipo;
    private BigDecimal precio;
    private Boolean activo;

    public static ProductoDTO fromEntity(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoDTO dto = new ProductoDTO();
        dto.setIdproducto(producto.getIdproducto());
        dto.setCodigo_barras(producto.getCodigo_barras());
        dto.setNombre(producto.getNombre());
        dto.setTipo(producto.getTipo());
        dto.setPrecio(producto.getPrecio());
        dto.setActivo(producto.getActivo());
        return dto;
    }

    public Producto toEntity() {
        Producto producto = new Producto();
        producto.setIdproducto(idproducto);
        producto.setCodigo_barras(codigo_barras);
        producto.setNombre(nombre);
        producto.setTipo(tipo);
        producto.setPrecio(precio);
        if (activo != null) {
            producto.setActivo(activo);
        }
        return producto;
    }

    public Integer getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
