package fr.classcord.ui;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientInviteUI extends JFrame {
    private static final Color BG_COLOR = new Color(34, 40, 49);
    private static final Color PANEL_COLOR = new Color(44, 54, 63);
    private static final Color FIELD_COLOR = new Color(57, 62, 70);
    private static final Color FG_COLOR = new Color(238, 238, 238);
    private static final Color ACCENT_COLOR = new Color(0, 173, 181);

    private JTextField ipField = new JTextField("127.0.0.1");
    private JTextField portField = new JTextField("12345");
    private JTextField pseudoField = new JTextField("Pseudo");
    private JButton connectButton = new JButton("Se connecter");

    private JTextArea messagesArea = new JTextArea();
    private JTextField messageField = new JTextField();
    private JButton sendButton = new JButton("Envoyer");

    private ClientInvite client;
    private String pseudo;

    public ClientInviteUI(ClientInvite client, String pseudo) {
        this();
        this.client = client;
        this.pseudo = pseudo;
        connectButton.setEnabled(false);
        ipField.setEnabled(false);
        portField.setEnabled(false);
        pseudoField.setEnabled(false);
        appendMessage("Connecté en tant que " + pseudo);
        // Lance l'écoute des messages
        client.listen(msg -> {
            try {
                JSONObject json = new JSONObject(msg);
                if ("message".equals(json.optString("type"))) {
                    String from = json.optString("from", "???");
                    String content = json.optString("content", "");
                    appendMessage("[" + from + "] : " + content);
                } else if ("status".equals(json.optString("type"))) {
                    String user = json.optString("user", "???");
                    String state = json.optString("state", "");
                    appendMessage("[Serveur] : " + user + " est " + state);
                } else {
                    appendMessage("[Serveur] : " + msg);
                }
            } catch (Exception e) {
                appendMessage("[Erreur réception] : " + msg);
            }
        });
    }

    public ClientInviteUI() {
        setTitle("Classcord - Invité");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Appliquer le style sombre à la fenêtre principale
        getContentPane().setBackground(BG_COLOR);

        // Panel de connexion avec GridBagLayout pour un alignement précis
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(PANEL_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port:");
        JLabel pseudoLabel = new JLabel("Pseudo:");
        styleLabel(ipLabel);
        styleLabel(portLabel);
        styleLabel(pseudoLabel);

        styleField(ipField);
        styleField(portField);
        styleField(pseudoField);
        styleButton(connectButton);

        gbc.gridy = 0;
        gbc.gridx = 0;
        topPanel.add(ipLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        topPanel.add(ipField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(portLabel, gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        topPanel.add(portField, gbc);

        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(pseudoLabel, gbc);
        gbc.gridx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        topPanel.add(pseudoField, gbc);

        gbc.gridx = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(connectButton, gbc);

        JPanel topPanelWithMargin = new JPanel(new BorderLayout());
        topPanelWithMargin.setBackground(BG_COLOR);
        topPanelWithMargin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanelWithMargin.add(topPanel, BorderLayout.CENTER);

        // Zone d'affichage des messages
        messagesArea.setEditable(false);
        messagesArea.setBackground(FIELD_COLOR);
        messagesArea.setForeground(FG_COLOR);
        messagesArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        messagesArea.setCaretColor(ACCENT_COLOR);
        messagesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(messagesArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(FIELD_COLOR);

        // Panel d'envoi de message
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(PANEL_COLOR);
        styleField(messageField);
        styleButton(sendButton);
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Layout principal
        setLayout(new BorderLayout(5, 5));
        add(topPanelWithMargin, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        connectButton.addActionListener(e -> connect());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
    }

    private void styleLabel(JLabel label) {
        label.setForeground(FG_COLOR);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleField(JTextField field) {
        field.setBackground(FIELD_COLOR);
        field.setForeground(FG_COLOR);
        field.setCaretColor(ACCENT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BG_COLOR);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void connect() {
        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());
        pseudo = pseudoField.getText().trim();

        client = new ClientInvite();

        try {
            client.connect(ip, port);
            client.listen(msg -> {
                try {
                    JSONObject json = new JSONObject(msg);
                    if ("message".equals(json.optString("type"))) {
                        String from = json.optString("from", "???");
                        String content = json.optString("content", "");
                        appendMessage("[" + from + "] : " + content);
                    } else if ("status".equals(json.optString("type"))) {
                        String user = json.optString("user", "???");
                        String state = json.optString("state", "");
                        appendMessage("[Serveur] : " + user + " est " + state);
                    } else {
                        appendMessage("[Serveur] : " + msg);
                    }
                } catch (Exception e) {
                    appendMessage("[Erreur réception] : " + msg);
                }
            });
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