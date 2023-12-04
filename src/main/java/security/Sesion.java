/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import javax.servlet.http.HttpSession;

public class Sesion {

    public static void crearsesion(HttpSession session, String logi, String pass, String token, String dato) {
        session.setAttribute("logueado", "1");
        session.setAttribute("logi", logi);
        session.setAttribute("pass", pass);
        session.setAttribute("token", token);
        session.setAttribute("dato", dato);
    }

    public static boolean sesionvalida(HttpSession session) {
        String logueado = (String) session.getAttribute("logueado");
        return logueado != null && logueado.equals("1");
    }

    public static void cerrarsesion(HttpSession session) {
        session.removeAttribute("logueado");
        session.removeAttribute("logi");
        session.removeAttribute("pass");
        session.removeAttribute("token");
        session.removeAttribute("dato");
        session.invalidate();
    }

    public static String getLogi(HttpSession session) {
        Object ologi = session.getAttribute("logi");
        return ologi != null ? ologi.toString() : "";
    }

    public static String getPass(HttpSession session) {
        Object opass = session.getAttribute("pass");
        return opass != null ? opass.toString() : "";
    }

    public static String getToken(HttpSession session) {
        Object otoken = session.getAttribute("token");
        return otoken != null ? otoken.toString() : "";
    }

    public static String getDato(HttpSession session) {
        Object odato = session.getAttribute("dato");
        return odato != null ? odato.toString() : "";
    }
}
