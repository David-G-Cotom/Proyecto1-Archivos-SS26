/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 *
 * @author david
 */
public class ProductoRequestDTO {

    @NotNull(message = "idCategoria es obligatorio")
    private Integer idCategoria;

    @NotBlank(message = "nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "precioVenta es obligatorio")
    @PositiveOrZero(message = "precioVenta no puede ser negativo")
    private BigDecimal precioVenta;

    @NotNull(message = "stockMinimo es obligatorio")
    @PositiveOrZero(message = "stockMinimo no puede ser negativo")
    private Integer stockMinimo;

    public ProductoRequestDTO() {
    }

    public ProductoRequestDTO(Integer idCategoria, String nombre, String descripcion, BigDecimal precioVenta, Integer stockMinimo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.stockMinimo = stockMinimo;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

}
