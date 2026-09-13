/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.ClienteRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ClienteResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.service.ClienteService;
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
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar(@RequestParam(defaultValue = "false") boolean soloActivos) {
        return this.clienteService.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Integer id) {
        return this.clienteService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clienteService.crear(request));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteRequestDTO request) {
        return this.clienteService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public ClienteResponseDTO desactivar(@PathVariable Integer id) {
        return this.clienteService.desactivar(id);
    }

    @PatchMapping("/{id}/reactivar")
    public ClienteResponseDTO reactivar(@PathVariable Integer id) {
        return this.clienteService.reactivar(id);
    }

}
