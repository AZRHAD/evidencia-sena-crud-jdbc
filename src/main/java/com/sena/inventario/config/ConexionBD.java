package com.sena.inventario.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC pura con la base de datos MySQL (db_inventario_sena).
 * <p>
 * Aplica el estándar de codificación Google/Oracle Java Style Guide:
 * <ul>
 *   <li>Nombre de clase en PascalCase (ConexionBD).</li>
 *   <li>Constantes en UPPER_SNAKE_CASE.</li>
 *   <li>Métodos y variables en camelCase.</li>
 *   <li>Uso estricto de java.sql.Connection y java.sql.DriverManager sin frameworks ORM.</li>
 * </ul>
 * </p>
 *
 * @author Aprendiz SENA - ADSO
 * @version 1.0.0
 */
public final class ConexionBD {

    /**
     * Controlador JDBC oficial de MySQL 8+.
     */
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    /**
     * Nombre del host o servidor de la base de datos.
     */
    private static final String HOST_BD = System.getProperty("db.host", "localhost");

    /**
     * Puerto estándar del servicio MySQL.
     */
    private static final String PUERTO_BD = System.getProperty("db.port", "3306");

    /**
     * Nombre de la base de datos asignada para la evidencia SENA.
     */
    private static final String NOMBRE_BD = "db_inventario_sena";

    /**
     * Cadena de conexión JDBC con parámetros de zona horaria y codificación UTF-8.
     */
    private static final String URL_BD = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
            HOST_BD, PUERTO_BD, NOMBRE_BD
    );

    /**
     * Usuario de autenticación en MySQL (configurable vía propiedad del sistema o por defecto 'root').
     */
    private static final String USUARIO_BD = System.getProperty("db.user", "root");

    /**
     * Contraseña del usuario de autenticación en MySQL.
     */
    private static final String PASSWORD_BD = System.getProperty("db.password", "");

    /**
     * Constructor privado para prevenir la instanciación de una clase de utilidad estática.
     */
    private ConexionBD() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada.");
    }

    /**
     * Obtiene una nueva instancia activa de {@link Connection} hacia la base de datos MySQL.
     *
     * @return Objeto {@link Connection} listo para ejecutar sentencias SQL.
     * @throws SQLException Si ocurre un error de comunicación o credenciales inválidas.
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Carga explícita del driver JDBC
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL en el classpath: " + e.getMessage());
            throw new SQLException("Driver JDBC no disponible: " + DRIVER_CLASS, e);
        }

        return DriverManager.getConnection(URL_BD, USUARIO_BD, PASSWORD_BD);
    }

    /**
     * Cierra de forma segura una conexión JDBC activa verificando que no sea nula ni esté previamente cerrada.
     *
     * @param conexion Objeto {@link Connection} que se desea cerrar.
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Advertencia al cerrar la conexión JDBC: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene la URL de conexión configurada (útil para diagnósticos y pruebas).
     *
     * @return La URL JDBC como cadena de caracteres.
     */
    public static String getUrlBd() {
        return URL_BD;
    }

    /**
     * Obtiene el usuario configurado para la base de datos.
     *
     * @return Nombre de usuario de conexión.
     */
    public static String getUsuarioBd() {
        return USUARIO_BD;
    }
}
