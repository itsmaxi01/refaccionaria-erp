import { ventasPendiente, totalByIdVenta, totalPagadoByIdVenta } from "../api/ventaPendiente.js";
import { dibujarVentas } from "../components/dibujarVentas.js";
import { filtrarVentasPendientes } from "../domain/filtros.js";

async function ventasPagos() {
    const contenedor = document.getElementById("ventasPendientes");
    const buscador = document.getElementById("buscador");
    const botonBuscar = document.getElementById("btnBuscar");
    contenedor.textContent = "Cargando ventas pendientes...";
    buscador.disabled = true;
    botonBuscar.disabled = true;
    try {
        const pendientes = await ventasPendiente();
        const ventas = [];
        for (const venta of pendientes) {
            const [total, totalPagado] = await Promise.all([
                totalByIdVenta(venta.idventa), totalPagadoByIdVenta(venta.idventa)
            ]);
            ventas.push({ ...venta, total, totalPagado });
        }
        const filtrar = () => dibujarVentas(
            filtrarVentasPendientes(ventas, buscador.value.toLowerCase()), contenedor
        );
        buscador.addEventListener("input", filtrar);
        botonBuscar.addEventListener("click", filtrar);
        buscador.disabled = false;
        botonBuscar.disabled = false;
        filtrar();
    } catch (error) {
        console.error(error);
        contenedor.textContent = "No se pudieron cargar las ventas: " + error.message;
    }
}

ventasPagos();
