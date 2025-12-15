package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;
import com.sandro.gestionpeliculas.modelo.Actuacion; // Importante para la lista

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartoDAO {

    /**
     * Obtiene la lista de actuaciones (Actores + Personaje) de una película.
     */
    public List<Actuacion> obtenerReparto(int idPelicula) {
        List<Actuacion> lista = new ArrayList<>();

        // Hacemos JOIN entre la tabla intermedia 'actua' y la tabla 'actor'
        String sql = "SELECT a.id, a.nombre, a.nacionalidad, ac.personaje " +
                "FROM actor a " +
                "JOIN actua ac ON a.id = ac.id_actor " +
                "WHERE ac.id_pelicula = ?";

        try (Connection con = ConexionBBDD.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // 1. Reconstruimos el objeto Actor
                Actor actor = new Actor();
                actor.setId(rs.getInt("id"));
                actor.setNombre(rs.getString("nombre"));
                actor.setNacionalidad(rs.getString("nacionalidad"));

                // 2. Obtenemos el personaje
                String personaje = rs.getString("personaje");

                // 3. Creamos el objeto Actuacion (Actor + Personaje)
                Actuacion actuacion = new Actuacion(actor, personaje);
                lista.add(actuacion);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reparto: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * ESTE ES EL MÉTODO QUE TE FALTABA.
     * Inserta una nueva relación en la tabla 'actua'.
     */
    public boolean agregarReparto(int idPelicula, int idActor, String personaje) {
        String sql = "INSERT INTO actua (id_pelicula, id_actor, personaje) VALUES (?, ?, ?)";

        try (Connection con = ConexionBBDD.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);
            pst.setInt(2, idActor);
            pst.setString(3, personaje);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al añadir reparto (posible duplicado): " + e.getMessage());
            return false;
        }
    }

    /**
     * Método opcional para eliminar (por si lo implementas en el futuro)
     */
    public boolean eliminarReparto(int idPelicula, int idActor) {
        String sql = "DELETE FROM actua WHERE id_pelicula = ? AND id_actor = ?";

        try (Connection con = ConexionBBDD.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);
            pst.setInt(2, idActor);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}