import { cargarClientes } from "../api/Clientes.js";
import { dibujarClientes } from "../components/dibujarClientes.js";
import { dibujarDetalleVenta } from "../components/dibujarDetalleVenta.js";
import { obtenerCarrito } from "../localStorage/carritoStorage.js";
import { eliminarCarrito } from "../localStorage/carritoStorage.js";
import { registrarVenta } from "../api/Venta.js";
import { calcularPago } from "../components/pagosLogic.js";


async function detalleVenta() {

    const contenedorClientes = document.getElementById("cliente");
    const contenedorDetalleVenta = document.getElementById("detalleVenta");

    const tipoVenta = document.getElementById("tipoVenta");
    const montoPago = document.getElementById("montoPago");
    const metodoPago = document.getElementById("metodoPago");
    const btnConfirmar = document.getElementById("confirmarVenta");
    const subtotalElemento = document.getElementById("subtotal");
    const montoAbonadoElemento = document.getElementById("montoAbonado");
    const cambioElemento = document.getElementById("cambio");
    const filaCambio = document.getElementById("filaCambio");
    const errorPago = document.getElementById("errorPago");

    const clientes = await cargarClientes();
    console.log(clientes);
    const carrito = obtenerCarrito();

    if (!Array.isArray(carrito) || carrito.length === 0) {
        throw new Error("El carrito esta vacio");
    }

    const venta = {
        idCliente: null,
        tipoVenta: tipoVenta.value,
        detalles: carrito.map(item => ({
            idInventario: Number(item.id_inventario),
            cantidad: Number(item.cantidadVenta),
            precioUnitario: Number(item.producto?.precio)
        })),
        pago: {
            monto_abonado: 0,
            monto_recibido: 0,
            metodo: metodoPago.value
        }
    };

    let pagoEditado = false;
    montoPago.addEventListener("input", () => {
        pagoEditado = true;
        actualizarResumenPago();
    });
    metodoPago.addEventListener("change", actualizarResumenPago);

    function obtenerTotalVenta() {
        return venta.detalles.reduce(
            (acc, item) => acc + item.cantidad * item.precioUnitario,
            0
        );
    }

    function actualizarResumenPago() {
        try {
            const { pago, cambio } = calcularPago(
                montoPago.value,
                obtenerTotalVenta(),
                metodoPago.value
            );

            montoAbonadoElemento.textContent = pago.monto_abonado.toFixed(2);
            cambioElemento.textContent = cambio.toFixed(2);
            filaCambio.hidden = pago.metodo !== "EFECTIVO";
            errorPago.textContent = "";
        } catch (error) {
            montoAbonadoElemento.textContent = "0.00";
            cambioElemento.textContent = "0.00";
            filaCambio.hidden = metodoPago.value !== "EFECTIVO";
            errorPago.textContent = montoPago.value ? error.message : "";
        }
    }

    function actualizarSubtotal() {

        const subtotal = obtenerTotalVenta();

        subtotalElemento.textContent = `Total: $${subtotal.toFixed(2)}`;

        // Opcional: llenar automáticamente el monto de pago
        if (!pagoEditado) montoPago.value = subtotal.toFixed(2);
        actualizarResumenPago();
    }

    // Calcula el subtotal inicial
    actualizarSubtotal();

    // Cliente
    dibujarClientes(
        contenedorClientes,
        clientes,
        clienteSeleccionado => {

            venta.idCliente = clienteSeleccionado
                ? Number(clienteSeleccionado)
                : null;

            console.log(venta);

        }
    );

    // Productos
    dibujarDetalleVenta(
        contenedorDetalleVenta,
        venta.detalles,
        carrito,
        detallesActualizados => {

            venta.detalles = detallesActualizados;

            actualizarSubtotal();

            console.log(venta);

        }
    );

    btnConfirmar.addEventListener("click", async () => {

        if (btnConfirmar.disabled) return;
        btnConfirmar.disabled = true;

    try {

        if (venta.detalles.length === 0 || venta.detalles.some(detalle =>
            !Number.isInteger(detalle.idInventario) || detalle.idInventario <= 0 ||
            !Number.isFinite(detalle.cantidad) || detalle.cantidad <= 0 ||
            !Number.isFinite(detalle.precioUnitario) || detalle.precioUnitario < 0
        )) {
            throw new Error("Hay productos con datos invalidos en la venta");
        }

        venta.tipoVenta = tipoVenta.value;

        if (!venta.tipoVenta) {
            throw new Error("Selecciona un tipo de venta");
        }

        const { pago } = calcularPago(
            montoPago.value,
            obtenerTotalVenta(),
            metodoPago.value
        );
        venta.pago = pago;

        await registrarVenta(venta);
        eliminarCarrito();

        window.location.href = "../index.html";

    } catch (error) {

        console.error(error);
        alert(error.message || "No se pudo registrar la venta");
        btnConfirmar.disabled = false;

    }

});

}

detalleVenta().catch(error => {
    console.error(error);
    document.getElementById("confirmarVenta").disabled = true;
    alert("No se pudo cargar la venta: " + error.message);
});
