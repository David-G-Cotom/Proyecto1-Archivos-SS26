/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.MovimientoInventarioResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.StockActualResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.service.InventarioService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/stock")
    public List<StockActualResponseDTO> listarStockActual() {
        return this.inventarioService.listarStockActual();
    }

    @GetMapping("/stock/{idProducto}")
    public StockActualResponseDTO consultarStock(@PathVariable Integer idProducto) {
        return this.inventarioService.consultarStockDeProducto(idProducto);
    }

    @GetMapping("/movimientos/{idProducto}")
    public List<MovimientoInventarioResponseDTO> listarMovimientos(@PathVariable Integer idProducto) {
        return this.inventarioService.listarMovimientosDeProducto(idProducto);
    }

}
