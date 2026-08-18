import { totalByIdVenta } from "../api/ventaPendiente.js";
import { totaPagadoByIdVenta } from "../api/ventaPendiente.js";

export async function dibujarVentas(ventas, contenedor,verDetalle) {

    contenedor.innerHTML = "";

    for (const venta of ventas) {

         console.log("ID:", venta.idventa);

        const total = await totalByIdVenta(venta.idventa);
        const totalPagado = await totaPagadoByIdVenta(venta.idventa);


        const div = document.createElement("div");
        div.classList.add("venta");

        div.innerHTML = `
            <p><strong>ID VENTA</strong> ${venta.idventa}</p>
            <p><strong>Cliente:</strong> ${venta.cliente.nombre}</p>
            <p><strong>Total:</strong> $${total}</p>
            <p><strong>TotalPagado:</strong> $${totalPagado}</p>
            <p><strong>Fecha:</strong> ${venta.fecha}</p>

            <button class="btn btn-primary" id="btnDetalle-${venta.idventa}">
                Ver detalle
            </button>
        `;

        contenedor.appendChild(div);
        contenedor.addEventListener("click", (e) => {
         if (e.target.matches("[id^='btnDetalle-']")) {
        const idVenta = e.target.id.replace("btnDetalle-", "");
        window.location.href = `detalleVentas.html?id=${idVenta}`;
    }
            });
    }
}