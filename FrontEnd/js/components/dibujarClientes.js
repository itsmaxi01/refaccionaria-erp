
export async function dibujarClientes(contenedorClientes,disponibles,Seleccionado){

 disponibles.forEach(cliente => {
            contenedorClientes.innerHTML += `
                <option value="${cliente.idCliente}">
                    ${cliente.nombre}
                </option>
            `;

        });
        contenedorClientes.addEventListener( "change", () => {   console.log(contenedorClientes.value); Seleccionado(contenedorClientes.value);});





}
export async function dibujarCliente(contenedorCliente,cliente){

    contenedorCliente.innerHTML = `
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">${cliente.nombre}</h5>
            <p class="card-text">Direccion: ${cliente.direccion}</p>
            <p class="card-text">Teléfono: ${cliente.telefono}</p>
        </div>
    </div>
    `;



}