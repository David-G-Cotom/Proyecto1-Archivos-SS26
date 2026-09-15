/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.VentaRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.VentaResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.service.VentaService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION', 'VENTAS')")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrar(@Valid @RequestBody VentaRequestDTO request) {
        VentaResponseDTO creada = this.ventaService.registrarVenta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public VentaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return this.ventaService.obtenerPorId(id);
    }

    @GetMapping
    public List<VentaResponseDTO> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        if (desde != null && hasta != null) {
            return this.ventaService.listarPorRangoDeFechas(desde, hasta);
        }
        return this.ventaService.listar();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<VentaResponseDTO> listarPorCliente(@PathVariable Integer idCliente) {
        return this.ventaService.listarPorCliente(idCliente);
    }

}
