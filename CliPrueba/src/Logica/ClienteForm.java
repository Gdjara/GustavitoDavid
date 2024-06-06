package Logica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ClienteForm extends JFrame {
    private String dbUsername;
    private String dbPassword;

    private JTextField txtClicodigo;
    private JTextField txtClinombre;
    private JTextField txtCliidentificacion;
    private JTextField txtClidireccion;
    private JTextField txtClitelefono;
    private JTextField txtClicelular;
    private JTextField txtCliemail;
    private JComboBox<String> cbClitipo;
    private JComboBox<String> cbClistatus;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnRetrieve;
    private JButton btnDeactivate;
    private JButton btnSearch;
    private JTable tableClientes;
    private DefaultTableModel tableModel;

    public ClienteForm(String dbUsername, String dbPassword) {
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;

        // Configuración del formulario y componentes
        setTitle("JaraSoft - Tabla Maestra Clientes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(0, 0, 128));
        add(mainPanel, BorderLayout.CENTER);

        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 128));
        titlePanel.setLayout(new BorderLayout());

        // Cargar y redimensionar la imagen
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/logo.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        titlePanel.add(logoLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("JaraSoft - Tabla Maestra Clientes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 204, 204));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 204, 204)), "Información del Cliente", 0, 0, null, new Color(0, 204, 204)));
        formPanel.setBackground(new Color(0, 0, 128));
        mainPanel.add(formPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblClicodigo = new JLabel("Código:");
        lblClicodigo.setForeground(new Color(255, 255, 255));
        JLabel lblClinombre = new JLabel("Nombre:");
        lblClinombre.setForeground(new Color(255, 255, 255));
        JLabel lblCliidentificacion = new JLabel("Identificación:");
        lblCliidentificacion.setForeground(new Color(255, 255, 255));
        JLabel lblClidireccion = new JLabel("Dirección:");
        lblClidireccion.setForeground(new Color(255, 255, 255));
        JLabel lblClitelefono = new JLabel("Teléfono:");
        lblClitelefono.setForeground(new Color(255, 255, 255));
        JLabel lblClicelular = new JLabel("Celular:");
        lblClicelular.setForeground(new Color(255, 255, 255));
        JLabel lblCliemail = new JLabel("Email:");
        lblCliemail.setForeground(new Color(255, 255, 255));
        JLabel lblClitipo = new JLabel("Tipo:");
        lblClitipo.setForeground(new Color(255, 255, 255));
        JLabel lblClistatus = new JLabel("Status:");
        lblClistatus.setForeground(new Color(255, 255, 255));

        txtClicodigo = new JTextField(20);
        txtClinombre = new JTextField(20);
        txtCliidentificacion = new JTextField(20);
        txtClidireccion = new JTextField(20);
        txtClitelefono = new JTextField(20);
        txtClicelular = new JTextField(20);
        txtCliemail = new JTextField(20);

        cbClitipo = new JComboBox<>(new String[]{"nat", "jur"});
        cbClistatus = new JComboBox<>(new String[]{"act", "ina"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblClicodigo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtClicodigo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblClinombre, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtClinombre, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblCliidentificacion, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtCliidentificacion, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblClidireccion, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtClidireccion, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblClitelefono, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(txtClitelefono, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(lblClicelular, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(txtClicelular, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(lblCliemail, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(txtCliemail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(lblClitipo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(cbClitipo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(lblClistatus, gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(cbClistatus, gbc);

        btnAdd = new JButton("Agregar");
        btnUpdate = new JButton("Actualizar");
        btnRetrieve = new JButton("Recuperar");
        btnDeactivate = new JButton("Desactivar");
        btnSearch = new JButton("Buscar por Código");

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        gbc.gridy = 9;
        formPanel.add(btnUpdate, gbc);
        gbc.gridx = 2;
        gbc.gridy = 9;
        formPanel.add(btnDeactivate, gbc);
        gbc.gridx = 1;
        gbc.gridy = 10;
        formPanel.add(btnRetrieve, gbc);
        gbc.gridx = 2;
        gbc.gridy = 10;
        formPanel.add(btnSearch, gbc);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCliente();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCliente();
            }
        });

        btnDeactivate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateCliente();
            }
        });

        btnRetrieve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrieveClientes();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCliente();
            }
        });

        // Panel de tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 204, 204)), "Lista de Clientes", 0, 0, null, new Color(0, 204, 204)));
        tablePanel.setBackground(new Color(0, 0, 128));
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CLICODIGO");
        tableModel.addColumn("CLINOMBRE");
        tableModel.addColumn("CLIIDENTIFICACION");
        tableModel.addColumn("CLIDIRECCION");
        tableModel.addColumn("CLITELEFONO");
        tableModel.addColumn("CLICELULAR");
        tableModel.addColumn("CLIEMAIL");
        tableModel.addColumn("CLITIPO");
        tableModel.addColumn("CLISTATUS");

        tableClientes = new JTable(tableModel);
        tableClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        tableClientes.setRowHeight(20);
        tableClientes.setBackground(new Color(44, 44, 44));
        tableClientes.setForeground(new Color(255, 255, 255));
        tableClientes.setSelectionBackground(new Color(0, 204, 204));
        tableClientes.setSelectionForeground(new Color(0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(tableClientes);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        retrieveClientes(); // Cargar los clientes al inicio
    }

    private void addCliente() {
        Cliente cliente = new Cliente();
        cliente.setClinombre(txtClinombre.getText());
        cliente.setCliidentificacion(txtCliidentificacion.getText());
        cliente.setClidireccion(txtClidireccion.getText());
        cliente.setClitelefono(txtClitelefono.getText());
        cliente.setClicelular(txtClicelular.getText());
        cliente.setCliemail(txtCliemail.getText());
        cliente.setClitipo(cbClitipo.getSelectedItem().toString());
        cliente.setClistatus(cbClistatus.getSelectedItem().toString());

        ClienteDAO dao = new ClienteDAO(dbUsername, dbPassword);
        try {
            dao.addCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente");
            retrieveClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el cliente: " + ex.getMessage());
        }
    }

    private void updateCliente() {
        if (txtClicodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para actualizar");
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setClicodigo(Integer.parseInt(txtClicodigo.getText()));
        cliente.setClinombre(txtClinombre.getText());
        cliente.setCliidentificacion(txtCliidentificacion.getText());
        cliente.setClidireccion(txtClidireccion.getText());
        cliente.setClitelefono(txtClitelefono.getText());
        cliente.setClicelular(txtClicelular.getText());
        cliente.setCliemail(txtCliemail.getText());
        cliente.setClitipo(cbClitipo.getSelectedItem().toString());
        cliente.setClistatus(cbClistatus.getSelectedItem().toString());

        ClienteDAO dao = new ClienteDAO(dbUsername, dbPassword);
        try {
            dao.updateCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente");
            retrieveClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el cliente: " + ex.getMessage());
        }
    }

    private void deactivateCliente() {
        if (txtClicodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para desactivar");
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setClicodigo(Integer.parseInt(txtClicodigo.getText()));
        cliente.setClinombre(txtClinombre.getText());
        cliente.setCliidentificacion(txtCliidentificacion.getText());
        cliente.setClidireccion(txtClidireccion.getText());
        cliente.setClitelefono(txtClitelefono.getText());
        cliente.setClicelular(txtClicelular.getText());
        cliente.setCliemail(txtCliemail.getText());
        cliente.setClitipo(cbClitipo.getSelectedItem().toString());
        cliente.setClistatus("ina");

        ClienteDAO dao = new ClienteDAO(dbUsername, dbPassword);
        try {
            dao.updateCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente desactivado exitosamente");
            retrieveClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al desactivar el cliente: " + ex.getMessage());
        }
    }

    private void retrieveClientes() {
        ClienteDAO dao = new ClienteDAO(dbUsername, dbPassword);
        try {
            List<Cliente> clientes = dao.getAllClientes();
            tableModel.setRowCount(0);
            for (Cliente cliente : clientes) {
                tableModel.addRow(new Object[]{
                    cliente.getClicodigo(),
                    cliente.getClinombre(),
                    cliente.getCliidentificacion(),
                    cliente.getClidireccion(),
                    cliente.getClitelefono(),
                    cliente.getClicelular(),
                    cliente.getCliemail(),
                    cliente.getClitipo(),
                    cliente.getClistatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al recuperar los clientes: " + ex.getMessage());
        }
    }

    private void searchCliente() {
        if (txtClicodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar");
            return;
        }

        int codigo = Integer.parseInt(txtClicodigo.getText());

        ClienteDAO dao = new ClienteDAO(dbUsername, dbPassword);
        try {
            Cliente cliente = dao.getClienteByCodigo(codigo);
            if (cliente != null) {
                txtClinombre.setText(cliente.getClinombre());
                txtCliidentificacion.setText(cliente.getCliidentificacion());
                txtClidireccion.setText(cliente.getClidireccion());
                txtClitelefono.setText(cliente.getClitelefono());
                txtClicelular.setText(cliente.getClicelular());
                txtCliemail.setText(cliente.getCliemail());
                cbClitipo.setSelectedItem(cliente.getClitipo());
                cbClistatus.setSelectedItem(cliente.getClistatus());
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el cliente: " + ex.getMessage());
        }
    }
}
