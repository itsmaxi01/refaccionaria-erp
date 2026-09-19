
import { apiFetch } from "./http.js";

export async function cargarClientes() {
    const clientes = await apiFetch("/Clientes");
    return clientes.filter(cliente => cliente.activo === true);
}

export async function obtenerCliente(idCliente) {
    return apiFetch(`/Clientes/Clientes/${idCliente}`);
}
