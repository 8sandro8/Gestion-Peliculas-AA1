package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Actor;

import java.sql.*;
import java.time.LocalDate; // <--- ¡ESTA ES LA LÍNEA QUE FALTABA!
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    // --- 1. MÉTODOS DE LECTURA (DOBLE NOMBRE PARA EVITAR ERRORES) ---

    // Opción A: obtenerTodos
    public List<Actor> obtenerTodos() {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT * FROM actor";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                // Control de nulos para la fecha
                LocalDate fecha = null;
                if (rs.getDate("fecha_nacimiento") != null) {
                    fecha = rs.getDate("fecha_nacimiento").toLocalDate();
                }

                Actor a = new Actor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        fecha,
                        rs.getString("nacionalidad")
                );
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

    // Opción B: listarTodos (El puente)
    public List<Actor> listarTodos() {
        return obtenerTodos();
    }

    // --- 2. INSERTAR ---
    public boolean insertar(Actor a) {
        String sql = "INSERT INTO actor (nombre, fecha_nacimiento, nacionalidad) VALUES (?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            // Convertimos LocalDate a Date de SQL
            st.setDate(2, (a.getFechaNacimiento() != null) ? Date.valueOf(a.getFechaNacimiento()) : null);
            st.setString(3, a.getNacionalidad());

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
        String sql = "UPDATE actor SET nombre=?, fecha_nacimiento=?, nacionalidad=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getNombre());
            st.setDate(2, (a.getFechaNacimiento() != null) ? Date.valueOf(a.getFechaNacimiento()) : null);
            st.setString(3, a.getNacionalidad());
            st.setInt(4, a.getId());

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
            e.printStackTrace();
            return false;
        }
    }
}