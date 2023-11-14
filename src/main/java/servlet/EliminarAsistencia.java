/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.TablaAsistenciaJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.AES;

@WebServlet("/eliminar-asistencia")
public class EliminarAsistencia extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try ( PrintWriter out = response.getWriter()) {
            String token = request.getParameter("token");
            String dato = request.getParameter("dato");
            final String secretKey = "ssshhhhhhhhhhh!!!!";

            String decryptedString = AES.decrypt(token, secretKey);
            if (decryptedString.equals(dato)) {
                // Aquí puedes continuar con el proceso de eliminación
                // Verifica si el registro debe ser eliminado y realiza la eliminación si corresponde
                int idAsistencia = Integer.parseInt(request.getParameter("idAsistencia"));
                TablaAsistenciaJpaController asistenciaDAO = new TablaAsistenciaJpaController();

                if (asistenciaDAO.findTablaAsistencia(idAsistencia) != null) {
                    asistenciaDAO.destroy(idAsistencia);
                    out.print("{\"resultado\":\"ok\"}");
                } else {
                    out.print("{\"resultado\":\"No se encontró la asistencia con el ID proporcionado.\"}");
                }
            } else {
                out.print("{\"resultado\":\"error\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error al eliminar la asistencia: " + e.getMessage());
        }

    }
}
