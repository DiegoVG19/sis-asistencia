/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.UsuarioJpaController;
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
@WebServlet("/obtenerUsuario")
public class ObtenerUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtén el nombre y apellido del usuario desde la base de datos
        String usuario = request.getParameter("usuario");
        UsuarioJpaController usuarioService = new UsuarioJpaController();
        String nombreCompleto = usuarioService.obtenerNombreApellidoPorUsername(usuario);

        // Puedes enviar la respuesta en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Si el nombreCompleto es null, puedes manejar ese caso según tus necesidades
        if (nombreCompleto != null) {
            response.getWriter().write("{\"nombreCompleto\": \"" + nombreCompleto + "\"}");
        } else {
            response.getWriter().write("{\"error\": \"Usuario no encontrado\"}");
        }
    }
}
