package org.example.dao;

import org.example.modelo.ClienteVO;
import org.example.util.ConexionPostgreSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection conn;

    public ClienteDAO() {
        try {
            this.conn = ConexionPostgreSQL.getConnection();
        } catch (SQLException e) {
            System.out.println("Error al obtener la conexión : " + e.getMessage());
        }
    }

    public boolean agregarCliente(ClienteVO cliente) {
        String sql = "CALL sp_agregar_cliente(?, ?, ?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, cliente.getNombre());
            cs.setString(2, cliente.getCorreo());
            cs.setString(3, cliente.getTelefono());
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCliente(int cliente_id) {
        String sql = "CALL sp_eliminar_cliente(?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, cliente_id);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<ClienteVO> obtenerClientes() {
        List<ClienteVO> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY cliente_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ClienteVO(
                        rs.getInt("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener clientes: " + e.getMessage());
        }
        return lista;
    }
}
