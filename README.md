# Kevin Senasson

# Projet Classcord - Jour 1

### Fonctionnalités ajoutées

- Création de la classe `User` avec les attributs `username` et `status`
- Création de la classe `Message` avec les attributs `type`, `subtype`, `from`, `to`, `content`, `timestamp`
- Ajout des constructeurs pour chaque classe
- Implémentation de la méthode `toString` pour faciliter l’affichage des objets
- Réalisation de premiers tests simples pour vérifier le bon fonctionnement des méthodes `toString`

- **Classe `Message`**

  - Attributs publics : `type`, `subtype`, `from`, `to`, `content`, `timestamp`
  - Constructeur pour initialiser tous les attributs
  - (À compléter : méthodes pour manipuler les messages)

- **Tests**

  - Vérification de l’affichage des objets `Message` dans la console

**Résumé du jour 1**

- Création des classes principales
- Mise en place des constructeurs et méthodes de base
- petit test de fonctionnement

---

## Comment lancer le projet

1. Play sur vs code et affichage dans la console

# 📖 Jour 2 – Connexion au serveur et tchat en mode invité

## Objectifs de la journée

- Permettre à l'utilisateur de se connecter au serveur en entrant une IP et un port.
- Envoyer des messages au serveur en tant qu'invité (sans authentification).
- Recevoir des messages en temps réel depuis le serveur.
- Afficher les messages entrants dans la console ou dans une interface graphique Swing.

---

## Fonctionnalités réalisées

- Création de la classe `ClientInvite` dans le package `fr.classcord.network`.
- Connexion au serveur via une socket TCP.
- Utilisation d’un `PrintWriter` pour envoyer des messages et d’un `BufferedReader` pour recevoir les messages.
- Méthode `connect(String ip, int port)` pour initier la connexion.
- Méthode `send(String message)` pour envoyer un message JSON.
- Création et envoi d’objets JSON avec `org.json.JSONObject` :
  ```java
  JSONObject message = new JSONObject();
  message.put("type", "message");
  message.put("subtype", "global");
  message.put("to", "global");
  message.put("from", pseudo);
  message.put("content", messageText);
  ```
- Envoi du message via le `PrintWriter`, suivi d’un saut de ligne.
- Réception des messages dans un thread secondaire indépendant du thread principal, pour garantir la réactivité de l’interface.
- Affichage asynchrone et clair de chaque message reçu (pseudo + contenu) dans la console ou dans la fenêtre Swing.
- Interface graphique Swing sombre et moderne :
  - Champs pour l’IP, le port, le pseudo, bouton de connexion.
  - Zone d’affichage des messages.
  - Champ de saisie et bouton pour envoyer un message.

---

## Exigences techniques respectées

- Le thread réseau de réception est indépendant du thread principal.
- Aucun blocage ne fige l’application.
- Affichage clair et formaté de chaque message reçu.

---

## Bonus réalisés

- Encapsulation de la logique JSON dans la classe `Message` avec les méthodes `toJson()` et `fromJson(String)`.
- Interface graphique Swing complète et agréable à utiliser.

---

## Remarques

- Le serveur doit être lancé avant de connecter les clients.
- Plusieurs clients peuvent se connecter et échanger des messages en temps réel.
- Les messages sont affichés avec le pseudo de l’expéditeur pour plus de clarté.

## Jour 3 – Authentification et gestion des comptes utilisateurs

### Fonctionnalités implémentées

- **Connexion au serveur** :  
  L’utilisateur saisit l’adresse IP et le port du serveur. Une fois connecté, il accède à l’interface d’authentification.

- **Inscription et connexion** :  
  L’utilisateur peut créer un compte (inscription) ou se connecter avec un compte existant.  
  Lors de l’inscription, les informations sont envoyées au serveur sous forme de message JSON :

  ```json
  {
    "type": "register",
    "username": "alice",
    "password": "azerty"
  }
  ```

  Après une inscription réussie, le client effectue automatiquement la connexion :

  ```json
  {
    "type": "login",
    "username": "alice",
    "password": "azerty"
  }
  ```

- **Gestion des réponses serveur** :

  - En cas de succès, l’utilisateur accède à la fenêtre de chat.
  - En cas d’erreur (ex : pseudo déjà utilisé), un message d’erreur s’affiche et l’utilisateur peut réessayer.

- **Mode invité** :  
  L’utilisateur peut accéder au chat sans compte via le bouton « Mode invité ».

- **Sécurité et confort** :
  - Le mot de passe est masqué dans l’interface.
  - Le dernier pseudo utilisé est mémorisé localement pour faciliter la reconnexion.

### Protocole d’échange

- Les échanges avec le serveur se font au format JSON, conformément au cahier des charges.

### Lancement

1. Lancer l’application via la classe `UserLM`.
2. Se connecter au serveur (IP/port).
3. S’authentifier (inscription + connexion automatique, ou connexion directe, ou mode invité).
4. Accéder à la fenêtre de chat.
