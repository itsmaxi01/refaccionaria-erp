import { totalByIdVenta } from "../api/ventaPendiente.js";
export async function dibujarVenta(venta,contenedorVenta){
    console.log(venta);
    const total = await totalByIdVenta(venta.idventa);

    contenedorVenta.innerHTML = `
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Venta</h5>
            <p class="card-text">ID: ${venta.idventa}</p>
            <p class="card-text">Fecha: ${venta.fecha}</p>
            <p class="card-text">Tipo de venta: ${venta.tipo_venta}</p>
            <p class="card-text">Total: ${total}</p>
        </div>
    </div>
    `;

}