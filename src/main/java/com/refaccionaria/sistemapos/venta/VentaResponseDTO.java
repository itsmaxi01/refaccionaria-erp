package com.refaccionaria.sistemapos.venta;

import com.refaccionaria.sistemapos.cliente.ClienteDTO;

import java.time.LocalDate;

public class VentaResponseDTO {
    private Integer idventa;
    private ClienteDTO cliente;
    private LocalDate fecha;
    private EstadoVenta estado;
    private String tipo_venta;

    public static VentaResponseDTO fromEntity(Venta venta) {
        if (venta == null) {
            return null;
        }
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setIdventa(venta.getIdventa());
        dto.setCliente(ClienteDTO.fromEntity(venta.getCliente()));
        dto.setFecha(venta.getFecha());
        dto.setEstado(venta.getEstado());
        dto.setTipo_venta(venta.getTipo_venta());
        return dto;
    }

    public Integer getIdventa() {
        return idventa;
    }

    public void setIdventa(Integer idventa) {
        this.idventa = idventa;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }
}
