export function filtrarProductos(productos, textoBuscado) {
    return productos.filter(({ producto }) =>
        producto.nombre.toLowerCase().includes(textoBuscado)
    );
}

export function filtrarVentasPendientes(ventas, textoBuscado) {
    return ventas.filter(venta =>
        (venta.cliente?.nombre ?? "Sin cliente")
            .toLowerCase()
            .includes(textoBuscado)
    );
}
