# Evidencia SENA: GA7-220501096-AA2-EV01
## Codificación de Módulos del Software — Módulo de Inventario (JDBC Puro & Patrón DAO)

---

### Información General de la Evidencia

* **Institución:** Servicio Nacional de Aprendizaje (SENA)
* **Programa de Formación:** Análisis y Desarrollo de Software (ADSO)
* **Fase del Proyecto:** Ejecución
* **Resultado de Aprendizaje:** 220501096-04 — *Codificar el software de acuerdo con el diseño establecido.*
* **Actividad de Aprendizaje:** GA7-220501096-AA2 — *Aplicar estándares de codificación, de acuerdo con el diseño.*
* **Evidencia de Desempeño:** Codificación de módulos del software (GA7-220501096-AA2-EV01).
* **Tecnología:** Java 17 LTS, Apache Maven 3.9+, MySQL 8.0+, Git.

---

## 1. Matriz de Cumplimiento de la Lista de Chequeo

El proyecto ha sido diseñado e implementado para satisfacer al 100% las cuatro (4) variables e indicadores de logro del instrumento de evaluación del SENA:

| No. | Indicador de Logro / Criterio | Estado | Evidencia en el Código |
|:---:|:---|:---:|:---|
| **1** | **Realiza conexiones con bases de datos utilizando JDBC.** | **CUMPLE (SÍ)** | Implementación de `ConexionBD` utilizando `java.sql.Connection` y `java.sql.DriverManager` junto con el driver oficial `com.mysql:mysql-connector-j`. Se descartan explícitamente frameworks ORM (JPA, Hibernate, Spring Data). |
| **2** | **Aplica el CRUD.** | **CUMPLE (SÍ)** | Patrón DAO en `ProductoDAO` con los métodos requeridos: `insertarProducto` (Create), `listarProductos` (Read), `actualizarProducto` (Update) y `eliminarProducto` (Delete), además del método utilitario `buscarPorId`. |
| **3** | **Utiliza herramientas de versionamiento para el proyecto.** | **CUMPLE (SÍ)** | Repositorio Git inicializado con rama principal `main`, exclusión de binarios mediante `.gitignore` y trazabilidad mediante commits semánticos progresivos (Conventional Commits). |
| **4** | **Utiliza un estándar de codificación definido.** | **CUMPLE (SÍ)** | Adhesión estricta a la **Google/Oracle Java Style Guide**: clases en `PascalCase`, métodos y variables en `camelCase`, constantes en `UPPER_SNAKE_CASE`, arquitectura en capas (`config`, `modelo`, `dao`, `main`) y documentación completa con **Javadoc**. |

---

## 2. Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura en capas desacopladas mediante el patrón **DAO (Data Access Object)**:

```text
evidencia-sena-crud-jdbc/
├── .gitignore                                          # Exclusión de artefactos compilados, IDEs y temporales
├── pom.xml                                             # Descriptor de proyecto Maven y dependencias
├── script_bd.sql                                       # Script SQL DDL/DML de la base de datos MySQL
├── README.md                                           # Documentación técnica y guía de estándares
└── src/
    └── main/
        └── java/
            └── com/
                └── sena/
                    └── inventario/
                        ├── config/
                        │   └── ConexionBD.java         # Gestión de conexión JDBC pura con MySQL
                        ├── modelo/
                        │   └── Producto.java           # Entidad de dominio encapsulada (JavaBean)
                        ├── dao/
                        │   ├── IProductoDAO.java       # Interfaz contractual de operaciones CRUD
                        │   └── ProductoDAO.java        # Implementación concreta del DAO con JDBC PreparedStatement
                        └── main/
                            └── Main.java               # Punto de entrada, prueba de ciclo CRUD y menú interactivo
```

---

## 3. Estándar de Codificación Aplicado

Para cumplir estrictamente con el **Indicador 4**, se adoptaron las convenciones mundiales de **Google Java Style Guide** y las normas oficiales de **Oracle**:

### 3.1 Convenciones de Nombres (Naming Conventions)
* **Clases e Interfaces (`PascalCase`):**
  * `ConexionBD`: Clase utilitaria de infraestructura.
  * `Producto`: Entidad de modelo de negocio.
  * `IProductoDAO`: Interfaz de operaciones de persistencia.
  * `ProductoDAO`: Implementación del DAO.
  * `Main`: Clase principal de aplicación.
* **Métodos y Atributos (`camelCase`):**
  * Métodos: `obtenerConexion()`, `cerrarConexion()`, `insertarProducto()`, `listarProductos()`, `actualizarProducto()`, `eliminarProducto()`, `buscarPorId()`.
  * Variables y atributos: `idProducto`, `nombre`, `descripcion`, `precio`, `stock`, `categoria`, `fechaCreacion`.
* **Constantes (`UPPER_SNAKE_CASE`):**
  * En `ConexionBD`: `DRIVER_CLASS`, `URL_BD`, `USUARIO_BD`, `PASSWORD_BD`.
  * En `ProductoDAO`: `SQL_INSERT`, `SQL_SELECT_ALL`, `SQL_SELECT_BY_ID`, `SQL_UPDATE`, `SQL_DELETE`.
  * En `Main`: `SEPARADOR_LINEA`, `SEPARADOR_CORTO`.

### 3.2 Organización de Paquetes
* `com.sena.inventario.config`: Componentes de configuración de infraestructura y conexión a base de datos.
* `com.sena.inventario.modelo`: Clases entidad con atributos privados, constructores sobrecargados, getters, setters y `toString()`.
* `com.sena.inventario.dao`: Capa de abstracción de datos para aislar las sentencias SQL de la lógica de presentación.
* `com.sena.inventario.main`: Capa de interacción y demostración.

### 3.3 Buenas Prácticas y Calidad de Código
* **Prevención de Inyección SQL:** Todas las consultas parametrizadas se ejecutan a través de `java.sql.PreparedStatement`, evitando concatenación insegura de cadenas.
* **Gestión Segura de Recursos (Try-With-Resources):** Todas las instancias de `Connection`, `PreparedStatement` y `ResultSet` se gestionan dentro de bloques `try (...)` para garantizar el cierre determinista de conexiones y evitar fugas de memoria (*connection leaks*).
* **Documentación con Javadoc:** Cada clase, interfaz, método público y atributo relevante cuenta con comentarios descriptivos estándar `@author`, `@version`, `@param`, `@return` y `@throws`.

---

## 4. Configuración de la Base de Datos

### 4.1 Script `script_bd.sql`
El archivo [`script_bd.sql`](script_bd.sql) ubicado en la raíz del proyecto contiene las sentencias DDL y DML para aprovisionar el entorno:

```sql
-- Creación de la BD
CREATE DATABASE IF NOT EXISTS db_inventario_sena
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE db_inventario_sena;

-- Tabla de productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    categoria VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_precio_positivo CHECK (precio >= 0),
    CONSTRAINT chk_stock_positivo CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 4.2 Ejecución del Script en MySQL
Abra una terminal o MySQL Workbench / phpMyAdmin y ejecute:

```bash
mysql -u root -p < script_bd.sql
```

---

## 5. Compilación y Ejecución del Proyecto

### 5.1 Requisitos Previos
* **Java Development Kit (JDK):** Versión 17 LTS o superior instalada y en variable `JAVA_HOME`.
* **Apache Maven:** Versión 3.8+ instalada.
* **Servicio MySQL:** En ejecución en el puerto `3306`.

### 5.2 Compilación con Maven
Desde la raíz del proyecto, ejecute:

```bash
# Limpiar y compilar el código fuente
mvn clean compile

# Empaquetar el JAR ejecutable y copiar dependencias a target/lib
mvn package -DskipTests
```

### 5.3 Ejecución de la Aplicación

#### Opción A: Demostración Automatizada del Ciclo CRUD Completo
Ejecuta la validación de conexión JDBC, inserta un producto de prueba, lista el inventario, actualiza el producto, lo elimina y confirma la integridad:

```bash
# Con Maven exec plugin:
mvn exec:java

# O directamente mediante el archivo JAR empaquetado:
java -jar target/evidencia-sena-crud-jdbc-1.0.0.jar
```

#### Opción B: Modo Menú Interactivo por Consola
Permite al evaluador o usuario interactuar directamente con el inventario:

```bash
java -jar target/evidencia-sena-crud-jdbc-1.0.0.jar --interactivo
```

#### Opción C: Con Parámetros de Conexión Personalizados (si su MySQL tiene contraseña)
```bash
java -Ddb.user=root -Ddb.password=su_password -jar target/evidencia-sena-crud-jdbc-1.0.0.jar
```

---

## 6. Historial de Versionamiento con Git

Siguiendo el **Indicador 3**, se aplicó control de versiones estructurado mediante **Commits Semánticos**:

```text
* commit 4 (HEAD -> main): feat(main): implementar clase Main de prueba integral, empaquetado y documentacion completa en README
* commit 3: feat(dao): implementar patron DAO con operaciones CRUD completas en ProductoDAO
* commit 2: feat(modelo): implementar modelo de dominio Producto y clase de conexion JDBC pura ConexionBD
* commit 1: chore(setup): configurar estructura de Maven, gitignore y script de base de datos
```

Comando para verificar el historial en cualquier momento:
```bash
git log --oneline --graph --decorate
```

---

## 7. Conclusión del Cumplimiento

Este proyecto materializa integralmente las competencias técnicas requeridas por la evidencia **GA7-220501096-AA2-EV01**, demostrando destreza en:
1. Conexión nativa robusta a motores relacionales mediante la API JDBC estándar.
2. Diseño modular basado en patrones de arquitectura reconocidos (DAO + JavaBean).
3. Disciplina de ingeniería de software a través de estándares de codificación formales (Google/Oracle Style Guide).
4. Trazabilidad profesional de código mediante Git.
