/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/proceso")
public class Proceso {
    private static Set<Session> sessiones = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + "Ha abierto una conexion");
        sessiones.add(session);
        //session.getBasicRemote().sendText("Conexion establecida");
    }

    @OnMessage
    public void onMessage(String mensaje, Session session) {
        System.out.println("Mensaje" + session.getId() + mensaje);
        try {
            for (Session s : sessiones) {
                s.getBasicRemote().sendText( mensaje);
            }
            
        } catch (IOException ex) {
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessiones.remove(session);
        System.out.println("Session " + session.getId() + " ha terminado");
    }
}
