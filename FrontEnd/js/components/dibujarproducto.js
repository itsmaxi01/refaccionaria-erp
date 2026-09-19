export function dibujarProducto(inventario, contenedor, alAgregar) {

    const producto = document.createElement("div");
    producto.classList.add("producto");

    for (const [etiqueta, texto] of [
        ["h3", inventario.producto.nombre],
        ["p", "Código: " + inventario.producto.codigo_barras],
        ["p", "Precio: $" + inventario.producto.precio],
        ["p", "Cantidad: " + inventario.cantidad]
    ]) {
        const elemento = document.createElement(etiqueta);
        elemento.textContent = texto;
        producto.appendChild(elemento);
    }

    const boton = document.createElement("button");
    boton.textContent = "Agregar";

    boton.addEventListener("click", () => {
        alAgregar(inventario.id_inventario);
    });

    producto.appendChild(boton);
    contenedor.appendChild(producto);

}

export function dibujarProductos(inventarios, contenedor, alAgregar) {
    contenedor.replaceChildren();
    inventarios.forEach(inventario =>
        dibujarProducto(inventario, contenedor, alAgregar)
    );
}
