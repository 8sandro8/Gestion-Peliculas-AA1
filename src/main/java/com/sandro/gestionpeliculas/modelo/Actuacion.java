package com.sandro.gestionpeliculas.modelo;

public class Actuacion {

    // No guardamos solo el ID, guardamos el objeto Actor entero para poder ver su nombre y foto
    private Actor actor;
    private int idPelicula;
    private String personaje;
    private String tipoPapel; // Ej: Protagonista, Secundario, Cameo...

    // --- CONSTRUCTORES ---
    public Actuacion() {
    }

    public Actuacion(Actor actor, int idPelicula, String personaje, String tipoPapel) {
        this.actor = actor;
        this.idPelicula = idPelicula;
        this.personaje = personaje;
        this.tipoPapel = tipoPapel;
    }

    // --- GETTERS Y SETTERS ---
    public Actor getActor() { return actor; }
    public void setActor(Actor actor) { this.actor = actor; }

    public int getIdPelicula() { return idPelicula; }
    public void setIdPelicula(int idPelicula) { this.idPelicula = idPelicula; }

    public String getPersonaje() { return personaje; }
    public void setPersonaje(String personaje) { this.personaje = personaje; }

    public String getTipoPapel() { return tipoPapel; }
    public void setTipoPapel(String tipoPapel) { this.tipoPapel = tipoPapel; }

    // Helpers para la tabla visual (FXML)
    public String getNombreActor() {
        return (actor != null) ? actor.getNombre() : "Desconocido";
    }
}