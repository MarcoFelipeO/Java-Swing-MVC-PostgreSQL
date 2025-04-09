package org.example.dao;

import org.example.modelo.VentaVO;
import org.example.util.ConexionPostgreSQL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private Connection conn;

    public VentaDAO() {
        try {
            this.conn = ConexionPostgreSQL.getConnection();
        } catch (SQLException e) {
            System.out.println("Error al obtener la conexión: " + e.getMessage());
        }
    }

    public boolean registrarVenta(VentaVO venta) {
        String sql = "CALL sp_registrar_venta(?, ?, ?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, venta.getCliente_id());
            cs.setInt(2, venta.getJoya_id());
            cs.setInt(3, venta.getCantidad());
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
            return false;
        }
    }

    public List<VentaVO> obtenerVentas() {
        List<VentaVO> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta ORDER BY fecha DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VentaVO venta = new VentaVO(
                        rs.getInt("venta_id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("joya_id"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha").toLocalDate()
                );
                lista.add(venta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ventas: " + e.getMessage());
        }
        return lista;
    }

    public List<VentaVO> obtenerVentasFiltradas(Integer clienteId, LocalDate fecha) {
        List<VentaVO> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM venta WHERE 1=1");

        if (clienteId != null) sql.append(" AND cliente_id = ?");
        if (fecha != null) sql.append(" AND fecha = ?");
        sql.append(" ORDER BY fecha DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (clienteId != null) ps.setInt(index++, clienteId);
            if (fecha != null) ps.setDate(index, Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new VentaVO(
                        rs.getInt("venta_id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("joya_id"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar ventas: " + e.getMessage());
        }

        return lista;
    }


    public int obtenerTotalVentas() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM venta";
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar ventas: " + e.getMessage());
        }
        return total;
    }

    public String obtenerJoyaMasVendida() {
        String nombre = "N/A";
        String sql = "SELECT j.nombre FROM venta v JOIN joya j ON v.joya_id = j.joya_id " +
                "GROUP BY j.nombre ORDER BY SUM(v.cantidad) DESC LIMIT 1";
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nombre = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error joya más vendida: " + e.getMessage());
        }
        return nombre;
    }

    public String obtenerClienteConMasCompras() {
        String nombre = "N/A";
        String sql = "SELECT c.nombre FROM venta v JOIN cliente c ON v.cliente_id = c.cliente_id " +
                "GROUP BY c.nombre ORDER BY SUM(v.cantidad) DESC LIMIT 1";
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nombre = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error cliente top : " + e.getMessage());
        }
        return nombre;
    }


}
