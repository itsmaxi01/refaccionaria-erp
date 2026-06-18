package com.refaccionaria.sistemapos.venta;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

