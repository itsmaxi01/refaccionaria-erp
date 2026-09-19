import { apiFetch } from "./http.js";

export async function cargarDetallesVentas(id_venta){
    return apiFetch(`/detallesVentas/${id_venta}`);
}
