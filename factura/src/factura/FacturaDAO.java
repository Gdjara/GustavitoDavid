package factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public List<Factura> getFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM FACTURAS";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Factura factura = new Factura(
                    rs.getString("FACNUMERO"),
                    rs.getString("CLICODIGO"),
                    rs.getDate("FACFECHA").toString(),
                    rs.getBigDecimal("FACSUBTOTAL"),
                    rs.getBigDecimal("FACDESCUENTO"),
                    rs.getBigDecimal("FACIVA"),
                    rs.getBigDecimal("FACSUBTOTAL").add(rs.getBigDecimal("FACIVA")),
                    rs.getString("FACSTATUS"),
                    rs.getString("FACFORMAPAGO")
                );
                facturas.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return facturas;
    }

    public void addFactura(Factura factura) {
        String sql = "INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACFORMAPAGO, FACSTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, 'ABI')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, factura.getNumero());
            pstmt.setString(2, factura.getCliente());
            pstmt.setDate(3, Date.valueOf(factura.getFecha()));
            pstmt.setBigDecimal(4, factura.getSubtotal());
            pstmt.setBigDecimal(5, factura.getDescuento());
            pstmt.setBigDecimal(6, factura.getIva());
            pstmt.setString(7, factura.getFormaPago().length() > 5 ? factura.getFormaPago().substring(0, 5) : factura.getFormaPago());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getLastFacturaNumero() {
        String lastNumero = null;
        String sql = "SELECT FACNUMERO FROM FACTURAS ORDER BY FACNUMERO DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                lastNumero = rs.getString("FACNUMERO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastNumero;
    }

    public Producto getProductoPorCodigo(String codigo) {
        Producto producto = null;
        String sql = "SELECT * FROM PRODUCTOS WHERE PROCODIGO = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto(
                        rs.getString("PROCODIGO"),
                        rs.getString("PRODESCRIPCION"),
                        rs.getBigDecimal("PROPRECIOUM")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return producto;
    }

    public Cliente getClientePorCodigo(String codigo) {
        Cliente cliente = null;
        String sql = "SELECT * FROM CLIENTES WHERE CLICODIGO = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getString("CLICODIGO"),
                        rs.getString("CLINOMBRE")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cliente;
    }

    public void addDetalleFactura(DetalleFactura detalle) {
        String sql = "INSERT INTO detalles_factura (FACNUMERO, PROCODIGO, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, detalle.getFacnumero());
            pstmt.setString(2, detalle.getProcodigo());
            pstmt.setBigDecimal(3, detalle.getCantidad());
            pstmt.setBigDecimal(4, detalle.getPrecioUnitario());
            pstmt.setBigDecimal(5, detalle.getSubtotal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFactura(Factura factura) {
        String sql = "UPDATE FACTURAS SET CLICODIGO=?, FACFECHA=?, FACSUBTOTAL=?, FACDESCUENTO=?, FACIVA=?, FACFORMAPAGO=?, FACSTATUS=? WHERE FACNUMERO=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, factura.getCliente());
            pstmt.setDate(2, Date.valueOf(factura.getFecha()));
            pstmt.setBigDecimal(3, factura.getSubtotal());
            pstmt.setBigDecimal(4, factura.getDescuento());
            pstmt.setBigDecimal(5, factura.getIva());
            pstmt.setString(6, factura.getFormaPago());
            pstmt.setString(7, factura.getStatus());
            pstmt.setString(8, factura.getNumero());  // Cambiado a 8 por el número de parámetros
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFactura(String numeroFactura) {
        String sql = "UPDATE FACTURAS SET FACSTATUS='ANU' WHERE FACNUMERO=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroFactura);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
