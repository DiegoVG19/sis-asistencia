/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Piero
 */
public class Cliente {

    public static void main(String[] args) {
        try {
            int port = 8088;

            // Clave del servidor
            int b = 3;

            // Cliente p, q y clave
            double clientP, clientG, clientA, B, Bdash;
            String Bstr;

            // Estableciendo la conexión
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Esperando al cliente en el puerto " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Conectado a " + server.getRemoteSocketAddress());

            // Clave privada del servidor
            System.out.println("Del servidor: Clave Privada = " + b);

            // Acepta los datos del cliente
            DataInputStream in = new DataInputStream(server.getInputStream());

            clientP = Integer.parseInt(in.readUTF()); // Recibe p
            System.out.println("Del Cliente: P = " + clientP);

            clientG = Integer.parseInt(in.readUTF()); // Recibe g
            System.out.println("Del Cliente: G = " + clientG);

            clientA = Double.parseDouble(in.readUTF()); // Recibe el dato A que sea procesado
            System.out.println("Del Cliente: Clave pública = " + clientA);

            // Envía la clave pública del servidor al cliente
            DiffieHellman serverDiffieHellman = new DiffieHellman();
            String serverPublicKey = serverDiffieHellman.getPublicKey();
            OutputStream outToClient = server.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);
            out.writeUTF(serverPublicKey);

            // Crea una instancia de DiffieHellman y configura la clave pública del cliente
            DiffieHellman diffieHellman = new DiffieHellman();
            diffieHellman.setReceiverPublicKey(String.valueOf(clientA));

            B = ((Math.pow(clientG, b)) % clientP); // Cálculo del dato B
            Bstr = Double.toString(B);

            // Envía el dato de B al cliente
            out.writeUTF(Bstr);

            Bdash = ((Math.pow(clientA, b)) % clientP); // Calcula la clave secreta

            System.out.println("Clave secreta para realizar el cifrado simétrico = " + Bdash);
            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}