/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.UsuarioJpaController;
import dto.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author redcr
 */
@WebServlet("/obtenerUsuarios")
public class ObtenerUsuarios extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la lista de usuarios desde el controlador JPA
        List<Usuario> usuarios = obtenerUsuariosDesdeBaseDeDatos();

        // Convertir la lista de usuarios a JSON
        String jsonUsuarios = convertirUsuariosAJSON(usuarios);

        // Establecer el tipo de contenido y enviar la respuesta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonUsuarios);
    }

    private List<Usuario> obtenerUsuariosDesdeBaseDeDatos() {
        UsuarioJpaController usuarioController = new UsuarioJpaController();
        return usuarioController.findUsuarioEntities();
    }

    private String convertirUsuariosAJSON(List<Usuario> usuarios) {
        Gson gson = new Gson();
        return gson.toJson(usuarios);
    }
}

