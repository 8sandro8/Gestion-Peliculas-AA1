package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Director {

    // --- ATRIBUTOS (Los 5 que tu código está buscando) ---
    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String webOficial;
    private String nacionalidad;

    // --- CONSTRUCTOR 1: VACÍO ---
    public Director() {
    }

    // --- CONSTRUCTOR 2: COMPLETO (ID + Nombre) ---
    // (Por si acaso alguna parte usa solo estos dos)
    public Director(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // --- CONSTRUCTOR 3: EL QUE ESTÁ FALLANDO AHORA (5 Argumentos) ---
    public Director(int id, String nombre, LocalDate fechaNacimiento, String webOficial, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.webOficial = webOficial;
        this.nacionalidad = nacionalidad;
    }

    // --- GETTERS Y SETTERS (¡Ahora sí tienen código dentro!) ---

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

    // --- TO STRING (Para el ComboBox) ---
    @Override
    public String toString() {
        return nombre;
    }
}