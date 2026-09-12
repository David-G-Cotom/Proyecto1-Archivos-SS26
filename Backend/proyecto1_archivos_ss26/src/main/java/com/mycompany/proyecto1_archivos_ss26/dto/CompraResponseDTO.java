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
public class CompraResponseDTO {

    private Integer idCompra;
    private Integer idProveedor;
    private String nombreProveedor;
    private String username;
    private LocalDateTime fechaCompra;
    private BigDecimal total;
    private List<DetalleCompraResponseDTO> detalles;

    public CompraResponseDTO() {
    }

    public CompraResponseDTO(Integer idCompra, Integer idProveedor, String nombreProveedor, String username, LocalDateTime fechaCompra, BigDecimal total, List<DetalleCompraResponseDTO> detalles) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.username = username;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.detalles = detalles;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleCompraResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraResponseDTO> detalles) {
        this.detalles = detalles;
    }

}
