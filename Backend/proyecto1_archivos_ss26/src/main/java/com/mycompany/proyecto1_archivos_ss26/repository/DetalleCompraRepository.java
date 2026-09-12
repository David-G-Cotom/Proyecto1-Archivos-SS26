/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.repository;

import com.mycompany.proyecto1_archivos_ss26.entity.DetalleCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author david
 */
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    /**
     * Lotes disponibles de un producto ordenados del mas antiguo al mas nuevo
     *
     * @param idProducto
     * @param cantidadDisponibleMinima
     * @return los detalles de compra de un producto ordenado desde el Primer
     * detalle en Entrar hasta el ultimo en ser registrado
     */
    List<DetalleCompra> findByProducto_IdProductoAndCantidadDisponibleGreaterThanOrderByCompra_FechaCompraAsc(
            Integer idProducto, Integer cantidadDisponibleMinima);

}
