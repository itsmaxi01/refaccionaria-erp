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

    if ((existente?.cantidadVenta ?? 0) + 1 > inventario.cantidad) {
        alert("No hay más unidades disponibles de este artículo");
        return;
    }

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

export function dibujarCarrito(carrito, contenedor, alCambiar = () => {}) {
    contenedor.replaceChildren();
    function actualizar() {
        alCambiar(carrito);
        dibujarCarrito(carrito, contenedor, alCambiar);
    }
    function boton(texto, accion) {
        const elemento = document.createElement("button");
        elemento.type = "button";
        elemento.textContent = texto;
        elemento.addEventListener("click", accion);
        return elemento;
    }
    carrito.forEach((item, index) => {
        const fila = document.createElement("div");
        fila.className = "ItemCarrito";
        for (const [tag, texto] of [
            ["h3", item.producto.nombre],
            ["p", "Código: " + item.producto.codigo_barras],
            ["p", "Precio: $" + item.producto.precio],
            ["p", "Cantidad: " + item.cantidadVenta]
        ]) {
            const elemento = document.createElement(tag);
            elemento.textContent = texto;
            fila.appendChild(elemento);
        }
        fila.appendChild(boton("Quitar una unidad", () => {
            if (item.cantidadVenta > 1) item.cantidadVenta--;
            else carrito.splice(index, 1);
            actualizar();
        }));
        fila.appendChild(boton("Eliminar artículo", () => {
            carrito.splice(index, 1);
            actualizar();
        }));
        contenedor.appendChild(fila);
    });
    if (carrito.length) {
        contenedor.appendChild(boton("Vaciar carrito", () => {
            vaciarCarrito(carrito);
            actualizar();
        }));
    } else {
        contenedor.textContent = "Carrito vacío";
    }
    const subtotal = carrito.reduce(
        (acc, item) => acc + item.producto.precio * item.cantidadVenta, 0
    );
    document.getElementById("subtotal").textContent = 
        `Total: $${subtotal.toFixed(2)}`;
}

export function vaciarCarrito(carrito) {

    carrito.length = 0;

}