package org.example.dao;

import org.example.modelo.JoyaVO;
import org.example.util.ConexionPostgreSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoyaDAO {

    private Connection conn;

    public JoyaDAO() {
        try {
            this.conn = ConexionPostgreSQL.getConnection();
        } catch (SQLException e) {
            System.out.println("Error al obtener la conexión: " + e.getMessage());
        }
    }

    public boolean agregarJoya(JoyaVO joya) {
        String sql = "CALL sp_agregar_joya(?, ?, ?, ?, ?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, joya.getNombre());
            cs.setString(2, joya.getMaterial());
            cs.setDouble(3, joya.getPeso());
            cs.setDouble(4, joya.getPrecio());
            cs.setInt(5, joya.getStock());
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar joya: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarJoya(JoyaVO joya) {
        String sql = "CALL sp_actualizar_joya(?, ?, ?, ?, ?, ?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, joya.getJoya_id());
            cs.setString(2, joya.getNombre());
            cs.setString(3, joya.getMaterial());
            cs.setDouble(4, joya.getPeso());
            cs.setDouble(5, joya.getPrecio());
            cs.setInt(6, joya.getStock());
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar joya : " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarJoya(int joya_id) {
        String sql = "CALL sp_eliminar_joya(?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, joya_id);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar joya: " + e.getMessage());
            return false;
        }
    }

    public List<JoyaVO> obtenerJoyas() {
        List<JoyaVO> lista = new ArrayList<>();
        String sql = "SELECT * FROM joya ORDER BY joya_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new JoyaVO(
                        rs.getInt("joya_id"),
                        rs.getString("nombre"),
                        rs.getString("material"),
                        rs.getDouble("peso"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener joyas: " + e.getMessage());
        }
        return lista;
    }
}
