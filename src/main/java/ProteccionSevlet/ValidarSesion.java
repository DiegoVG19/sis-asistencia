/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ProteccionServlet;

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
@WebServlet(name = "ValidarSesion", urlPatterns = {"/validarsesion"})
public class ValidarSesion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(true);
                String logueado = Sesion.sesionvalida(session) ? "1" : "0";
                String logi = Sesion.getLogi(session);
                String pass = Sesion.getPass(session);
                String token = Sesion.getToken(session);
                String dato = Sesion.getDato(session);

                if (logueado.equals("0")) {
                    out.println("{\"resultado\":\"error\"}");
                } else {
                    out.println("{\"resultado\":\"ok\",\"logi\":\"" + logi
                            + "\",\"pass\":\"" + pass + "\",\"token\":\"" + token + "\",\"dato\":\"" + dato + "\"}");
                }
            } catch (Exception ex) {
                out.println("{\"resultado\":\"error\"}");
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
