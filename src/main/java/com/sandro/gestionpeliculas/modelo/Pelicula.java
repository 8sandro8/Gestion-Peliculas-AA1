package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Pelicula {

    // Atributos que coinciden con las columnas de tu tabla 'pelicula'
    private int id;
    private String titulo;
    private LocalDate fechaLanzamiento; // En SQL es DATE
    private int duracion;
    private double presupuesto;         // En SQL es DECIMAL/FLOAT
    private boolean esMas18;            // En SQL es BOOLEAN
    private String cartelUrl;

    // IDs de las claves foráneas (para saber de quién es)
    private int idDirector;
    private int idGenero;

    // CONSTRUCTOR VACÍO (Necesario a veces para librerías)
    public Pelicula() {
    }

    // CONSTRUCTOR COMPLETO (Para crear pelis nuevas)
    public Pelicula(int id, String titulo, LocalDate fechaLanzamiento, int duracion, double presupuesto, boolean esMas18, String cartelUrl, int idDirector, int idGenero) {
        this.id = id;
        this.titulo = titulo;
        this.fechaLanzamiento = fechaLanzamiento;
        this.duracion = duracion;
        this.presupuesto = presupuesto;
        this.esMas18 = esMas18;
        this.cartelUrl = cartelUrl;
        this.idDirector = idDirector;
        this.idGenero = idGenero;
    }

    // GETTERS Y SETTERS (Para poder leer y escribir los datos)
    // Truco: En IntelliJ puedes generarlos con Alt+Insert -> Getter and Setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public boolean isEsMas18() { return esMas18; }
    public void setEsMas18(boolean esMas18) { this.esMas18 = esMas18; }

    public String getCartelUrl() { return cartelUrl; }
    public void setCartelUrl(String cartelUrl) { this.cartelUrl = cartelUrl; }

    public int getIdDirector() { return idDirector; }
    public void setIdDirector(int idDirector) { this.idDirector = idDirector; }

    public int getIdGenero() { return idGenero; }
    public void setIdGenero(int idGenero) { this.idGenero = idGenero; }

    // TOSTRING (Para imprimir la peli por consola y ver que tiene datos)
    @Override
    public String toString() {
        return titulo + " (" + fechaLanzamiento.getYear() + ")";
    }
}