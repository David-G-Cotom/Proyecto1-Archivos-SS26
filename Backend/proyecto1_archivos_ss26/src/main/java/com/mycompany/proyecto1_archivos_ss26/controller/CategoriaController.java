/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.controller;

import com.mycompany.proyecto1_archivos_ss26.dto.CategoriaResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Categoria;
import com.mycompany.proyecto1_archivos_ss26.repository.CategoriaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        List<CategoriaResponseDTO> resultado = new ArrayList<>();
        List<Categoria> categorias = this.categoriaRepository.findAll();
        for (Categoria categoria : categorias) {
            resultado.add(this.mapear(categoria));
        }
        return resultado;
    }

    private CategoriaResponseDTO mapear(Categoria c) {
        return new CategoriaResponseDTO(c.getIdCategoria(), c.getNombre(), c.getDescripcion());
    }

}
