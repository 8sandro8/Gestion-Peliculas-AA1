package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Director;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAO {

    // --- MÉTODOS DE LECTURA (EL TRUCO PARA QUE FUNCIONEN AMBOS CONTROLADORES) ---

    // OPCIÓN 1: listarTodos (La que usa PeliculasController)
    public List<Director> listarTodos() {
        List<Director> lista = new ArrayList<>();
        String sql = "SELECT * FROM director";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Director d = new Director();
                d.setId(rs.getInt("id"));
                d.setNombre(rs.getString("nombre"));
                lista.add(d);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // OPCIÓN 2: obtenerTodos (La que usa DirectoresController)
    // ESTE ES EL QUE FALTABA. Simplemente llama al de arriba.
    public List<Director> obtenerTodos() {
        return listarTodos();
    }

    // -------------------------------------------------------------------

    // --- INSERTAR ---
    public boolean insertar(Director d) {
        String sql = "INSERT INTO director (nombre) VALUES (?)";
        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());
            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- ACTUALIZAR ---
    public boolean actualizar(Director d) {
        String sql = "UPDATE director SET nombre=? WHERE id=?";
        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());
            st.setInt(2, d.getId());
            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- ELIMINAR ---
    public boolean eliminar(int id) {
        String sqlDesvincular = "UPDATE pelicula SET id_director = NULL WHERE id_director = ?";
        String sqlBorrar = "DELETE FROM director WHERE id = ?";
        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false);
            PreparedStatement st1 = con.prepareStatement(sqlDesvincular);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

            PreparedStatement st2 = con.prepareStatement(sqlBorrar);
            st2.setInt(1, id);
            int filas = st2.executeUpdate();
            st2.close();

            con.commit();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        }
    }
}