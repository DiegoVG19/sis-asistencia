/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.JsonObject;
import dao.AsistenciaJpaController;
import dao.EmpleadoJpaController;
import dto.Asistencia;
import dto.Empleado;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;

@WebServlet("/RegistroAsistenciaServlet")
public class RegistroAsistenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Obtiene el valor del DNI desde el formulario
        String dni = request.getParameter("txtdni");

        // Validar el DNI y obtener el ID del empleado
        EmpleadoJpaController empleadoController = new EmpleadoJpaController(emf);
        Empleado empleado = empleadoController.findEmpleadoByDni(dni);

        // Objeto JSON de respuesta
        JsonObject respuestaJson = new JsonObject();

        if (empleado != null) {
            // Actualiza la entrada en la tabla de asistencia
            boolean exito = actualizarEntrada(empleado.getIdEmpleado());

            if (exito) {
                respuestaJson.addProperty("exito", true);
                respuestaJson.addProperty("mensaje", "Entrada registrada correctamente");
            } else {
                respuestaJson.addProperty("exito", false);
                respuestaJson.addProperty("mensaje", "Error al registrar la entrada");
            }
        } else {
            respuestaJson.addProperty("exito", false);
            respuestaJson.addProperty("mensaje", "Error: DNI no válido");
        }

        // Envía la respuesta al cliente
        out.print(respuestaJson.toString());
        out.flush();
    }

    private boolean actualizarEntrada(int idEmpleado) {
        try {
            // Utiliza AsistenciaJpaController para realizar la actualización en la tabla de asistencia
            AsistenciaJpaController asistenciaController = new AsistenciaJpaController(emf);

            // Obtén la fecha actual
            Date fechaActual = new Date();

            // Formatea la fecha al formato deseado ('yyyy-MM-dd HH:mm:ss')
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(fechaActual);

            // Busca la entrada en la tabla de asistencia para el empleado
            Asistencia asistenciaExistente = asistenciaController.findAsistenciaByEmpleado(idEmpleado);

            if (asistenciaExistente != null) {
                // Ya existe una entrada para el empleado, actualiza la entrada existente
                asistenciaExistente.setEntrada(Timestamp.valueOf(formattedDate));
                asistenciaController.edit(asistenciaExistente);
            } else {
                // No existe una entrada para el empleado, esto podría ser un escenario de error
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
