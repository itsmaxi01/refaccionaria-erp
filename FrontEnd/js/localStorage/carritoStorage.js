export function guardarCarrito(carrito) {

    localStorage.setItem(
        "carrito",
        JSON.stringify(carrito)
    );

}

export function obtenerCarrito() {

    const carrito = localStorage.getItem("carrito");

    return carrito ? JSON.parse(carrito) : [];

}

export function eliminarCarrito() {

    localStorage.removeItem("carrito");

}
