export function filtrarVentasPendientes(textoBuscado,ventas){
  
  
    const ventasFiltradas = ventas.filter(venta =>
    (venta.cliente?.nombre ?? "Sin cliente").toLowerCase().includes(textoBuscado)
);
    return ventasFiltradas;
}