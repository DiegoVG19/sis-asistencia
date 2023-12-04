/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package SistemaServlet;

import com.google.gson.Gson;
import dao.AsistenciaJpaController;
import java.io.IOException;
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
@WebServlet("/obtenerAsistencias")
public class ObtenerAsistencias extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AsistenciaJpaController asistenciaController;

    public ObtenerAsistencias() {
        // Utilizamos la misma EntityManagerFactory que ya tienes configurada en AsistenciaJpaController
        asistenciaController = new AsistenciaJpaController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try ( PrintWriter out = response.getWriter()) {
            // Consulta SQL para obtener los datos
            String sql = "SELECT asistencia.id_asistencia, asistencia.entrada, asistencia.salida, "
                    + "empleado.id_empleado, empleado.nombre as nom_empleado, empleado.apellido, empleado.dni, "
                    + "cargo.id_cargo, cargo.nombre as nom_cargo "
                    + "FROM asistencia "
                    + "INNER JOIN empleado ON asistencia.id_empleado = empleado.id_empleado "
                    + "INNER JOIN cargo ON empleado.cargo = cargo.id_cargo";

            // Obtener la EntityManager desde la EntityManagerFactory en AsistenciaJpaController
            EntityManager em = asistenciaController.getEntityManager();
            Query query = em.createNativeQuery(sql);

            // Obtener los resultados de la consulta
            List<Object[]> results = query.getResultList();

            // Convertir resultados a JSON
            Gson gson = new Gson();
            String json = gson.toJson(results);

            // Escribir la respuesta JSON al cliente
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener datos de asistencias");
        }
    }
}

