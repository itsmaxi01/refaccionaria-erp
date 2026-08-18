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

formPago.addEventListener("submit", async (event) => {
    event.preventDefault();

    const monto = document.getElementById("monto").value;
    const metodo = document.getElementById("tipoPago").value;

    console.log(monto);
    console.log(tipoPago);
     const pago = {
        monto: monto,
        metodo: metodo
    };


    try {
        const respuesta = await generarPagos(pago, idVenta);

        console.log("Pago generado:", respuesta);


    } catch (error) {
        console.error("Error al generar el pago:", error);
    }
    window.location.href = "../index.html";

});




    


}

generarPago();