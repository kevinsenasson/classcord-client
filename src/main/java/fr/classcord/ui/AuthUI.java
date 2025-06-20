package fr.classcord.ui;

import fr.classcord.network.ClientInvite;

import javax.swing.*;
import java.awt.*;

/**
 * Interface graphique pour la connexion au serveur Classcord.
 */
public class AuthUI extends JFrame {

    // --- Attributs ---
    private JTextField ipField = new JTextField("127.0.0.1"); // Champ pour l'adresse IP
    private JTextField portField = new JTextField("12345"); // Champ pour le port
    private JButton connectButton = new JButton("Connexion serveur"); // Bouton de connexion
    private JLabel serverStatus = new JLabel(" "); // Label pour l'état du serveur
    private ClientInvite client; // Client réseau

    // --- Constructeurs ---

    /**
     * Constructeur de la fenêtre d'authentification.
     * Initialise l'interface graphique et les listeners.
     */
    public AuthUI() {
        setTitle("Classcord - Connexion serveur");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Connexion au serveur"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Adresse IP :"), gbc);
        gbc.gridx = 1;
        panel.add(ipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Port :"), gbc);
        gbc.gridx = 1;
        panel.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(connectButton, gbc);

        gbc.gridy = 3;
        panel.add(serverStatus, gbc);

        add(panel);

        connectButton.addActionListener(e -> connectToServer());
    }

    // --- Méthodes ---

    /**
     * Tente de se connecter au serveur avec les informations saisies.
     * Affiche le résultat dans l'interface.
     */
    private void connectToServer() {
        String ip = ipField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException e) {
            serverStatus.setForeground(Color.RED);
            serverStatus.setText("Port invalide.");
            return;
        }
        connectButton.setEnabled(false);
        serverStatus.setForeground(Color.BLACK);
        serverStatus.setText("Connexion en cours...");
        new Thread(() -> {
            try {
                client = new ClientInvite();
                client.connect(ip, port);
                SwingUtilities.invokeLater(() -> {
                    serverStatus.setForeground(new Color(0, 128, 0));
                    serverStatus.setText("Connexion réussie !");
                    new UserUI(client).setVisible(true);
                    dispose();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    serverStatus.setForeground(Color.RED);
                    serverStatus.setText("Erreur : " + ex.getMessage());
                    connectButton.setEnabled(true);
                });
            }
        }).start();
    }

    /**
     * Point d'entrée de l'application pour lancer l'interface d'authentification.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthUI().setVisible(true));
    }
}