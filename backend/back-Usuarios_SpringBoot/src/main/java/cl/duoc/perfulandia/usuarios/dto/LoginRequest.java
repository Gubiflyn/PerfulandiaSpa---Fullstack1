package cl.duoc.perfulandia.usuarios.dto;

public class LoginRequest {

    private String email;
    private String contrasena;

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}