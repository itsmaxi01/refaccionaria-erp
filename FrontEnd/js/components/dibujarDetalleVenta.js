export function dibujarDetalleVenta(contenedorDetalleVenta, detalles,carrito, actualizarDetalle) {

    contenedorDetalleVenta.innerHTML = "";

    detalles.forEach((detalle, index) => {

        contenedorDetalleVenta.innerHTML += `
            <div class="ItemDetalleVenta">
                <p>${carrito[index].producto.nombre}</p>
                <p>Cantidad: ${detalle.cantidad}</p>

                <label>Precio:</label>
                <input
                    type="number"
                    class="inputPrecio"
                    data-index="${index}"
                    value="${detalle.precioUnitario}"
                    min="0"
                    step="0.01"
                >
            </div>
        `;
    });

    contenedorDetalleVenta.querySelectorAll(".inputPrecio").forEach(input => {

        input.addEventListener("change", e => {

            const index = Number(e.target.dataset.index);

            detalles[index].precioUnitario = Number(e.target.value);

            actualizarDetalle(detalles);

        });

    });

}