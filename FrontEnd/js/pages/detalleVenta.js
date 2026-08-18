import { cargarClientes } from "../api/Clientes.js";
import { dibujarClientes } from "../components/dibujarClientes.js";
import { dibujarDetalleVenta } from "../components/dibujarDetalleVenta.js";
import { obtenerCarrito } from "../localStorage/carritoStorage.js";
import { eliminarCarrito } from "../localStorage/carritoStorage.js";
import { registrarVenta } from "../api/Venta.js";


async function detalleVenta() {

    const contenedorClientes = document.getElementById("cliente");
    const contenedorDetalleVenta = document.getElementById("detalleVenta");

    const tipoVenta = document.getElementById("tipoVenta");
    const montoPago = document.getElementById("montoPago");
    const metodoPago = document.getElementById("metodoPago");
    const btnConfirmar = document.getElementById("confirmarVenta");
    const subtotalElemento = document.getElementById("subtotal");

    const clientes = await cargarClientes();
    console.log(clientes);
    const carrito = obtenerCarrito();

    const venta = {
        idCliente: null,
        tipoVenta: tipoVenta.value,
        detalles: carrito.map(item => ({
            idInventario: item.id_inventario,
            cantidad: item.cantidadVenta,
            precioUnitario: item.producto.precio
        })),
        pago: {
            monto: 0,
            metodo: metodoPago.value
        }
    };

    function actualizarSubtotal() {

        const subtotal = venta.detalles.reduce(
            (acc, item) => acc + item.cantidad * item.precioUnitario,
            0
        );

        subtotalElemento.textContent = `Total: $${subtotal.toFixed(2)}`;

        // Opcional: llenar automáticamente el monto de pago
        montoPago.value = subtotal.toFixed(2);
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

        eliminarCarrito();
        

    venta.tipoVenta = tipoVenta.value;

    venta.pago = {
        monto: Number(montoPago.value),
        metodo: metodoPago.value
    };

    try {

        await registrarVenta(venta);

        window.location.href = "../index.html";

    } catch (error) {

        console.error(error);
        alert("No se pudo registrar la venta");

    }

});

}

detalleVenta();