/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package UsuarioServlet;

import com.google.gson.JsonObject;
import dao.CargoJpaController;
import dao.EmpleadoJpaController;
import dto.Cargo;
import dto.Empleado;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author redcr
 */
@WebServlet(name = "RegistrosEmpleados", urlPatterns = {"/registrosEmpleados"})
public class RegistrosEmpleados extends HttpServlet {

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // Indica que la respuesta será un objeto JSON

        String nombre = request.getParameter("txtnombre");
        String apellido = request.getParameter("txtapellido");
        String dni = request.getParameter("txtdni");
        String idcargo = request.getParameter("txtcargo");

        Integer idCargo = Integer.parseInt(idcargo);

        // Instanciar el controlador JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");
        EmpleadoJpaController empleadoController = new EmpleadoJpaController(emf);

        CargoJpaController cargoController = new CargoJpaController(emf);

        Cargo cargo = cargoController.obtenerCargoPorId(idCargo);
        // Crear un nuevo objeto Empleado y establecer los valores
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setNombre(nombre);
        nuevoEmpleado.setApellido(apellido);
        nuevoEmpleado.setDni(dni);
        nuevoEmpleado.setCargo(cargo); // Almacenar la contraseña cifrada

        // Insertar en la base de datos
        boolean registroExitoso = insertarEmpleadoEnBD(empleadoController, nuevoEmpleado);

        // Crear un objeto JSON con la información del registro
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("registroExitoso", registroExitoso);

        // Escribir el objeto JSON en la respuesta
        try ( PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }

        // Cerrar la fábrica de EntityManager
        emf.close();
    }

    private boolean insertarEmpleadoEnBD(EmpleadoJpaController empleadoController, Empleado empleado) {
        try {
            // Llamar al método create del controlador JPA
            empleadoController.create(empleado);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
