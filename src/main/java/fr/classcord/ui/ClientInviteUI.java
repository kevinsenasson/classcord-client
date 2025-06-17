package fr.classcord.ui;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientInviteUI extends JFrame {
    private JTextField ipField = new JTextField("127.0.0.1");
    private JTextField portField = new JTextField("12345");
    private JTextField pseudoField = new JTextField("Pseudo");
    private JButton connectButton = new JButton("Se connecter");

    private JTextArea messagesArea = new JTextArea();
    private JTextField messageField = new JTextField();
    private JButton sendButton = new JButton("Envoyer");

    private ClientInvite client;
    private String pseudo;

    public ClientInviteUI() {
        setTitle("Classcord - Invité");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Panel de connexion
        JPanel topPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        topPanel.add(new JLabel("IP:"));
        topPanel.add(ipField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(new JLabel("Pseudo:"));
        topPanel.add(pseudoField);
        topPanel.add(connectButton);

        // Zone d'affichage des messages
        messagesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messagesArea);

        // Panel d'envoi de message
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Layout principal
        setLayout(new BorderLayout(5, 5));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        connectButton.addActionListener(e -> connect());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
    }

    private void connect() {
        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());
        pseudo = pseudoField.getText().trim();

        client = new ClientInvite();

        try {
            client.connect(ip, port);
            client.listen(this::appendMessage); // Affiche chaque message reçu dans la zone de texte
            appendMessage("Connecté au serveur !");
            connectButton.setEnabled(false);
            ipField.setEnabled(false);
            portField.setEnabled(false);
            pseudoField.setEnabled(false);
        } catch (Exception ex) {
            appendMessage("Erreur de connexion : " + ex.getMessage());
        }
    }

    private void sendMessage() {
        if (client == null)
            return;
        String messageText = messageField.getText().trim();
        if (messageText.isEmpty())
            return;

        JSONObject message = new JSONObject();
        message.put("type", "message");
        message.put("subtype", "global");
        message.put("to", "global");
        message.put("from", pseudo);
        message.put("content", messageText);

        client.send(message.toString());
        appendMessage("[Moi] : " + messageText);
        messageField.setText("");
    }

    private void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            messagesArea.append(msg + "\n");
            messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientInviteUI().setVisible(true));
    }
}