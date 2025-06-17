package fr.classcord.app;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;
import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Entrez votre pseudo : ");
        String pseudo = console.readLine();

        ClientInvite client = new ClientInvite();
        client.connect("10.0.108.81", 12345); // adapte l’IP et le port
        client.listen(null);

        while (true) {
            String messageText = console.readLine();
            JSONObject message = new JSONObject();
            message.put("type", "message");
            message.put("subtype", "global");
            message.put("to", "global");
            message.put("from", pseudo);
            message.put("content", messageText);
            client.send(message.toString());
        }
    }
}
