package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Director;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAO {

    // 1. LEER TODOS (SELECT)
    public List<Director> obtenerTodos() {
        List<Director> lista = new ArrayList<>();
        String sql = "SELECT * FROM director";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Director d = new Director(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("nacionalidad"),
                        rs.getString("web_oficial")
                );
                lista.add(d);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al leer directores: " + e.getMessage());
        }
        return lista;
    }

    // 2. INSERTAR (CREATE)
    public boolean insertar(Director d) {
        String sql = "INSERT INTO director (nombre, fecha_nacimiento, nacionalidad, web_oficial) VALUES (?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());
            st.setDate(2, Date.valueOf(d.getFechaNacimiento()));
            st.setString(3, d.getNacionalidad());
            st.setString(4, d.getWebOficial());

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar director: " + e.getMessage());
            return false;
        }
    }

    // 3. ACTUALIZAR (UPDATE)
    public boolean actualizar(Director d) {
        String sql = "UPDATE director SET nombre=?, fecha_nacimiento=?, nacionalidad=?, web_oficial=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, d.getNombre());
            st.setDate(2, Date.valueOf(d.getFechaNacimiento()));
            st.setString(3, d.getNacionalidad());
            st.setString(4, d.getWebOficial());
            st.setInt(5, d.getId()); // El ID va al final para el WHERE

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar director: " + e.getMessage());
            return false;
        }
    }

    // 4. ELIMINAR (DELETE) - Con limpieza de películas huérfanas
    public boolean eliminar(int id) {
        // Antes de borrar al director, podríamos poner a NULL el director en sus películas
        // para no borrar las películas enteras (eso sería muy destructivo).
        String sqlDesvincularPeliculas = "UPDATE pelicula SET id_director = NULL WHERE id_director = ?";
        String sqlBorrarDirector = "DELETE FROM director WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false); // Transacción

            // A) Desvincular sus películas (se quedan sin director, pero no se borran)
            PreparedStatement st1 = con.prepareStatement(sqlDesvincularPeliculas);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

            // B) Borrar al director
            PreparedStatement st2 = con.prepareStatement(sqlBorrarDirector);
            st2.setInt(1, id);
            int filas = st2.executeUpdate();
            st2.close();

            con.commit();
            con.close();
            return filas > 0;

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("❌ Error al eliminar director: " + e.getMessage());
            return false;
        }
    }
}