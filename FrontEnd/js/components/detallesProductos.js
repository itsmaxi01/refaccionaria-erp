export function dibujarDetalles(detalles, contenedorDetalles) {
    contenedorDetalles.replaceChildren();
    detalles.forEach(detalle => {
        const fila = document.createElement("tr");
        for (const valor of [
            detalle.inventario.producto.nombre,
            detalle.cantidad,
            detalle.precioUnitario,
            detalle.subtotal
        ]) {
            const celda = document.createElement("td");
            celda.textContent = valor;
            fila.appendChild(celda);
        }
        contenedorDetalles.appendChild(fila);
    });
}
