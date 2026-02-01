package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Director;
import com.sandro.gestionpeliculas.modelo.Pelicula;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {

    private DirectorDAO directorDAO = new DirectorDAO();

    public List<Pelicula> listarTodas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM pelicula";

        Connection con = ConexionBBDD.conectar();
        if (con == null) {
            return lista;
        }

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                try {
                    Pelicula p = new Pelicula();

                    p.setId(rs.getInt("id"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setDuracion(rs.getInt("duracion"));
                    p.setPresupuesto(rs.getDouble("presupuesto"));
                    p.setEsMas18(rs.getBoolean("es_mas_18"));
                    p.setCartelUrl(rs.getString("cartel_url"));
                    p.setIdGenero(rs.getInt("id_genero"));

                    try {
                        Date fechaSql = rs.getDate("fecha_lanzamiento");
                        if (fechaSql != null) {
                            p.setFechaLanzamiento(fechaSql.toLocalDate());
                        } else {
                            p.setFechaLanzamiento(LocalDate.now());
                        }
                    } catch (SQLException e) {
                        try {
                            int anio = rs.getInt("anio");
                            p.setFechaLanzamiento(LocalDate.of(anio, 1, 1));
                        } catch (Exception ex) {
                            p.setFechaLanzamiento(LocalDate.now());
                        }
                    }

                    int idDirector = rs.getInt("id_director");
                    p.setIdDirector(idDirector);

                    if (idDirector > 0) {
                        Director d = directorDAO.obtenerPorId(idDirector);
                        p.setDirector(d);
                    }

                    lista.add(p);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insertar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, es_mas_18, cartel_url, id_genero, id_director) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) throw new SQLException("Error de conexión");

        PreparedStatement st = null;
        try {
            st = con.prepareStatement(sql);
            st.setString(1, p.getTitulo());
            st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            st.setInt(3, p.getDuracion());
            st.setDouble(4, p.getPresupuesto());
            st.setBoolean(5, p.isEsMas18());
            st.setString(6, p.getCartelUrl());
            st.setInt(7, p.getIdGenero());

            if (p.getDirector() != null) {
                st.setInt(8, p.getDirector().getId());
            } else {
                st.setInt(8, p.getIdDirector());
            }

            st.executeUpdate();

        } finally {
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }

    public void actualizar(Pelicula p) throws SQLException {
        String sql = "UPDATE pelicula SET titulo=?, fecha_lanzamiento=?, duracion=?, presupuesto=?, es_mas_18=?, cartel_url=?, id_genero=?, id_director=? WHERE id=?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) throw new SQLException("Error de conexión");

        PreparedStatement st = null;
        try {
            st = con.prepareStatement(sql);
            st.setString(1, p.getTitulo());
            st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            st.setInt(3, p.getDuracion());
            st.setDouble(4, p.getPresupuesto());
            st.setBoolean(5, p.isEsMas18());
            st.setString(6, p.getCartelUrl());
            st.setInt(7, p.getIdGenero());

            if (p.getDirector() != null) {
                st.setInt(8, p.getDirector().getId());
            } else {
                st.setInt(8, p.getIdDirector());
            }

            st.setInt(9, p.getId());

            st.executeUpdate();

        } finally {
            if (st != null) st.close();
            if (con != null) con.close();
        }
    }

    public boolean eliminar(int id) {
        String sqlBorrarPeli = "DELETE FROM pelicula WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sqlBorrarPeli);
            st.setInt(1, id);
            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}