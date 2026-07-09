package cl.duoc.perfulandia.usuarios.dto;

public class LoginResponse {

    private Boolean autenticado;
    private String mensaje;
    private UsuarioResponse usuario;

    public LoginResponse(Boolean autenticado, String mensaje, UsuarioResponse usuario) {
        this.autenticado = autenticado;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public Boolean getAutenticado() {
        return autenticado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }
}