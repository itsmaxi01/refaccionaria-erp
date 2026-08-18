export function filtrarProductos(productos, textoBuscado){

    const productosFiltrados = productos.filter(producto =>
    producto.producto.nombre.toLowerCase().includes(textoBuscado)
);

return productosFiltrados;






    
}