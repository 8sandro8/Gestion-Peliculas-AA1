package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Actor {

    // --- ATRIBUTOS ---
    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String fotoUrl; // <--- NUEVO CAMPO

    // --- CONSTRUCTORES ---

    public Actor() {
    }

    public Actor(int id, String nombre, LocalDate fechaNacimiento, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
    }

    // --- GETTERS Y SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getFotoUrl() { return fotoUrl; } // <--- NUEVO
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; } // <--- NUEVO

    @Override
    public String toString() {
        return nombre;
    }
}