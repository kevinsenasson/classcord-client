package fr.classcord.network;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

/**
 * Classe gérant la connexion client au serveur et la communication réseau.
 */
public class ClientInvite {

    // --- Attributs ---
    private Socket socket; // Socket de connexion au serveur
    private PrintWriter out; // Flux de sortie pour envoyer des messages
    private BufferedReader in; // Flux d'entrée pour recevoir des messages

    // --- Constructeurs ---
    // Aucun constructeur personnalisé, utilisation du constructeur par défaut

    // --- Méthodes ---

    /**
     * Établit la connexion au serveur avec l'IP et le port spécifiés.
     */
    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Envoie un message au serveur.
     */
    public void send(String message) {
        out.println(message);
    }

    /**
     * Lance l'écoute des messages entrants dans un thread séparé.
     * 
     * @param listener Callback appelé à chaque message reçu.
     */
    public void listen(MessageListener listener) {
        new Thread(() -> {
            String line;
            try {
                while ((line = getIn().readLine()) != null) {
                    listener.onMessage(line);
                }
            } catch (IOException e) {
                listener.onMessage("Erreur de réception : " + e.getMessage());
            }
        }).start();
    }

    /**
     * Retourne le flux d'entrée pour lire les messages du serveur.
     */
    public BufferedReader getIn() {
        return in;
    }

    // --- Classes/Interfaces internes ---

    /**
     * Interface pour le callback de réception de message.
     */
    public interface MessageListener {
        void onMessage(String message);
    }
}