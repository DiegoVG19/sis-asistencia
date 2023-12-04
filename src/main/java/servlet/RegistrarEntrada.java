/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dao.EmpleadoJpaController;
import dto.Empleado;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
@WebServlet("/registrarEntrada")
public class RegistrarEntrada extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el valor del DNI desde los parámetros de la solicitud
        String dni = request.getParameter("dni");

        // Obtiene la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaActual = dateFormat.format(new Date());

        // Formato de respuesta en JSON utilizando Gson
        JsonObject jsonResponse = new JsonObject();

        // Configura la respuesta HTTP
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Utiliza la EntityManagerFactory para obtener una EntityManager
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");
            EntityManager em = emf.createEntityManager();

            // Inicia la transacción
            em.getTransaction().begin();

            try {
                // Obtén el empleado por DNI
                EmpleadoJpaController empleadoController = new EmpleadoJpaController(emf);
                Integer idempleado = empleadoController.findEmpleadoIdByDni(dni);
                

                if (idempleado !=null) {
                    // Consulta SQL para actualizar la entrada en la tabla 'asistencia'
                    String sql = "UPDATE asistencia SET entrada = ? WHERE id_empleado = ?";

                    // Crea la consulta SQL
                    Query query = em.createNativeQuery(sql)
                            .setParameter(1, fechaActual)
                            .setParameter(2, idempleado);

                    // Ejecuta la actualización
                    int rowCount = query.executeUpdate();

                    // Confirma la transacción
                    em.getTransaction().commit();

                    // Agrega el resultado al objeto JSON
                    if (rowCount > 0) {
                        jsonResponse.addProperty("status", "ok");
                    } else {
                        jsonResponse.addProperty("status", "error");
                        jsonResponse.addProperty("message", "No se pudo actualizar la asistencia.");
                    }
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Empleado no encontrado.");
                }
            } catch (Exception e) {
                // Maneja las excepciones aquí
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Error en la actualización de la asistencia.");
            } finally {
                // Cierra la EntityManager
                em.close();
            }

            // Utiliza Gson para convertir el objeto JSON a una cadena y enviarlo
            Gson gson = new Gson();
            out.print(gson.toJson(jsonResponse));
        }
    }
}
