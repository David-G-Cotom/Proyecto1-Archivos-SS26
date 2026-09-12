/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 *
 * @author david
 */
public class DetalleCompraRequestDTO {

    @NotNull(message = "idProducto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "cantidad es obligatoria")
    @Positive(message = "cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "precioUnitario es obligatorio")
    @PositiveOrZero(message = "precioUnitario no puede ser negativo")
    private BigDecimal precioUnitario;

    public DetalleCompraRequestDTO() {
    }

    public DetalleCompraRequestDTO(Integer idProducto, Integer cantidad, BigDecimal precioUnitario) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

}
