export async function generarPagos(pago, idVenta) {
    
    const response = await fetch(`http://localhost:8081/Pago/${idVenta}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(pago)
    });

   if (!response.ok) {
    const error = await response.text();
    console.error("Respuesta del backend:", error);

    throw new Error(`Error HTTP ${response.status}: ${error}`);
}

    return await response.json();
}