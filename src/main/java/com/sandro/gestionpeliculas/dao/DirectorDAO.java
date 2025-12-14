package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Director;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAO {

    // --- MÉTODOS DE LECTURA ---

    // 1. LISTAR TODOS (Con todos los campos: nombre, nacionalidad, web, fecha)
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
                d.setNacionalidad(rs.getString("nacionalidad"));
                d.setWebOficial(rs.getString("web_oficial"));

                // Controlar la fecha por si es NULL en la BBDD
                Date fechaSql = rs.getDate("fecha_nacimiento");
                if (fechaSql != null) {
                    d.setFechaNacimiento(fechaSql.toLocalDate());
                }

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

    // 2. OBTRNER TODOS (Alias para compatibilidad)
    public List<Director> obtenerTodos() {
        return listarTodos();
    }

    // 3. OBTENER POR ID (¡IMPORTANTE! Este es el que usa PeliculaDAO)
    public Director obtenerPorId(int id) {
        Director d = null;
        String sql = "SELECT * FROM director WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return null;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                d = new Director();
                d.setId(rs.getInt("id"));
                d.setNombre(rs.getString("nombre"));
                d.setNacionalidad(rs.getString("nacionalidad"));
                d.setWebOficial(rs.getString("web_oficial"));

                Date fechaSql = rs.getDate("fecha_nacimiento");
                if (fechaSql != null) {
                    d.setFechaNacimiento(fechaSql.toLocalDate());
                }
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    // --- INSERTAR (Completo con todos los campos) ---
    public boolean insertar(Director d) {
        String sql = "INSERT INTO director (nombre, fecha_nacimiento, nacionalidad, web_oficial) VALUES (?, ?, ?, ?)";
        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());

            if (d.getFechaNacimiento() != null) {
                st.setDate(2, Date.valueOf(d.getFechaNacimiento()));
            } else {
                st.setDate(2, null);
            }

            st.setString(3, d.getNacionalidad());
            st.setString(4, d.getWebOficial());

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- ACTUALIZAR (Completo con todos los campos) ---
    public boolean actualizar(Director d) {
        String sql = "UPDATE director SET nombre=?, fecha_nacimiento=?, nacionalidad=?, web_oficial=? WHERE id=?";
        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());

            if (d.getFechaNacimiento() != null) {
                st.setDate(2, Date.valueOf(d.getFechaNacimiento()));
            } else {
                st.setDate(2, null);
            }

            st.setString(3, d.getNacionalidad());
            st.setString(4, d.getWebOficial());
            st.setInt(5, d.getId());

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
        // Primero desvinculamos de películas (poner a NULL), luego borramos director
        String sqlDesvincular = "UPDATE pelicula SET id_director = NULL WHERE id_director = ?";
        String sqlBorrar = "DELETE FROM director WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false); // Transacción

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