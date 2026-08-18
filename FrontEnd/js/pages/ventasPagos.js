import { ventasPendiente } from "../api/ventaPendiente.js";
import { dibujarVentas } from "../components/dibujarVentas.js";
import { filtrarVentasPendientes } from "../components/pagosLogic.js";


async function ventasPagos(){
    const ventas = await ventasPendiente();
    console.log(ventas);
    const contenedorVentas = document.getElementById("ventasPendientes");
    dibujarVentas(ventas, contenedorVentas);
    const buscador = document.getElementById("buscador");
    buscador.addEventListener("input",()=>{
        console.log(ventas);
        const textoBuscado = buscador.value.toLowerCase();
        const ventasBuscadas = filtrarVentasPendientes(textoBuscado,ventas); //filtro por nombre
        dibujarVentas(ventasBuscadas,contenedorVentas);



    });


}
ventasPagos();