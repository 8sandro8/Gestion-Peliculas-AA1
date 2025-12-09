package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Actor {
    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private int numPremios; // Extra para cumplir requisitos

    public Actor() {}

    public Actor(int id, String nombre, LocalDate fechaNacimiento, String nacionalidad, int numPremios) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.numPremios = numPremios;
    }

    // GETTERS Y SETTERS (Generar con Alt+Insert)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public int getNumPremios() { return numPremios; }
    public void setNumPremios(int numPremios) { this.numPremios = numPremios; }

    @Override
    public String toString() { return nombre; }
}