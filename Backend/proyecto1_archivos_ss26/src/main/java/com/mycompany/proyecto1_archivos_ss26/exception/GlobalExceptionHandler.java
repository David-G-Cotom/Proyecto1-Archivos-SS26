/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.exception;

import com.mycompany.proyecto1_archivos_ss26.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author david
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return this.construir(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponseDTO> manejarReglaDeNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        // 422 -> Unprocessable Entity / Contenido no procesable
        return this.construir(HttpStatus.valueOf(422), ex.getMessage(), req, null);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> manejarCredencialesInvalidas(BadCredentialsException ex, HttpServletRequest req) {
        return construir(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return this.construir(HttpStatus.BAD_REQUEST, "Datos invalidos en la solicitud", req, errores);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarGenerico(Exception ex, HttpServletRequest req) {
        return this.construir(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado en el servidor", req, null);
    }

    private ResponseEntity<ErrorResponseDTO> construir(HttpStatus status, String mensaje, HttpServletRequest req, Map<String, String> erroresDeCampo) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, req.getRequestURI(), erroresDeCampo
        );
        return ResponseEntity.status(status).body(body);
    }

}
