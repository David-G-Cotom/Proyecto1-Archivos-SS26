/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author david
 */
public class VentaResponseDTO {

    private Integer idVenta;
    private Integer idCliente;
    private String nombreCliente;
    private String username;
    private LocalDateTime fechaVenta;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private List<DetalleVentaResponseDTO> detalles;

    public VentaResponseDTO() {
    }

    public VentaResponseDTO(Integer idVenta, Integer idCliente, String nombreCliente, String username, LocalDateTime fechaVenta, BigDecimal subtotal, BigDecimal iva, BigDecimal total, List<DetalleVentaResponseDTO> detalles) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.username = username;
        this.fechaVenta = fechaVenta;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.detalles = detalles;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleVentaResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaResponseDTO> detalles) {
        this.detalles = detalles;
    }

}
