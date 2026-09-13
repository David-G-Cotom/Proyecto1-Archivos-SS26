/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.ProveedorRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ProveedorResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.service.ProveedorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<ProveedorResponseDTO> listar(@RequestParam(defaultValue = "false") boolean soloActivos) {
        return this.proveedorService.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public ProveedorResponseDTO obtenerPorId(@PathVariable Integer id) {
        return this.proveedorService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.proveedorService.crear(request));
    }

    @PutMapping("/{id}")
    public ProveedorResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ProveedorRequestDTO request) {
        return this.proveedorService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public ProveedorResponseDTO desactivar(@PathVariable Integer id) {
        return this.proveedorService.desactivar(id);
    }

    @PatchMapping("/{id}/reactivar")
    public ProveedorResponseDTO reactivar(@PathVariable Integer id) {
        return this.proveedorService.reactivar(id);
    }

}
