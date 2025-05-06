DROP DATABASE IF EXISTS el_zoggo;
CREATE DATABASE el_zoggo;
USE el_zoggo;

CREATE TABLE empleados (
  empleado_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  apellido_materno VARCHAR(50) NOT NULL,
  apellido_paterno VARCHAR(50),
  usuario VARCHAR(50) UNIQUE NOT NULL,
  password BLOB NOT NULL
);

CREATE TABLE clientes (
  cliente_id INT PRIMARY KEY AUTO_INCREMENT,
  telefono VARCHAR(10) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) NOT NULL
);

CREATE TABLE proveedores (
  proveedor_id INT PRIMARY KEY AUTO_INCREMENT,
  razon_social VARCHAR(100) NOT NULL,
  rfc VARCHAR(13) NOT NULL UNIQUE,
  direccion VARCHAR(200) NOT NULL,
  regimen_fiscal ENUM('601', '603', '605', '606', '612', '621', '625', '626') NOT NULL,
  correo VARCHAR(255) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  contacto_nombre VARCHAR(100),
  estatus ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO' NOT NULL
);

CREATE TABLE productos (
  producto_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  imagen TEXT,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  proveedor_id INT NOT NULL,
  FOREIGN KEY (proveedor_id) REFERENCES proveedores(proveedor_id)
);
CREATE TABLE metodos_pago (
  metodo_id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL -- Ej: 'Efectivo', 'Tarjeta', etc.
);
CREATE TABLE ventas (
  venta_id INT PRIMARY KEY AUTO_INCREMENT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  cliente_id INT NOT NULL,
  empleado_id INT NOT NULL,
  metodo_id INT NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id),
  FOREIGN KEY (metodo_id) REFERENCES metodos_pago(metodo_id)
);
CREATE TABLE detalle_venta (
  id INT PRIMARY KEY AUTO_INCREMENT,
  venta_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (venta_id) REFERENCES ventas(venta_id),
  FOREIGN KEY (producto_id) REFERENCES productos(producto_id)
);
CREATE TABLE orden_proveedor (
  orden_id INT PRIMARY KEY AUTO_INCREMENT,
  proveedor_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  FOREIGN KEY (proveedor_id) REFERENCES proveedores(proveedor_id),
  FOREIGN KEY (producto_id) REFERENCES productos(producto_id)
);
CREATE TABLE corte_caja (
  corte_id INT PRIMARY KEY AUTO_INCREMENT,
  fecha DATE NOT NULL,
  empleado_id INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  generado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id),
  UNIQUE(fecha)
);
CREATE TABLE movimientos_inventario (
  movimiento_id INT PRIMARY KEY AUTO_INCREMENT,
  producto_id INT NOT NULL,
  tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
  cantidad INT NOT NULL,
  motivo TEXT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  empleado_id INT,
  FOREIGN KEY (producto_id) REFERENCES productos(producto_id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id)
);
