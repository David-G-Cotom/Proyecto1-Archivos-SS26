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
public class CompraRequestDTO {

    @NotNull(message = "idProveedor es obligatorio")
    private Integer idProveedor;

    @NotBlank(message = "username es obligatorio")
    private String username;

    @NotEmpty(message = "la compra debe tener al menos un producto")
    @Valid
    private List<DetalleCompraRequestDTO> items;

    public CompraRequestDTO() {
    }

    public CompraRequestDTO(Integer idProveedor, String username, List<DetalleCompraRequestDTO> items) {
        this.idProveedor = idProveedor;
        this.username = username;
        this.items = items;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<DetalleCompraRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<DetalleCompraRequestDTO> items) {
        this.items = items;
    }

}
