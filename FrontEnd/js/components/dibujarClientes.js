
export function dibujarClientes(contenedorClientes, disponibles, seleccionado) {
    disponibles.forEach(cliente => {
        const opcion = document.createElement("option");
        opcion.value = cliente.idCliente;
        opcion.textContent = cliente.nombre;
        contenedorClientes.appendChild(opcion);
    });

    contenedorClientes.addEventListener("change", () => {
        seleccionado(contenedorClientes.value);
    });
}

export function dibujarCliente(contenedorCliente, cliente) {
    const tarjeta = document.createElement("div");
    tarjeta.className = "card";
    const cuerpo = document.createElement("div");
    cuerpo.className = "card-body";

    for (const [etiqueta, clase, texto] of [
        ["h5", "card-title", cliente.nombre],
        ["p", "card-text", "Direccion: " + cliente.direccion],
        ["p", "card-text", "Teléfono: " + cliente.telefono]
    ]) {
        const elemento = document.createElement(etiqueta);
        elemento.className = clase;
        elemento.textContent = texto;
        cuerpo.appendChild(elemento);
    }

    tarjeta.appendChild(cuerpo);
    contenedorCliente.replaceChildren(tarjeta);
}
