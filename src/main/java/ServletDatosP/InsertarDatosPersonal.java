/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ServletDatosP;

import dao.DatospersonalJpaController;
import dto.Datospersonal;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(name = "InsertarDatosPersonal", urlPatterns = {"/insertarDatosPersonal"})
public class InsertarDatosPersonal extends HttpServlet {

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
         try (PrintWriter out = response.getWriter()) {
        DatospersonalJpaController objDatospersonalJpaController = new DatospersonalJpaController();

            String nombre = request.getParameter("NOMBRE");
            String app = request.getParameter("APELLIDO PATERNO");
            String apm = request.getParameter("APELLIDO MATERNO");
            String dni = request.getParameter("DNI");
            String fechaStr = request.getParameter("FECHA");
            String direccion = request.getParameter("DIRECCION");
            String celular = request.getParameter("CELULAR");
            String genero = request.getParameter("GENERO");


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = dateFormat.parse(fechaStr);
           
            Datospersonal nuevoDato = new Datospersonal();

            nuevoDato.setNombPer(nombre);
            nuevoDato.setAppPer(app);
            nuevoDato.setApmaPer(apm);
            nuevoDato.setDNIPers(dni);
            nuevoDato.setFechaNac(fecha);
            nuevoDato.setDireccion(direccion);
            nuevoDato.setCelular(celular);
            nuevoDato.setGenero(genero);
            

            boolean resultado = objDatospersonalJpaController.insertarDatosPersonal(nuevoDato);
            if (resultado) {
                out.print("{\"resultado\":\"ok\"}");
            } else {
                out.print("{\"resultado\":\"error\"}");
            }
             }catch (ParseException ex) {
           Logger.getLogger(InsertarDatosPersonal.class.getName()).log(Level.SEVERE, null, ex);
            out.print("{\"resultado\":\"error\", \"mensaje\":\"Error en el formato de fecha\"}");
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
