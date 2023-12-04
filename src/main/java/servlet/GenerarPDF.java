/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.AsistenciaJpaController;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author redcr
 */
@WebServlet("/generarPdf")
public class GenerarPDF extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AsistenciaJpaController asistenciaController;

    public GenerarPDF() {
        asistenciaController = new AsistenciaJpaController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=asistencias.pdf");

        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Obtener la EntityManager desde la EntityManagerFactory en AsistenciaJpaController
            EntityManager em = asistenciaController.getEntityManager();

            // Consulta SQL para obtener los datos
            String sql = "SELECT asistencia.id_asistencia, asistencia.entrada, asistencia.salida, "
                    + "empleado.id_empleado, empleado.nombre as nom_empleado, empleado.apellido, empleado.dni, "
                    + "cargo.id_cargo, cargo.nombre as nom_cargo "
                    + "FROM asistencia "
                    + "INNER JOIN empleado ON asistencia.id_empleado = empleado.id_empleado "
                    + "INNER JOIN cargo ON empleado.cargo = cargo.id_cargo";

            // Crear la consulta
            Query query = em.createNativeQuery(sql);

            // Obtener los resultados de la consulta
            List<Object[]> results = query.getResultList();

            // Crear un documento PDF
            Document document = new Document();

            try {
                // Crear un escritor PDF
                PdfWriter.getInstance(document, baos);
                document.open();

                // Crear una tabla
                PdfPTable table = new PdfPTable(7); // El número 7 representa el número de columnas

                // Establecer ancho de las columnas
                float[] columnWidths = {8, 8, 7, 7, 7, 7, 7}; // Ajusta según tus necesidades
                table.setWidths(columnWidths);

                // Agregar encabezados de columna con estilo
                addTableHeader(table, "ID Asistencia");
                addTableHeader(table, "ID Empleado");
                addTableHeader(table, "Nombre");
                addTableHeader(table, "Apellido");
                addTableHeader(table, "Cargo");
                addTableHeader(table, "Entrada");
                addTableHeader(table, "Salida");

                // Agregar datos a la tabla con estilo
                for (Object[] result : results) {
                    table.addCell(result[0] != null ? result[0].toString() : ""); // ID Asistencia
                    table.addCell(result[3] != null ? result[3].toString() : ""); // ID Empleado
                    table.addCell(result[4] != null ? result[4].toString() : ""); // Nombre
                    table.addCell(result[5] != null ? result[5].toString() : ""); // Apellido
                    table.addCell(result[8] != null ? result[8].toString() : ""); // Cargo
                    table.addCell(result[1] != null ? result[1].toString() : ""); // Entrada
                    table.addCell(result[2] != null ? result[2].toString() : ""); // Salida
                }
                // Agregar la tabla al documento
                document.add(table);

            } catch (DocumentException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el archivo PDF");
            } finally {
                // Cerrar el documento
                document.close();
            }

            // Enviar el contenido del PDF como respuesta al cliente
            response.setContentLength(baos.size());
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener datos de asistencias");
        }
    }

    private void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell(new Paragraph(header));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
