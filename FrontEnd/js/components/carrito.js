export function agregarCarrito(carrito, inventarioDisponible, idInventario) {

    const inventario = inventarioDisponible.find(
        item => item.id_inventario === idInventario
    );

    if (!inventario) {
        console.error("Inventario no encontrado");
        return;
    }

    const existente = carrito.find(
        item => item.id_inventario === idInventario
    );

    if (existente) {

        existente.cantidadVenta++;

    } else {
         console.log("Inventario:", inventario);

        carrito.push({

            
            id_inventario: inventario.id_inventario,

            producto: inventario.producto,

            cantidadVenta: 1

        });

    }

}

export function dibujarCarrito(carrito, contenedor) {

    contenedor.innerHTML = "";

    carrito.forEach(item => {

        contenedor.innerHTML += `
            <div class="ItemCarrito">

                <h3>${item.producto.nombre}</h3>

                <p>Código: ${item.producto.codigo_barras}</p>

                <p>Precio: $${item.producto.precio}</p>

                <p>Cantidad: ${item.cantidadVenta}</p>

            </div>
        `;

    });

    const subtotal = carrito.reduce(
        (acc, item) => acc + item.producto.precio * item.cantidadVenta,
        0
    );

    document.getElementById("subtotal").textContent =
        `Total: $${subtotal.toFixed(2)}`;

}

export function vaciarCarrito(carrito) {

    carrito.length = 0;

}