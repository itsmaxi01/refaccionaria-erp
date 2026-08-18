
//VenttasPendientes
export async function ventasPendiente() {
    const url = "http://localhost:8081/Ventas";
    const ventasPendientes = await fetch(url);
    return ventasPendientes.json();
}

//Total By Id Venta
export async function totalByIdVenta(id_venta) {
    const url = `http://localhost:8081/detallesVentas/venta/${id_venta}/total`;
    const total = await fetch(url);
    return total.json();
}

export async function totaPagadoByIdVenta(id_venta) {
    const url = `http://localhost:8081/Pago/Venta/${id_venta}`;
    const total = await fetch(url);
    return total.json();
}

