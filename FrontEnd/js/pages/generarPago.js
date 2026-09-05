import { obtenerCliente } from "../api/Clientes.js";
import { dibujarCliente } from "../components/dibujarClientes.js";
import { generarPagos } from "../api/generarPago.js";



async function generarPago(){
    //obtener url
const params = new URLSearchParams(window.location.search);

const idVenta = params.get("id");
const idCliente = params.get("idCliente");

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

formPago.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (botonPago.disabled) return;
    botonPago.disabled = true;

    const monto = document.getElementById("monto").value;
    const metodo = document.getElementById("tipoPago").value;

    console.log(monto);
    console.log(metodo);
     const pago = {
        monto: monto,
        metodo: metodo
    };


    try {
        const respuesta = await generarPagos(pago, idVenta);

        console.log("Pago generado:", respuesta);
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