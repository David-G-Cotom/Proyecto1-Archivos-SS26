/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author david
 */
public class ResultadoConsumoDTO {

    private Integer idProducto;
    private Integer cantidadSolicitada;
    private BigDecimal costoUnitarioPromedio;
    private BigDecimal costoTotal;
    private List<ConsumoLoteDTO> lotesConsumidos;

    public ResultadoConsumoDTO() {
    }

    public ResultadoConsumoDTO(Integer idProducto, Integer cantidadSolicitada, BigDecimal costoUnitarioPromedio, BigDecimal costoTotal, List<ConsumoLoteDTO> lotesConsumidos) {
        this.idProducto = idProducto;
        this.cantidadSolicitada = cantidadSolicitada;
        this.costoUnitarioPromedio = costoUnitarioPromedio;
        this.costoTotal = costoTotal;
        this.lotesConsumidos = lotesConsumidos;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
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

    public List<ConsumoLoteDTO> getLotesConsumidos() {
        return lotesConsumidos;
    }

    public void setLotesConsumidos(List<ConsumoLoteDTO> lotesConsumidos) {
        this.lotesConsumidos = lotesConsumidos;
    }

}
