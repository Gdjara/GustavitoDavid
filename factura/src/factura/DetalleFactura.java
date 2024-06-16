package factura;

import java.math.BigDecimal;

public class DetalleFactura {
    private String facnumero;
    private String procodigo;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleFactura(String facnumero, String procodigo, BigDecimal cantidad, BigDecimal precioUnitario) {
        this.facnumero = facnumero;
        this.procodigo = procodigo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad.multiply(precioUnitario);
    }

    public String getFacnumero() {
        return facnumero;
    }

    public String getProcodigo() {
        return procodigo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
