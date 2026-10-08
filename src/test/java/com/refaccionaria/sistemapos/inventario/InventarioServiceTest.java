package com.refaccionaria.sistemapos.inventario;

import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.producto.Producto;
import com.refaccionaria.sistemapos.producto.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        inventarioService = new InventarioService(inventarioRepository, productoRepository);
    }

    @Test
    void descontarReduceLaExistencia() {
        Inventario inventario = inventario(8, "A-1");
        when(inventarioRepository.findByIdinventarioAndActivoTrue(3)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.descontar(3, 5);

        assertEquals(3, resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void descontarNoPermiteStockNegativo() {
        Inventario inventario = inventario(2, "A-1");
        when(inventarioRepository.findByIdinventarioAndActivoTrue(3)).thenReturn(Optional.of(inventario));

        assertThrows(ConflictException.class, () -> inventarioService.descontar(3, 3));

        assertEquals(2, inventario.getCantidad());
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void guardarNoPermiteMismoProductoYUbicacionIgnorandoMayusculas() {
        Producto producto = productoActivo(10);
        Inventario existente = inventario(4, "Bodega Norte");
        existente.setProducto(producto);
        Inventario nuevo = inventario(3, " bodega norte ");
        nuevo.setProducto(producto);

        when(productoRepository.findById(10)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto_IdproductoAndActivoTrue(10)).thenReturn(List.of(existente));

        assertThrows(ConflictException.class, () -> inventarioService.GuardarInventario(nuevo));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void guardarRechazaProductoInactivo() {
        Producto producto = productoActivo(11);
        producto.setActivo(false);
        Inventario inventario = inventario(5, "A-2");
        inventario.setProducto(producto);
        when(productoRepository.findById(11)).thenReturn(Optional.of(producto));

        assertThrows(ConflictException.class, () -> inventarioService.GuardarInventario(inventario));
    }

    private Inventario inventario(int cantidad, String ubicacion) {
        Inventario inventario = new Inventario();
        inventario.setCantidad(cantidad);
        inventario.setUbicacion(ubicacion);
        inventario.setActivo(true);
        return inventario;
    }

    private Producto productoActivo(int id) {
        Producto producto = new Producto();
        producto.setIdproducto(id);
        producto.setActivo(true);
        return producto;
    }
}
