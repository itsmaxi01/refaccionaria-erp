export async function cargarDetallesVentas(id_venta){
    const url = `http://localhost:8081/detallesVentas/${id_venta}`;
    try{
        console.log("Paso");
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error((await respuesta.text()) || `Error HTTP: ${respuesta.status}`);
        }
        const detalles = await respuesta.json();
        return detalles;
    }catch(error){
        console.error(error);
        throw error;
    }   






}