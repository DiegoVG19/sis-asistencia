/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.UsuarioJpaController;
import dto.Usuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.AES;
import security.SHA256;

@WebServlet("/actualizar-contrasena")
public class ActualizarContrasenaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los parámetros del request
        String usuario = request.getParameter("usuario");
        String nueva = request.getParameter("nueva");
        String token = request.getParameter("token");
        String dato = request.getParameter("dato");

        final String secretKey = "ssshhhhhhhhhhh!!!!";

        String decryptedToken = AES.decrypt(token, secretKey);

        // Comprobar si el token desencriptado coincide con el valor de "dato"
        if (decryptedToken.equals(dato)) {
            // Cifrar la nueva contraseña con SHA-256 utilizando tu clase SHA256
            String nuevaCifrada = SHA256.getSha256Hash(nueva);

            // Aquí debes realizar la actualización en tu base de datos utilizando UsuarioJpaController
            UsuarioJpaController usuarioController = new UsuarioJpaController();
            Usuario usuarioEntity = usuarioController.findUsuarioByUsername(usuario);

            // Comprobar si el usuario existe
            if (usuarioEntity != null) {
                // Actualizar la contraseña cifrada del usuario
                usuarioEntity.setPassUsua(nuevaCifrada);
                try {
                    usuarioController.edit(usuarioEntity); // Guardar la actualización
                    response.getWriter().write("{\"resultado\":\"ok\"}");
                } catch (Exception ex) {
                    Logger.getLogger(ActualizarContrasenaServlet.class.getName()).log(Level.SEVERE, null, ex);
                    response.getWriter().write("{\"resultado\":\"error\"}");
                }
            } else {
                response.getWriter().write("{\"resultado\":\"error\"}");
            }
        } else {
            response.getWriter().write("{\"resultado\":\"error\"}");
        }
    }
}
