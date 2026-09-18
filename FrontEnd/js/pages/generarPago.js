import { obtenerCliente } from "../api/Clientes.js";
import { dibujarCliente } from "../components/dibujarClientes.js";
import { generarPagos } from "../api/generarPago.js";
import { totalByIdVenta, totaPagadoByIdVenta } from "../api/ventaPendiente.js";
import { calcularPago } from "../components/pagosLogic.js";



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

console.log("idVenta:", idVenta);
console.log("idCliente:", idCliente);

//traer cliente completo 
const cliente = await obtenerCliente(idCliente);
console.log("cliente:", cliente);

//obtener contenedores
const contenedorCliente = document.getElementById("cliente");
console.log("contenedorCliente:", contenedorCliente);
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
const totalPagado = Number(await totaPagadoByIdVenta(idVenta));
const saldoPendiente = Number((totalVenta - totalPagado).toFixed(2));

if (!Number.isFinite(totalVenta) || !Number.isFinite(totalPagado)) {
    throw new Error("No se pudo calcular el saldo de la venta");
}

document.getElementById("saldoPendiente").textContent = saldoPendiente.toFixed(2);

if (saldoPendiente <= 0) {
    botonPago.disabled = true;
    throw new Error("La venta ya no tiene saldo pendiente");
}

function actualizarResumenPago() {
    try {
        const { pago, cambio } = calcularPago(
            montoRecibidoInput.value,
            saldoPendiente,
            metodoInput.value
        );

        montoAbonadoElemento.textContent = pago.monto_abonado.toFixed(2);
        cambioElemento.textContent = cambio.toFixed(2);
        filaCambio.hidden = pago.metodo !== "EFECTIVO";
        errorPago.textContent = "";
    } catch (error) {
        montoAbonadoElemento.textContent = "0.00";
        cambioElemento.textContent = "0.00";
        filaCambio.hidden = metodoInput.value !== "EFECTIVO";
        errorPago.textContent = montoRecibidoInput.value ? error.message : "";
    }
}

montoRecibidoInput.addEventListener("input", actualizarResumenPago);
metodoInput.addEventListener("change", actualizarResumenPago);
actualizarResumenPago();

formPago.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (botonPago.disabled) return;
    botonPago.disabled = true;

    try {
        const { pago } = calcularPago(
            montoRecibidoInput.value,
            saldoPendiente,
            metodoInput.value
        );

        await generarPagos(pago, idVenta);

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
