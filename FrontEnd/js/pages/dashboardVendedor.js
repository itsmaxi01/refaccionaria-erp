import { cargarProductos } from "../api/Inventario.js";
import { dibujarProducto } from "../components/dibujarproducto.js";
import { agregarCarrito, dibujarCarrito } from "../components/carrito.js";
import{ guardarCarrito} from "../localStorage/carritoStorage.js";
import { filtrarProductos } from "../components/inventarioLogic.js";

async function dashboardVendedor() {

    const contenedorProductos = document.getElementById("productos");
    const contenedorCarrito = document.getElementById("carrito");

    const productos = await cargarProductos();

    const carrito = [];

    productos.forEach(producto => {

        dibujarProducto(producto, contenedorProductos, idInventario => {

            agregarCarrito(carrito, productos, idInventario);

            guardarCarrito(carrito);

            dibujarCarrito(carrito, contenedorCarrito);

        });

    });

   
//escucha si alguien escribe
    const buscador = document.getElementById("buscador");
    buscador.addEventListener("input", (event) => {
        const textoBuscado = buscador.value.toLowerCase();
        const productosFiltrados=filtrarProductos(productos, textoBuscado); //pasarle la funcion de busqueda que filtre los productos y los devuelva 
        contenedorProductos.innerHTML = "";
        
    productosFiltrados.forEach(producto => {

        dibujarProducto(producto, contenedorProductos, idInventario => {

            agregarCarrito(carrito, productos, idInventario);

            guardarCarrito(carrito);

            dibujarCarrito(carrito, contenedorCarrito);

        });

    });
        

        });
   
   
    //dibujar los productos filtrados 


}

dashboardVendedor();