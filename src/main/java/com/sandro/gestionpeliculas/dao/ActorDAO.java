package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    // 1. LEER TODOS
    public List<Actor> obtenerTodos() {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT * FROM actor";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Actor a = new Actor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nacionalidad")
                );
                lista.add(a);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al leer actores: " + e.getMessage());
        }
        return lista;
    }

    // 2. INSERTAR
    public boolean insertar(Actor a) {
        String sql = "INSERT INTO actor (nombre, fecha_nacimiento, nacionalidad) VALUES (?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            st.setDate(2, Date.valueOf(a.getFechaNacimiento()));
            st.setString(3, a.getNacionalidad());

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar actor: " + e.getMessage());
            return false;
        }
    }

    // 3. ACTUALIZAR
    public boolean actualizar(Actor a) {
        String sql = "UPDATE actor SET nombre=?, fecha_nacimiento=?, nacionalidad=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            st.setDate(2, Date.valueOf(a.getFechaNacimiento()));
            st.setString(3, a.getNacionalidad());
            st.setInt(4, a.getId()); // ID al final para el WHERE

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar actor: " + e.getMessage());
            return false;
        }
    }

    // 4. ELIMINAR (Con limpieza)
    public boolean eliminar(int id) {
        // Primero borramos sus apariciones en películas (tabla 'actua')
        String sqlBorrarActuaciones = "DELETE FROM actua WHERE id_actor = ?";
        // Luego borramos al actor
        String sqlBorrarActor = "DELETE FROM actor WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false); // Transacción

            // A) Limpiar tabla intermedia
            PreparedStatement st1 = con.prepareStatement(sqlBorrarActuaciones);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

            // B) Borrar actor
            PreparedStatement st2 = con.prepareStatement(sqlBorrarActor);
            st2.setInt(1, id);
            int filas = st2.executeUpdate();
            st2.close();

            con.commit();
            con.close();
            return filas > 0;

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("❌ Error al eliminar actor: " + e.getMessage());
            return false;
        }
    }
}