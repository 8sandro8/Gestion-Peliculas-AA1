package com.sandro.gestionpeliculas.dao;

import com.sandro.gestionpeliculas.ConexionBBDD;
import com.sandro.gestionpeliculas.modelo.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {

    // --- MÉTODO 1: INSERTAR ---
    public boolean insertar(Pelicula p) {
        // Asegúrate de que los nombres de las columnas coinciden con tu BBDD en phpMyAdmin
        String sql = "INSERT INTO pelicula (titulo, fecha_lanzamiento, duracion, presupuesto, es_mas_18, cartel_url, id_genero, id_director) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, p.getTitulo());

            // Convertir LocalDate a SQL Date
            if (p.getFechaLanzamiento() != null) {
                st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            } else {
                st.setDate(2, null);
            }

            st.setInt(3, p.getDuracion());
            st.setDouble(4, p.getPresupuesto());
            st.setBoolean(5, p.isEsMas18());
            st.setString(6, p.getCartelUrl());

            // Usamos los IDs. Si es 0, podríamos intentar enviar NULL si la BBDD lo permite,
            // pero por simplicidad enviaremos el 0 o el ID que tenga.
            st.setInt(7, p.getIdGenero());

            // Si tenemos el objeto director, sacamos su ID. Si no, usamos el idDirector suelto.
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
            e.printStackTrace();
            return false;
        }
    }

    // --- MÉTODO 2: LISTAR TODAS (Antes llamado obtenerTodas) ---
    // NOTA: Le he puesto el nombre 'listarTodas' porque así lo llamas en el Controller
    public List<Pelicula> listarTodas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM pelicula";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return lista;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                // Usamos el constructor vacío y SETTERS para evitar errores de constructor
                Pelicula p = new Pelicula();

                p.setId(rs.getInt("id"));
                p.setTitulo(rs.getString("titulo"));

                Date fechaSql = rs.getDate("fecha_lanzamiento");
                if (fechaSql != null) {
                    p.setFechaLanzamiento(fechaSql.toLocalDate());
                }

                p.setDuracion(rs.getInt("duracion"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setEsMas18(rs.getBoolean("es_mas_18"));
                p.setCartelUrl(rs.getString("cartel_url"));
                p.setIdDirector(rs.getInt("id_director"));
                p.setIdGenero(rs.getInt("id_genero"));

                // NOTA: Aquí solo cargamos el ID del director.
                // Si quieres ver el NOMBRE del director en la tabla, necesitaríamos hacer
                // una segunda consulta o un JOIN SQL.
                // De momento, el Controller pondrá "Sin Director" si el objeto es null.

                lista.add(p);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al listar: " + e.getMessage());
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

            if (p.getFechaLanzamiento() != null) {
                st.setDate(2, Date.valueOf(p.getFechaLanzamiento()));
            } else {
                st.setDate(2, null);
            }

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

            st.setInt(9, p.getId()); // WHERE id = ?

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

    // --- MÉTODO 4: ELIMINAR ---
    public boolean eliminar(int id) {
        // Consultas para borrar en cascada manualmente (por si la BBDD no tiene ON DELETE CASCADE)
        String sqlBorrarActores = "DELETE FROM actua WHERE id_pelicula = ?";
        String sqlBorrarValoraciones = "DELETE FROM valora WHERE id_pelicula = ?";
        // Si tienes la tabla reflexiva de secuelas:
        String sqlDesvincularSecuelas = "UPDATE pelicula SET id_secuela_de = NULL WHERE id_secuela_de = ?";
        String sqlBorrarPeli = "DELETE FROM pelicula WHERE id = ?";

        Connection con = ConexionBBDD.conectar();
        if (con == null) return false;

        try {
            // Desactivamos autocommit para hacer una transacción (todo o nada)
            con.setAutoCommit(false);

            // 1. Borrar relaciones en 'actua'
            PreparedStatement st1 = con.prepareStatement(sqlBorrarActores);
            st1.setInt(1, id);
            st1.executeUpdate();
            st1.close();

            // 2. Borrar relaciones en 'valora' (si existe la tabla)
            try {
                PreparedStatement st2 = con.prepareStatement(sqlBorrarValoraciones);
                st2.setInt(1, id);
                st2.executeUpdate();
                st2.close();
            } catch (SQLException ignored) {
                // Ignoramos si la tabla no existe aún
            }

            // 3. Desvincular secuelas
            try {
                PreparedStatement st3 = con.prepareStatement(sqlDesvincularSecuelas);
                st3.setInt(1, id);
                st3.executeUpdate();
                st3.close();
            } catch (SQLException ignored) { }

            // 4. Borrar la película finalmente
            PreparedStatement st4 = con.prepareStatement(sqlBorrarPeli);
            st4.setInt(1, id);
            int filas = st4.executeUpdate();
            st4.close();

            // Confirmar cambios
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