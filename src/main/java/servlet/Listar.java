/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/listar-asistencia")
public class Listar extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
//            // Crea una instancia de AsistenciaJpaController
//            TablaAsistenciaJpaController asistenciaDAO = new TablaAsistenciaJpaController();
//
//            // Obtiene la lista de asistencias desde la base de datos
//            List<TablaAsistencia> asistenciaList = asistenciaDAO.findTablaAsistenciaEntities();
//
//            // Convierte la lista de objetos en formato JSON
//            Gson gson = new Gson();
//            String json = gson.toJson(asistenciaList);
//
//            // Escribe el JSON en la respuesta
//            response.getWriter().write(json);
        } catch (Exception e) {
            // Maneja cualquier excepción que pueda ocurrir
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
