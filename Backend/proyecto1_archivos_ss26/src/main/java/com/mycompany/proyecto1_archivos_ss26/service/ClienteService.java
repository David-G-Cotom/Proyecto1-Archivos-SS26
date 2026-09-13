/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.ClienteRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ClienteResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Cliente;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDTO> listar(boolean soloActivos) {
        List<Cliente> clientes = soloActivos ? this.clienteRepository.findByActivoTrue() : this.clienteRepository.findAll();
        List<ClienteResponseDTO> resultado = new ArrayList<>();
        for (Cliente cliente : clientes) {
            resultado.add(this.mapear(cliente));
        }
        return resultado;
    }

    public ClienteResponseDTO obtenerPorId(Integer idCliente) {
        return this.mapear(this.buscar(idCliente));
    }

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setNit(request.getNit());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());
        cliente.setActivo(true);
        return this.mapear(this.clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO actualizar(Integer idCliente, ClienteRequestDTO request) {
        Cliente cliente = this.buscar(idCliente);
        cliente.setNombre(request.getNombre());
        cliente.setNit(request.getNit());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());
        return this.mapear(this.clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO desactivar(Integer idCliente) {
        Cliente cliente = this.buscar(idCliente);
        cliente.setActivo(false);
        return this.mapear(this.clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO reactivar(Integer idCliente) {
        Cliente cliente = this.buscar(idCliente);
        cliente.setActivo(true);
        return this.mapear(this.clienteRepository.save(cliente));
    }

    private Cliente buscar(Integer idCliente) {
        return this.clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + idCliente));
    }

    private ClienteResponseDTO mapear(Cliente c) {
        return new ClienteResponseDTO(c.getIdCliente(), c.getNombre(), c.getNit(), c.getTelefono(), c.getEmail(), c.getDireccion(), c.getActivo());
    }

}
