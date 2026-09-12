/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.CompraRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.CompraResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.DetalleCompraRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.DetalleCompraResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Compra;
import com.mycompany.proyecto1_archivos_ss26.entity.DetalleCompra;
import com.mycompany.proyecto1_archivos_ss26.entity.MovimientoInventario;
import com.mycompany.proyecto1_archivos_ss26.entity.Producto;
import com.mycompany.proyecto1_archivos_ss26.entity.Proveedor;
import com.mycompany.proyecto1_archivos_ss26.entity.Usuario;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.modelos.TipoMovimiento;
import com.mycompany.proyecto1_archivos_ss26.repository.CompraRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.ProductoRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.ProveedorRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioService movimientoInventarioService;

    public CompraService(CompraRepository compraRepository, ProveedorRepository proveedorRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository, MovimientoInventarioService movimientoInventarioService) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @Transactional
    public CompraResponseDTO registrarCompra(CompraRequestDTO request) {
        Proveedor proveedor = this.proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un proveedor con id " + request.getIdProveedor()));

        Usuario usuario = this.usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un usuario con username '" + request.getUsername() + "'"));

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setTotal(BigDecimal.ZERO);

        BigDecimal totalAcumulado = BigDecimal.ZERO;

        for (DetalleCompraRequestDTO item : request.getItems()) {
            Producto producto = this.productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                    "No existe un producto con id " + item.getIdProducto()));

            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
            totalAcumulado = totalAcumulado.add(subtotal);

            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
            detalle.setCantidadDisponible(item.getCantidad());

            compra.agregarDetalle(detalle);
        }

        compra.setTotal(totalAcumulado);

        Compra compraGuardada = this.compraRepository.save(compra);

        for (DetalleCompra detalle : compraGuardada.getDetalles()) {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(detalle.getProducto());
            movimiento.setUsuario(usuario);
            movimiento.setTipoMovimiento(TipoMovimiento.ENTRADA);
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setFecha(compraGuardada.getFechaCompra());
            movimiento.setCompra(compraGuardada);

            this.movimientoInventarioService.registrarMovimiento(movimiento);
        }

        return this.mapearResponseDTO(compraGuardada);
    }

    public CompraResponseDTO obtenerPorId(Integer idCompra) {
        Compra compra = this.compraRepository.findById(idCompra)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una compra con id " + idCompra));
        return this.mapearResponseDTO(compra);
    }

    public List<CompraResponseDTO> listar() {
        List<Compra> compras = this.compraRepository.findAll();
        List<CompraResponseDTO> respuesta = new ArrayList<>();
        for (Compra compra : compras) {
            respuesta.add(this.mapearResponseDTO(compra));
        }
        return respuesta;
    }

    public List<CompraResponseDTO> listarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        List<Compra> compras = this.compraRepository.findByFechaCompraBetween(desde, hasta);
        List<CompraResponseDTO> respuesta = new ArrayList<>();
        for (Compra compra : compras) {
            respuesta.add(this.mapearResponseDTO(compra));
        }
        return respuesta;
    }

    private CompraResponseDTO mapearResponseDTO(Compra compra) {
        List<DetalleCompraResponseDTO> detalles = new ArrayList<>();
        for (DetalleCompra detalle : compra.getDetalles()) {
            detalles.add(new DetalleCompraResponseDTO(
                    detalle.getIdDetalleCompra(),
                    detalle.getProducto().getIdProducto(),
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.getSubtotal()
            ));
        }

        return new CompraResponseDTO(
                compra.getIdCompra(),
                compra.getProveedor().getIdProveedor(),
                compra.getProveedor().getNombre(),
                compra.getUsuario().getUsername(),
                compra.getFechaCompra(),
                compra.getTotal(),
                detalles);
    }

}
