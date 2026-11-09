# SERVICIO NACIONAL DE APRENDIZAJE (SENA)
## CENTRO DE FORMACIÓN EN ANÁLISIS Y DESARROLLO DE SOFTWARE
### PROGRAMA: TECNOLOGÍA EN ANÁLISIS Y DESARROLLO DE SOFTWARE (ADSO)

---

# INFORME TÉCNICO DE EVIDENCIA
## GA7-220501096-AA2-EV01: CODIFICACIÓN DE MÓDULOS DEL SOFTWARE

* **Fase del Proyecto:** Ejecución
* **Actividad de Proyecto:** AP7 - Codificar los módulos del sistema de información según requerimientos del cliente.
* **Actividad de Aprendizaje:** GA7-220501096-AA2 - Aplicar estándares de codificación, de acuerdo con el diseño.
* **Competencia:** 220501096 - Desarrollar la solución de software de acuerdo con el diseño y metodologías de desarrollo.
* **Resultado de Aprendizaje (RAP):** 220501096-04 - Codificar el software de acuerdo con el diseño establecido.
* **Módulo Implementado:** Módulo de Gestión de Inventario de Productos (`db_inventario_sena`)
* **Tecnologías Aplicadas:** Java 17 LTS, Apache Maven 3.9, MySQL 8.0, JDBC Pura (mysql-connector-j), Git.

---

## 1. INTRODUCCIÓN

En el ciclo de vida del desarrollo de software orientado a servicios y aplicaciones empresariales, la codificación rigurosa de módulos bajo estándares internacionales de calidad, modularidad y trazabilidad representa una competencia indispensable.

El presente informe consolida el desarrollo integral de la evidencia **GA7-220501096-AA2-EV01**, en la cual se construye un módulo *stand-alone* de inventario de productos en lenguaje **Java**, aplicando arquitectura desacoplada mediante el patrón de diseño **Data Access Object (DAO)** y comunicación directa con el motor de bases de datos relacional **MySQL** a través de la API estándar **JDBC pura** (`java.sql`), excluyendo intencionalmente capas de abstracción ORM para evidenciar el dominio nativo del acceso a datos.

Asimismo, se demuestra la aplicación estricta de las normas de estilo de **Google y Oracle Java**, complementado con una estrategia formal de versionamiento semántico bajo la herramienta **Git**.

---

## 2. MATRIZ DE CUMPLIMIENTO DEL INSTRUMENTO DE EVALUACIÓN

A continuación se detalla la correspondencia entre los ítems de la lista de chequeo oficial del SENA y los componentes técnicos desarrollados:

| No. | Variable / Indicador de Logro | Cumplimiento | Componente y Evidencia Técnica |
|:---:|:---|:---:|:---|
| **1** | **Realiza conexiones con bases de datos utilizando JDBC.** | **SÍ** | Clase `ConexionBD` en el paquete `com.sena.inventario.config`. Emplea exclusivamente `java.sql.Connection` y `java.sql.DriverManager` junto al conector oficial `com.mysql:mysql-connector-j` versión 8.3.0. Cumple con la restricción de **no usar** JPA, Hibernate ni Spring Data. |
| **2** | **Aplica el CRUD.** | **SÍ** | Interfaz `IProductoDAO` e implementación `ProductoDAO` en el paquete `com.sena.inventario.dao`. Integra de forma completa y probada los cuatro métodos esenciales: `insertarProducto` (Create), `listarProductos` (Read), `actualizarProducto` (Update) y `eliminarProducto` (Delete), además del método complementario `buscarPorId`. |
| **3** | **Utiliza herramientas de versionamiento para el proyecto.** | **SÍ** | Repositorio Git inicializado localmente en rama `main`. Archivo `.gitignore` debidamente estructurado. Realización de **4 commits semánticos progresivos** documentados con estándar *Conventional Commits* que abarcan desde el setup inicial hasta la entrega final. |
| **4** | **Utiliza un estándar de codificación definido.** | **SÍ** | Adopción de la **Google Java Style Guide** y convenciones de Oracle: nomenclatura PascalCase en clases/interfaces, camelCase en métodos/variables, UPPER_SNAKE_CASE en constantes SQL y de conexión, estructuración limpia por paquetes y documentación al 100% mediante **Javadoc**. |

---

## 3. ARQUITECTURA DE SOFTWARE Y PATRONES APLICADOS

### 3.1 Patrón Data Access Object (DAO)
El patrón DAO aísla y encapsula todos los accesos a la fuente de datos. La capa de presentación y la lógica de pruebas interactúan únicamente con la interfaz `IProductoDAO`, permitiendo que el mecanismo de almacenamiento pueda evolucionar sin impactar la lógica del negocio:

```
+-------------------------------------------------------------+
|                        Capa Main                            |
|             (com.sena.inventario.main.Main)                 |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                   Capa DAO (Contrato)                       |
|           (com.sena.inventario.dao.IProductoDAO)            |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|             Implementación DAO (JDBC Pura)                  |
|           (com.sena.inventario.dao.ProductoDAO)             |
+------------------------------+------------------------------+
         |                                           |
         v                                           v
+---------------------------------+  +------------------------+
|       Entidad de Dominio        |  |  Capa de Infraestructura|
| (com.sena.inventario.modelo.   |  |   (com.sena.inventario. |
|            Producto)            |  |    config.ConexionBD)  |
+---------------------------------+  +-----------+------------+
                                                 |
                                                 v
                                     +------------------------+
                                     |  MySQL 8.0 (Database)  |
                                     |  `db_inventario_sena`  |
                                     +------------------------+
```

### 3.2 Seguridad y Gestión de Recursos
* **Prevención de Inyección SQL:** Todas las operaciones DML y de consulta utilizan `java.sql.PreparedStatement` con asignación explícita mediante marcadores de posición (`?`), evitando la concatenación de cadenas vulnerables.
* **Patrón Try-With-Resources:** Se utiliza la sintaxis formal de Java 7+ para garantizar la liberación determinista de los descriptores de red y memoria de `Connection`, `PreparedStatement` y `ResultSet`.

---

## 4. BASE DE DATOS Y DICCIONARIO DE DATOS

### 4.1 Definición de la Base de Datos (`script_bd.sql`)
La persistencia del módulo se gestiona sobre el esquema **`db_inventario_sena`** en MySQL con codificación `utf8mb4`:

```sql
CREATE DATABASE IF NOT EXISTS db_inventario_sena
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_inventario_sena;

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria autoincremental',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre comercial del producto',
    descripcion VARCHAR(255) COMMENT 'Descripción detallada',
    precio DECIMAL(10, 2) NOT NULL COMMENT 'Precio unitario de venta (COP)',
    stock INT NOT NULL DEFAULT 0 COMMENT 'Existencias físicas en bodega',
    categoria VARCHAR(50) NOT NULL COMMENT 'Categoría o departamento',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Auditoría de fecha de creación',
    CONSTRAINT chk_precio_positivo CHECK (precio >= 0),
    CONSTRAINT chk_stock_positivo CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 4.2 Diccionario de Datos

| Campo | Tipo | Nulo | Clave | Predeterminado | Descripción |
|:---|:---|:---:|:---:|:---:|:---|
| `id_producto` | `INT` | NO | PRI | Auto_increment | Identificador único de cada artículo en el inventario. |
| `nombre` | `VARCHAR(100)` | NO | | | Nombre comercial y marca del producto. |
| `descripcion` | `VARCHAR(255)` | SÍ | | NULL | Especificaciones y características técnicas. |
| `precio` | `DECIMAL(10,2)` | NO | | | Valor unitario en pesos colombianos. Restricción `CHECK >= 0`. |
| `stock` | `INT` | NO | | 0 | Cantidad de unidades físicas. Restricción `CHECK >= 0`. |
| `categoria` | `VARCHAR(50)` | NO | | | Clasificación taxonómica (Tecnología, Periféricos, Audio, etc.). |
| `fecha_creacion` | `TIMESTAMP` | NO | | CURRENT_TIMESTAMP | Estampa de tiempo registrada por el servidor MySQL. |

---

## 5. ESTÁNDARES DE CODIFICACIÓN IMPLEMENTADOS

El código fuente respeta integralmente las directrices de la **Google Java Style Guide**:

### 5.1 Reglas de Nomenclatura
1. **Clases e Interfaces (`PascalCase`):**
   * `ConexionBD`: Clase utilitaria final con constructor privado.
   * `Producto`: Entidad de dominio encapsulada con sobrecarga de constructores.
   * `IProductoDAO`: Interfaz de operaciones de persistencia.
   * `ProductoDAO`: Implementación del DAO.
   * `Main`: Clase principal de ejecución.
2. **Métodos y Variables (`camelCase`):**
   * Métodos: `obtenerConexion()`, `cerrarConexion()`, `insertarProducto()`, `listarProductos()`, `actualizarProducto()`, `eliminarProducto()`, `buscarPorId()`, `mapearProducto()`.
   * Variables de instancia: `idProducto`, `nombre`, `descripcion`, `precio`, `stock`, `categoria`, `fechaCreacion`.
3. **Constantes Simbólicas (`UPPER_SNAKE_CASE`):**
   * `DRIVER_CLASS = "com.mysql.cj.jdbc.Driver"`
   * `URL_BD`, `USUARIO_BD`, `PASSWORD_BD`
   * Sentencias SQL parametrizadas: `SQL_INSERT`, `SQL_SELECT_ALL`, `SQL_SELECT_BY_ID`, `SQL_UPDATE`, `SQL_DELETE`.
   * Formateadores visuales: `SEPARADOR_LINEA`, `SEPARADOR_CORTO`.

### 5.2 Estructura Modular de Paquetes
* `com.sena.inventario.config`: Aislamiento de infraestructura de red y conexión a base de datos.
* `com.sena.inventario.modelo`: Clases entidad con atributos privados, constructores sobrecargados, métodos accesores/mutadores y sobreescritura de `equals()`, `hashCode()` y `toString()`.
* `com.sena.inventario.dao`: Contrato e implementación de acceso a datos bajo el patrón DAO.
* `com.sena.inventario.main`: Punto de entrada de la aplicación con rutinas de demostración automatizada y consola interactiva.

### 5.3 Documentación con Javadoc
Todos los archivos fuente contienen cabeceras formales de documentación con las etiquetas:
* `@author Aprendiz SENA - ADSO`
* `@version 1.0.0`
* `@param` con la descripción de cada parámetro de entrada.
* `@return` con la explicación del valor resultante.
* `@throws` con las excepciones comprobadas (`SQLException`).

---

## 6. GUÍA DE EJECUCIÓN PARA EL INSTRUCTOR

Para garantizar que el instructor pueda validar la evidencia de forma inmediata en cualquier equipo Windows, se han dispuesto tres vías de ejecución:

### Vía 1: Ejecución con un solo clic (Archivos por Lotes `.bat`)
1. Iniciar el servicio local de MySQL (puerto 3306) y cargar el script `script_bd.sql`.
2. Hacer doble clic en el archivo:
   * **`1_ejecutar_demostracion_automatica.bat`**: Ejecuta la comprobación de conectividad JDBC y todo el ciclo CRUD completo mostrando resultados en consola.
   * **`2_ejecutar_menu_interactivo.bat`**: Despliega un menú en consola para registrar, editar, listar o eliminar productos manualmente.
   * **`3_recompilar_proyecto.bat`**: Limpia y compila el proyecto con Maven.
   * **`4_ver_historial_git.bat`**: Muestra en pantalla el historial de commits semánticos de Git.

### Vía 2: Ejecución mediante línea de comandos con el archivo JAR
```bash
# Modo demostración automática:
java -jar target/evidencia-sena-crud-jdbc-1.0.0.jar

# Modo menú interactivo:
java -jar target/evidencia-sena-crud-jdbc-1.0.0.jar --interactivo

# Si la base de datos MySQL posee contraseña personalizada:
java -Ddb.user=root -Ddb.password=su_clave -jar target/evidencia-sena-crud-jdbc-1.0.0.jar
```

### Vía 3: Ejecución mediante Maven
```bash
mvn clean package -DskipTests
mvn exec:java
```

---

## 7. TRAZABILIDAD Y CONTROL DE VERSIONES (GIT)

El desarrollo del proyecto fue versionado paso a paso para reflejar la evolución del ciclo de vida del software:

```text
* 4c35a3a (HEAD -> main) feat(main): implementar clase Main de prueba integral, empaquetado y documentacion completa en README
* 64d4005 feat(dao): implementar patron DAO con operaciones CRUD completas en ProductoDAO
* 957d51e feat(modelo): implementar modelo de dominio Producto y clase de conexion JDBC pura ConexionBD
* 1e15cc3 chore(setup): configurar estructura de Maven, gitignore y script de base de datos
```

### Descripción de los Commits Semánticos:
1. **Commit 1 (`1e15cc3`):** Creación del archivo descriptor de Maven `pom.xml` con dependencia a `mysql-connector-j`, reglas de exclusión en `.gitignore` y definición de la base de datos en `script_bd.sql`.
2. **Commit 2 (`957d51e`):** Implementación de la clase `ConexionBD` para gestión de conexiones JDBC puras y de la entidad de dominio `Producto`.
3. **Commit 3 (`64d4005`):** Implementación de la interfaz `IProductoDAO` y de la clase concreta `ProductoDAO` con las 4 operaciones CRUD (`insertarProducto`, `listarProductos`, `actualizarProducto`, `eliminarProducto`).
4. **Commit 4 (`4c35a3a`):** Implementación de la clase `Main` con pruebas del ciclo CRUD completo, menú interactivo y documentación técnica en `README.md`.

---

## 8. CONCLUSIONES

1. Se verificó que el uso de **JDBC puro** mediante `PreparedStatement` ofrece un rendimiento superior y un control preciso sobre las sentencias SQL que interactúan con MySQL, cumpliendo a cabalidad con los lineamientos pedagógicos del SENA.
2. El desacoplamiento logrado mediante el **Patrón DAO** y la interfaz `IProductoDAO` facilita el mantenimiento, la escalabilidad y las pruebas unitarias del software sin alterar la lógica de negocio.
3. El apego estricto a las normas de codificación de **Google/Oracle Java Style Guide** asegura que el código sea legible, profesional y fácil de auditar por el equipo evaluador.
4. El versionamiento semántico con **Git** garantiza la trazabilidad histórica de cada etapa de desarrollo.
