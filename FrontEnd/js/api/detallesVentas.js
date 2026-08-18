export async function cargarDetallesVentas(id_venta){
    const url = `http://localhost:8081/detallesVentas/${id_venta}`;
    try{
        console.log("Paso");
        const respuesta = await fetch(url);
        const detalles = await respuesta.json();
        return detalles;
    }catch(error){
        console.error(error);
    }   






}