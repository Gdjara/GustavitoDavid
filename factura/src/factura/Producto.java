package factura;

import java.math.BigDecimal;

public class Producto {
    private String codigo;
    private String descripcion;
    private BigDecimal precio;

    public Producto(String codigo, String descripcion, BigDecimal precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
}
