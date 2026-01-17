package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Director {

    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String webOficial;
    private String nacionalidad;

    // --- CONSTRUCTOR 1: VACÍO ---
    public Director() {
    }

    // --- CONSTRUCTOR 2: COMPLETO (ID + Nombre) ---
    public Director(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Director(int id, String nombre, LocalDate fechaNacimiento, String webOficial, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.webOficial = webOficial;
        this.nacionalidad = nacionalidad;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getWebOficial() { return webOficial; }
    public void setWebOficial(String webOficial) { this.webOficial = webOficial; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    @Override
    public String toString() {
        return nombre;
    }
}