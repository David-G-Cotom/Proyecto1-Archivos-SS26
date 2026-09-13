/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.ConsumoLoteDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.DetalleVentaRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.DetalleVentaResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ResultadoConsumoDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.VentaRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.VentaResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Cliente;
import com.mycompany.proyecto1_archivos_ss26.entity.DetalleCompra;
import com.mycompany.proyecto1_archivos_ss26.entity.DetalleVenta;
import com.mycompany.proyecto1_archivos_ss26.entity.MovimientoInventario;
import com.mycompany.proyecto1_archivos_ss26.entity.Producto;
import com.mycompany.proyecto1_archivos_ss26.entity.Usuario;
import com.mycompany.proyecto1_archivos_ss26.entity.Venta;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.modelos.TipoMovimiento;
import com.mycompany.proyecto1_archivos_ss26.repository.ClienteRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.DetalleCompraRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.ProductoRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.UsuarioRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.VentaRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final MovimientoInventarioService movimientoInventarioService;
    private final InventarioService inventarioService;
    private final BigDecimal ivaTasa;

    public VentaService(VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            DetalleCompraRepository detalleCompraRepository,
            MovimientoInventarioService movimientoInventarioService,
            InventarioService inventarioService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.movimientoInventarioService = movimientoInventarioService;
        this.inventarioService = inventarioService;
        this.ivaTasa = BigDecimal.valueOf(0.12);
    }

    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {
        Cliente cliente = this.clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un cliente con id " + request.getIdCliente()));

        Usuario usuario = this.usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un usuario con username '" + request.getUsername() + "'"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setSubtotal(BigDecimal.ZERO);
        venta.setIva(BigDecimal.ZERO);
        venta.setTotal(BigDecimal.ZERO);

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO item : request.getItems()) {
            Producto producto = this.productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un producto con id " + item.getIdProducto()));

            ResultadoConsumoDTO resultado = this.inventarioService.calcularConsumo(
                    item.getIdProducto(), item.getCantidad());

            this.aplicarConsumoDeLotes(resultado.getLotesConsumidos());

            BigDecimal precioUnitarioVenta = producto.getPrecioVenta();
            BigDecimal subtotalLinea = precioUnitarioVenta.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotalAcumulado = subtotalAcumulado.add(subtotalLinea);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitarioVenta(precioUnitarioVenta);
            detalle.setSubtotal(subtotalLinea);
            detalle.setCostoUnitarioPromedio(resultado.getCostoUnitarioPromedio());
            detalle.setCostoTotal(resultado.getCostoTotal());

            venta.agregarDetalle(detalle);
        }

        BigDecimal ivaCalculado = subtotalAcumulado.multiply(this.ivaTasa).setScale(2, RoundingMode.HALF_UP);
        venta.setSubtotal(subtotalAcumulado);
        venta.setIva(ivaCalculado);
        venta.setTotal(subtotalAcumulado.add(ivaCalculado));

        Venta ventaGuardada = this.ventaRepository.save(venta);

        for (DetalleVenta detalle : ventaGuardada.getDetalles()) {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(detalle.getProducto());
            movimiento.setUsuario(usuario);
            movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setFecha(ventaGuardada.getFechaVenta());
            movimiento.setVenta(ventaGuardada);
            this.movimientoInventarioService.registrarMovimiento(movimiento);
        }

        return this.mapearAResponseDTO(ventaGuardada);
    }

    private void aplicarConsumoDeLotes(List<ConsumoLoteDTO> lotesConsumidos) {
        for (ConsumoLoteDTO consumo : lotesConsumidos) {
            DetalleCompra lote = this.detalleCompraRepository.findById(consumo.getIdDetalleCompra())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe el lote de compra " + consumo.getIdDetalleCompra()));
            lote.setCantidadDisponible(lote.getCantidadDisponible() - consumo.getCantidadTomada());
            this.detalleCompraRepository.save(lote);
        }
    }

    public VentaResponseDTO obtenerPorId(Integer idVenta) {
        Venta venta = this.ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una venta con id " + idVenta));
        return this.mapearAResponseDTO(venta);
    }

    public List<VentaResponseDTO> listar() {
        List<Venta> ventas = this.ventaRepository.findAll();
        List<VentaResponseDTO> listaDTOs = new ArrayList<>();

        for (Venta venta : ventas) {
            listaDTOs.add(this.mapearAResponseDTO(venta));
        }

        return listaDTOs;
    }

    public List<VentaResponseDTO> listarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        List<Venta> ventas = this.ventaRepository.findByFechaVentaBetween(desde, hasta);
        List<VentaResponseDTO> listaDTOs = new ArrayList<>();

        for (Venta venta : ventas) {
            listaDTOs.add(this.mapearAResponseDTO(venta));
        }

        return listaDTOs;
    }

    public List<VentaResponseDTO> listarPorCliente(Integer idCliente) {
        List<VentaResponseDTO> resultado = new ArrayList<>();
        List<Venta> ventasCliente = this.ventaRepository.findByCliente_IdClienteOrderByFechaVentaDesc(idCliente);
        for (Venta venta : ventasCliente) {
            resultado.add(this.mapearAResponseDTO(venta));
        }
        return resultado;
    }

    private VentaResponseDTO mapearAResponseDTO(Venta venta) {
        List<DetalleVentaResponseDTO> detallesDTO = new ArrayList<>();
        List<DetalleVenta> detalles = venta.getDetalles();
        for (DetalleVenta d : detalles) {
            DetalleVentaResponseDTO detalleDTO = new DetalleVentaResponseDTO(
                    d.getIdDetalleVenta(),
                    d.getProducto().getIdProducto(),
                    d.getProducto().getNombre(),
                    d.getCantidad(),
                    d.getPrecioUnitarioVenta(),
                    d.getSubtotal(),
                    d.getCostoUnitarioPromedio(),
                    d.getCostoTotal()
            );
            detallesDTO.add(detalleDTO);
        }

        return new VentaResponseDTO(
                venta.getIdVenta(),
                venta.getCliente().getIdCliente(),
                venta.getCliente().getNombre(),
                venta.getUsuario().getUsername(),
                venta.getFechaVenta(),
                venta.getSubtotal(),
                venta.getIva(),
                venta.getTotal(),
                detallesDTO);
    }

}
