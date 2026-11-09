package com.sena.inventario.dao;

import com.sena.inventario.modelo.Producto;
import java.util.List;

/**
 * Contrato de operaciones CRUD para la entidad {@link Producto}.
 * <p>
 * Define la interfaz del patrón Data Access Object (DAO) para desacoplar
 * la lógica de negocio del acceso y persistencia en la base de datos MySQL.
 * Cumple con los lineamientos del SENA para la evidencia GA7-220501096-AA2-EV01.
 * </p>
 *
 * @author Aprendiz SENA - ADSO
 * @version 1.0.0
 */
public interface IProductoDAO {

    /**
     * Inserta un nuevo registro de producto en la base de datos MySQL.
     *
     * @param producto Objeto {@link Producto} con los datos a persistir.
     * @return {@code true} si la inserción fue exitosa; {@code false} en caso contrario.
     */
    boolean insertarProducto(Producto producto);

    /**
     * Recupera la lista completa de productos registrados en la base de datos.
     *
     * @return Lista de objetos {@link Producto} encontrados. Si no hay registros, retorna una lista vacía.
     */
    List<Producto> listarProductos();

    /**
     * Actualiza los datos de un producto existente identificado por su {@code idProducto}.
     *
     * @param producto Objeto {@link Producto} con la información actualizada.
     * @return {@code true} si al menos una fila fue afectada por la actualización; {@code false} si no.
     */
    boolean actualizarProducto(Producto producto);

    /**
     * Elimina físicamente un producto de la base de datos mediante su clave primaria.
     *
     * @param idProducto Identificador único del producto a eliminar.
     * @return {@code true} si el producto fue eliminado correctamente; {@code false} si no existía o falló.
     */
    boolean eliminarProducto(int idProducto);

    /**
     * Busca y recupera un producto específico a partir de su identificador único.
     *
     * @param idProducto Clave primaria del producto buscado.
     * @return Instancia de {@link Producto} correspondiente al ID, o {@code null} si no se encuentra.
     */
    Producto buscarPorId(int idProducto);
}
