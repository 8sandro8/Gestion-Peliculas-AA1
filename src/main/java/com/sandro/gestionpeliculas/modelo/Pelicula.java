package com.sandro.gestionpeliculas.modelo;

import java.time.LocalDate;

public class Pelicula {

    // ATRIBUTOS
    private int id;
    private String titulo;
    private int duracion;
    private String genero;
    private Director director;
    private LocalDate fechaLanzamiento;
    private double rating;
    private boolean tieneOscar;

    // NUEVOS CAMPOS DEL DAO
    private double presupuesto;
    private boolean esMas18;
    private String cartelUrl;

    // IDs auxiliares
    private int idDirector;
    private int idGenero;

    // CONSTRUCTOR VACÍO
    public Pelicula() {
    }

    // GETTERS Y SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Director getDirector() { return director; }
    public void setDirector(Director director) {
        this.director = director;
        if (director != null) this.idDirector = director.getId();
    }

    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isTieneOscar() { return tieneOscar; }
    public void setTieneOscar(boolean tieneOscar) { this.tieneOscar = tieneOscar; }

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

    // MÉTODO CALCULADO: AÑO
    public int getAnio() {
        if (fechaLanzamiento != null) return fechaLanzamiento.getYear();
        return 0;
    }

    public void setAnio(int anio) {
        this.fechaLanzamiento = LocalDate.of(anio, 1, 1);
    }

    @Override
    public String toString() {
        return titulo + " (" + getAnio() + ")";
    }
}