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
public class ConsumoLoteDTO {

    private Integer idDetalleCompra;
    private Integer cantidadTomada;
    private BigDecimal costoUnitarioDelLote;

    public ConsumoLoteDTO() {
    }

    public ConsumoLoteDTO(Integer idDetalleCompra, Integer cantidadTomada, BigDecimal costoUnitarioDelLote) {
        this.idDetalleCompra = idDetalleCompra;
        this.cantidadTomada = cantidadTomada;
        this.costoUnitarioDelLote = costoUnitarioDelLote;
    }

    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Integer getCantidadTomada() {
        return cantidadTomada;
    }

    public void setCantidadTomada(Integer cantidadTomada) {
        this.cantidadTomada = cantidadTomada;
    }

    public BigDecimal getCostoUnitarioDelLote() {
        return costoUnitarioDelLote;
    }

    public void setCostoUnitarioDelLote(BigDecimal costoUnitarioDelLote) {
        this.costoUnitarioDelLote = costoUnitarioDelLote;
    }

}
