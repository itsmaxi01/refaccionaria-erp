import { obtenerCliente } from "../api/Clientes.js";
import { dibujarCliente } from "../components/dibujarClientes.js";
import { generarPagos } from "../api/generarPago.js";
import { totalByIdVenta, totalPagadoByIdVenta } from "../api/ventaPendiente.js";
import { crearControlPago } from "../components/pagoForm.js";



async function generarPago(){
    //obtener url
const params = new URLSearchParams(window.location.search);

const idVenta = Number(params.get("id"));
const idCliente = Number(params.get("idCliente"));

if (!Number.isInteger(idVenta) || idVenta <= 0) {
    throw new Error("La venta indicada no es valida");
}

if (!Number.isInteger(idCliente) || idCliente <= 0) {
    throw new Error("El cliente indicado no es valido");
}

//traer cliente completo 
const cliente = await obtenerCliente(idCliente);

//obtener contenedores
const contenedorCliente = document.getElementById("cliente");
dibujarCliente(contenedorCliente,cliente);


const formPago = document.getElementById("formPago");
const botonPago = document.getElementById("registrarPago");
const montoRecibidoInput = document.getElementById("montoRecibido");
const metodoInput = document.getElementById("tipoPago");
const montoAbonadoElemento = document.getElementById("montoAbonado");
const cambioElemento = document.getElementById("cambio");
const filaCambio = document.getElementById("filaCambio");
const errorPago = document.getElementById("errorPago");
const totalVenta = Number(await totalByIdVenta(idVenta));
const totalPagado = Number(await totalPagadoByIdVenta(idVenta));
const saldoPendiente = Number((totalVenta - totalPagado).toFixed(2));

if (!Number.isFinite(totalVenta) || !Number.isFinite(totalPagado)) {
    throw new Error("No se pudo calcular el saldo de la venta");
}

document.getElementById("saldoPendiente").textContent = saldoPendiente.toFixed(2);

if (saldoPendiente <= 0) {
    botonPago.disabled = true;
    throw new Error("La venta ya no tiene saldo pendiente");
}

const controlPago = crearControlPago({
    montoInput: montoRecibidoInput,
    metodoInput,
    montoAbonadoElemento,
    cambioElemento,
    filaCambio,
    errorElemento: errorPago,
    obtenerSaldo: () => saldoPendiente
});

formPago.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (botonPago.disabled) return;
    botonPago.disabled = true;

    try {
        await generarPagos(controlPago.obtenerPago(), idVenta);

        window.location.href = "../index.html";


    } catch (error) {
        console.error("Error al generar el pago:", error);
        alert(error.message || "No se pudo registrar el pago");
        botonPago.disabled = false;
    }

});




    


}

generarPago().catch(error => {
    console.error(error);
    document.getElementById("registrarPago").disabled = true;
    alert("No se pudo cargar el formulario: " + error.message);
});
