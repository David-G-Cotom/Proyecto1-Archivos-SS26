/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import java.math.BigDecimal;

/**
 *
 * @author david
 */
public class DetalleVentaResponseDTO {

    private Integer idDetalleVenta;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitarioVenta;
    private BigDecimal subtotal;
    private BigDecimal costoUnitarioPromedio;
    private BigDecimal costoTotal;

    public DetalleVentaResponseDTO() {
    }

    public DetalleVentaResponseDTO(Integer idDetalleVenta, Integer idProducto, String nombreProducto, Integer cantidad, BigDecimal precioUnitarioVenta, BigDecimal subtotal, BigDecimal costoUnitarioPromedio, BigDecimal costoTotal) {
        this.idDetalleVenta = idDetalleVenta;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitarioVenta = precioUnitarioVenta;
        this.subtotal = subtotal;
        this.costoUnitarioPromedio = costoUnitarioPromedio;
        this.costoTotal = costoTotal;
    }

    public Integer getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Integer idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getCostoUnitarioPromedio() {
        return costoUnitarioPromedio;
    }

    public void setCostoUnitarioPromedio(BigDecimal costoUnitarioPromedio) {
        this.costoUnitarioPromedio = costoUnitarioPromedio;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

}
