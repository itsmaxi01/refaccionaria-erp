
import { apiFetch } from "./http.js";

export async function cargarProductos() {
    const productos = await apiFetch("/Inventario");
    return productos.filter(item => item.producto.activo && item.cantidad > 0);
}
