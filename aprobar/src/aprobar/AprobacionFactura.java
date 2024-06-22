package aprobar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AprobacionFactura {

    public static void aprobarFactura(String facNumero) throws SQLException {
        Connection conn = ConexionBaseDatos.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn.setAutoCommit(false);

            // Obtener los productos de la factura
            String obtenerProductosSQL = "SELECT procodigo, pxfcantidad, pxfvalor FROM PXF WHERE facnumero = ?";
            ps = conn.prepareStatement(obtenerProductosSQL);
            ps.setString(1, facNumero);
            rs = ps.executeQuery();

            double totalCostoInicial = 0;
            double totalEgresos = 0;

            while (rs.next()) {
                String procodigo = rs.getString("procodigo");
                int cantidad = rs.getInt("pxfcantidad");
                double valor = rs.getDouble("pxfvalor");

                // Obtener el saldo inicial y el costo unitario del producto
                String obtenerProductoSQL = "SELECT prosaldoinicial, procostoum FROM PRODUCTOS WHERE procodigo = ?";
                PreparedStatement psProducto = conn.prepareStatement(obtenerProductoSQL);
                psProducto.setString(1, procodigo);
                ResultSet rsProducto = psProducto.executeQuery();

                if (rsProducto.next()) {
                    double saldoInicial = rsProducto.getDouble("prosaldoinicial");
                    double costoUnitario = rsProducto.getDouble("procostoum");

                    // Calcular los valores para los asientos contables
                    totalCostoInicial += saldoInicial * costoUnitario;
                    totalEgresos += cantidad * valor;

                    // Actualizar los egresos del producto
                    String actualizarProductoSQL = "UPDATE PRODUCTOS SET proegresos = proegresos + ? WHERE procodigo = ?";
                    PreparedStatement psUpdateProducto = conn.prepareStatement(actualizarProductoSQL);
                    psUpdateProducto.setInt(1, cantidad);
                    psUpdateProducto.setString(2, procodigo);
                    psUpdateProducto.executeUpdate();
                }
            }

           

            // Insertar en la tabla SALIDAS
            String salDescripcion = "Salida por aprobación de factura " + facNumero;
            if (salDescripcion.length() > 30) salDescripcion = salDescripcion.substring(0, 30);
            String insertarSalidaSQL = "INSERT INTO SALIDAS (empcodigo, saldescripcion, salfecha, salcantidadtotal, salreferencia) VALUES ('EMP-111', ?, current_date, ?, ?)";
            ps = conn.prepareStatement(insertarSalidaSQL);
            ps.setString(1, salDescripcion);
            ps.setDouble(2, totalEgresos);
            ps.setString(3, facNumero);
            ps.executeUpdate();

            // Obtener el código de salida generado
            String obtenerSalidaCodigoSQL = "SELECT currval(pg_get_serial_sequence('salidas', 'salcodigo')) as salcodigo";
            ps = conn.prepareStatement(obtenerSalidaCodigoSQL);
            rs = ps.executeQuery();
            int salCodigo = 0;
            if (rs.next()) {
                salCodigo = rs.getInt("salcodigo");
            }

            // Insertar en la tabla PXS
            String insertarPXSsql = "INSERT INTO PXS (salcodigo, procodigo, pxscantidad) SELECT ?, procodigo, pxfcantidad FROM PXF WHERE facnumero = ?";
            ps = conn.prepareStatement(insertarPXSsql);
            ps.setInt(1, salCodigo);
            ps.setString(2, facNumero);
            ps.executeUpdate();

            // Insertar en la tabla FACTURA_SALIDA
            String insertarFacturaSalidaSQL = "INSERT INTO FACTURA_SALIDA (facnumero, salcodigo) VALUES (?, ?)";
            ps = conn.prepareStatement(insertarFacturaSalidaSQL);
            ps.setString(1, facNumero);
            ps.setInt(2, salCodigo);
            ps.executeUpdate();

            // Actualizar el estado de la factura y PXF
            String actualizarFacturaSQL = "UPDATE FACTURAS SET facstatus = 'APR' WHERE facnumero = ?";
            ps = conn.prepareStatement(actualizarFacturaSQL);
            ps.setString(1, facNumero);
            ps.executeUpdate();

            String actualizarPXFSQL = "UPDATE PXF SET pxfstatus = 'APR' WHERE facnumero = ?";
            ps = conn.prepareStatement(actualizarPXFSQL);
            ps.setString(1, facNumero);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            ex.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.setAutoCommit(true);
        }
    }
}
