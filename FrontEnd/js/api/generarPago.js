import { apiFetch } from "./http.js";

export async function generarPagos(pago, idVenta) {
    return apiFetch(`/Pago/${idVenta}`, {
        method: "POST",
        body: JSON.stringify(pago)
    }, {
        crearMensaje: (estado, mensaje) => `Error HTTP ${estado}: ${mensaje}`
    });
}
