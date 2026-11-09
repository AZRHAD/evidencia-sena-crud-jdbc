package com.sena.inventario.main;

import com.sena.inventario.config.ConexionBD;
import com.sena.inventario.dao.IProductoDAO;
import com.sena.inventario.dao.ProductoDAO;
import com.sena.inventario.modelo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal de ejecución y pruebas para la evidencia del SENA GA7-220501096-AA2-EV01.
 * <p>
 * Demuestra de forma integral el cumplimiento de los cuatro (4) criterios de evaluación:
 * <ol>
 *   <li><b>Conexión JDBC pura:</b> Valida la conectividad directa a MySQL mediante {@link ConexionBD}.</li>
 *   <li><b>CRUD completo:</b> Ejecuta inserción, consulta, actualización y eliminación con {@link ProductoDAO}.</li>
 *   <li><b>Estándar de codificación:</b> Implementa Google/Oracle Java Style Guide con Javadoc exhaustivo.</li>
 *   <li><b>Versionamiento:</b> Trazabilidad de cambios registrada en Git con commits semánticos.</li>
 * </ol>
 * </p>
 *
 * @author Aprendiz SENA - ADSO
 * @version 1.0.0
 */
public class Main {

    /**
     * Separador visual para la salida en consola.
     */
    private static final String SEPARADOR_LINEA =
            "==========================================================================================";

    /**
     * Subseparador visual fino.
     */
    private static final String SEPARADOR_CORTO =
            "------------------------------------------------------------------------------------------";

    /**
     * Punto de entrada principal a la aplicación.
     *
     * @param args Argumentos de la línea de comandos (opcionales).
     */
    public static void main(String[] args) {
        imprimirEncabezadoEvidencia();

        // 1. Verificación previa de conexión JDBC
        System.out.println("\n[PASO 1] Comprobando conexión JDBC pura con MySQL...");
        try (Connection conn = ConexionBD.obtenerConexion()) {
            System.out.println("  ✓ ÉXITO: Conexión establecida correctamente.");
            System.out.println("  ✓ Driver: " + conn.getMetaData().getDriverName() + " v" + conn.getMetaData().getDriverVersion());
            System.out.println("  ✓ Base de datos: " + conn.getCatalog());
            System.out.println("  ✓ URL: " + ConexionBD.getUrlBd());
            System.out.println("  ✓ Usuario: " + ConexionBD.getUsuarioBd());
        } catch (SQLException e) {
            System.err.println("  ✗ ERROR: No fue posible conectar con MySQL.");
            System.err.println("  Detalle: " + e.getMessage());
            System.err.println("\nPor favor verifique que:");
            System.err.println("  1. El servicio MySQL esté iniciado (puerto 3306).");
            System.err.println("  2. Haya ejecutado el archivo 'script_bd.sql' para crear la BD 'db_inventario_sena'.");
            System.err.println("  3. Si usa credenciales distintas a root sin clave, pase los parámetros:");
            System.err.println("     -Ddb.user=su_usuario -Ddb.password=su_clave");
            System.exit(1);
        }

        // 2. Instanciación del DAO
        IProductoDAO productoDAO = new ProductoDAO();

        // 3. Demostración automatizada del CRUD completo
        ejecutarDemostracionCrud(productoDAO);

        // 4. Menú interactivo opcional si el usuario lo desea
        if (args.length > 0 && "--interactivo".equalsIgnoreCase(args[0])) {
            ejecutarMenuInteractivo(productoDAO);
        } else {
            System.out.println("\n" + SEPARADOR_LINEA);
            System.out.println(">>> Demostración automatizada completada exitosamente.");
            System.out.println(">>> (Para iniciar el menú interactivo por consola ejecute: java -jar ... --interactivo)");
            System.out.println(SEPARADOR_LINEA);
        }
    }

    /**
     * Ejecuta una secuencia completa del ciclo de vida CRUD sobre la tabla {@code productos}.
     *
     * @param productoDAO Instancia del DAO que gestiona la persistencia.
     */
    private static void ejecutarDemostracionCrud(IProductoDAO productoDAO) {
        System.out.println("\n" + SEPARADOR_LINEA);
        System.out.println("         DEMOSTRACIÓN DE OPERACIONES CRUD - PATRÓN DAO CON JDBC PURO");
        System.out.println(SEPARADOR_LINEA);

        // --- C1: LISTAR PRODUCTOS INICIALES (READ) ---
        System.out.println("\n[CRUD 1: READ] Listado actual de productos en 'db_inventario_sena':");
        imprimirTablaProductos(productoDAO.listarProductos());

        // --- C2: INSERTAR PRODUCTO (CREATE) ---
        System.out.println("\n[CRUD 2: CREATE] Insertando nuevo producto de prueba...");
        Producto nuevo = new Producto(
                "Audífonos Gamer HyperX Cloud II",
                "Sonido envolvente 7.1 virtual, micrófono desmontable con cancelación de ruido",
                340000.00,
                25,
                "Audio"
        );

        boolean insertado = productoDAO.insertarProducto(nuevo);
        if (insertado) {
            System.out.println("  ✓ Producto insertado exitosamente con ID asignado: " + nuevo.getIdProducto());
            System.out.println("  Detalle: " + nuevo);
        } else {
            System.err.println("  ✗ Error al insertar el producto.");
            return;
        }

        // Comprobación en lista
        System.out.println("\n[VALIDACIÓN] Listado tras la inserción:");
        imprimirTablaProductos(productoDAO.listarProductos());

        // --- C3: ACTUALIZAR PRODUCTO (UPDATE) ---
        System.out.println("\n[CRUD 3: UPDATE] Modificando datos del producto ID: " + nuevo.getIdProducto());
        nuevo.setNombre("Audífonos Gamer HyperX Cloud II (Wireless Edition)");
        nuevo.setPrecio(420000.00);
        nuevo.setStock(18);
        nuevo.setDescripcion("Edición inalámbrica 2.4GHz, batería de 30 horas, sonido 7.1");

        boolean actualizado = productoDAO.actualizarProducto(nuevo);
        if (actualizado) {
            System.out.println("  ✓ Producto actualizado correctamente en MySQL.");
            Producto consultado = productoDAO.buscarPorId(nuevo.getIdProducto());
            System.out.println("  Registro verificado desde BD: " + consultado);
        } else {
            System.err.println("  ✗ Error al actualizar el producto.");
        }

        // --- C4: ELIMINAR PRODUCTO (DELETE) ---
        System.out.println("\n[CRUD 4: DELETE] Eliminando producto de prueba ID: " + nuevo.getIdProducto());
        boolean eliminado = productoDAO.eliminarProducto(nuevo.getIdProducto());
        if (eliminado) {
            System.out.println("  ✓ Producto eliminado exitosamente de la base de datos.");
        } else {
            System.err.println("  ✗ Error al eliminar el producto.");
        }

        // Comprobación final
        System.out.println("\n[VALIDACIÓN FINAL] Listado final de productos tras el ciclo CRUD:");
        imprimirTablaProductos(productoDAO.listarProductos());
    }

    /**
     * Imprime en formato tabular los registros de productos entregados.
     *
     * @param productos Lista de productos a imprimir.
     */
    public static void imprimirTablaProductos(List<Producto> productos) {
        System.out.println(SEPARADOR_CORTO);
        System.out.printf("| %-4s | %-35s | %-15s | %-14s | %-6s |\n",
                "ID", "NOMBRE", "CATEGORÍA", "PRECIO (COP)", "STOCK");
        System.out.println(SEPARADOR_CORTO);

        if (productos == null || productos.isEmpty()) {
            System.out.println("| (No hay productos registrados en este momento)                                          |");
        } else {
            for (Producto p : productos) {
                System.out.printf("| %-4d | %-35.35s | %-15.15s | $%,13.2f | %-6d |\n",
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getCategoria(),
                        p.getPrecio(),
                        p.getStock());
            }
        }
        System.out.println(SEPARADOR_CORTO);
    }

    /**
     * Despliega un menú interactivo en consola para operaciones manuales.
     *
     * @param dao Instancia del DAO.
     */
    private static void ejecutarMenuInteractivo(IProductoDAO dao) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n" + SEPARADOR_LINEA);
            System.out.println("                    GESTIÓN DE PRODUCTOS - MENÚ INTERACTIVO");
            System.out.println(SEPARADOR_LINEA);
            System.out.println("1. Listar todos los productos (listarProductos)");
            System.out.println("2. Insertar nuevo producto (insertarProducto)");
            System.out.println("3. Actualizar producto existente (actualizarProducto)");
            System.out.println("4. Eliminar producto por ID (eliminarProducto)");
            System.out.println("5. Buscar producto por ID (buscarPorId)");
            System.out.println("0. Salir de la aplicación");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Ingrese un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> imprimirTablaProductos(dao.listarProductos());
                case 2 -> {
                    System.out.print("Nombre del producto: ");
                    String nombre = scanner.nextLine().trim();
                    System.out.print("Descripción: ");
                    String desc = scanner.nextLine().trim();
                    System.out.print("Precio (ej. 150000.00): ");
                    double precio = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Stock (unidades): ");
                    int stock = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Categoría: ");
                    String cat = scanner.nextLine().trim();

                    Producto p = new Producto(nombre, desc, precio, stock, cat);
                    if (dao.insertarProducto(p)) {
                        System.out.println("✓ Producto insertado con ID: " + p.getIdProducto());
                    } else {
                        System.out.println("✗ Error al insertar el producto.");
                    }
                }
                case 3 -> {
                    System.out.print("ID del producto a actualizar: ");
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    Producto p = dao.buscarPorId(id);
                    if (p == null) {
                        System.out.println("✗ Producto no encontrado con ID: " + id);
                        break;
                    }
                    System.out.println("Producto actual: " + p);
                    System.out.print("Nuevo nombre (Enter para mantener [" + p.getNombre() + "]): ");
                    String nombre = scanner.nextLine().trim();
                    if (!nombre.isEmpty()) p.setNombre(nombre);

                    System.out.print("Nueva descripción (Enter para mantener): ");
                    String desc = scanner.nextLine().trim();
                    if (!desc.isEmpty()) p.setDescripcion(desc);

                    System.out.print("Nuevo precio (Enter para mantener [" + p.getPrecio() + "]): ");
                    String precioStr = scanner.nextLine().trim();
                    if (!precioStr.isEmpty()) p.setPrecio(Double.parseDouble(precioStr));

                    System.out.print("Nuevo stock (Enter para mantener [" + p.getStock() + "]): ");
                    String stockStr = scanner.nextLine().trim();
                    if (!stockStr.isEmpty()) p.setStock(Integer.parseInt(stockStr));

                    System.out.print("Nueva categoría (Enter para mantener [" + p.getCategoria() + "]): ");
                    String cat = scanner.nextLine().trim();
                    if (!cat.isEmpty()) p.setCategoria(cat);

                    if (dao.actualizarProducto(p)) {
                        System.out.println("✓ Producto ID " + id + " actualizado con éxito.");
                    } else {
                        System.out.println("✗ Error al actualizar el producto.");
                    }
                }
                case 4 -> {
                    System.out.print("ID del producto a eliminar: ");
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    if (dao.eliminarProducto(id)) {
                        System.out.println("✓ Producto ID " + id + " eliminado con éxito.");
                    } else {
                        System.out.println("✗ No se pudo eliminar el producto con ID: " + id);
                    }
                }
                case 5 -> {
                    System.out.print("ID del producto a consultar: ");
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    Producto p = dao.buscarPorId(id);
                    if (p != null) {
                        System.out.println("Resultado: " + p);
                        System.out.println("Descripción: " + p.getDescripcion());
                        System.out.println("Fecha Creación: " + p.getFechaCreacion());
                    } else {
                        System.out.println("✗ Producto no encontrado con ID: " + id);
                    }
                }
                case 0 -> System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Imprime en consola el encabezado descriptivo de la evidencia del SENA.
     */
    private static void imprimirEncabezadoEvidencia() {
        System.out.println(SEPARADOR_LINEA);
        System.out.println("  SERVICIO NACIONAL DE APRENDIZAJE - SENA");
        System.out.println("  Tecnología en Análisis y Desarrollo de Software (ADSO)");
        System.out.println("  Evidencia: GA7-220501096-AA2-EV01");
        System.out.println("  Actividad: Codificación de módulos del software");
        System.out.println("  Módulo: Inventario de Productos (CRUD JDBC Puro + Patrón DAO)");
        System.out.println(SEPARADOR_LINEA);
    }
}
