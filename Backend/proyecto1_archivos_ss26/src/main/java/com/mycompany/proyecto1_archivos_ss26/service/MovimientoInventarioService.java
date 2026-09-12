/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.entity.MovimientoInventario;
import com.mycompany.proyecto1_archivos_ss26.entity.Producto;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.modelos.TipoMovimiento;
import com.mycompany.proyecto1_archivos_ss26.repository.MovimientoInventarioRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoRepository,
            ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {
        MovimientoInventario guardado = this.movimientoRepository.save(movimiento);

        this.aplicarMovimientoAlStock(
                movimiento.getProducto().getIdProducto(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad()
        );

        return guardado;
    }

    @Transactional
    public void eliminarMovimiento(Integer idMovimiento) {
        MovimientoInventario movimiento = this.movimientoRepository.findById(idMovimiento)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un movimiento con id " + idMovimiento));

        this.aplicarMovimientoAlStock(
                movimiento.getProducto().getIdProducto(),
                this.invertir(movimiento.getTipoMovimiento()),
                movimiento.getCantidad()
        );

        this.movimientoRepository.delete(movimiento);
    }

    private void aplicarMovimientoAlStock(Integer idProducto, TipoMovimiento tipo, Integer cantidad) {
        Producto producto = this.productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un producto con id " + idProducto));

        if (tipo == TipoMovimiento.SALIDA) {
            producto.setStockActual(producto.getStockActual() - cantidad);
        } else {
            producto.setStockActual(producto.getStockActual() + cantidad);
        }
        this.productoRepository.save(producto);
    }

    private TipoMovimiento invertir(TipoMovimiento tipo) {
        return tipo == TipoMovimiento.SALIDA ? TipoMovimiento.ENTRADA : TipoMovimiento.SALIDA;
    }
}
