package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartoDAO {

    // 1. OBTENER ACTORES DE UNA PELÍCULA
    // Este método hace un JOIN para sacar los nombres de los actores de una peli concreta
    public List<Actor> obtenerActoresPorPelicula(int idPelicula) {
        List<Actor> lista = new ArrayList<>();
        // Unimos la tabla 'actor' con la tabla 'actua'
        String sql = "SELECT a.* FROM actor a JOIN actua ac ON a.id = ac.id_actor WHERE ac.id_pelicula = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idPelicula);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Actor actor = new Actor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nacionalidad")
                );
                lista.add(actor);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 2. AÑADIR ACTOR A PELÍCULA (Crear relación)
    public boolean anadirActorAPelicula(int idActor, int idPelicula) {
        // Importante: Asegúrate de que en tu BBDD la tabla se llame 'actua'
        // y las columnas 'id_actor' e 'id_pelicula'
        String sql = "INSERT INTO actua (id_actor, id_pelicula) VALUES (?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idActor);
            st.setInt(2, idPelicula);

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            // Seguramente falle si intentas añadir el mismo actor dos veces (Clave duplicada)
            System.out.println("⚠️ Error al añadir actor (¿Ya está en el reparto?): " + e.getMessage());
            return false;
        }
    }

    // 3. ELIMINAR ACTOR DE PELÍCULA
    public boolean eliminarActorDePelicula(int idActor, int idPelicula) {
        String sql = "DELETE FROM actua WHERE id_actor = ? AND id_pelicula = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idActor);
            st.setInt(2, idPelicula);

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}