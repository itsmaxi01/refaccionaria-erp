import { apiFetch } from "./http.js";

export async function registrarVenta(venta) {
    return apiFetch("/Ventas", {
        method: "POST",
        body: JSON.stringify(venta)
    }, {
        mensajePredeterminado: "Error al registrar la venta"
    });
}
