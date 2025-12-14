package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    // --- 1. LEER TODOS ---
    public List<Actor> obtenerTodos() {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT * FROM actor";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                LocalDate fecha = null;
                if (rs.getDate("fecha_nacimiento") != null) {
                    fecha = rs.getDate("fecha_nacimiento").toLocalDate();
                }

                Actor a = new Actor();
                a.setId(rs.getInt("id"));
                a.setNombre(rs.getString("nombre"));
                a.setFechaNacimiento(fecha);
                a.setNacionalidad(rs.getString("nacionalidad"));

                // --- NUEVO: LEER FOTO ---
                a.setFotoUrl(rs.getString("foto_url"));

                lista.add(a);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al leer actores: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<Actor> listarTodos() {
        return obtenerTodos();
    }

    // --- 2. INSERTAR ---
    public boolean insertar(Actor a) {
        // Añadimos foto_url al SQL
        String sql = "INSERT INTO actor (nombre, fecha_nacimiento, nacionalidad, foto_url) VALUES (?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            st.setDate(2, (a.getFechaNacimiento() != null) ? Date.valueOf(a.getFechaNacimiento()) : null);
            st.setString(3, a.getNacionalidad());
            st.setString(4, a.getFotoUrl()); // <--- NUEVO

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar actor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- 3. ACTUALIZAR ---
    public boolean actualizar(Actor a) {
        // Añadimos foto_url al SQL
        String sql = "UPDATE actor SET nombre=?, fecha_nacimiento=?, nacionalidad=?, foto_url=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            st.setDate(2, (a.getFechaNacimiento() != null) ? Date.valueOf(a.getFechaNacimiento()) : null);
            st.setString(3, a.getNacionalidad());
            st.setString(4, a.getFotoUrl()); // <--- NUEVO
            st.setInt(5, a.getId());         // El ID pasa a ser el 5º parámetro

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar actor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- 4. ELIMINAR ---
    public boolean eliminar(int id) {
        String sqlBorrarActuaciones = "DELETE FROM actua WHERE id_actor = ?";
        String sqlBorrarActor = "DELETE FROM actor WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false);

            PreparedStatement st1 = con.prepareStatement(sqlBorrarActuaciones);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

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
            e.printStackTrace();
            return false;
        }
    }
}