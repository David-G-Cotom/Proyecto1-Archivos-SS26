/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.ProductoRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ProductoResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar(@RequestParam(defaultValue = "false") boolean soloActivos) {
        return this.productoService.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Integer id) {
        return this.productoService.obtenerPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION', 'INVENTARIO')")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productoService.crear(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION', 'INVENTARIO')")
    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoRequestDTO request) {
        return this.productoService.actualizar(id, request);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION', 'INVENTARIO')")
    @PatchMapping("/{id}/desactivar")
    public ProductoResponseDTO desactivar(@PathVariable Integer id) {
        return this.productoService.desactivar(id);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION', 'INVENTARIO')")
    @PatchMapping("/{id}/reactivar")
    public ProductoResponseDTO reactivar(@PathVariable Integer id) {
        return this.productoService.reactivar(id);
    }

}
