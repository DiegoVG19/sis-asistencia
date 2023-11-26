/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class JwtGenerator {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("4f7d52e13611c1c92b83d45c5b5eacd477c05f110187f0ecf1495a5293f73eb4".getBytes());

    public static String generateToken(String subject) {
        String token = Jwts.builder()
                .setSubject(subject)
                .signWith(SECRET_KEY)
                .compact();

        System.out.println("Token generado: " + token);
        return token;
    }

    public static void main(String[] args) {
        String token = generateToken("diego20231118000446");

        // Intenta decodificar el token
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            System.out.println("Token decodificado con éxito.");
        } catch (Exception e) {
            System.out.println("Error al decodificar el token: " + e.getMessage());
        }
    }
}
