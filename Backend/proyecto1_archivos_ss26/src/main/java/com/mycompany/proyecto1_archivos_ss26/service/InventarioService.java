/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.StockActualResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.projection.StockActualProjection;
import com.mycompany.proyecto1_archivos_ss26.repository.ProductoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class InventarioService {

    private final ProductoRepository productoRepository;

    public InventarioService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<StockActualResponseDTO> listarStockActual() {
        List<StockActualProjection> stockActuales = this.productoRepository.listarStockActual();
        List<StockActualResponseDTO> respuesta = new ArrayList<>();
        for (StockActualProjection stockActual : stockActuales) {
            respuesta.add(this.mapear(stockActual));
        }
        return respuesta;
    }

    public StockActualResponseDTO consultarStockDeProducto(Integer idProducto) {
        StockActualProjection proyeccion = this.productoRepository.obtenerStockActual(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + idProducto));
        return this.mapear(proyeccion);
    }

    private StockActualResponseDTO mapear(StockActualProjection p) {
        boolean bajoStock = p.getStockActual() <= p.getStockMinimo();
        return new StockActualResponseDTO(p.getIdProducto(), p.getNombre(), p.getStockActual(), p.getStockMinimo(), bajoStock);
    }

}
