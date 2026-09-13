/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_archivos_ss26.dto;

/**
 *
 * @author david
 */
public class ProveedorResponseDTO {

    private Integer idProveedor;
    private String nombre;
    private String nit;
    private String telefono;
    private String email;
    private String direccion;
    private Boolean activo;

    public ProveedorResponseDTO() {
    }

    public ProveedorResponseDTO(Integer idProveedor, String nombre, String nit, String telefono, String email, String direccion, Boolean activo) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = activo;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}
