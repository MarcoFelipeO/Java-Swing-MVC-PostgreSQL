package org.example.vista;

import org.example.dao.JoyaDAO;
import org.example.modelo.JoyaVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventarioJoyaVista extends JFrame {

    private JTable tablaJoyas;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtMaterial, txtPeso, txtPrecio, txtStock;
    private JButton btnAgregar, btnEliminar, btnActualizar, btnRefrescar;

    private final JoyaDAO joyaDAO = new JoyaDAO();

    public InventarioJoyaVista() {
        setTitle("Inventario de Joyas");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Material", "Peso", "Precio", "Stock"}, 0);
        tablaJoyas = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaJoyas);

        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        txtNombre = new JTextField();
        txtMaterial = new JTextField();
        txtPeso = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        formPanel.add(new JLabel("Nombre"));
        formPanel.add(new JLabel("Material"));
        formPanel.add(new JLabel("Peso"));
        formPanel.add(new JLabel("Precio"));
        formPanel.add(new JLabel("Stock"));
        formPanel.add(new JLabel()); // Espacio vacío

        formPanel.add(txtNombre);
        formPanel.add(txtMaterial);
        formPanel.add(txtPeso);
        formPanel.add(txtPrecio);
        formPanel.add(txtStock);

        btnAgregar = new JButton("Agregar Joya");
        btnActualizar = new JButton("Actualizar Seleccionada");
        btnEliminar = new JButton("Eliminar Seleccionada");
        btnRefrescar = new JButton("Refrescar Lista");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuPrincipalVista().setVisible(true);
        });

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVolver.add(btnVolver);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(buttonPanel, BorderLayout.CENTER);
        panelInferior.add(panelVolver, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarJoya());
        btnEliminar.addActionListener(e -> eliminarJoya());
        btnActualizar.addActionListener(e -> actualizarJoya());
        btnRefrescar.addActionListener(e -> cargarTabla());

        cargarTabla();
        tablaJoyas.getSelectionModel().addListSelectionListener(event -> {
            int fila = tablaJoyas.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtMaterial.setText(modelo.getValueAt(fila, 2).toString());
                txtPeso.setText(modelo.getValueAt(fila, 3).toString());
                txtPrecio.setText(modelo.getValueAt(fila, 4).toString());
                txtStock.setText(modelo.getValueAt(fila, 5).toString());
            }
        });

    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<JoyaVO> joyas = joyaDAO.obtenerJoyas();
        for (JoyaVO j : joyas) {
            modelo.addRow(new Object[]{
                    j.getJoya_id(),
                    j.getNombre(),
                    j.getMaterial(),
                    j.getPeso(),
                    j.getPrecio(),
                    j.getStock()
            });
        }
    }

    private void agregarJoya() {
        try {
            String nombre = txtNombre.getText();
            String material = txtMaterial.getText();
            double peso = Double.parseDouble(txtPeso.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());

            JoyaVO nueva = new JoyaVO(0, nombre, material, peso, precio, stock);
            if (joyaDAO.agregarJoya(nueva)) {
                JOptionPane.showMessageDialog(this, "✅ Joya agregada correctamente.");
                cargarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar joya.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Verifica los datos ingresados.");
        }
    }

    private void eliminarJoya() {
        int fila = tablaJoyas.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar joya ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (joyaDAO.eliminarJoya(id)) {
                    JOptionPane.showMessageDialog(this, "✅ Joya eliminada.");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al eliminar joya.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una fila para eliminar.");
        }
    }

    private void actualizarJoya() {
        int fila = tablaJoyas.getSelectedRow();
        if (fila >= 0) {
            try {
                int id = (int) modelo.getValueAt(fila, 0);
                String nombre = txtNombre.getText();
                String material = txtMaterial.getText();
                double peso = Double.parseDouble(txtPeso.getText());
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());

                JoyaVO joya = new JoyaVO(id, nombre, material, peso, precio, stock);
                if (joyaDAO.actualizarJoya(joya)) {
                    JOptionPane.showMessageDialog(this, "✅ Joya actualizada.");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al actualizar.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Verifica los datos.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una fila para actualizar.");
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtMaterial.setText("");
        txtPeso.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }
}
