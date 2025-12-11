package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Actor {

    // Atributos
    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String nacionalidad;

    // 1. CONSTRUCTOR VACÍO (Necesario a veces)
    public Actor() {
    }

    // 2. CONSTRUCTOR COMPLETO (¡ESTE ES EL QUE TE FALTA!)
    public Actor(int id, String nombre, LocalDate fechaNacimiento, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
    }

    // 3. GETTERS Y SETTERS (Para poder leer y escribir los datos)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    // Para que al imprimir salga bonito (opcional)
    @Override
    public String toString() {
        return nombre;
    }
}