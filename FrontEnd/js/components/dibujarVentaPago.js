export function dibujarVenta(venta, contenedorVenta, total) {
    const tarjeta = document.createElement("div");
    tarjeta.className = "card";
    const cuerpo = document.createElement("div");
    cuerpo.className = "card-body";

    for (const [etiqueta, clase, texto] of [
        ["h5", "card-title", "Venta"],
        ["p", "card-text", "ID: " + venta.idventa],
        ["p", "card-text", "Fecha: " + venta.fecha],
        ["p", "card-text", "Tipo de venta: " + venta.tipo_venta],
        ["p", "card-text", "Total: " + total]
    ]) {
        const elemento = document.createElement(etiqueta);
        elemento.className = clase;
        elemento.textContent = texto;
        cuerpo.appendChild(elemento);
    }

    tarjeta.appendChild(cuerpo);
    contenedorVenta.replaceChildren(tarjeta);
}
