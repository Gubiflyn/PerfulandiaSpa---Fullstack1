package cl.duoc.perfulandia.ventas.dto;

public class CrearVentaRequest {

    private Long usuarioId;
    private String nombreCliente;
    private String emailCliente;
    private Long perfumeId;
    private Integer cantidad;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public Long getPerfumeId() {
        return perfumeId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public void setPerfumeId(Long perfumeId) {
        this.perfumeId = perfumeId;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}