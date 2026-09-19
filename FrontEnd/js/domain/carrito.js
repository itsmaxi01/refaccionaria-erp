export function agregarAlCarrito(carrito, inventarioDisponible, idInventario) {
    const inventario = inventarioDisponible.find(
        item => item.id_inventario === idInventario
    );

    if (!inventario) {
        console.error("Inventario no encontrado");
        return false;
    }

    const existente = carrito.find(
        item => item.id_inventario === idInventario
    );

    if ((existente?.cantidadVenta ?? 0) + 1 > inventario.cantidad) {
        alert("No hay más unidades disponibles de este artículo");
        return false;
    }

    if (existente) {
        existente.cantidadVenta++;
    } else {
        carrito.push({
            id_inventario: inventario.id_inventario,
            producto: inventario.producto,
            cantidadVenta: 1
        });
    }

    return true;
}

export function quitarUnidad(carrito, indice) {
    if (carrito[indice].cantidadVenta > 1) carrito[indice].cantidadVenta--;
    else carrito.splice(indice, 1);
}

export function eliminarArticulo(carrito, indice) {
    carrito.splice(indice, 1);
}

export function vaciarCarrito(carrito) {
    carrito.length = 0;
}

export function calcularSubtotal(carrito) {
    return carrito.reduce(
        (total, item) => total + item.producto.precio * item.cantidadVenta,
        0
    );
}
