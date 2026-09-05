package com.refaccionaria.sistemapos.venta;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Ventas")
public class VentaController {

    private VentaService ventaService;

    public VentaController(VentaService ventaService){
        this.ventaService = ventaService;
    }


    @PostMapping
    public Venta RealizarVenta(@RequestBody VentaDto venta){
        return ventaService.crearVenta(venta);
    }


    @GetMapping
    public List<Venta> ventasPendientes(){ return ventaService.ventasPendientes();}










}

