/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.TablaAsistenciaJpaController;
import dto.TablaAsistencia;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.AES;

@WebServlet("/actualizar-asistencia")
public class ActualizarAsistencia extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String token = request.getParameter("token");
            String dato = request.getParameter("dato");
            final String secretKey = "ssshhhhhhhhhhh!!!!";

            String decryptedString = AES.decrypt(token, secretKey);
            if (decryptedString.equals(dato)) {
                // Los valores "token" y "dato" son válidos, continúa con la actualización de los datos
                int idAsistencia = Integer.parseInt(request.getParameter("idAsistencia"));
                String empleado = request.getParameter("empleado");
                String asistencia = request.getParameter("asistencia");

                // Crear una instancia de TablaAsistenciaJpaController
                TablaAsistenciaJpaController asistenciaDAO = new TablaAsistenciaJpaController();

                // Obtener la asistencia existente de la base de datos
                TablaAsistencia asistenciaExistente = asistenciaDAO.findTablaAsistencia(idAsistencia);

                if (asistenciaExistente != null) {
                    // Actualizar los datos de asistencia
                    asistenciaExistente.setEmpleado(empleado);

                    if (asistencia.length() > 0) {
                        asistenciaExistente.setAsistencia(asistencia.charAt(0));
                    }

                    // Guardar los cambios en la base de datos
                    asistenciaDAO.edit(asistenciaExistente);

                    // Responder con un mensaje de éxito
                    out.print("{\"resultado\":\"ok\"}");
                } else {
                    out.print("{\"resultado\":\"No se encontró la asistencia con el ID proporcionado.\"}");
                }
            } else {
                // Los valores "token" y "dato" no son válidos, responde con un mensaje de error
                out.print("{\"resultado\":\"error\"}");
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace();
            response.getWriter().println("Error al actualizar los datos: " + e.getMessage());
        }
    }
}

