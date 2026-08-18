
export function dibujarTotal(total,totalPagado)
    {
    const totalElemento = document.getElementById("total");
    const totalPagadoElemento = document.getElementById("totalPagado");

    totalElemento.textContent = total;
    totalPagadoElemento.textContent = totalPagado;




    }