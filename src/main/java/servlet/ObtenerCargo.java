/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CargoJpaController;
import dto.Cargo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@WebServlet("/obtenerCargos")
public class ObtenerCargo extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CargoJpaController cargoController;

    public ObtenerCargo() {
        // Utilizamos la misma EntityManagerFactory que ya tienes configurada en CargoJpaController
        cargoController = new CargoJpaController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Consulta SQL para obtener los datos
            String sql = "SELECT cargo.id_cargo, cargo.nombre FROM cargo";

            // Obtener la EntityManager desde la EntityManagerFactory en CargoJpaController
            EntityManager em = cargoController.getEntityManager();
            Query query = em.createNativeQuery(sql);

            // Obtener los resultados de la consulta
            List<Object[]> results = query.getResultList();

            // Convertir resultados a JSON
            List<Map<String, Object>> cargosList = new ArrayList<>();
            for (Object[] result : results) {
                Map<String, Object> cargoMap = new HashMap<>();
                cargoMap.put("id_cargo", result[0]);
                cargoMap.put("nombre", result[1]);
                cargosList.add(cargoMap);
            }

            Gson gson = new Gson();
            String json = gson.toJson(cargosList);

            // Escribir la respuesta JSON al cliente
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener datos de cargos");
        }
    }
}


