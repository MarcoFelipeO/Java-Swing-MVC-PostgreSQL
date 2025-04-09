package org.example.vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalVista extends JFrame {

    public MenuPrincipalVista() {
        setTitle("Sistema de Inventario y Ventas de Joyería");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnInventario = new JButton("Inventario de Joyas");
        JButton btnVentas = new JButton("Registrar Venta");
        JButton btnReportes = new JButton("Reporte de Ventas");
        JButton btnSalir = new JButton("Salir");

        add(btnInventario);
        add(btnVentas);
        add(btnReportes);
        add(btnSalir);

        btnInventario.addActionListener(e -> {
            dispose(); // cerrar menú
            new InventarioJoyaVista().setVisible(true);
        });

        btnVentas.addActionListener(e -> {
            dispose();
            new VentaVista().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            dispose();
            new ReporteVentasVista().setVisible(true);
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }
}
