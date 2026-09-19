import {
    calcularSubtotal,
    eliminarArticulo,
    quitarUnidad,
    vaciarCarrito
} from "../domain/carrito.js";

function crearBoton(texto, accion) {
    const boton = document.createElement("button");
    boton.type = "button";
    boton.textContent = texto;
    boton.addEventListener("click", accion);
    return boton;
}

export function dibujarCarrito(
    carrito,
    contenedor,
    subtotalElemento,
    alCambiar = () => {}
) {
    contenedor.replaceChildren();

    function actualizar() {
        alCambiar(carrito);
        dibujarCarrito(carrito, contenedor, subtotalElemento, alCambiar);
    }

    carrito.forEach((item, indice) => {
        const fila = document.createElement("div");
        fila.className = "ItemCarrito";

        for (const [etiqueta, texto] of [
            ["h3", item.producto.nombre],
            ["p", "Código: " + item.producto.codigo_barras],
            ["p", "Precio: $" + item.producto.precio],
            ["p", "Cantidad: " + item.cantidadVenta]
        ]) {
            const elemento = document.createElement(etiqueta);
            elemento.textContent = texto;
            fila.appendChild(elemento);
        }

        fila.appendChild(crearBoton("Quitar una unidad", () => {
            quitarUnidad(carrito, indice);
            actualizar();
        }));
        fila.appendChild(crearBoton("Eliminar artículo", () => {
            eliminarArticulo(carrito, indice);
            actualizar();
        }));
        contenedor.appendChild(fila);
    });

    if (carrito.length) {
        contenedor.appendChild(crearBoton("Vaciar carrito", () => {
            vaciarCarrito(carrito);
            actualizar();
        }));
    } else {
        contenedor.textContent = "Carrito vacío";
    }

    subtotalElemento.textContent = `Total: $${calcularSubtotal(carrito).toFixed(2)}`;
}
