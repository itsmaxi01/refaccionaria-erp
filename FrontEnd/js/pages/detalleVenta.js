import { cargarClientes } from "../api/Clientes.js";
import { dibujarClientes } from "../components/dibujarClientes.js";
import { dibujarDetalleVenta } from "../components/dibujarDetalleVenta.js";
import { obtenerCarrito } from "../localStorage/carritoStorage.js";
import { eliminarCarrito } from "../localStorage/carritoStorage.js";
import { registrarVenta } from "../api/Venta.js";
import { crearControlPago } from "../components/pagoForm.js";


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
    montoPago.addEventListener("input", () => { pagoEditado = true; });

    function obtenerTotalVenta() {
        return venta.detalles.reduce(
            (acc, item) => acc + item.cantidad * item.precioUnitario,
            0
        );
    }

    const controlPago = crearControlPago({
        montoInput: montoPago,
        metodoInput: metodoPago,
        montoAbonadoElemento,
        cambioElemento,
        filaCambio,
        errorElemento: errorPago,
        obtenerSaldo: obtenerTotalVenta
    });

    function actualizarSubtotal() {

        const subtotal = obtenerTotalVenta();

        subtotalElemento.textContent = `Total: $${subtotal.toFixed(2)}`;

        // Opcional: llenar automáticamente el monto de pago
        if (!pagoEditado) montoPago.value = subtotal.toFixed(2);
        controlPago.actualizar();
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

        venta.pago = controlPago.obtenerPago();

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
