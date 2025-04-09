package org.example.vista;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dao.ClienteDAO;
import org.example.dao.VentaDAO;
import org.example.modelo.ClienteVO;
import org.example.modelo.VentaVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ReporteVentasVista extends JFrame {

    private JComboBox<ClienteVO> comboClientes;
    private JTextField txtFecha;
    private JButton btnFiltrar;
    private JTable tablaVentas;
    private DefaultTableModel modelo;

    private JLabel lblTotalVentas, lblJoyaMasVendida, lblClienteTop;

    private final VentaDAO ventaDAO = new VentaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public ReporteVentasVista() {
        setTitle("Reporte de Ventas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de filtros
        JPanel filtroPanel = new JPanel(new FlowLayout());
        comboClientes = new JComboBox<>();
        txtFecha = new JTextField(10);
        btnFiltrar = new JButton("Filtrar");

        filtroPanel.add(new JLabel("Cliente:"));
        filtroPanel.add(comboClientes);
        filtroPanel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        filtroPanel.add(txtFecha);
        filtroPanel.add(btnFiltrar);

        // Tabla
        modelo = new DefaultTableModel(new String[]{"ID Venta", "Cliente ID", "Joya ID", "Cantidad", "Fecha"}, 0);
        tablaVentas = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaVentas);

        // Resumen
        lblTotalVentas = new JLabel("Total de ventas: ");
        lblJoyaMasVendida = new JLabel("Joya más vendida: ");
        lblClienteTop = new JLabel("Cliente con más compras: ");

        JPanel panelResumen = new JPanel(new GridLayout(3, 1, 5, 5));
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen de Ventas"));
        panelResumen.add(lblTotalVentas);
        panelResumen.add(lblJoyaMasVendida);
        panelResumen.add(lblClienteTop);

        // Botones inferiores
        JButton btnExportar = new JButton("Exportar a Excel");
        btnExportar.addActionListener(e -> exportarExcel());

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuPrincipalVista().setVisible(true);
        });

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnExportar);
        panelInferior.add(btnVolver);

        // Agregar al JFrame
        add(filtroPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelResumen, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnFiltrar.addActionListener(e -> {
            filtrarVentas();
            cargarResumenVentas();
        });

        cargarClientes();
        filtrarVentas();
        cargarResumenVentas();
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();
        comboClientes.addItem(null); // Opción para "Todos"
        for (ClienteVO c : clienteDAO.obtenerClientes()) {
            comboClientes.addItem(c);
        }
    }

    private void filtrarVentas() {
        ClienteVO cliente = (ClienteVO) comboClientes.getSelectedItem();
        Integer clienteId = cliente != null ? cliente.getCliente_id() : null;

        LocalDate fecha = null;
        String fechaTexto = txtFecha.getText().trim();
        if (!fechaTexto.isEmpty()) {
            try {
                fecha = LocalDate.parse(fechaTexto);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "⚠️ Fecha inválida. Usa el formato YYYY-MM-DD");
                return;
            }
        }

        List<VentaVO> ventas = ventaDAO.obtenerVentasFiltradas(clienteId, fecha);
        modelo.setRowCount(0);
        for (VentaVO v : ventas) {
            modelo.addRow(new Object[]{
                    v.getVenta_id(),
                    v.getCliente_id(),
                    v.getJoya_id(),
                    v.getCantidad(),
                    v.getFecha()
            });
        }
    }

    private void cargarResumenVentas() {
        lblTotalVentas.setText("Total de ventas: " + ventaDAO.obtenerTotalVentas());
        lblJoyaMasVendida.setText("Joya más vendida: " + ventaDAO.obtenerJoyaMasVendida());
        lblClienteTop.setText("Cliente con más compras: " + ventaDAO.obtenerClienteConMasCompras());
    }

    private void exportarExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        fileChooser.setSelectedFile(new File("reporte_ventas.xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Ventas");

                // Cabecera
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    headerRow.createCell(i).setCellValue(modelo.getColumnName(i));
                }

                // Datos
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        Object valor = modelo.getValueAt(i, j);
                        row.createCell(j).setCellValue(valor != null ? valor.toString() : "");
                    }
                }

                try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(this, "✅ Reporte exportado correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error al exportar: " + ex.getMessage());
            }
        }
    }
}
