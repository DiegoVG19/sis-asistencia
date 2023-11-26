/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author redcr
 */
@WebServlet("/eliminarUsuario")
public class EliminarUsuario extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioJpaController usuarioController;

    public EliminarUsuario() {
        // Utilizamos la misma EntityManagerFactory que ya tienes configurada en UsuarioJpaController
        usuarioController = new UsuarioJpaController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el ID del parámetro de la solicitud
        String idAsString = request.getParameter("id");

        if (idAsString != null && !idAsString.isEmpty()) {
            try {
                // Convertir el ID a entero
                int id = Integer.parseInt(idAsString);

                // Llamar al método de la controladora para eliminar el usuario por ID
                usuarioController.destroy(id);

                // Enviar una respuesta exitosa
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Usuario eliminado con éxito");
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
                response.getWriter().println("Error al intentar eliminar el usuario");
            }
        } else {
            // Enviar una respuesta de error si no se proporciona un ID
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error: Falta el parámetro 'id' en la solicitud");
        }
    }
}

