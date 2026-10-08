package com.refaccionaria.sistemapos.inventario;

import com.refaccionaria.sistemapos.producto.ProductoDTO;

public class InventarioDTO {
    private Integer id_inventario;
    private ProductoDTO producto;
    private Integer cantidad;
    private String ubicacion;
    private Boolean activo;

    public static InventarioDTO fromEntity(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        InventarioDTO dto = new InventarioDTO();
        dto.setId_inventario(inventario.getId_inventario());
        dto.setProducto(ProductoDTO.fromEntity(inventario.getProducto()));
        dto.setCantidad(inventario.getCantidad());
        dto.setUbicacion(inventario.getUbicacion());
        dto.setActivo(inventario.getActivo());
        return dto;
    }

    public Inventario toEntity() {
        Inventario inventario = new Inventario();
        inventario.setProducto(producto == null ? null : producto.toEntity());
        inventario.setCantidad(cantidad);
        inventario.setUbicacion(ubicacion);
        if (activo != null) {
            inventario.setActivo(activo);
        }
        return inventario;
    }

    public Integer getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(Integer id_inventario) {
        this.id_inventario = id_inventario;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
