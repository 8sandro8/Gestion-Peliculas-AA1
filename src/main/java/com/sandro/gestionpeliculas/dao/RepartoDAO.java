package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;
import com.sandro.gestionpeliculas.modelo.Actuacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartoDAO {

    // 1. OBTENER REPARTO COMPLETO (Con Personaje y Tipo)
    public List<Actuacion> obtenerReparto(int idPelicula) {
        List<Actuacion> lista = new ArrayList<>();
        // JOIN para sacar datos del actor + datos de la actuación
        String sql = "SELECT a.id, a.nombre, a.nacionalidad, a.foto_url, ac.personaje, ac.tipo_papel " +
                "FROM actor a " +
                "JOIN actua ac ON a.id = ac.id_actor " +
                "WHERE ac.id_pelicula = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idPelicula);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                // Reconstruimos el Actor (lo básico)
                Actor actor = new Actor();
                actor.setId(rs.getInt("id"));
                actor.setNombre(rs.getString("nombre"));
                actor.setNacionalidad(rs.getString("nacionalidad"));
                actor.setFotoUrl(rs.getString("foto_url"));

                // Creamos la Actuación con el Personaje y el Rol
                Actuacion act = new Actuacion(
                        actor,
                        idPelicula,
                        rs.getString("personaje"),
                        rs.getString("tipo_papel")
                );
                lista.add(act);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 2. AÑADIR ACTOR A PELÍCULA (Con Personaje y Tipo)
    public boolean agregarActor(int idPelicula, int idActor, String personaje, String tipoPapel) {
        String sql = "INSERT INTO actua (id_pelicula, id_actor, personaje, tipo_papel) VALUES (?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idPelicula);
            st.setInt(2, idActor);
            st.setString(3, personaje);
            st.setString(4, tipoPapel);

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al agregar reparto: " + e.getMessage());
            return false;
        }
    }

    // 3. ELIMINAR DEL REPARTO
    public boolean eliminarActor(int idPelicula, int idActor) {
        String sql = "DELETE FROM actua WHERE id_pelicula = ? AND id_actor = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idPelicula);
            st.setInt(2, idActor);

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