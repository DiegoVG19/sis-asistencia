/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ServletEmpleado;

import com.google.gson.Gson;
import dao.EmpleadoJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(name = "ListadoEmpleado", urlPatterns = {"/listadoEmpleado"})
public class ListadoEmpleado extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            EmpleadoJpaController objEmpleadoJpaController = new EmpleadoJpaController();
            List<Object[]> Empleado = objEmpleadoJpaController.listar();
            List<Map<String, Object>> EmpleadoMapList = new ArrayList<>();

            for (Object[] EmpleadoData : Empleado) {
                Map<String, Object> EmpleadoMap = new HashMap<>();
                EmpleadoMap.put("EMPRESA", EmpleadoData[0]);
                EmpleadoMap.put("CARGO", EmpleadoData[1]);
                EmpleadoMap.put("NOMBRE", EmpleadoData[2]);
                EmpleadoMap.put("APELLIDO PATERNO", EmpleadoData[3]);
                EmpleadoMap.put("APELLIDO MATERNO", EmpleadoData[4]);
                EmpleadoMap.put("FECHA NACIMIENTO", EmpleadoData[5]);
                EmpleadoMap.put("DNI", EmpleadoData[6]);
                EmpleadoMap.put("DIRECCION", EmpleadoData[7]);
                   EmpleadoMap.put("CELULAR", EmpleadoData[8]);
                EmpleadoMapList.add(EmpleadoMap);
            }

            Gson gson = new Gson();

            String jsonClientes = gson.toJson(EmpleadoMapList);
            out.print(jsonClientes);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
