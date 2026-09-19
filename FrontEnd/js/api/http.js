const API_BASE_URL = "http://localhost:8081";

export async function apiFetch(ruta, opciones = {}, configurarError = {}) {
    const encabezados = new Headers(opciones.headers ?? {});

    if (opciones.body != null && !encabezados.has("Content-Type")) {
        encabezados.set("Content-Type", "application/json");
    }

    const respuesta = await fetch(`${API_BASE_URL}${ruta}`, {
        ...opciones,
        headers: encabezados
    });

    if (!respuesta.ok) {
        const mensaje = await respuesta.text();
        if (configurarError.crearMensaje) {
            throw new Error(configurarError.crearMensaje(respuesta.status, mensaje));
        }
        throw new Error(
            mensaje || configurarError.mensajePredeterminado || `Error HTTP: ${respuesta.status}`
        );
    }

    if (respuesta.status === 204) return null;

    const contenido = await respuesta.text();
    return contenido ? JSON.parse(contenido) : null;
}
