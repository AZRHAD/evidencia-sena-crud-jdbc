-- =============================================================================
-- SERVICIO NACIONAL DE APRENDIZAJE - SENA
-- Programa: Análisis y Desarrollo de Software (ADSO)
-- Evidencia: GA7-220501096-AA2-EV01 (Codificación de módulos del software)
-- Archivo: script_bd.sql
-- Motor: MySQL 8.0+
-- Descripción: Creación de la base de datos db_inventario_sena, definición
--              de la tabla productos e inserción de datos iniciales.
-- =============================================================================

-- 1. Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS db_inventario_sena
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_inventario_sena;

-- 2. Eliminación preventiva de la tabla para idempotencia
DROP TABLE IF EXISTS productos;

-- 3. Definición de la estructura de la tabla productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria autoincremental',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre comercial del producto',
    descripcion VARCHAR(255) COMMENT 'Descripción detallada de especificaciones',
    precio DECIMAL(10, 2) NOT NULL COMMENT 'Precio unitario del producto en COP',
    stock INT NOT NULL DEFAULT 0 COMMENT 'Existencias en inventario',
    categoria VARCHAR(50) NOT NULL COMMENT 'Categoría de clasificación',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro en el sistema',
    CONSTRAINT chk_precio_positivo CHECK (precio >= 0),
    CONSTRAINT chk_stock_positivo CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de inventario de productos';

-- 4. Inserción de datos iniciales para pruebas
INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES
('Portátil ASUS VivoBook 15', 'Intel Core i5 12va Gen, 16GB RAM, 512GB SSD NVMe', 2850000.00, 15, 'Tecnología'),
('Mouse Inalámbrico Logitech M170', 'Sensor óptico 1000 DPI, receptor USB 2.4 GHz', 45000.00, 50, 'Periféricos'),
('Teclado Mecánico Redragon Kumara', 'Switches Outemu Blue, retroiluminación RGB, anti-ghosting', 185000.00, 20, 'Periféricos'),
('Monitor Gamer LG 24 Pulgadas', 'Panel IPS FHD 1080p, 75Hz, FreeSync, HDMI', 520000.00, 12, 'Pantallas'),
('Impresora Multifuncional Epson EcoTank L3250', 'Sistema continuo de tinta, Wi-Fi Direct, escáner', 890000.00, 8, 'Impresión');

-- 5. Consulta de validación
SELECT 
    id_producto,
    nombre,
    precio,
    stock,
    categoria,
    fecha_creacion
FROM productos;
