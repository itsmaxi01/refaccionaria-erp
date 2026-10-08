package com.refaccionaria.sistemapos.venta;

import com.refaccionaria.sistemapos.cliente.Cliente;
import com.refaccionaria.sistemapos.cliente.ClienteRepository;
import com.refaccionaria.sistemapos.inventario.Inventario;
import com.refaccionaria.sistemapos.inventario.InventarioRepository;
import com.refaccionaria.sistemapos.pago.PagoRepository;
import com.refaccionaria.sistemapos.producto.Producto;
import com.refaccionaria.sistemapos.producto.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class VentaIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private PagoRepository pagoRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        pagoRepository.deleteAll();
        detalleVentaRepository.deleteAll();
        ventaRepository.deleteAll();
        inventarioRepository.deleteAll();
        clienteRepository.deleteAll();
        productoRepository.deleteAll();
    }

    @Test
    void ventaPagadaRecorreHttpServicioYBaseDeDatos() throws Exception {
        Inventario inventario = guardarInventario("Manzana", "15.50", 20);

        mockMvc.perform(post("/Ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ventaJson(null, inventario.getId_inventario(), 2, "31.00", "40.00")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"estado\":\"PAGADA\"")));

        assertEquals(1, ventaRepository.count());
        assertEquals(1, detalleVentaRepository.count());
        assertEquals(1, pagoRepository.count());
        assertEquals(18, inventarioRepository.findById(inventario.getId_inventario()).orElseThrow().getCantidad());

        Venta venta = ventaRepository.findAll().getFirst();
        mockMvc.perform(get("/detallesVentas/{idVenta}", venta.getIdventa()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idDetalle\"")))
                .andExpect(content().string(containsString("\"inventario\"")))
                .andExpect(content().string(containsString("\"precioUnitario\":15.50")));

        mockMvc.perform(get("/Pago"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idpago\"")))
                .andExpect(content().string(containsString("\"monto_abonado\":31.00")));
    }

    @Test
    void faltaDeStockDevuelveConflictoYRevierteTodaLaVenta() throws Exception {
        Inventario inventario = guardarInventario("Pera", "20.00", 1);

        mockMvc.perform(post("/Ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ventaJson(null, inventario.getId_inventario(), 2, "40.00", "40.00")))
                .andExpect(status().isConflict());

        assertEquals(0, ventaRepository.count());
        assertEquals(0, detalleVentaRepository.count());
        assertEquals(0, pagoRepository.count());
        assertEquals(1, inventarioRepository.findById(inventario.getId_inventario()).orElseThrow().getCantidad());
    }

    @Test
    void ventaParcialYAbonoPosteriorTerminanSaldados() throws Exception {
        Inventario inventario = guardarInventario("Naranja", "50.00", 10);
        Cliente cliente = new Cliente("Cliente prueba", "5550000000", "Dirección", "MINORISTA");
        cliente = clienteRepository.save(cliente);

        mockMvc.perform(post("/Ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ventaJson(cliente.getIdCliente(), inventario.getId_inventario(), 2, "40.00", "40.00")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"estado\":\"PARCIAL\"")));

        Venta venta = ventaRepository.findAll().getFirst();
        mockMvc.perform(post("/Pago/{idVenta}", venta.getIdventa())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "monto_abonado": 60.00,
                                  "monto_recibido": 60.00,
                                  "metodo": "TRANSFERENCIA"
                                }
                                """))
                .andExpect(status().isOk());

        assertEquals(EstadoVenta.SALDADA, ventaRepository.findById(venta.getIdventa()).orElseThrow().getEstado());
        assertEquals(2, pagoRepository.findByVenta_Idventa(venta.getIdventa()).size());
    }

    @Test
    void dtosDeCatalogosConservanEntradasYContratoJson() throws Exception {
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo_barras": "ABC-123",
                                  "nombre": "Producto DTO",
                                  "tipo": "PRUEBA",
                                  "precio": 12.50
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"codigo_barras\":\"ABC-123\"")))
                .andExpect(content().string(containsString("\"activo\":true")));

        Producto producto = productoRepository.findAll().getFirst();
        mockMvc.perform(post("/Inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "producto": {"idproducto": %d},
                                  "cantidad": 7,
                                  "ubicacion": "A-DTO"
                                }
                                """.formatted(producto.getIdproducto())))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id_inventario\"")))
                .andExpect(content().string(containsString("\"producto\"")))
                .andExpect(content().string(containsString("\"cantidad\":7")));

        mockMvc.perform(post("/Clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Cliente DTO",
                                  "telefono": "5551234567",
                                  "direccion": "Dirección DTO",
                                  "tipoCliente": "MINORISTA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idCliente\"")))
                .andExpect(content().string(containsString("\"tipoCliente\":\"MINORISTA\"")))
                .andExpect(content().string(containsString("\"activo\":true")));

        mockMvc.perform(get("/productos/{id}", producto.getIdproducto()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idproducto\":" + producto.getIdproducto())));
    }

    private Inventario guardarInventario(String nombre, String precio, int cantidad) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setTipo("FRUTA");
        producto.setCodigo_barras("TEST-" + nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setActivo(true);
        producto = productoRepository.save(producto);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(cantidad);
        inventario.setUbicacion("PRUEBAS");
        inventario.setActivo(true);
        return inventarioRepository.save(inventario);
    }

    private String ventaJson(Integer clienteId, Integer inventarioId, int cantidad,
                             String montoAbonado, String montoRecibido) {
        String cliente = clienteId == null ? "null" : clienteId.toString();
        return """
                {
                  "idCliente": %s,
                  "tipoVenta": "CONTADO",
                  "detalles": [
                    {
                      "idInventario": %d,
                      "cantidad": %d
                    }
                  ],
                  "pago": {
                    "monto_abonado": %s,
                    "monto_recibido": %s,
                    "metodo": "EFECTIVO"
                  }
                }
                """.formatted(cliente, inventarioId, cantidad, montoAbonado, montoRecibido);
    }
}
