# Sistema de Gestión para Refaccionaria — ERP/POS

**Java 21 · Spring Boot 4 · MySQL · REST API · Spring Data JPA · JavaScript Vanilla**

Sistema web para la gestión de **inventario, ventas, clientes y pagos** de una refaccionaria, desarrollado a partir de procesos y necesidades observadas en un negocio real.

El proyecto busca resolver principalmente la falta de seguimiento sobre **qué productos existen, dónde se encuentran físicamente, qué se vendió, a quién se vendió y qué ventas continúan con saldo pendiente**.

Actualmente cuenta con un **flujo funcional de vendedor** y se encuentra en desarrollo activo.

> **Estado:** MVP en desarrollo. El objetivo es estabilizar los principales flujos, completar las funciones administrativas y realizar pruebas antes de implementarlo progresivamente en la operación de la refaccionaria real.

![Vista principal del sistema](docs/images/sistema-principal.png)

---

## El problema

La operación de la refaccionaria no contaba con un sistema centralizado para dar seguimiento a información como:

* Ubicación física de las piezas.
* Cantidad disponible por ubicación.
* Productos vendidos.
* Clientes asociados a las ventas.
* Precios utilizados en cada operación.
* Ventas pendientes de pago.
* Abonos realizados por los clientes.
* Productos que continúan en existencia.

Uno de los principales problemas era el **inventario**: un mismo producto puede encontrarse en diferentes lugares físicos y con diferentes cantidades.

El modelo de datos fue diseñado tomando este problema como uno de sus puntos principales.

---

## Funcionalidades actuales

### Inventario y productos

* Registro y consulta de productos.
* Inventario separado por ubicación física.
* Control de existencias.
* Descuento automático de inventario al realizar una venta.
* Validación para evitar vender más unidades de las disponibles.
* Eliminación lógica de productos mediante estado `activo`.
* Un producto con existencias no puede ser desactivado.

### Ventas

* Creación de ventas con múltiples productos.
* Carrito de compra.
* Ventas con o sin cliente registrado.
* Manejo de ventas de mostrador y crédito.
* Precios modificables durante la operación.
* Conservación del precio histórico utilizado en cada venta.
* Estados de venta según su situación de pago.
* Consulta de ventas pendientes.
* Consulta del detalle de una venta.

### Clientes y pagos

* Registro y consulta de clientes.
* Asociación de clientes con ventas.
* Registro de pago inicial.
* Registro de abonos posteriores.
* Seguimiento del saldo de una venta.
* Restricción de deuda para compradores no registrados.

---

## Modelo de inventario

Una de las principales decisiones de diseño fue separar **Producto** de **Inventario**.

`Producto` representa la pieza dentro del catálogo:

```text
Producto
├── id
├── nombre
├── código de barras
├── precio sugerido
└── activo
```

`Inventario` representa la existencia física del producto:

```text
Inventario
├── id
├── producto
├── ubicación
└── cantidad
```

De esta manera, un mismo producto puede existir en diferentes ubicaciones sin duplicar su información.

Por ejemplo:

```text
Bujía NGK
│
├── Estante A-01 → 2 unidades
└── Bodega B-04  → 3 unidades
```

Al realizar una venta, el sistema descuenta específicamente el **registro de inventario seleccionado**, no solamente el producto.

Esto permite conocer de qué ubicación física salió la mercancía y mantener las existencias independientes por ubicación.

---

## Modelo de ventas

Una venta contiene uno o varios `DetalleVenta`.

```text
Venta
│
├── Cliente
│
├── DetalleVenta
│   ├── Inventario
│   ├── Cantidad
│   ├── Precio unitario
│   └── Subtotal
│
└── Pago(s)
```

El estado financiero de una venta puede ser:

```text
PENDIENTE → PARCIAL → PAGADA
```

El tipo de venta y su estado financiero representan conceptos diferentes.

Una venta puede realizarse en mostrador y quedar parcialmente pagada si existe un cliente registrado responsable del saldo.

En cambio, si no existe un cliente asociado, la operación debe ser liquidada completamente.

---

## Precio histórico

El precio almacenado en `Producto` funciona como un **precio sugerido** para nuevas operaciones.

Durante una venta, el vendedor puede utilizar ese precio o especificar otro.

El precio realmente utilizado queda almacenado en `DetalleVenta`, evitando que cambios posteriores en el catálogo modifiquen el valor histórico de ventas anteriores.

Ejemplo:

```text
Precio sugerido actual: $150

Venta:
Precio utilizado: $130

El precio del producto cambia posteriormente a $170.

La venta conserva: $130
```

---

## Consistencia de las operaciones

La creación de una venta modifica varias partes del sistema:

1. Se valida la información recibida.
2. Se crea la venta.
3. Se consulta el inventario de cada producto.
4. Se valida el stock disponible.
5. Se descuenta el inventario correspondiente.
6. Se determina el precio utilizado.
7. Se crean los detalles de venta.
8. Se calcula el total.
9. Se registra el pago inicial.
10. Se determina el estado financiero de la venta.

Este proceso se ejecuta mediante `@Transactional`, evitando conservar parcialmente una operación si ocurre un error durante su procesamiento.

---

## Reglas de negocio implementadas

Algunas de las reglas actualmente protegidas desde el backend son:

* Una venta debe contener al menos un detalle.
* No se permiten cantidades menores o iguales a cero.
* Cada detalle debe indicar el inventario del que saldrá la mercancía.
* No es posible vender más unidades que las disponibles.
* El stock se descuenta de una ubicación específica.
* Una venta con saldo pendiente necesita un cliente registrado.
* Los pagos no pueden contener montos negativos.
* El precio sugerido puede modificarse durante una venta.
* El precio realmente utilizado se conserva en el historial.
* Los productos utilizan eliminación lógica.
* No es posible desactivar un producto mientras alguna de sus ubicaciones tenga existencias.

---

## Arquitectura

El backend está organizado principalmente **por dominio**, manteniendo juntas las responsabilidades relacionadas con cada módulo.

```text
src/main/java/com/refaccionaria/

├── cliente/
│   ├── Cliente
│   ├── ClienteController
│   ├── ClienteRepository
│   └── ClienteService
│
├── inventario/
│   ├── Inventario
│   ├── InventarioController
│   ├── InventarioRepository
│   └── InventarioService
│
├── producto/
│   ├── Producto
│   ├── ProductoController
│   ├── ProductoRepository
│   └── ProductoService
│
├── pago/
│   ├── Pago
│   ├── PagoController
│   ├── PagoDTO
│   ├── PagoRepository
│   └── PagoService
│
├── venta/
│   ├── Venta
│   ├── VentaController
│   ├── VentaDto
│   ├── VentaRepository
│   ├── VentaService
│   ├── DetalleVenta
│   ├── DetalleVentaController
│   ├── DetalleVentaDTO
│   ├── DetalleVentaRepository
│   └── DetalleVentaService
│
└── config/
```

---

## Frontend

El frontend está desarrollado con **HTML, CSS y JavaScript Vanilla**.

La lógica JavaScript se separa según responsabilidad:

```text
FrontEnd/js/

├── api/           # Comunicación con la API REST
├── components/    # Renderizado de elementos de interfaz
├── localStorage/  # Persistencia local del carrito
├── pages/         # Orquestadores de cada página
├── app.js
└── carrito.js
```

La comunicación con el backend se realiza mediante `fetch`.

Actualmente está implementado el flujo principal del vendedor:

```text
Buscar producto
      ↓
Agregar al carrito
      ↓
Seleccionar cliente
      ↓
Configurar la venta
      ↓
Confirmar productos y precios
      ↓
Registrar venta
      ↓
Descontar inventario
      ↓
Registrar pago
      ↓
Consultar ventas pendientes
      ↓
Consultar detalle
      ↓
Registrar nuevos abonos
```

---

## Capturas

### Selección de productos

![Selección de productos](docs/images/productos.png)

### Carrito y creación de venta

![Carrito](docs/images/carrito.png)

### Ventas pendientes

![Ventas pendientes](docs/images/ventas-pendientes.png)

### Detalle de venta

![Detalle de venta](docs/images/detalle-venta.png)

### Registro de pago

![Registro de pago](docs/images/pago.png)

---

## Tecnologías

### Backend

* Java 21 LTS
* Spring Boot 4.0.6
* Spring Web MVC
* Spring Data JPA
* Gradle
* MySQL
* MySQL Connector/J
* OpenAPI / Swagger

### Frontend

* HTML5
* CSS3
* JavaScript Vanilla
* Fetch API
* LocalStorage

---

## API REST

La API puede consultarse y probarse mediante Swagger/OpenAPI mientras el backend está en ejecución.

```text
http://localhost:8081/swagger-ui/index.html
```

---

## Base de datos

La base de datos utiliza **MySQL** y fue modelada específicamente para los procesos del sistema.

El esquema no es generado automáticamente por Hibernate.

Para ejecutar el proyecto se debe importar el archivo SQL incluido en el repositorio y posteriormente configurar la conexión en `application.properties`.

---

## Ejecución local

### Requisitos

* Java 21
* MySQL
* Gradle Wrapper
* Navegador web

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
cd refaccionaria-erp
```

### 2. Importar la base de datos

Crear la base de datos MySQL e importar el archivo `.sql` incluido en el proyecto.

### 3. Configurar la conexión

Configurar las credenciales de MySQL en:

```text
src/main/resources/application.properties
```

### 4. Ejecutar Spring Boot

Windows:

```bash
gradlew.bat bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

### 5. Ejecutar el frontend

El frontend puede ejecutarse mediante un servidor local, por ejemplo **Live Server**.

---

## Estado del proyecto

### Implementado

* [x] Modelo de productos e inventario.
* [x] Inventario por ubicación.
* [x] Gestión de clientes.
* [x] Flujo principal de vendedor.
* [x] Carrito.
* [x] Registro de ventas.
* [x] Descuento automático de stock.
* [x] Validaciones de inventario.
* [x] Precios personalizados por venta.
* [x] Conservación del precio histórico.
* [x] Ventas pendientes y parcialmente pagadas.
* [x] Registro de pagos y abonos.
* [x] Eliminación lógica de productos.
* [x] API REST documentada con Swagger.
* [x] Frontend funcional.

---

## Próximas mejoras

El proyecto continúa en desarrollo. Entre las siguientes mejoras se encuentran:

* [ ] Autenticación y login.
* [ ] Roles y permisos.
* [ ] Dashboard administrativo.
* [ ] Completar endpoints administrativos.
* [ ] Manejo global de excepciones.
* [ ] Excepciones personalizadas de dominio.
* [ ] Sustituir estados almacenados como `String` por `enum`.
* [ ] Unificar nomenclatura de estados de venta.
* [ ] Pruebas unitarias y de integración.
* [ ] Refactorizar el orquestador de creación de ventas.
* [ ] Eliminar información derivable almacenada innecesariamente.
* [ ] Mejorar el modelo de pagos.
* [ ] Separar monto recibido de monto aplicado a una venta.
* [ ] Evitar abonos superiores al saldo pendiente.
* [ ] Implementar control y flujo de caja.
* [ ] Mejorar la búsqueda de productos desde backend.
* [ ] Mejorar interfaz y experiencia de usuario.
* [ ] Preparar despliegue en servidor Linux.
* [ ] Realizar pruebas en el entorno del negocio.

---

## Objetivo de implementación real

Este proyecto **no nació únicamente como un ejercicio académico**. Su diseño parte de procesos y problemas observados directamente en una refaccionaria real.

Actualmente se encuentra en etapa de desarrollo y validación. El objetivo durante los próximos meses es completar el MVP, estabilizar los principales flujos y realizar las pruebas necesarias antes de introducirlo en la operación del negocio.

Una vez alcanzado un nivel suficiente de estabilidad, está planeado realizar su **despliegue e implementación progresiva en la refaccionaria para su uso en un entorno real**.

A partir de ese punto, el proyecto continuará evolucionando de acuerdo con la retroalimentación, errores y nuevas necesidades encontradas durante su utilización.
