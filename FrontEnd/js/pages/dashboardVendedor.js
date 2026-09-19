import { cargarProductos } from "../api/Inventario.js";
import { dibujarProductos } from "../components/dibujarproducto.js";
import { dibujarCarrito } from "../components/carritoView.js";
import{ guardarCarrito, obtenerCarrito} from "../localStorage/carritoStorage.js";
import { agregarAlCarrito } from "../domain/carrito.js";
import { filtrarProductos } from "../domain/filtros.js";

async function dashboardVendedor() {

    const contenedorProductos = document.getElementById("productos");
    const contenedorCarrito = document.getElementById("carrito");
    const subtotalElemento = document.getElementById("subtotal");

    const productos = await cargarProductos();

    const carrito = obtenerCarrito();
    dibujarCarrito(carrito, contenedorCarrito, subtotalElemento, guardarCarrito);

    const agregarProducto = idInventario => {
        agregarAlCarrito(carrito, productos, idInventario);
        guardarCarrito(carrito);
        dibujarCarrito(carrito, contenedorCarrito, subtotalElemento, guardarCarrito);
    };

    dibujarProductos(productos, contenedorProductos, agregarProducto);

   
//escucha si alguien escribe
    const buscador = document.getElementById("buscador");
    buscador.addEventListener("input", (event) => {
        const textoBuscado = buscador.value.toLowerCase();
        const productosFiltrados=filtrarProductos(productos, textoBuscado); //pasarle la funcion de busqueda que filtre los productos y los devuelva 
        dibujarProductos(productosFiltrados, contenedorProductos, agregarProducto);
        

        });
}

dashboardVendedor().catch(error => {
    console.error(error);
    document.getElementById("productos").textContent = "No se pudieron cargar los productos: " + error.message;
});
