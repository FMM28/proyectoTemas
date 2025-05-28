DROP DATABASE IF EXISTS el_zorro;
CREATE DATABASE el_zorro;
USE el_zorro;

DROP USER IF EXISTS 'elzorro'@'localhost';
CREATE USER 'elzorro'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON el_zorro.* TO 'elzorro'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE rol(
  rol_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(10) NOT NULL
);

CREATE TABLE empleado (
  empleado_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  apellido_paterno VARCHAR(50) NOT NULL,
  apellido_materno VARCHAR(50) NOT NULL,
  rol_id INT NOT NULL,
  usuario VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  FOREIGN KEY (rol_id) REFERENCES rol(rol_id)
);

CREATE TABLE cliente (
  cliente_id INT PRIMARY KEY AUTO_INCREMENT,
  telefono VARCHAR(10) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido_paterno VARCHAR(50) NOT NULL,
  apellido_materno VARCHAR(50) NOT NULL,
  correo VARCHAR(100) NOT NULL
);

CREATE TABLE categoria (
  categoria_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL
);

CREATE TABLE proveedor (
  proveedor_id INT PRIMARY KEY AUTO_INCREMENT,
  razon_social VARCHAR(100) NOT NULL,
  rfc VARCHAR(13) NOT NULL UNIQUE,
  direccion VARCHAR(200) NOT NULL,
  c_postal VARCHAR(5) NOT NULL,
  regimen_fiscal ENUM('_601', '_603', '_605', '_606', '_612', '_621', '_625', '_626') NOT NULL,
  correo VARCHAR(255) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  contacto_nombre VARCHAR(100),
  estatus ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO' NOT NULL
);

CREATE TABLE metodo_pago (
  metodo_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL -- Ej: 'Efectivo', 'Tarjeta', etc.
);

CREATE TABLE producto (
  producto_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  imagen TEXT,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  categoria_id INT NOT NULL,
  proveedor_id INT NOT NULL,
  FOREIGN KEY (categoria_id) REFERENCES categoria(categoria_id),
  FOREIGN KEY (proveedor_id) REFERENCES proveedor(proveedor_id)
);

CREATE TABLE venta (
  venta_id INT PRIMARY KEY AUTO_INCREMENT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  cliente_id INT,
  empleado_id INT NOT NULL,
  metodo_id INT NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id),
  FOREIGN KEY (empleado_id) REFERENCES empleado(empleado_id),
  FOREIGN KEY (metodo_id) REFERENCES metodo_pago(metodo_id)
);

CREATE TABLE detalle_venta (
  detalle_id INT PRIMARY KEY AUTO_INCREMENT,
  venta_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (venta_id) REFERENCES venta(venta_id),
  FOREIGN KEY (producto_id) REFERENCES producto(producto_id)
);

CREATE TABLE orden_proveedor (
  orden_id INT PRIMARY KEY AUTO_INCREMENT,
  proveedor_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  empleado_id INT NOT NULL,
  fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
  FOREIGN KEY (proveedor_id) REFERENCES proveedor(proveedor_id),
  FOREIGN KEY (producto_id) REFERENCES producto(producto_id),
  FOREIGN KEY (empleado_id) REFERENCES empleado(empleado_id)
);

CREATE TABLE movimiento_producto (
  movimiento_id INT PRIMARY KEY AUTO_INCREMENT,
  producto_id INT NOT NULL,
  cantidad_inicio INT NOT NULL,
  cantidad_fin INT NOT NULL,
  empleado_id INT NOT NULL,
  fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
  FOREIGN KEY (producto_id) REFERENCES producto(producto_id),
  FOREIGN KEY (empleado_id) REFERENCES empleado(empleado_id)
);

