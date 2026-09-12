/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.service;

import com.mycompany.proyecto1_archivos_ss26.dto.ProductoRequestDTO;
import com.mycompany.proyecto1_archivos_ss26.dto.ProductoResponseDTO;
import com.mycompany.proyecto1_archivos_ss26.entity.Categoria;
import com.mycompany.proyecto1_archivos_ss26.entity.Producto;
import com.mycompany.proyecto1_archivos_ss26.exception.RecursoNoEncontradoException;
import com.mycompany.proyecto1_archivos_ss26.repository.CategoriaRepository;
import com.mycompany.proyecto1_archivos_ss26.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author david
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoResponseDTO> listar(boolean soloActivos) {
        List<Producto> productos = soloActivos ? productoRepository.findByActivoTrue() : productoRepository.findAll();
        List<ProductoResponseDTO> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            resultado.add(this.mapear(producto));
        }
        return resultado;
    }

    public ProductoResponseDTO obtenerPorId(Integer idProducto) {
        return this.mapear(this.buscar(idProducto));
    }

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        Categoria categoria = this.buscarCategoria(request.getIdCategoria());

        Producto producto = new Producto();
        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setActivo(true);

        return this.mapear(this.productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO actualizar(Integer idProducto, ProductoRequestDTO request) {
        Producto producto = this.buscar(idProducto);
        Categoria categoria = this.buscarCategoria(request.getIdCategoria());

        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockMinimo(request.getStockMinimo());

        return this.mapear(this.productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO desactivar(Integer idProducto) {
        Producto producto = this.buscar(idProducto);
        producto.setActivo(false);
        return this.mapear(this.productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO reactivar(Integer idProducto) {
        Producto producto = this.buscar(idProducto);
        producto.setActivo(true);
        return this.mapear(this.productoRepository.save(producto));
    }

    private Producto buscar(Integer idProducto) {
        return this.productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + idProducto));
    }

    private Categoria buscarCategoria(Integer idCategoria) {
        return this.categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una categoria con id " + idCategoria));
    }

    private ProductoResponseDTO mapear(Producto p) {
        return new ProductoResponseDTO(
                p.getIdProducto(),
                p.getCategoria().getIdCategoria(),
                p.getCategoria().getNombre(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecioVenta(),
                p.getStockMinimo(),
                p.getActivo());
    }

}
