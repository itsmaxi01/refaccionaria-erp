package com.refaccionaria.sistemapos.pago;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/Pago")
public class PagoController {

    private PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<PagoResponseDTO> ListarPagos() {
        return pagoService.ListarPagos().stream()
                .map(PagoResponseDTO::fromEntity)
                .toList();
    }

    @PostMapping("/{idVenta}")
    public PagoResponseDTO registrarPago(@RequestBody PagoDTO pago, @PathVariable Integer idVenta) {
        return PagoResponseDTO.fromEntity(pagoService.Registrar_Pago(pago, idVenta));
    }
    @GetMapping("/Venta/{idVenta}")
    public BigDecimal totalPagadoById(@PathVariable Integer idVenta) {
        return pagoService.totalPagadoById(idVenta);
    }



}
