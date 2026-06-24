package com.refaccionaria.sistemapos.pago;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Pago")
public class PagoController {

    private PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> ListarPagos() {
        return pagoService.ListarPagos();
    }

    @PostMapping("/{idVenta}")
    public Pago registrarPago(@RequestBody PagoDTO pago, @PathVariable Integer idVenta) {
        return pagoService.Registrar_Pago(pago, idVenta);
    }


}
