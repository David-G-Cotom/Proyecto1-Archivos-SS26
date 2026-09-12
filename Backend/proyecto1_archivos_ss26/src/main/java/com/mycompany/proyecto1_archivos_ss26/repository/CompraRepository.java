/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.repository;

import com.mycompany.proyecto1_archivos_ss26.entity.Compra;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author david
 */
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByFechaCompraBetween(LocalDateTime desde, LocalDateTime hasta);

}
