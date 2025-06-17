package fr.classcord.model;

import org.json.JSONObject;

public class Message {
	private String type;
	private String subtype;
	private String from;
	private String to;
	private String content;
	private String timestamp;

	public Message(String type, String subtype, String from, String to, String content, String timestamp) {
		this.type = type;
		this.subtype = subtype;
		this.from = from;
		this.to = to;
		this.content = content;
		this.timestamp = timestamp;
	}

	// Getters
	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getContent() {
		return content;
	}

	public String getTimestamp() {
		return timestamp;
	}

	// Setters
	public void setType(String type) {
		this.type = type;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	// Convertit l'objet Message en JSONObject
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("subtype", subtype);
		json.put("from", from);
		json.put("to", to);
		json.put("content", content);
		json.put("timestamp", timestamp);
		return json;
	}

	// Crée un objet Message à partir d'une chaîne JSON
	public static Message fromJson(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		return new Message(
				json.optString("type", null),
				json.optString("subtype", null),
				json.optString("from", null),
				json.optString("to", null),
				json.optString("content", null),
				json.optString("timestamp", null));
	}

	@Override
	public String toString() {
		return "[" + from + " → " + to + "] : " + content;
	}
}