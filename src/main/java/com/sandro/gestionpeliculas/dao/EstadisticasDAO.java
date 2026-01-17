package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class EstadisticasDAO {

    // 1. Contar Películas Totales
    public int contarPeliculas() {
        return contar("SELECT COUNT(*) FROM pelicula");
    }

    // 2. Contar Actores Totales
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

        String sql = "SELECT g.nombre, COUNT(*) as cantidad " +
                "FROM pelicula p " +
                "JOIN genero g ON p.id_genero = g.id " +
                "GROUP BY g.nombre";

        try (Connection con = ConexionBBDD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String genero = rs.getString(1); // Coge el nombre del género
                int cantidad = rs.getInt("cantidad");
                datos.put(genero, cantidad);
            }
        } catch (Exception e) {
            System.out.println("Error cargando gráfico: " + e.getMessage());
            e.printStackTrace();
        }
        return datos;
    }
}