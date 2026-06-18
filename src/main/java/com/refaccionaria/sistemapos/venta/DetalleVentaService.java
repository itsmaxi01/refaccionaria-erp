package com.refaccionaria.sistemapos.venta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaService {
    private DetalleVentaRepository detalleVentaRepository;
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository){
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<DetalleVenta> ListarDetalles(){
        return detalleVentaRepository.findAll();
    }
    public DetalleVenta GuardarDetalle(DetalleVenta detalleVenta){
        return detalleVentaRepository.save(detalleVenta);
    }





}
