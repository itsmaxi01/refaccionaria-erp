export function dibujarProducto(inventario, contenedor, alAgregar) {

    const producto = document.createElement("div");
    producto.classList.add("producto");

    producto.innerHTML = `
        <h3>${inventario.producto.nombre}</h3>

        <p>Código: ${inventario.producto.codigo_barras}</p>

        <p>Precio: $${inventario.producto.precio}</p>

        <p>Cantidad: ${inventario.cantidad}</p>
    `;

    const boton = document.createElement("button");
    boton.textContent = "Agregar";

    boton.addEventListener("click", () => {
        alAgregar(inventario.id_inventario);
    });

    producto.appendChild(boton);
    contenedor.appendChild(producto);

}