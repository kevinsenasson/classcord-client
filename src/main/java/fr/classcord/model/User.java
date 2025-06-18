package fr.classcord.model;

import fr.classcord.network.ClientInvite;
import org.json.JSONObject;

public class User {
	private String username;
	private String status;

	public User(String username, String status) {
		this.username = username;
		this.status = status;
	}

	// Getters et Setters
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Méthode pour s'inscrire
	public static String register(ClientInvite client, String username, String password) throws Exception {
		JSONObject json = new JSONObject();
		json.put("type", "register");
		json.put("username", username);
		json.put("password", password);
		client.send(json.toString());
		String response = client.getIn().readLine();
		return response;
	}

	// Méthode pour se connecter
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