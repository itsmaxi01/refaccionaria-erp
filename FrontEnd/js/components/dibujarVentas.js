export function dibujarVentas(ventas, contenedor, verDetalle) {
    contenedor.replaceChildren();
    if (ventas.length === 0) {
        contenedor.textContent = "No hay ventas pendientes que coincidan.";
        return;
    }
    for (const venta of ventas) {
        const div = document.createElement("div");
        div.classList.add("venta");
        for (const [etiqueta, valor] of [
            ["ID VENTA", venta.idventa],
            ["Cliente", venta.cliente?.nombre ?? "Sin cliente"],
            ["Total", "$" + venta.total],
            ["TotalPagado", "$" + venta.totalPagado],
            ["Fecha", venta.fecha]
        ]) {
            const parrafo = document.createElement("p");
            const titulo = document.createElement("strong");
            titulo.textContent = etiqueta + ": ";
            parrafo.append(titulo, String(valor));
            div.appendChild(parrafo);
        }
        const boton = document.createElement("button");
        boton.className = "btn btn-primary";
        boton.id = "btnDetalle-" + venta.idventa;
        boton.textContent = "Ver detalle";
        boton.addEventListener("click", () => {
            if (verDetalle) verDetalle(venta.idventa);
            else window.location.href = "detalleVentas.html?id=" + encodeURIComponent(venta.idventa);
        });
        div.appendChild(boton);
        contenedor.appendChild(div);
    }
}
