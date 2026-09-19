import { cargarDetallesVentas } from "../api/detallesVentas.js";
import { totalByIdVenta, totalPagadoByIdVenta } from "../api/ventaPendiente.js";
import { dibujarTotal } from "../components/dibujarTotal.js";
import { dibujarVenta } from "../components/dibujarVentaPago.js";
import { dibujarDetalles } from "../components/detallesProductos.js";

async function detallesPagos() {

    const params = new URLSearchParams(window.location.search);
    const idVenta = params.get("id");

    // Obtener todos los detalles de la venta
    const detalles = await cargarDetallesVentas(idVenta);
    if (!detalles.length) throw new Error("No se encontraron detalles de esta venta");

    // Obtener totales
    const total = await totalByIdVenta(idVenta);
    const totalPagado = await totalPagadoByIdVenta(idVenta);

    // Dibujar resumen
    dibujarTotal(
        total,
        totalPagado,
        document.getElementById("total"),
        document.getElementById("totalPagado")
    );

    // Separar la información
    const venta = detalles[0].venta;
    const cliente = venta.cliente;

    const detallesProductos = detalles.map(({ venta, ...detalle }) => detalle);

    // Obtener contenedores
    const contenedorVenta = document.getElementById("Venta");
    const contenedorDetalles = document.getElementById("productos");

    // Dibujar información
    dibujarVenta(venta, contenedorVenta, total);
    dibujarDetalles(detallesProductos, contenedorDetalles);

    // Mandar a pagos
    const idCliente = cliente?.idCliente;

    const botonPago = document.getElementById("generarPago");

    if (idCliente == null) {
        botonPago.disabled = true;
        return;
    }

    botonPago.addEventListener("click", () => {

        window.location.href =
            `abonarPagos.html?id=${idVenta}&idCliente=${idCliente}`;

    });
}

detallesPagos().catch(error => {
    console.error(error);
    document.getElementById("generarPago").disabled = true;
    alert("No se pudo cargar el detalle: " + error.message);
});
