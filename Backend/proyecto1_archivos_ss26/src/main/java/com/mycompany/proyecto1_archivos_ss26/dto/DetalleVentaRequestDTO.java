/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 *
 * @author david
 */
public class DetalleVentaRequestDTO {

    @NotNull(message = "idProducto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "cantidad es obligatoria")
    @Positive(message = "cantidad debe ser mayor a 0")
    private Integer cantidad;

    public DetalleVentaRequestDTO() {
    }

    public DetalleVentaRequestDTO(Integer idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}
