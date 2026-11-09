package com.sena.inventario.dao;

import com.sena.inventario.config.ConexionBD;
import com.sena.inventario.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta del patrón Data Access Object (DAO) para la entidad {@link Producto}.
 * <p>
 * Utiliza <b>JDBC puro</b> con {@link PreparedStatement} para prevenir ataques de
 * inyección SQL y garantizar un alto rendimiento. No utiliza frameworks ORM como JPA,
 * Hibernate o Spring Data, acatando estrictamente el instrumento de evaluación del SENA.
 * </p>
 * <p>
 * Estándares aplicados:
 * <ul>
 *   <li>Nombre de clase en PascalCase (ProductoDAO).</li>
 *   <li>Nombres de métodos en camelCase (insertarProducto, listarProductos, actualizarProducto, eliminarProducto).</li>
 *   <li>Constantes SQL en UPPER_SNAKE_CASE.</li>
 *   <li>Manejo seguro de recursos con sentencias {@code try-with-resources}.</li>
 * </ul>
 * </p>
 *
 * @author Aprendiz SENA - ADSO
 * @version 1.0.0
 */
public class ProductoDAO implements IProductoDAO {

    /**
     * Sentencia SQL parametrizada para la inserción de un nuevo producto.
     */
    private static final String SQL_INSERT =
            "INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";

    /**
     * Sentencia SQL para consultar todos los productos ordenados por su identificador.
     */
    private static final String SQL_SELECT_ALL =
            "SELECT id_producto, nombre, descripcion, precio, stock, categoria, fecha_creacion " +
            "FROM productos ORDER BY id_producto ASC";

    /**
     * Sentencia SQL parametrizada para consultar un producto por su clave primaria.
     */
    private static final String SQL_SELECT_BY_ID =
            "SELECT id_producto, nombre, descripcion, precio, stock, categoria, fecha_creacion " +
            "FROM productos WHERE id_producto = ?";

    /**
     * Sentencia SQL parametrizada para actualizar los campos de un producto existente.
     */
    private static final String SQL_UPDATE =
            "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria = ? " +
            "WHERE id_producto = ?";

    /**
     * Sentencia SQL parametrizada para eliminar un producto según su identificador.
     */
    private static final String SQL_DELETE =
            "DELETE FROM productos WHERE id_producto = ?";

    /**
     * Inserta un nuevo producto en la tabla {@code productos} de MySQL.
     * Si la inserción es satisfactoria, asigna al objeto el ID autoincremental generado.
     *
     * @param producto Objeto {@link Producto} con la información a persistir.
     * @return {@code true} si el registro se insertó correctamente, {@code false} en caso de error.
     */
    @Override
    public boolean insertarProducto(Producto producto) {
        if (producto == null) {
            System.err.println("Error: El producto a insertar no puede ser nulo.");
            return false;
        }

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rsClaves = ps.getGeneratedKeys()) {
                    if (rsClaves.next()) {
                        producto.setIdProducto(rsClaves.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al insertar producto: " + e.getMessage());
        }

        return false;
    }

    /**
     * Recupera todos los registros de la tabla {@code productos}.
     *
     * @return Lista de {@link Producto} con los datos recuperados de la BD.
     */
    @Override
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto producto = mapearProducto(rs);
                lista.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al listar productos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza los datos de un producto existente en la base de datos.
     *
     * @param producto Objeto {@link Producto} con el ID existente y los nuevos valores.
     * @return {@code true} si se actualizó el registro, {@code false} si no se encontró o hubo error.
     */
    @Override
    public boolean actualizarProducto(Producto producto) {
        if (producto == null || producto.getIdProducto() == null) {
            System.err.println("Error: Se requiere un producto válido con ID para actualizar.");
            return false;
        }

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());
            ps.setInt(6, producto.getIdProducto());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar producto ID " + producto.getIdProducto() + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Elimina un producto por su clave primaria {@code idProducto}.
     *
     * @param idProducto Identificador del producto a eliminar.
     * @return {@code true} si la eliminación fue efectiva, {@code false} en caso contrario.
     */
    @Override
    public boolean eliminarProducto(int idProducto) {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idProducto);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar producto ID " + idProducto + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca un producto por su clave primaria.
     *
     * @param idProducto Clave primaria del producto.
     * @return Instancia de {@link Producto} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Producto buscarPorId(int idProducto) {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al buscar producto por ID " + idProducto + ": " + e.getMessage());
        }

        return null;
    }

    /**
     * Método auxiliar privado para mapear la fila actual de un {@link ResultSet} a una entidad {@link Producto}.
     *
     * @param rs Cursor del {@link ResultSet} posicionado en una fila válida.
     * @return Objeto {@link Producto} con los campos correspondientes.
     * @throws SQLException Si ocurre un error al leer las columnas de la fila.
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return producto;
    }
}
