package factura;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class FacturacionApp {
    private JFrame frame;
    private JTable table;
    private FacturaDAO facturaDAO;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FacturacionApp window = new FacturacionApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FacturacionApp() {
        facturaDAO = new FacturaDAO();
        initialize();
        loadFacturas();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JButton btnIngresarFactura = new JButton("Ingresar Factura");
        btnIngresarFactura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFacturaForm();
            }
        });
        panel.add(btnIngresarFactura);

        table = new JTable();
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadFacturas() {
        List<Factura> facturas = facturaDAO.getFacturas();
        String[] columnNames = {"Acciones", "Número", "Cliente", "Fecha", "Subtotal", "Descuento", "IVA", "Total", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo permitir editar la columna de botones
            }
        };

        for (Factura factura : facturas) {
            Object[] row = {
                factura,
                factura.getNumero(),
                factura.getCliente(),
                factura.getFecha(),
                factura.getSubtotal(),
                factura.getDescuento(),
                factura.getIva(),
                factura.getTotal(),
                factura.getStatus()
            };
            model.addRow(row);
        }
        table.setModel(model);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void openFacturaForm() {
        FacturaForm facturaForm = new FacturaForm(facturaDAO, this);
        facturaForm.setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

            editButton = new JButton(resizeIcon(new ImageIcon("images/edit.png"), 24, 24));
            editButton.setPreferredSize(new Dimension(30, 30));

            deleteButton = new JButton(resizeIcon(new ImageIcon("images/delete.png"), 24, 24));
            deleteButton.setPreferredSize(new Dimension(30, 30));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;
        private Factura factura;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

            editButton = new JButton(resizeIcon(new ImageIcon("images/edit.png"), 24, 24));
            editButton.setPreferredSize(new Dimension(30, 30));
            editButton.addActionListener(e -> openFacturaDialog(factura));
            panel.add(editButton);

            deleteButton = new JButton(resizeIcon(new ImageIcon("images/delete.png"), 24, 24));
            deleteButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.addActionListener(e -> {
                facturaDAO.deleteFactura(factura.getNumero());
                loadFacturas();
            });
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            factura = (Factura) value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return factura;
        }
    }

    private void openFacturaDialog(Factura factura) {
        FacturaForm facturaForm = new FacturaForm(facturaDAO, this, factura);
        facturaForm.setVisible(true);
    }
}
