
package fr.classcord.network;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class ClientInvite {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) {
        out.println(message);
    }

    public void listen() {
        new Thread(() -> {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    System.out.println("Message reçu : " + line);
                }
            } catch (IOException e) {
                System.out.println("Erreur de réception : " + e.getMessage());
            }
        }).start();
    }
}