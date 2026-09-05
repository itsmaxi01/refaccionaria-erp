package com.refaccionaria.sistemapos.venta;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/detallesVentas")
public class DetalleVentaController {
        private final DetalleVentaService detalleVentaService;
        public DetalleVentaController(DetalleVentaService detalleventaService){
            this.detalleVentaService=detalleventaService;
        }
    @GetMapping("/venta/{idVenta}/total")
    public BigDecimal totalByidVenta(@PathVariable Integer idVenta){
            return detalleVentaService.TotalByVenta(idVenta);
    }
    @GetMapping
    public List<DetalleVenta> listarDetalles(){
            return detalleVentaService.ListarDetalles();
    }

    @GetMapping("/{idVenta}")
    public List<DetalleVenta> listarDetallesByIdVenta(@PathVariable Integer idVenta){
        return detalleVentaService.ListarDetallesById(idVenta);
    }
















}
