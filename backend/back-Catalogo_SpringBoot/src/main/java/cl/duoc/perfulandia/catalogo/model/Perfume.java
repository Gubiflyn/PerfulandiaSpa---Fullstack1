package cl.duoc.perfulandia.catalogo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfumes")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String marca;
    private Integer ml;
    private Integer precio;
    private Integer stock;
    private String imagenUrl;
    private Boolean activo = true;

    public Perfume() {
    }

    public Perfume(String nombre, String marca, Integer ml, Integer precio, Integer stock, String imagenUrl) {
        this.nombre = nombre;
        this.marca = marca;
        this.ml = ml;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public Integer getMl() {
        return ml;
    }

    public Integer getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setMl(Integer ml) {
        this.ml = ml;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}