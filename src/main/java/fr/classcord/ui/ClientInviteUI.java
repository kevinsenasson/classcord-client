package fr.classcord.ui;

import fr.classcord.network.ClientInvite;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface graphique principale pour l'invité sur Classcord.
 * Permet la connexion, l'affichage des utilisateurs et l'envoi de messages.
 */
public class ClientInviteUI extends JFrame {

    // --- Attributs ---

    // Couleurs de l'interface
    private static final Color BG_COLOR = new Color(34, 40, 49);
    private static final Color PANEL_COLOR = new Color(44, 54, 63);
    private static final Color FIELD_COLOR = new Color(57, 62, 70);
    private static final Color FG_COLOR = new Color(238, 238, 238);
    private static final Color ACCENT_COLOR = new Color(0, 173, 181);

    // Icônes pour les statuts
    private static final Color ICON_ONLINE = new Color(0, 200, 0);
    private static final Color ICON_AWAY = new Color(255, 140, 0);
    private static final Color ICON_DND = new Color(200, 0, 0);
    private static final Color ICON_INVISIBLE = new Color(120, 120, 120);

    // Champs de connexion
    private JTextField ipField = new JTextField("127.0.0.1");
    private JTextField portField = new JTextField("12345");
    private JTextField pseudoField = new JTextField("Pseudo");
    private JButton connectButton = new JButton("Se connecter");

    // ComboBox pour le statut utilisateur
    private JComboBox<String> statusCombo = new JComboBox<>(
            new String[] { "Disponible", "Absent", "Ne pas déranger", "Invisible" });

    // Zone de chat
    private JTextArea messagesArea = new JTextArea();
    private JTextField messageField = new JTextField();
    private JButton sendButton = new JButton("Envoyer");

    // Liste des utilisateurs connectés
    private DefaultListModel<String> userListModel = new DefaultListModel<>();
    private JList<String> userList = new JList<>(userListModel);
    private Map<String, String> userStatusMap = new HashMap<>(); // pseudo -> statut

    // Pour savoir si on envoie un MP ou un global
    private String selectedRecipient = "global";
    private JLabel toLabel = new JLabel("À : global");

    private ClientInvite client;
    private String pseudo;

    // --- Constructeurs ---

    /**
     * Constructeur utilisé après connexion, avec client et pseudo.
     */
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
        client.listen(msg -> handleIncomingMessage(msg));

        // Demande la liste des utilisateurs connectés au serveur (une seule fois)
        requestUsersList();
    }

    /**
     * Constructeur par défaut, initialise l'interface graphique.
     */
    public ClientInviteUI() {
        setTitle("Classcord - Invité");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
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

        // ComboBox pour le statut utilisateur
        gbc.gridx = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(statusCombo, gbc);

        JPanel topPanelWithMargin = new JPanel(new BorderLayout());
        topPanelWithMargin.setBackground(BG_COLOR);
        topPanelWithMargin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanelWithMargin.add(topPanel, BorderLayout.CENTER);

        // Panel pour la liste des utilisateurs connectés
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(PANEL_COLOR);
        JLabel userLabel = new JLabel("Utilisateurs connectés");
        userLabel.setForeground(FG_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userPanel.add(userLabel, BorderLayout.NORTH);
        userList.setBackground(FIELD_COLOR);
        userList.setForeground(FG_COLOR);
        userList.setSelectionBackground(ACCENT_COLOR);
        userList.setSelectionForeground(BG_COLOR);

        // Renderer personnalisé pour afficher l'icône de statut
        userList.setCellRenderer(new UserListCellRenderer());

        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        userPanel.setPreferredSize(new Dimension(220, 0));

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

        // Label pour afficher à qui on écrit
        toLabel.setForeground(FG_COLOR);
        toLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        bottomPanel.add(toLabel, BorderLayout.WEST);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Layout principal
        setLayout(new BorderLayout(5, 5));
        add(topPanelWithMargin, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(userPanel, BorderLayout.EAST);

        // Actions
        connectButton.addActionListener(e -> connect());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        // Sélection d'utilisateur pour MP
        userList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selected = userList.getSelectedValue();
                if (selected != null && !selected.equals(pseudo)) {
                    selectedRecipient = selected;
                    toLabel.setText("À : " + selected);
                } else {
                    selectedRecipient = "global";
                    toLabel.setText("À : global");
                }
            }
        });

        // Listener pour la combo de statut
        statusCombo.addActionListener(e -> {
            String state = "online";
            switch (statusCombo.getSelectedIndex()) {
                case 1:
                    state = "away";
                    break;
                case 2:
                    state = "dnd";
                    break;
                case 3:
                    state = "invisible";
                    break;
            }
            sendStatusToServer(state);
        });
    }

    // --- Méthodes ---

    // Méthodes de style pour l'interface
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

    // Connexion au serveur
    private void connect() {
        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());
        pseudo = pseudoField.getText().trim();

        client = new ClientInvite();

        try {
            client.connect(ip, port);
            client.listen(msg -> handleIncomingMessage(msg));
            appendMessage("Connecté au serveur !");
            connectButton.setEnabled(false);
            ipField.setEnabled(false);
            portField.setEnabled(false);
            pseudoField.setEnabled(false);

            // Demande la liste des utilisateurs connectés au serveur (une seule fois)
            requestUsersList();
        } catch (Exception ex) {
            appendMessage("Erreur de connexion : " + ex.getMessage());
        }
    }

    // Envoie la demande de liste des utilisateurs connectés
    private void requestUsersList() {
        if (client != null) {
            JSONObject request = new JSONObject();
            request.put("type", "users");
            client.send(request.toString());
        }
    }

    // Envoie le statut choisi au serveur
    private void sendStatusToServer(String state) {
        if (client != null) {
            JSONObject statusMsg = new JSONObject();
            statusMsg.put("type", "status");
            statusMsg.put("state", state);
            client.send(statusMsg.toString());
            // Mets à jour ton propre statut localement
            userStatusMap.put(pseudo, state);
            updateUserListDisplay();
        }
    }

    // Gère les messages entrants (status, global, privé, users)
    private void handleIncomingMessage(String msg) {
        try {
            JSONObject json = new JSONObject(msg);
            String type = json.optString("type");
            if ("message".equals(type)) {
                String subtype = json.optString("subtype", "global");
                String from = json.optString("from", "???");
                String content = json.optString("content", "");
                if ("private".equals(subtype)) {
                    appendPrivateMessage("[MP de " + from + "] : " + content);
                } else {
                    appendMessage("[" + from + "] : " + content);
                }
            } else if ("status".equals(type)) {
                String user = json.optString("user", "???");
                String state = json.optString("state", "");
                userStatusMap.put(user, state);
                updateUserListDisplay();
            } else if ("users".equals(type)) {
                JSONArray users = json.optJSONArray("users");
                if (users != null) {
                    updateUsersList(users);
                }
            } else {
                appendMessage("[Serveur] : " + msg);
            }
        } catch (Exception e) {
            appendMessage("[Erreur réception] : " + msg);
        }
    }

    // Affiche un MP avec préfixe
    private void appendPrivateMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            messagesArea.append(msg + "\n");
            messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
        });
    }

    // Met à jour la liste des utilisateurs connectés à partir d'un JSONArray
    private void updateUsersList(JSONArray users) {
        SwingUtilities.invokeLater(() -> {
            String previouslySelected = userList.getSelectedValue();

            userListModel.clear();
            for (int i = 0; i < users.length(); i++) {
                String user = users.optString(i);
                if (!userStatusMap.containsKey(user)) {
                    userStatusMap.put(user, "online");
                }
                // On n'affiche pas les utilisateurs "invisible" sauf moi-même
                String state = userStatusMap.get(user);
                if (!"invisible".equals(state) || user.equals(pseudo)) {
                    userListModel.addElement(user);
                }
            }
            userStatusMap.keySet().removeIf(u -> !userListModel.contains(u));

            if (previouslySelected != null && userListModel.contains(previouslySelected)) {
                userList.setSelectedValue(previouslySelected, true);
            } else {
                userList.clearSelection();
            }
        });
    }

    // Met à jour l'affichage de la liste des utilisateurs avec leur statut
    private void updateUserListDisplay() {
        SwingUtilities.invokeLater(() -> {
            String previouslySelected = userList.getSelectedValue();
            userListModel.clear();
            for (String user : userStatusMap.keySet()) {
                String state = userStatusMap.get(user);
                // On n'affiche pas les utilisateurs "invisible" sauf moi-même
                if (!"invisible".equals(state) || user.equals(pseudo)) {
                    userListModel.addElement(user);
                }
            }
            if (previouslySelected != null && userListModel.contains(previouslySelected)) {
                userList.setSelectedValue(previouslySelected, true);
            } else {
                userList.clearSelection();
            }
        });
    }

    // Envoie un message global ou privé selon la sélection
    private void sendMessage() {
        if (client == null)
            return;
        String messageText = messageField.getText().trim();
        if (messageText.isEmpty())
            return;

        JSONObject message = new JSONObject();
        message.put("type", "message");
        if (!"global".equals(selectedRecipient)) {
            message.put("subtype", "private");
            message.put("to", selectedRecipient);
        } else {
            message.put("subtype", "global");
            message.put("to", "global");
        }
        message.put("from", pseudo);
        message.put("content", messageText);

        client.send(message.toString());
        if (!"global".equals(selectedRecipient)) {
            appendPrivateMessage("[MP à " + selectedRecipient + "] : " + messageText);
        } else {
            appendMessage("[Moi] : " + messageText);
        }
        messageField.setText("");
    }

    // Affiche un message dans la zone de chat
    private void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            messagesArea.append(msg + "\n");
            messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
        });
    }

    // --- Classes/Interfaces internes ---

    /**
     * Renderer personnalisé pour la JList des utilisateurs, affiche l'icône de
     * statut.
     */
    private class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String user = value.toString();
            String state = userStatusMap.getOrDefault(user, "online");
            Color iconColor = ICON_ONLINE;
            switch (state) {
                case "away":
                    iconColor = ICON_AWAY;
                    break;
                case "dnd":
                    iconColor = ICON_DND;
                    break;
                case "invisible":
                    iconColor = ICON_INVISIBLE;
                    break;
            }
            label.setIcon(createStatusIcon(iconColor));
            label.setText(user + " (" + state + ")");
            return label;
        }
    }

    /**
     * Crée une petite icône ronde colorée pour le statut.
     */
    private Icon createStatusIcon(Color color) {
        int size = 10;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(color);
        g2.fillOval(0, 0, size, size);
        g2.dispose();
        return new ImageIcon(image);
    }

    /**
     * Point d'entrée de l'application pour lancer l'interface invité.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientInviteUI().setVisible(true));
    }
}