package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Director {
    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String webOficial; // Extra para cumplir con los 5 atributos

    public Director() {}

    public Director(int id, String nombre, LocalDate fechaNacimiento, String nacionalidad, String webOficial) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.webOficial = webOficial;
    }

    // GETTERS Y SETTERS (Generar con Alt+Insert -> Getter and Setter para TODOS)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getWebOficial() { return webOficial; }
    public void setWebOficial(String webOficial) { this.webOficial = webOficial; }

    @Override
    public String toString() { return nombre; }
}