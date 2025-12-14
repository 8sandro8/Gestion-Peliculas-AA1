package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Director;
import com.sandro.gestionpeliculas.modelo.Pelicula;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {

    // Necesitamos el DAO de directores para "traducir" el ID numérico al objeto Director
    private DirectorDAO directorDAO = new DirectorDAO();

    // --- MÉTODO 1: LISTAR TODAS (El más importante ahora) ---
    public List<Pelicula> listarTodas() {
        List<Pelicula> lista = new ArrayList<>();
        // Asegúrate de que tu tabla se llama 'pelicula' (singular) o 'peliculas' (plural)
        String sql = "SELECT * FROM pelicula";

        Connection con = ConexionBBDD.conectar();
        if (con == null) {
            System.out.println("❌ No hay conexión en PeliculaDAO");
            return lista;
        }

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("🔍 Buscando películas..."); // Debug

            while (rs.next()) {
                try {
                    Pelicula p = new Pelicula();

                    // Asignación de datos básicos
                    p.setId(rs.getInt("id"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setDuracion(rs.getInt("duracion"));
                    p.setPresupuesto(rs.getDouble("presupuesto"));
                    p.setEsMas18(rs.getBoolean("es_mas_18"));
                    p.setCartelUrl(rs.getString("cartel_url"));
                    p.setIdGenero(rs.getInt("id_genero"));

                    // --- FECHA ---
                    // IMPORTANTE: Si en tu base de datos la columna se llama "anio" (int), cambia esta línea.
                    // Aquí asumimos que es tipo DATE y se llama "fecha_lanzamiento".
                    try {
                        Date fechaSql = rs.getDate("fecha_lanzamiento");
                        if (fechaSql != null) {
                            p.setFechaLanzamiento(fechaSql.toLocalDate());
                        } else {
                            // Si es nula, ponemos fecha actual para que no falle
                            p.setFechaLanzamiento(LocalDate.now());
                        }
                    } catch (SQLException e) {
                        // Si falla porque la columna no existe, intentamos leer 'anio'
                        try {
                            int anio = rs.getInt("anio");
                            p.setFechaLanzamiento(LocalDate.of(anio, 1, 1));
                        } catch (Exception ex) {
                            System.out.println("⚠️ No se encontró columna fecha_lanzamiento ni anio. Usando fecha actual.");
                            p.setFechaLanzamiento(LocalDate.now());
                        }
                    }

                    // --- DIRECTOR ---
                    int idDirector = rs.getInt("id_director");
                    p.setIdDirector(idDirector);

                    // Recuperamos el objeto Director completo para que salga el nombre en la tabla
                    if (idDirector > 0) {
                        Director d = directorDAO.obtenerPorId(idDirector);
                        p.setDirector(d);
                    }

                    lista.add(p);
                    System.out.println("✅ Película cargada: " + p.getTitulo());

                } catch (Exception e) {
                    System.out.println("❌ Error leyendo una fila de película (saltando...): " + e.getMessage());
                    e.printStackTrace();
                }
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error general al listar películas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // --- MÉTODO 2: INSERTAR ---
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
            st.setInt(7, p.getIdGenero());

            if (p.getDirector() != null) {
                st.setInt(8, p.getDirector().getId());
            } else {
                st.setInt(8, p.getIdDirector());
            }

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar: " + e.getMessage());
            return false;
        }
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
            st.setInt(7, p.getIdGenero());

            if (p.getDirector() != null) {
                st.setInt(8, p.getDirector().getId());
            } else {
                st.setInt(8, p.getIdDirector());
            }

            st.setInt(9, p.getId());

            int filas = st.executeUpdate();
            st.close();
            con.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // --- MÉTODO 4: ELIMINAR ---
    public boolean eliminar(int id) {
        String sqlBorrarPeli = "DELETE FROM pelicula WHERE id = ?";
        // Opcional: Borrar tablas relacionadas antes si hay FK (actua, valora, etc)
        // Por simplicidad, intentamos borrar directo:

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
            System.out.println("❌ Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}