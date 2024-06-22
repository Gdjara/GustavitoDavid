package aprobar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AprobacionFacturaUI extends JFrame {
    private JTextField facturaField;
    private JButton aprobarButton;

    public AprobacionFacturaUI() {
        setTitle("Aprobación de Factura");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        facturaField = new JTextField(20);
        aprobarButton = new JButton("Aprobar Factura");
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Número de Factura:"));
        panel.add(facturaField);
        panel.add(aprobarButton);
        
        aprobarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroFactura = facturaField.getText();
                AprobacionFactura aprobacionFactura = new AprobacionFactura();
                try {
                    aprobacionFactura.aprobarFactura(numeroFactura);
                    JOptionPane.showMessageDialog(null, "Factura aprobada con éxito!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al aprobar la factura: " + ex.getMessage());
                }
            }
        });
        
        add(panel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AprobacionFacturaUI().setVisible(true);
            }
        });
    }
}
