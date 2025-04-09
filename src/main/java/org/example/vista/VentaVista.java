package org.example.vista;

import org.example.dao.ClienteDAO;
import org.example.dao.JoyaDAO;
import org.example.dao.VentaDAO;
import org.example.modelo.ClienteVO;
import org.example.modelo.JoyaVO;
import org.example.modelo.VentaVO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class VentaVista extends JFrame {

    private JComboBox<ClienteVO> comboClientes;
    private JComboBox<JoyaVO> comboJoyas;
    private JTextField txtCantidad;
    private JLabel lblStockDisponible;
    private JButton btnVender;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final JoyaDAO joyaDAO = new JoyaDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    public VentaVista() {
        setTitle("Registrar Venta");
        setSize(800, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLayout(new GridLayout(4, 1, 10, 10));

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboClientes = new JComboBox<>();
        comboJoyas = new JComboBox<>();
        txtCantidad = new JTextField();
        lblStockDisponible = new JLabel("Stock: -");
        btnVender = new JButton("Realizar Venta");

        formPanel.add(crearFila("Cliente:", comboClientes));
        formPanel.add(crearFila("Joya:", comboJoyas));
        formPanel.add(crearFila("Stock disponible:", lblStockDisponible));
        formPanel.add(crearFila("Cantidad:", txtCantidad));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(btnVender);

        add(formPanel, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuPrincipalVista().setVisible(true);
        });
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVolver.add(btnVolver);
        add(panelVolver, BorderLayout.SOUTH);

        comboJoyas.addActionListener(e -> mostrarStock());
        btnVender.addActionListener(e -> realizarVenta());

        cargarClientes();
        cargarJoyas();
    }

    private JPanel crearFila(String labelTexto, JComponent campo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelTexto);
        label.setPreferredSize(new Dimension(140, 25));
        campo.setPreferredSize(new Dimension(600, 25)); //
        panel.add(label);
        panel.add(campo);
        return panel;
    }


    private void cargarClientes() {
        comboClientes.removeAllItems();
        for (ClienteVO c : clienteDAO.obtenerClientes()) {
            comboClientes.addItem(c);
        }
    }

    private void cargarJoyas() {
        comboJoyas.removeAllItems();
        for (JoyaVO j : joyaDAO.obtenerJoyas()) {
            comboJoyas.addItem(j);
        }
        mostrarStock();
    }

    private void mostrarStock() {
        JoyaVO joya = (JoyaVO) comboJoyas.getSelectedItem();
        if (joya != null) {
            lblStockDisponible.setText("Stock: " + joya.getStock());
        } else {
            lblStockDisponible.setText("Stock: -");
        }
    }

    private void realizarVenta() {
        ClienteVO cliente = (ClienteVO) comboClientes.getSelectedItem();
        JoyaVO joya = (JoyaVO) comboJoyas.getSelectedItem();

        if (cliente == null || joya == null) {
            JOptionPane.showMessageDialog(this, "Selecciona cliente y joya.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }

        if (cantidad <= 0 || cantidad > joya.getStock()) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida o stock insuficiente.");
            return;
        }

        VentaVO venta = new VentaVO(0, cliente.getCliente_id(), joya.getJoya_id(), cantidad, LocalDate.now());
        if (ventaDAO.registrarVenta(venta)) {
            JOptionPane.showMessageDialog(this, "✅ Venta registrada correctamente.");
            cargarJoyas();
            txtCantidad.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar la venta.");
        }
    }
}
