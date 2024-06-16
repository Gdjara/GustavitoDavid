package factura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaForm extends JDialog {
    private final FacturaDAO facturaDAO;
    private final FacturacionApp parent;
    private Factura factura;

    private JTextField txtClienteCodigo;
    private JTextField txtClienteNombre;
    private JTextField txtProductoCodigo;
    private JTextField txtProductoDescripcion;
    private JTextField txtProductoCantidad;
    private JTable tblProductos;
    private DefaultTableModel productosModel;
    private JTextField txtSubtotal;
    private JTextField txtDescuento;
    private JTextField txtIva;
    private JTextField txtTotal;
    private JRadioButton rbEfectivo;
    private JRadioButton rbTarjeta;

    public FacturaForm(FacturaDAO facturaDAO, FacturacionApp parent) {
        this(facturaDAO, parent, null);
    }

    public FacturaForm(FacturaDAO facturaDAO, FacturacionApp parent, Factura factura) {
        this.facturaDAO = facturaDAO;
        this.parent = parent;
        this.factura = factura;
        initialize();
    }

    private void initialize() {
        setTitle(factura == null ? "Nueva Factura" : "Editar Factura");
        setSize(800, 700);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel panelCliente = new JPanel(new GridLayout(2, 2));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Cliente"));

        panelCliente.add(new JLabel("Cliente Código:"));
        txtClienteCodigo = new JTextField();
        txtClienteCodigo.addActionListener(e -> cargarCliente());
        panelCliente.add(txtClienteCodigo);

        panelCliente.add(new JLabel("Cliente Nombre:"));
        txtClienteNombre = new JTextField();
        txtClienteNombre.setEditable(false);
        panelCliente.add(txtClienteNombre);

        add(panelCliente);

        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBorder(BorderFactory.createTitledBorder("Productos"));

        JPanel panelProductoForm = new JPanel(new GridLayout(1, 5));
        panelProductoForm.add(new JLabel("Producto Código:"));
        txtProductoCodigo = new JTextField();
        txtProductoCodigo.addActionListener(e -> cargarProducto());
        panelProductoForm.add(txtProductoCodigo);

        panelProductoForm.add(new JLabel("Producto Descripción:"));
        txtProductoDescripcion = new JTextField();
        txtProductoDescripcion.setEditable(false);
        panelProductoForm.add(txtProductoDescripcion);

        panelProductoForm.add(new JLabel("Cantidad:"));
        txtProductoCantidad = new JTextField();
        panelProductoForm.add(txtProductoCantidad);

        JButton btnAddProducto = new JButton("Añadir Producto");
        btnAddProducto.addActionListener(e -> añadirProducto());
        panelProductoForm.add(btnAddProducto);

        panelProductos.add(panelProductoForm);

        productosModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        tblProductos = new JTable(productosModel);
        panelProductos.add(new JScrollPane(tblProductos));

        JButton btnRemoveProducto = new JButton("Eliminar Producto");
        btnRemoveProducto.addActionListener(e -> eliminarProducto());
        panelProductos.add(btnRemoveProducto);

        add(panelProductos);

        JPanel panelTotales = new JPanel(new GridLayout(5, 2));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales"));

        panelTotales.add(new JLabel("Subtotal:"));
        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);
        panelTotales.add(txtSubtotal);

        panelTotales.add(new JLabel("Descuento:"));
        txtDescuento = new JTextField();
        txtDescuento.addActionListener(e -> calcularTotal());
        panelTotales.add(txtDescuento);

        panelTotales.add(new JLabel("IVA (15%):"));
        txtIva = new JTextField();
        txtIva.setEditable(false);
        panelTotales.add(txtIva);

        panelTotales.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        panelTotales.add(txtTotal);

        panelTotales.add(new JLabel("Forma de Pago:"));
        JPanel panelFormaPago = new JPanel(new GridLayout(1, 2));
        rbEfectivo = new JRadioButton("Efectivo");
        rbTarjeta = new JRadioButton("Tarjeta");
        ButtonGroup formaPagoGroup = new ButtonGroup();
        formaPagoGroup.add(rbEfectivo);
        formaPagoGroup.add(rbTarjeta);
        panelFormaPago.add(rbEfectivo);
        panelFormaPago.add(rbTarjeta);
        panelTotales.add(panelFormaPago);

        add(panelTotales);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarFactura();
            }
        });
        add(btnGuardar);

        if (factura != null) {
            cargarDatosFactura();
        }
    }

    private void cargarCliente() {
        String codigo = txtClienteCodigo.getText();
        Cliente cliente = facturaDAO.getClientePorCodigo(codigo);
        if (cliente != null) {
            txtClienteNombre.setText(cliente.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarProducto() {
        String codigo = txtProductoCodigo.getText();
        Producto producto = facturaDAO.getProductoPorCodigo(codigo);
        if (producto != null) {
            txtProductoDescripcion.setText(producto.getDescripcion());
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void añadirProducto() {
        String codigo = txtProductoCodigo.getText();
        String descripcion = txtProductoDescripcion.getText();
        String cantidadStr = txtProductoCantidad.getText();
        if (codigo.isEmpty() || descripcion.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos del producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal cantidad;
        try {
            cantidad = new BigDecimal(cantidadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto producto = facturaDAO.getProductoPorCodigo(codigo);
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = cantidad.multiply(precioUnitario);

        productosModel.addRow(new Object[]{codigo, descripcion, cantidad, precioUnitario, subtotal});

        calcularTotal();
    }

    private void eliminarProducto() {
        int selectedRow = tblProductos.getSelectedRow();
        if (selectedRow != -1) {
            productosModel.removeRow(selectedRow);
            calcularTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < productosModel.getRowCount(); i++) {
            subtotal = subtotal.add((BigDecimal) productosModel.getValueAt(i, 4));
        }
        txtSubtotal.setText(subtotal.toString());

        BigDecimal descuento = BigDecimal.ZERO;
        try {
            descuento = new BigDecimal(txtDescuento.getText());
        } catch (NumberFormatException e) {
            // Si el campo de descuento está vacío o tiene un valor inválido, se asume 0
        }

        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal iva = subtotalConDescuento.multiply(new BigDecimal("0.15"));
        BigDecimal total = subtotalConDescuento.add(iva);

        txtIva.setText(iva.toString());
        txtTotal.setText(total.toString());
    }

    private void cargarDatosFactura() {
        txtClienteCodigo.setText(factura.getCliente());
        // Completa con otros datos de la factura
    }

    private void guardarFactura() {
        String cliente = txtClienteCodigo.getText();
        BigDecimal subtotal = new BigDecimal(txtSubtotal.getText().isEmpty() ? "0" : txtSubtotal.getText());
        BigDecimal descuento = new BigDecimal(txtDescuento.getText().isEmpty() ? "0" : txtDescuento.getText());
        BigDecimal iva = new BigDecimal(txtIva.getText().isEmpty() ? "0" : txtIva.getText());
        BigDecimal total = new BigDecimal(txtTotal.getText().isEmpty() ? "0" : txtTotal.getText());
        String formaPago = rbEfectivo.isSelected() ? "EFEC" : "TARJ";

        // Truncar formaPago a 5 caracteres si es necesario
        if (formaPago.length() > 5) {
            formaPago = formaPago.substring(0, 5);
        }

        if (factura == null) {
            String numero = generateNumeroFactura();
            String fecha = LocalDate.now().toString();
            factura = new Factura(numero, cliente, fecha, subtotal, descuento, iva, total, "ABI", formaPago);
            facturaDAO.addFactura(factura);
        } else {
            factura.setCliente(cliente);
            factura.setSubtotal(subtotal);
            factura.setDescuento(descuento);
            factura.setIva(iva);
            factura.setTotal(total);
            factura.setFormaPago(formaPago);
            facturaDAO.updateFactura(factura);
        }

        guardarDetallesFactura();

        parent.loadFacturas();
        dispose();
    }

    private void guardarDetallesFactura() {
        String facnumero = factura.getNumero();
        for (int i = 0; i < productosModel.getRowCount(); i++) {
            String procodigo = (String) productosModel.getValueAt(i, 0);
            BigDecimal cantidad = (BigDecimal) productosModel.getValueAt(i, 2);
            BigDecimal precioUnitario = (BigDecimal) productosModel.getValueAt(i, 3);
            DetalleFactura detalle = new DetalleFactura(facnumero, procodigo, cantidad, precioUnitario);
            facturaDAO.addDetalleFactura(detalle);
        }
    }

    private String generateNumeroFactura() {
        String lastNumero = facturaDAO.getLastFacturaNumero();
        int nextNumero = 1;

        if (lastNumero != null && lastNumero.startsWith("FAC-")) {
            String lastNumeroStr = lastNumero.substring(4).trim();
            try {
                nextNumero = Integer.parseInt(lastNumeroStr) + 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return String.format("FAC-%03d", nextNumero);
    }
}
