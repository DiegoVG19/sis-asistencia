/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import dao.TablaAsistenciaJpaController;
import dto.TablaAsistencia;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.AES;

/**
 *
 * @author redcr
 */
@WebServlet(name = "GenerarPdfServlet", urlPatterns = {"/generar-pdf"})
public class GenerarPdfServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los datos desde el base de datos
        List<TablaAsistencia> asistenciaData = obtenerDatosDesdeBaseDeDatos();

        // Obtener los parámetros "token" y "dato" de la solicitud
        String token = request.getParameter("token");
        String dato = request.getParameter("dato");
        
        final String secretKey = "ssshhhhhhhhhhh!!!!";

        String decryptedToken = AES.decrypt(token, secretKey);

        // Validar el token desencriptado
        if (decryptedToken.equals(dato)) {
            // Si la validación es exitosa, generar y enviar el PDF
            try {
                Document document = new Document();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter.getInstance(document, baos);
                document.open();

                // Configurar fuente y tamaño para el texto
                Font font = new Font(Font.HELVETICA, 12, Font.NORMAL);

                for (TablaAsistencia asistencia : asistenciaData) {
                    // Crear un párrafo con los datos de la asistencia
                    Paragraph paragraph = new Paragraph(
                            "ID Asistencia: " + asistencia.getIdAsistencia() + "\n"
                            + "Empleado: " + asistencia.getEmpleado() + "\n"
                            + "Asistencia: " + asistencia.getAsistencia() + "\n\n",
                            font
                    );
                    document.add(paragraph);
                }

                document.close();

                // Configurar la respuesta HTTP para el PDF
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=asistencia.pdf");

                // Enviar el PDF como respuesta
                ServletOutputStream out = response.getOutputStream();
                baos.writeTo(out);
                out.flush();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            // Si la validación falla, enviar una respuesta de error
            try ( PrintWriter out = response.getWriter()) {
                out.print("{\"resultado\":\"error\"}");
            }
        }
    }

    private List<TablaAsistencia> obtenerDatosDesdeBaseDeDatos() {
        TablaAsistenciaJpaController tablaAsistenciaController = new TablaAsistenciaJpaController();
        List<TablaAsistencia> asistenciaData = tablaAsistenciaController.findTablaAsistenciaEntities();
        return asistenciaData;
    }
}
