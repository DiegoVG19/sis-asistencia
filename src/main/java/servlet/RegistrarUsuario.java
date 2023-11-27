/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.JsonObject;
import dao.UsuarioJpaController;
import dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.SHA256;

/**
 *
 * @author redcr
 */
@WebServlet(name = "RegistrarUsuario", urlPatterns = {"/registrarUsuario"})
public class RegistrarUsuario extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // Indica que la respuesta será un objeto JSON

        String nombre = request.getParameter("txtnombre");
        String apellido = request.getParameter("txtapellido");
        String usuario = request.getParameter("txtusuario");
        String clave = request.getParameter("txtclave");

        // Validar datos si es necesario
        // Cifrar la contraseña utilizando SHA-256
        String claveCifrada = SHA256.getSha256Hash(clave);

        // Instanciar el controlador JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");
        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);

        // Crear un nuevo objeto Usuario y establecer los valores
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setLogiUsua(usuario);
        nuevoUsuario.setPassUsua(claveCifrada); // Almacenar la contraseña cifrada

        // Insertar en la base de datos
        boolean registroExitoso = insertarUsuarioEnBD(usuarioController, nuevoUsuario);

        // Crear un objeto JSON con la información del registro
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("registroExitoso", registroExitoso);

        // Escribir el objeto JSON en la respuesta
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }

        // Cerrar la fábrica de EntityManager
        emf.close();
    }

    private boolean insertarUsuarioEnBD(UsuarioJpaController usuarioController, Usuario usuario) {
        try {
            // Llamar al método create del controlador JPA
            usuarioController.create(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

