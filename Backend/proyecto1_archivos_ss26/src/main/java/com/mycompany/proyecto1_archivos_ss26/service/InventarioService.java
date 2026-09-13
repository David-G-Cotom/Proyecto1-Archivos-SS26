/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.ConsumoLoteDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.MovimientoInventarioResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ResultadoConsumoDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.StockActualResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.DetalleCompra;
import com.mycompany.proyecto1_archivos_ss26.entity.MovimientoInventario;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.exception.ReglaNegocioException;
import com.mycompany.proyecto1_archivos_ss26.projection.StockActualProjection;
import com.mycompany.proyecto1_archivos_ss26.repository.DetalleCompraRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.MovimientoInventarioRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class InventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    public InventarioService(MovimientoInventarioRepository movimientoInventarioRepository,
            DetalleCompraRepository detalleCompraRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.detalleCompraRepository = detalleCompraRepository;
    }

    public List<StockActualResponseDTO> listarStockActual() {
        List<StockActualProjection> stockActuales = this.movimientoInventarioRepository.listarStockActual();
        List<StockActualResponseDTO> respuesta = new ArrayList<>();
        for (StockActualProjection stockActual : stockActuales) {
            respuesta.add(this.mapear(stockActual));
        }
        return respuesta;
    }

    public StockActualResponseDTO consultarStockDeProducto(Integer idProducto) {
        StockActualProjection proyeccion = this.movimientoInventarioRepository.obtenerStockActual(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + idProducto));
        return this.mapear(proyeccion);
    }

    public List<MovimientoInventarioResponseDTO> listarMovimientosDeProducto(Integer idProducto) {
        List<MovimientoInventario> movimientos = this.movimientoInventarioRepository.findByProducto_IdProductoOrderByFechaDesc(idProducto);
        List<MovimientoInventarioResponseDTO> respuesta = new ArrayList<>();
        for (MovimientoInventario movimiento : movimientos) {
            respuesta.add(this.mapearMovimiento(movimiento));
        }
        return respuesta;
    }

    public ResultadoConsumoDTO calcularConsumo(Integer idProducto, Integer cantidadSolicitada) {
        List<DetalleCompra> lotesDisponibles = this.detalleCompraRepository
                .findByProducto_IdProductoAndCantidadDisponibleGreaterThanOrderByCompra_FechaCompraAsc(idProducto, 0);
        int totalDisponible = 0;
        for (DetalleCompra detalle : lotesDisponibles) {
            totalDisponible += detalle.getCantidadDisponible();
        }

        if (totalDisponible < cantidadSolicitada) {
            throw new ReglaNegocioException(
                    "Stock insuficiente para el producto " + idProducto + ": solicitado " + cantidadSolicitada
                    + ", disponible " + totalDisponible);
        }

        List<ConsumoLoteDTO> consumos = new ArrayList<>();
        int restante = cantidadSolicitada;
        BigDecimal costoAcumulado = BigDecimal.ZERO;

        for (DetalleCompra lote : lotesDisponibles) {
            if (restante <= 0) {
                break;
            }
            int tomar = Math.min(restante, lote.getCantidadDisponible());
            costoAcumulado = costoAcumulado.add(lote.getPrecioUnitario().multiply(BigDecimal.valueOf(tomar)));
            consumos.add(new ConsumoLoteDTO(lote.getIdDetalleCompra(), tomar, lote.getPrecioUnitario()));
            restante -= tomar;
        }

        BigDecimal costoUnitarioPromedio = cantidadSolicitada == 0
                ? BigDecimal.ZERO
                : costoAcumulado.divide(BigDecimal.valueOf(cantidadSolicitada), 2, RoundingMode.HALF_UP);

        return new ResultadoConsumoDTO(idProducto, cantidadSolicitada, costoUnitarioPromedio, costoAcumulado, consumos);
    }

    private StockActualResponseDTO mapear(StockActualProjection p) {
        boolean bajoStock = p.getStockActual() <= p.getStockMinimo();
        return new StockActualResponseDTO(p.getIdProducto(), p.getNombre(), p.getStockActual(), p.getStockMinimo(), bajoStock);
    }

    private MovimientoInventarioResponseDTO mapearMovimiento(MovimientoInventario m) {
        String origen;
        if (m.getCompra() != null) {
            origen = "Compra #" + m.getCompra().getIdCompra();
        } else if (m.getVenta() != null) {
            origen = "Venta #" + m.getVenta().getIdVenta();
        } else {
            origen = "Ajuste manual";
        }
        return new MovimientoInventarioResponseDTO(
                m.getIdMovimiento(), m.getProducto().getIdProducto(), m.getTipoMovimiento().name(),
                m.getCantidad(), m.getFecha(), origen);
    }

}
