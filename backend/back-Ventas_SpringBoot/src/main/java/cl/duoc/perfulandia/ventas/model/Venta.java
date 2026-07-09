package cl.duoc.perfulandia.ventas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private String nombreCliente;
    private String emailCliente;

    private Long perfumeId;
    private String nombrePerfume;
    private String marcaPerfume;

    private Integer cantidad;
    private Integer precioUnitario;
    private Integer totalVenta;

    private String estado;
    private LocalDateTime fechaVenta;

    public Venta() {
    }

    public Venta(
            Long usuarioId,
            String nombreCliente,
            String emailCliente,
            Long perfumeId,
            String nombrePerfume,
            String marcaPerfume,
            Integer cantidad,
            Integer precioUnitario,
            Integer totalVenta
    ) {
        this.usuarioId = usuarioId;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.perfumeId = perfumeId;
        this.nombrePerfume = nombrePerfume;
        this.marcaPerfume = marcaPerfume;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.totalVenta = totalVenta;
        this.estado = "REALIZADA";
        this.fechaVenta = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

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

    public String getNombrePerfume() {
        return nombrePerfume;
    }

    public String getMarcaPerfume() {
        return marcaPerfume;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Integer getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getTotalVenta() {
        return totalVenta;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setNombrePerfume(String nombrePerfume) {
        this.nombrePerfume = nombrePerfume;
    }

    public void setMarcaPerfume(String marcaPerfume) {
        this.marcaPerfume = marcaPerfume;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(Integer precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setTotalVenta(Integer totalVenta) {
        this.totalVenta = totalVenta;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}