export async function dibujarDetalles(detalles, contenedorDetalles) {

    console.log(detalles);

    detalles.forEach(detalle => {

        contenedorDetalles.innerHTML += `
            <tr>
                <td>${detalle.inventario.producto.nombre}</td>
                <td>${detalle.cantidad}</td>
                <td>${detalle.precioUnitario}</td>
                <td>${detalle.subtotal}</td>
            </tr>
        `;

    });

}