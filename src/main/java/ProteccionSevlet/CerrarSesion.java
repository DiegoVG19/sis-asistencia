/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ProteccionSevlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import security.Sesion;

/**
 *
 * @author redcr
 */
@WebServlet(name = "CerrarSesion", urlPatterns = {"/cerrarsesion"})
public class CerrarSesion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(false);
                if (session != null && Sesion.sesionvalida(session)) {
                    Sesion.cerrarsesion(session);
                    out.println("{\"resultado\":\"ok\"}");
                } else {
                    out.println("{\"resultado\":\"error\",\"mensaje\":\"No hay sesión activa para cerrar\"}");
                }
            } catch (Exception ex) {
                out.println("{\"resultado\":\"error\",\"mensaje\":\"" + ex.getMessage() + "\"}");
            }
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
