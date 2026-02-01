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

        String sql = "SELECT g.nombre, COUNT(p.id) as cantidad " +
                "FROM pelicula p " +
                "JOIN genero g ON p.id_genero = g.id " +
                "GROUP BY g.nombre";

        try (Connection con = ConexionBBDD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String nombreGenero = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                datos.put(nombreGenero, cantidad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }
}