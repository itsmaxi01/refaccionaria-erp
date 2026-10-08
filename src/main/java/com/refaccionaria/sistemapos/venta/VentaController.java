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
    public VentaResponseDTO RealizarVenta(@RequestBody VentaDto venta){
        return VentaResponseDTO.fromEntity(ventaService.crearVenta(venta));
    }


    @GetMapping
    public List<VentaResponseDTO> ventasPendientes(){
        return ventaService.ventasPendientes().stream()
                .map(VentaResponseDTO::fromEntity)
                .toList();
    }










}

