
import { apiFetch } from "./http.js";

//VenttasPendientes
export async function ventasPendiente() {
    return apiFetch("/Ventas");
}

//Total By Id Venta
export async function totalByIdVenta(id_venta) {
    return apiFetch(`/detallesVentas/venta/${id_venta}/total`);
}

export async function totalPagadoByIdVenta(id_venta) {
    return apiFetch(`/Pago/Venta/${id_venta}`);
}

