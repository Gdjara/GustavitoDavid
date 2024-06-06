package Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Inicio de Sesión");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblUsername = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblUsername, gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtPassword, gbc);

        btnLogin = new JButton("Iniciar Sesión");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
    }

    private void authenticate() {
        String username = txtUsername.getText();
        char[] password = txtPassword.getPassword();

        if (validateCredentials(username, new String(password))) {
            // Si las credenciales son correctas, abrir la interfaz principal
            ClienteForm clienteForm = new ClienteForm(username, new String(password));
            clienteForm.setVisible(true);
            dispose(); // Cerrar la ventana de inicio de sesión
        } else {
            // Si las credenciales son incorrectas, mostrar un mensaje de error
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateCredentials(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection(username, password)) {
            return true; // Conexión exitosa
        } catch (SQLException e) {
            return false; // Error al conectar
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
