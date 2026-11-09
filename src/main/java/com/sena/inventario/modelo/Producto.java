package com.sena.inventario.modelo;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Entidad del modelo de dominio que representa un producto del inventario.
 * <p>
 * Sigue los lineamientos de Google/Oracle Java Style Guide:
 * <ul>
 *   <li>Nombre de clase en PascalCase.</li>
 *   <li>Atributos privados encapsulados con nombres en camelCase.</li>
 *   <li>Métodos accesores (getters) y mutadores (setters) en camelCase.</li>
 *   <li>Documentación Javadoc completa en cada miembro.</li>
 * </ul>
 * </p>
 *
 * @author Aprendiz SENA - ADSO
 * @version 1.0.0
 */
public class Producto {

    /**
     * Identificador único autoincremental del producto en la base de datos.
     */
    private Integer idProducto;

    /**
     * Nombre comercial o descripción corta del producto.
     */
    private String nombre;

    /**
     * Descripción detallada de especificaciones y características.
     */
    private String descripcion;

    /**
     * Precio unitario de venta en Pesos Colombianos (COP).
     */
    private double precio;

    /**
     * Cantidad de unidades físicas disponibles en stock.
     */
    private int stock;

    /**
     * Categoría o departamento de clasificación del producto.
     */
    private String categoria;

    /**
     * Estampa de tiempo correspondiente al registro del producto en el sistema.
     */
    private Timestamp fechaCreacion;

    /**
     * Constructor por defecto sin parámetros.
     */
    public Producto() {
    }

    /**
     * Constructor para la creación de un nuevo producto previo a su persistencia
     * (sin identificador ni fecha de creación, ya que son generados automáticamente por la BD).
     *
     * @param nombre      Nombre comercial del producto.
     * @param descripcion Descripción detallada del producto.
     * @param precio      Precio unitario de venta.
     * @param stock       Cantidad disponible en existencias.
     * @param categoria   Categoría de clasificación.
     */
    public Producto(String nombre, String descripcion, double precio, int stock, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Constructor completo con todos los atributos mapeados desde la base de datos.
     *
     * @param idProducto    Identificador único del producto.
     * @param nombre        Nombre comercial del producto.
     * @param descripcion   Descripción detallada del producto.
     * @param precio        Precio unitario de venta.
     * @param stock         Cantidad disponible en existencias.
     * @param categoria     Categoría de clasificación.
     * @param fechaCreacion Fecha y hora de creación del registro.
     */
    public Producto(Integer idProducto, String nombre, String descripcion, double precio,
                    int stock, String categoria, Timestamp fechaCreacion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el identificador único del producto.
     *
     * @return Identificador del producto o {@code null} si aún no ha sido persistido.
     */
    public Integer getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador único del producto.
     *
     * @param idProducto Nuevo identificador asignado.
     */
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el nombre comercial del producto.
     *
     * @return Nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre comercial del producto.
     *
     * @param nombre Nuevo nombre para el producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada del producto.
     *
     * @return Descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada del producto.
     *
     * @param descripcion Nueva descripción para el producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio unitario del producto en COP.
     *
     * @return Precio unitario.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario del producto.
     *
     * @param precio Nuevo precio unitario (debe ser mayor o igual a cero).
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad de existencias en stock.
     *
     * @return Unidades disponibles.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad de existencias en stock.
     *
     * @param stock Nuevas existencias disponibles.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return Nombre de la categoría.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría de clasificación.
     *
     * @param categoria Nueva categoría asignada.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la fecha y hora de creación del registro.
     *
     * @return Estampa de tiempo de la creación.
     */
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación del registro.
     *
     * @param fechaCreacion Estampa de tiempo asignada.
     */
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(idProducto, producto.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }

    @Override
    public String toString() {
        return String.format(
                "Producto [ID=%-3s | Nombre=%-30s | Categoria=%-15s | Precio=$%,10.2f | Stock=%-4d]",
                (idProducto != null ? idProducto : "N/A"),
                nombre,
                categoria,
                precio,
                stock
        );
    }
}
