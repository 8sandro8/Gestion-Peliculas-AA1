package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {

    // --- MÉTODO 1: INSERTAR ---
    public boolean insertar(Pelicula p) {
        String sql = "INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, es_mas_18, cartel_url, id_genero, id_director) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, p.getTitulo());
            st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            st.setInt(3, p.getDuracion());
            st.setDouble(4, p.getPresupuesto());
            st.setBoolean(5, p.isEsMas18());
            st.setString(6, p.getCartelUrl());

            // --- CORRECCIÓN: YA NO PONEMOS 1 A FUEGO ---
            st.setInt(7, p.getIdGenero());    // Usamos el ID real del objeto
            st.setInt(8, p.getIdDirector());  // Usamos el ID real del objeto
            // ------------------------------------------

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- MÉTODO 2: OBTENER TODAS ---
    public List<Pelicula> obtenerTodas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM pelicula";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Pelicula p = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_lanzamiento").toLocalDate(),
                        rs.getInt("duracion"),
                        rs.getDouble("presupuesto"),
                        rs.getBoolean("es_mas_18"),
                        rs.getString("cartel_url"),
                        rs.getInt("id_director"),
                        rs.getInt("id_genero")
                );
                lista.add(p);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // --- MÉTODO 3: ACTUALIZAR ---
    public boolean actualizar(Pelicula p) {
        String sql = "UPDATE pelicula SET titulo=?, fecha_lanzamiento=?, duracion=?, presupuesto=?, es_mas_18=?, cartel_url=?, id_genero=?, id_director=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, p.getTitulo());
            st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            st.setInt(3, p.getDuracion());
            st.setDouble(4, p.getPresupuesto());
            st.setBoolean(5, p.isEsMas18());
            st.setString(6, p.getCartelUrl());

            // --- CORRECCIÓN AQUÍ TAMBIÉN ---
            st.setInt(7, p.getIdGenero());    // ID Real
            st.setInt(8, p.getIdDirector());  // ID Real
            // -------------------------------

            st.setInt(9, p.getId()); // El ID para el WHERE va al final

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- MÉTODO 4: ELIMINAR (Modo Terminator) ---
    public boolean eliminar(int id) {
        String sqlBorrarActores = "DELETE FROM actua WHERE id_pelicula = ?";
        String sqlBorrarValoraciones = "DELETE FROM valora WHERE id_pelicula = ?";
        String sqlDesvincularSecuelas = "UPDATE pelicula SET id_secuela_de = NULL WHERE id_secuela_de = ?";
        String sqlBorrarPeli = "DELETE FROM pelicula WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            con.setAutoCommit(false);

            PreparedStatement st1 = con.prepareStatement(sqlBorrarActores);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

            PreparedStatement st2 = con.prepareStatement(sqlBorrarValoraciones);
            st2.setInt(1, id);
            st2.executeUpdate();
            st2.close();

            PreparedStatement st3 = con.prepareStatement(sqlDesvincularSecuelas);
            st3.setInt(1, id);
            st3.executeUpdate();
            st3.close();

            PreparedStatement st4 = con.prepareStatement(sqlBorrarPeli);
            st4.setInt(1, id);
            int filas = st4.executeUpdate();
            st4.close();

            con.commit();
            con.close();
            return filas > 0;

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("❌ Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}