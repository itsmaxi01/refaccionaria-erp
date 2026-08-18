export function filtrarVentasPendientes(textoBuscado,ventas){
  
  
    const ventasFiltradas = ventas.filter(venta =>
    venta.cliente.nombre.toLowerCase().includes(textoBuscado)
);
    return ventasFiltradas;
}