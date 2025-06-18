package fr.classcord.ui;

import fr.classcord.network.ClientInvite;
import fr.classcord.model.User;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserUI extends JFrame {
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("Se connecter");
    private JButton registerButton = new JButton("S'inscrire");
    private JButton inviteButton = new JButton("Mode invité");
    private JLabel messageLabel = new JLabel(" ");
    private ClientInvite client;

    public UserUI(ClientInvite client) {
        this.client = client;
        setTitle("Classcord - Authentification");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 270);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(44, 54, 63));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nom d'utilisateur :");
        userLabel.setForeground(Color.WHITE);
        JLabel passLabel = new JLabel("Mot de passe :");
        passLabel.setForeground(Color.WHITE);
        messageLabel.setForeground(Color.RED);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 54, 63));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(inviteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Bonus : mémoriser le dernier pseudo utilisé
        String lastUser = readLastUsername();
        if (!lastUser.isEmpty())
            usernameField.setText(lastUser);

        loginButton.addActionListener(e -> handleAuth("login"));
        registerButton.addActionListener(e -> handleRegisterThenLogin());
        inviteButton.addActionListener(e -> openInviteMode());
    }

    private void handleAuth(String type) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setText("Vérification en cours...");
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        new Thread(() -> {
            try {
                String response = User.login(client, username, password);
                JSONObject resp = new JSONObject(response);
                if (resp.optString("status").equals("ok")) {
                    saveLastUsername(username);
                    SwingUtilities.invokeLater(() -> {
                        messageLabel.setForeground(new Color(0, 128, 0));
                        messageLabel.setText("Bienvenue " + username + " !");
                        openChatWindow(username, client);
                        dispose();
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(resp.optString("error", "Erreur inconnue"));
                        loginButton.setEnabled(true);
                        registerButton.setEnabled(true);
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Erreur : " + ex.getMessage());
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                });
            }
        }).start();
    }

    // Register puis login automatique si succès
    private void handleRegisterThenLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setText("Inscription en cours...");
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        new Thread(() -> {
            try {
                String registerResponse = User.register(client, username, password);
                JSONObject registerResp = new JSONObject(registerResponse);
                if (registerResp.optString("status").equals("ok")) {
                    // Inscription réussie, on fait le login
                    SwingUtilities.invokeLater(() -> messageLabel.setText("Inscription réussie, connexion..."));
                    String loginResponse = User.login(client, username, password);
                    JSONObject loginResp = new JSONObject(loginResponse);
                    if (loginResp.optString("status").equals("ok")) {
                        saveLastUsername(username);
                        SwingUtilities.invokeLater(() -> {
                            messageLabel.setForeground(new Color(0, 128, 0));
                            messageLabel.setText("Bienvenue " + username + " !");
                            openChatWindow(username, client);
                            dispose();
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(loginResp.optString("error", "Erreur inconnue"));
                            loginButton.setEnabled(true);
                            registerButton.setEnabled(true);
                        });
                    }
                } else {
                    // Erreur à l'inscription
                    SwingUtilities.invokeLater(() -> {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(registerResp.optString("error", "Erreur inconnue"));
                        loginButton.setEnabled(true);
                        registerButton.setEnabled(true);
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Erreur : " + ex.getMessage());
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void openInviteMode() {
        dispose();
        new ClientInviteUI().setVisible(true);
    }

    private void openChatWindow(String username, ClientInvite client) {
        new ClientInviteUI(client, username).setVisible(true);
    }

    // Bonus : mémoriser le dernier pseudo utilisé
    private void saveLastUsername(String username) {
        try (FileWriter fw = new FileWriter("lastuser.txt")) {
            fw.write(username);
        } catch (IOException ignored) {
        }
    }

    private String readLastUsername() {
        try (BufferedReader br = new BufferedReader(new FileReader("lastuser.txt"))) {
            return br.readLine();
        } catch (IOException e) {
            return "";
        }
    }
}