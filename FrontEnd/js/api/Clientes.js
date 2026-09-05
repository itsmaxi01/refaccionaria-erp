
export async function cargarClientes() {
    const Url = "http://localhost:8081/Clientes";

    try{
        const respuesta = await fetch(Url)
        if (!respuesta.ok) {
            throw new Error((await respuesta.text()) || `Error HTTP: ${respuesta.status}`);
        }
        
        const clientes = await respuesta.json(); 

        const disponibles = clientes.filter(cliente => cliente.activo === true);

        return disponibles;

    } catch (error) {
         console.error(error);
        throw error;
    }



}
export async function obtenerCliente(idCliente) {

    const url = `http://localhost:8081/Clientes/Clientes/${idCliente}`;

    try {

        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error((await respuesta.text()) || `Error HTTP: ${respuesta.status}`);
        }


        const cliente = await respuesta.json();

        return cliente;

    } catch (error) {

        console.error(error);
        throw error;
    }
}