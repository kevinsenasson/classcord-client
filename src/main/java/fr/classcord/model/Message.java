package fr.classcord.model;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;
import java.io.*;

/**
 * Classe principale pour l'application de messagerie.
 * Permet à un utilisateur de se connecter et d'envoyer des messages globaux.
 */
public class message {
    /**
     * Point d'entrée principal de l'application.
     */
    public static void main(String[] args) throws IOException {
        String pseudo = demanderPseudo();
        ClientInvite client = new ClientInvite();
        client.connect("10.0.108.52", 12345); // adapte l’IP et le port
        client.listen(null);
        boucleEnvoiMessages(client, pseudo);
    }

    /**
     * Demande à l'utilisateur de saisir son pseudo via la console.
     * 
     * @return Le pseudo saisi par l'utilisateur.
     */
    private static String demanderPseudo() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Entrez votre pseudo : ");
        return console.readLine();
    }

    /**
     * Boucle principale d'envoi des messages à tous les utilisateurs (global).
     * 
     * @param client Instance du client connecté au serveur.
     * @param pseudo Pseudo de l'utilisateur courant.
     */
    private static void boucleEnvoiMessages(ClientInvite client, String pseudo) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
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