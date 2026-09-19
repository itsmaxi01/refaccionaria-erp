-- Esquema base del MVP. Generado a partir de la base local refaccionaria.
-- Los valores válidos de venta.estado son: PENDIENTE, PARCIAL, SALDADA y PAGADA.

CREATE DATABASE IF NOT EXISTS refaccionaria
    DEFAULT CHARACTER SET latin1
    DEFAULT COLLATE latin1_swedish_ci;

USE refaccionaria;

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) DEFAULT NULL,
    direccion VARCHAR(255) DEFAULT NULL,
    tipo_cliente VARCHAR(30) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT NOT NULL AUTO_INCREMENT,
    codigo_barras VARCHAR(50) DEFAULT NULL,
    nombre VARCHAR(150) NOT NULL,
    tipo VARCHAR(50) DEFAULT NULL,
    precio DECIMAL(10,2) NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    PRIMARY KEY (id_producto),
    UNIQUE KEY uk_producto_codigo_barras (codigo_barras),
    KEY idx_producto_nombre (nombre),
    KEY idx_producto_codigo (codigo_barras)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(30) NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_nombre (nombre_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS inventario (
    id_inventario INT NOT NULL AUTO_INCREMENT,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_inventario),
    UNIQUE KEY uk_inventario_producto_ubicacion (id_producto, ubicacion),
    KEY idx_inventario_producto (id_producto),
    CONSTRAINT fk_inventario_producto
        FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS venta (
    id_venta INT NOT NULL AUTO_INCREMENT,
    id_cliente INT DEFAULT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL,
    tipo_venta VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_venta),
    KEY idx_venta_cliente (id_cliente),
    CONSTRAINT fk_venta_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS pago (
    id_pago INT NOT NULL AUTO_INCREMENT,
    id_venta INT NOT NULL,
    monto_abonado DECIMAL(10,2) NOT NULL,
    monto_recibido DECIMAL(10,2) NOT NULL,
    metodo VARCHAR(20) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_pago),
    KEY idx_pago_venta (id_venta),
    CONSTRAINT fk_pago_venta
        FOREIGN KEY (id_venta) REFERENCES venta (id_venta)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle INT NOT NULL AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_inventario INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_detalle),
    KEY idx_detalle_venta (id_venta),
    KEY idx_detalle_inventario (id_inventario),
    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (id_venta) REFERENCES venta (id_venta),
    CONSTRAINT fk_detalle_inventario
        FOREIGN KEY (id_inventario) REFERENCES inventario (id_inventario)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
