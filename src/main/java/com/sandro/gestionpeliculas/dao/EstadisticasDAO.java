package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasDAO {

    public int contarPeliculas() {
        return contar("SELECT COUNT(*) FROM pelicula");
    }

    public int contarActores() {
        return contar("SELECT COUNT(*) FROM actor");
    }

    private int contar(String sql) {
        int total = 0;
        try (Connection con = ConexionBBDD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public Map<String, Integer> contarPeliculasPorGenero() {
        Map<String, Integer> datos = new HashMap<>();

        String sql = "SELECT id_genero, COUNT(*) as cantidad FROM pelicula GROUP BY id_genero";

        try (Connection con = ConexionBBDD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // CORRECCIÓN AQUÍ: Leemos 'id_genero'
                int idGenero = rs.getInt("id_genero");
                int cantidad = rs.getInt("cantidad");

                String nombreGenero = obtenerNombreGenero(idGenero);
                datos.put(nombreGenero, cantidad);
            }
        } catch (Exception e) {
            System.out.println("Error en gráfico: " + e.getMessage());
            e.printStackTrace();
        }
        return datos;
    }

    private String obtenerNombreGenero(int id) {
        switch (id) {
            case 1: return "Acción";
            case 2: return "Comedia";
            case 3: return "Drama";
            case 4: return "Terror";
            case 5: return "Crimen/Drama";
            case 6: return "Ciencia Ficción";
            default: return "Otro (" + id + ")";
        }
    }
}