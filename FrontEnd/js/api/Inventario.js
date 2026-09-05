
export async function cargarProductos() {
    const URL = "http://localhost:8081/Inventario";

    try{
        const respuesta = await fetch(URL);
        if (!respuesta.ok) {
            throw new Error((await respuesta.text()) || `Error HTTP: ${respuesta.status}`);
        }
        const productos = await respuesta.json();
        return productos.filter(item => item.producto.activo && item.cantidad > 0); //devuelve solo los productos activos
        
    } catch (error) {
        console.error(error);
        throw error;
    }
      

}