export function dibujarDetalleVenta(contenedorDetalleVenta, detalles,carrito, actualizarDetalle) {

    contenedorDetalleVenta.replaceChildren();

    detalles.forEach((detalle, index) => {

        const fila = document.createElement("div");
        fila.className = "ItemDetalleVenta";

        const nombre = document.createElement("p");
        nombre.textContent = carrito[index].producto.nombre;
        const cantidad = document.createElement("p");
        cantidad.textContent = "Cantidad: " + detalle.cantidad;
        const etiqueta = document.createElement("label");
        etiqueta.textContent = "Precio:";
        const input = document.createElement("input");
        input.type = "number";
        input.className = "inputPrecio";
        input.dataset.index = index;
        input.value = detalle.precioUnitario;
        input.min = "0";
        input.step = "0.01";

        input.addEventListener("change", e => {
            const indice = Number(e.target.dataset.index);
            detalles[indice].precioUnitario = Number(e.target.value);
            actualizarDetalle(detalles);
        });

        fila.append(nombre, cantidad, etiqueta, input);
        contenedorDetalleVenta.appendChild(fila);
    });
}
