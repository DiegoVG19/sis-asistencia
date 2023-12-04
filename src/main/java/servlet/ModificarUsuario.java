/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ModificarUsua", urlPatterns = {"/modificarUsua"})
public class ModificarUsuario extends HttpServlet {

     private static final long serialVersionUID = 1L;
    private final UsuarioJpaController usuarioController;

    public ModificarUsuario() {
        // Utilizamos la misma EntityManagerFactory que ya tienes configurada en UsuarioJpaController
        usuarioController = new UsuarioJpaController();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los datos del formulario
        String idAsString = "1";
        String nombre = request.getParameter("txtnombre");
        String apellido = request.getParameter("txtapellido");
        String usuario = request.getParameter("txtusuario");
        String clave = request.getParameter("txtclave");

        if (idAsString != null && !idAsString.isEmpty()) {
            try {
                // Convertir el ID a entero
                int id = Integer.parseInt(idAsString);

                // Obtener el usuario existente por ID
                Usuario usuarioExistente = usuarioController.findUsuario(id);

                // Validar la contraseña
                String passHash = SHA256.getSha256Hash(clave);
                Usuario usuarioValidado = usuarioController.validar(passHash);

                if (usuarioValidado != null) {
                    // La contraseña es válida, proceder con la actualización de datos
                    usuarioExistente.setNombre(nombre);
                    usuarioExistente.setApellido(apellido);
                    usuarioExistente.setLogiUsua(usuario);

                    // Llamar al método de la controladora para actualizar el usuario
                    usuarioController.edit(usuarioExistente);

                    // Enviar una respuesta exitosa
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("Usuario modificado con éxito");
                } else {
                    // La contraseña no es válida, enviar una respuesta de error
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().println("Error: Contraseña no válida");
                }
            } catch (NumberFormatException e) {
                // Enviar una respuesta de error si el ID no es un número válido
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Error: El ID no es un número válido");
            } catch (NonexistentEntityException e) {
                // Enviar una respuesta de error si no se encuentra el usuario por el ID
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Error: No se encontró el usuario con el ID especificado");
            } catch (Exception e) {
                // Enviar una respuesta de error en caso de cualquier otra excepción
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error al intentar modificar el usuario");
            }
        } else {
            // Enviar una respuesta de error si no se proporciona un ID
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error: Falta el parámetro 'id' en la solicitud");
        }
    }

}
