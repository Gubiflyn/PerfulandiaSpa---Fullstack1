const API = {
  catalogo: "http://localhost:8081/api/perfumes",
  usuarios: "http://localhost:8082/api/usuarios",
  ventas: "http://localhost:8083/api/ventas",
  contactos: "http://localhost:8084/api/contactos"
};

let perfumes = [];
let usuarioActual = null;

document.addEventListener("DOMContentLoaded", () => {
  cargarCatalogo();
  cargarVentas();

  document.getElementById("registroForm").addEventListener("submit", registrarUsuario);
  document.getElementById("loginForm").addEventListener("submit", iniciarSesion);
  document.getElementById("ventaForm").addEventListener("submit", crearVenta);
  document.getElementById("contactoForm").addEventListener("submit", enviarContacto);
  document.getElementById("btnActualizarVentas").addEventListener("click", cargarVentas);
});

async function cargarCatalogo() {
  try {
    const respuesta = await fetch(API.catalogo);
    validarRespuesta(respuesta);

    perfumes = await respuesta.json();

    mostrarCatalogo();
    cargarOpcionesPerfumes();

  } catch (error) {
    mostrarAlerta("No se pudo cargar el catálogo. Revisa que el backend de catálogo esté encendido.", "error");
  }
}

function mostrarCatalogo() {
  const contenedor = document.getElementById("catalogoContainer");
  contenedor.innerHTML = "";

  if (perfumes.length === 0) {
    contenedor.innerHTML = `
      <div class="col-12 text-center">
        <p>No hay perfumes disponibles.</p>
      </div>
    `;
    return;
  }

  perfumes.forEach((perfume) => {
    const columna = document.createElement("div");
    columna.className = "col-md-6 col-lg-3";

    columna.innerHTML = `
      <div class="card product-card">
        <img
          src="${perfume.imagenUrl}"
          class="product-image"
          alt="${perfume.nombre}"
        />

        <div class="card-body">
          <p class="product-brand">${perfume.marca}</p>
          <h5 class="product-title">${perfume.nombre}</h5>
          <p class="text-muted mb-2">${perfume.ml} ml</p>

          <div class="d-flex justify-content-between align-items-center mb-3">
            <span class="product-price">${formatearPrecio(perfume.precio)}</span>
            <span class="stock-badge">Stock: ${perfume.stock}</span>
          </div>

          <button
            class="btn btn-gold w-100"
            onclick="seleccionarPerfume(${perfume.id})"
            ${perfume.stock <= 0 ? "disabled" : ""}
          >
            ${perfume.stock <= 0 ? "Sin stock" : "Comprar"}
          </button>
        </div>
      </div>
    `;

    contenedor.appendChild(columna);
  });
}

function cargarOpcionesPerfumes() {
  const select = document.getElementById("ventaPerfume");

  select.innerHTML = `<option value="">Seleccione un perfume</option>`;

  perfumes.forEach((perfume) => {
    const opcion = document.createElement("option");
    opcion.value = perfume.id;
    opcion.textContent = `${perfume.nombre} - ${perfume.marca} | Stock: ${perfume.stock}`;
    opcion.disabled = perfume.stock <= 0;

    select.appendChild(opcion);
  });
}

function seleccionarPerfume(id) {
  document.getElementById("ventaPerfume").value = id;
  document.getElementById("compras").scrollIntoView({ behavior: "smooth" });
}

async function registrarUsuario(evento) {
  evento.preventDefault();

  const datos = {
    nombre: document.getElementById("registroNombre").value,
    email: document.getElementById("registroEmail").value,
    contrasena: document.getElementById("registroContrasena").value
  };

  try {
    const respuesta = await fetch(`${API.usuarios}/registrar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(datos)
    });

    if (!respuesta.ok) {
      const mensajeError = await respuesta.text();
      throw new Error(mensajeError);
    }

    const usuario = await respuesta.json();

    mostrarAlerta(`Usuario registrado: ${usuario.nombre}`, "success");
    document.getElementById("registroForm").reset();

  } catch (error) {
    mostrarAlerta(error.message || "No se pudo registrar el usuario.", "error");
  }
}

async function iniciarSesion(evento) {
  evento.preventDefault();

  const datos = {
    email: document.getElementById("loginEmail").value,
    contrasena: document.getElementById("loginContrasena").value
  };

  try {
    const respuesta = await fetch(`${API.usuarios}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(datos)
    });

    if (!respuesta.ok) {
      const mensajeError = await respuesta.text();
      throw new Error(mensajeError);
    }

    const resultado = await respuesta.json();

    if (resultado.autenticado) {
      usuarioActual = resultado.usuario;

      document.getElementById("ventaNombre").value = usuarioActual.nombre;
      document.getElementById("ventaEmail").value = usuarioActual.email;

      mostrarUsuarioActivo();
      mostrarAlerta("Login correcto", "success");
      document.getElementById("loginForm").reset();
    }

  } catch (error) {
    mostrarAlerta(error.message || "No se pudo iniciar sesión.", "error");
  }
}

function mostrarUsuarioActivo() {
  const caja = document.getElementById("usuarioActivo");

  caja.classList.remove("d-none");
  caja.innerHTML = `
    <strong>Usuario activo</strong><br>
    ${usuarioActual.nombre} · ${usuarioActual.email}<br>
    Rol: ${usuarioActual.rol}
  `;
}

async function crearVenta(evento) {
  evento.preventDefault();

  const datos = {
    usuarioId: usuarioActual ? usuarioActual.id : null,
    nombreCliente: document.getElementById("ventaNombre").value,
    emailCliente: document.getElementById("ventaEmail").value,
    perfumeId: Number(document.getElementById("ventaPerfume").value),
    cantidad: Number(document.getElementById("ventaCantidad").value)
  };

  try {
    const respuesta = await fetch(API.ventas, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(datos)
    });

    if (!respuesta.ok) {
      const mensajeError = await respuesta.text();
      throw new Error(mensajeError);
    }

    const venta = await respuesta.json();

    mostrarAlerta(`Compra realizada. Total: ${formatearPrecio(venta.totalVenta)}`, "success");

    document.getElementById("ventaCantidad").value = 1;

    await cargarCatalogo();
    await cargarVentas();

  } catch (error) {
    mostrarAlerta(error.message || "No se pudo realizar la compra.", "error");
  }
}

async function cargarVentas() {
  const contenedor = document.getElementById("ventasContainer");

  try {
    const respuesta = await fetch(API.ventas);
    validarRespuesta(respuesta);

    const ventas = await respuesta.json();

    if (ventas.length === 0) {
      contenedor.innerHTML = `<p class="text-muted">Aún no hay ventas registradas.</p>`;
      return;
    }

    contenedor.innerHTML = `
      <table class="table table-hover">
        <thead>
          <tr>
            <th>ID</th>
            <th>Cliente</th>
            <th>Perfume</th>
            <th>Cantidad</th>
            <th>Total</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          ${ventas.map((venta) => `
            <tr>
              <td>${venta.id}</td>
              <td>${venta.nombreCliente}</td>
              <td>${venta.nombrePerfume}</td>
              <td>${venta.cantidad}</td>
              <td>${formatearPrecio(venta.totalVenta)}</td>
              <td>${venta.estado}</td>
            </tr>
          `).join("")}
        </tbody>
      </table>
    `;

  } catch (error) {
    contenedor.innerHTML = `
      <p class="text-danger">
        No se pudieron cargar las ventas. Revisa que el backend de ventas esté encendido.
      </p>
    `;
  }
}

async function enviarContacto(evento) {
  evento.preventDefault();

  const datos = {
    nombre: document.getElementById("contactoNombre").value,
    email: document.getElementById("contactoEmail").value,
    asunto: document.getElementById("contactoAsunto").value,
    mensaje: document.getElementById("contactoMensaje").value
  };

  try {
    const respuesta = await fetch(API.contactos, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(datos)
    });

    if (!respuesta.ok) {
      const mensajeError = await respuesta.text();
      throw new Error(mensajeError);
    }

    mostrarAlerta("Mensaje enviado correctamente", "success");
    document.getElementById("contactoForm").reset();

  } catch (error) {
    mostrarAlerta(error.message || "No se pudo enviar el mensaje.", "error");
  }
}

function formatearPrecio(valor) {
  return new Intl.NumberFormat("es-CL", {
    style: "currency",
    currency: "CLP"
  }).format(valor);
}

async function validarRespuesta(respuesta) {
  if (!respuesta.ok) {
    const mensaje = await respuesta.text();
    throw new Error(mensaje);
  }
}

function mostrarAlerta(mensaje, tipo) {
  const alerta = document.getElementById("alerta");

  alerta.textContent = mensaje;
  alerta.className = `custom-alert ${tipo}`;
  alerta.classList.remove("d-none");

  setTimeout(() => {
    alerta.classList.add("d-none");
  }, 3500);
}