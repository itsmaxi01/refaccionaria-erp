export async function registrarVenta(venta) {

    const respuesta = await fetch("http://localhost:8081/Ventas", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
       } ,
        body: JSON.stringify(venta)
    });

    if (!respuesta.ok) {
        throw new Error((await respuesta.text()) || "Error al registrar la venta");
    }

    return await respuesta.json(); }

    
    