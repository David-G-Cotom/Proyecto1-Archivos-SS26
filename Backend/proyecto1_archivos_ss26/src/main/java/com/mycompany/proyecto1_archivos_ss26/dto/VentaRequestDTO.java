/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author david
 */
public class VentaRequestDTO {

    @NotNull(message = "idCliente es obligatorio")
    private Integer idCliente;

    @NotBlank(message = "username es obligatorio")
    private String username;

    @NotEmpty(message = "la venta debe tener al menos un producto")
    @Valid
    private List<DetalleVentaRequestDTO> items;

    public VentaRequestDTO() {
    }

    public VentaRequestDTO(Integer idCliente, String username, List<DetalleVentaRequestDTO> items) {
        this.idCliente = idCliente;
        this.username = username;
        this.items = items;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<DetalleVentaRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<DetalleVentaRequestDTO> items) {
        this.items = items;
    }

}
