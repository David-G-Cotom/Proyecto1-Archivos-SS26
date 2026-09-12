/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.repository;

import com.mycompany.proyecto1_archivos_ss26.entity.Producto;
import com.mycompany.proyecto1_archivos_ss26.projection.StockActualProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author david
 */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrue();

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);

    @Query("""
            SELECT p.idProducto AS idProducto,
                   p.nombre     AS nombre,
                   p.stockMinimo AS stockMinimo,
                   p.stockActual AS stockActual
            FROM Producto p
            WHERE p.activo = true
            """)
    List<StockActualProjection> listarStockActual();

    @Query("""
            SELECT p.idProducto AS idProducto,
                   p.nombre     AS nombre,
                   p.stockMinimo AS stockMinimo,
                   p.stockActual AS stockActual
            FROM Producto p
            WHERE p.idProducto = :idProducto
            """)
    Optional<StockActualProjection> obtenerStockActual(@Param("idProducto") Integer idProducto);

}
