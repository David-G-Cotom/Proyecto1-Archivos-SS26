/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.ProveedorRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ProveedorResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Proveedor;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProveedorResponseDTO> listar(boolean soloActivos) {
        List<Proveedor> proveedores = soloActivos ? this.proveedorRepository.findByActivoTrue() : this.proveedorRepository.findAll();
        List<ProveedorResponseDTO> resultado = new ArrayList<>();
        for (Proveedor proveedor : proveedores) {
            resultado.add(this.mapear(proveedor));
        }
        return resultado;
    }

    public ProveedorResponseDTO obtenerPorId(Integer idProveedor) {
        return this.mapear(this.buscar(idProveedor));
    }

    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setNit(request.getNit());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setActivo(true);
        return this.mapear(this.proveedorRepository.save(proveedor));
    }

    @Transactional
    public ProveedorResponseDTO actualizar(Integer idProveedor, ProveedorRequestDTO request) {
        Proveedor proveedor = this.buscar(idProveedor);
        proveedor.setNombre(request.getNombre());
        proveedor.setNit(request.getNit());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setDireccion(request.getDireccion());
        return this.mapear(this.proveedorRepository.save(proveedor));
    }

    @Transactional
    public ProveedorResponseDTO desactivar(Integer idProveedor) {
        Proveedor proveedor = this.buscar(idProveedor);
        proveedor.setActivo(false);
        return this.mapear(this.proveedorRepository.save(proveedor));
    }

    @Transactional
    public ProveedorResponseDTO reactivar(Integer idProveedor) {
        Proveedor proveedor = this.buscar(idProveedor);
        proveedor.setActivo(true);
        return this.mapear(this.proveedorRepository.save(proveedor));
    }

    private Proveedor buscar(Integer idProveedor) {
        return this.proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un proveedor con id " + idProveedor));
    }

    private ProveedorResponseDTO mapear(Proveedor p) {
        return new ProveedorResponseDTO(p.getIdProveedor(), p.getNombre(), p.getNit(), p.getTelefono(), p.getEmail(), p.getDireccion(), p.getActivo());
    }

}
