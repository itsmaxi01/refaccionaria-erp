function agregarCarrito(carrito, idInventario,contenedor) {

    const inventario = inventariodisponible.find(
        i => i.id_inventario === idInventario
    );

    if (!inventario) {
        console.error("Inventario no encontrado");
        return;
    }

    const existente = carrito.find(
        item =>
            item.producto.codigo_barras ===
            inventario.producto.codigo_barras
    );

    if (existente) {

        existente.cantidadVenta++;

    } else {

        carrito.push({

            id_inventario: inventario.id_inventario,

            producto: inventario.producto,

            cantidadVenta: 1

        });

    }

    const subtotal = carrito.reduce((acc, item) => {
    return acc + item.producto.precio * item.cantidadVenta;
}, 0);

    dibujarCarrito(contenedor,subtotal);

    console.log(carrito);

}

function dibujarCarrito(contenedor) {

  

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


document.getElementById("subtotal").textContent =
    `Total: $${subtotal.toFixed(2)}`;


}

function vaciarCarrito(carrito) 
    {
         carrito = [];
    dibujarCarrito();
    }