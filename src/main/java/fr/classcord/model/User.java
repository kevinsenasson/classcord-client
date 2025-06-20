package fr.classcord.model;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;

/**
 * Classe représentant un utilisateur de l'application.
 */
public class User {

	// --- Attributs ---
	private String username; // Nom d'utilisateur
	private String status; // Statut de l'utilisateur

	// --- Constructeurs ---

	/**
	 * Constructeur User.
	 * 
	 * @param username Nom d'utilisateur
	 * @param status   Statut de l'utilisateur
	 */
	public User(String username, String status) {
		this.username = username;
		this.status = status;
	}

	// --- Méthodes ---

	// Getters et Setters

	/**
	 * Retourne le nom d'utilisateur.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Définit le nom d'utilisateur.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Retourne le statut de l'utilisateur.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Définit le statut de l'utilisateur.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	// Méthodes statiques pour l'inscription et la connexion

	/**
	 * Méthode pour inscrire un nouvel utilisateur.
	 * 
	 * @param client   Instance du client réseau
	 * @param username Nom d'utilisateur
	 * @param password Mot de passe
	 * @return Réponse du serveur
	 */
	public static String register(ClientInvite client, String username, String password) throws Exception {
		JSONObject json = new JSONObject();
		json.put("type", "register");
		json.put("username", username);
		json.put("password", password);
		client.send(json.toString());
		String response = client.getIn().readLine();
		return response;
	}

	/**
	 * Méthode pour connecter un utilisateur existant.
	 * 
	 * @param client   Instance du client réseau
	 * @param username Nom d'utilisateur
	 * @param password Mot de passe
	 * @return Réponse du serveur
	 */
	public static String login(ClientInvite client, String username, String password) throws Exception {
		JSONObject json = new JSONObject();
		json.put("type", "login");
		json.put("username", username);
		json.put("password", password);
		client.send(json.toString());
		String response = client.getIn().readLine();
		return response;
	}
}