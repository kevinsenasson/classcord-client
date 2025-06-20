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

# Jour 3 – Authentification et gestion des comptes utilisateurs

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

# Classcord - Jour 4

## Fonctionnalités implémentées

- Affichage dynamique de la liste des utilisateurs connectés dans l’interface Swing (panneau de droite).
- Mise à jour automatique de la liste lors des connexions/déconnexions et à intervalle régulier.
- Possibilité d’envoyer un message privé à un utilisateur sélectionné dans la liste.
- Distinction visuelle claire entre messages globaux et messages privés dans la zone de chat.
- Les messages privés ne sont visibles que par le destinataire concerné.

## Utilisation

1. Lancez l’application et connectez-vous.
2. La liste des utilisateurs connectés s’affiche à droite.
3. Sélectionnez un utilisateur pour lui envoyer un message privé, sinon envoyez un message global.
4. Les messages privés sont affichés avec un préfixe `[MP de ...]` ou `[MP à ...]`.

## Remarques techniques

- La liste des utilisateurs est synchronisée avec le serveur à chaque connexion et toutes les 5 secondes.
- La sélection d’un utilisateur pour les messages privés est conservée lors du rafraîchissement de la liste.

# ClassCord

## Jour 5 - Gestion des statuts et finalisation du projet

### Objectifs de la journée

- Permettre aux utilisateurs de choisir leur statut (disponible, absent, invisible).
- Envoyer ce statut au serveur et l'afficher dans la liste des connectés.
- Améliorer l'affichage graphique de l'application (interface Swing).
- Vérifier toutes les fonctionnalités du projet et corriger les bugs.
- Préparer les livrables techniques pour finaliser la réalisation professionnelle.

---

## Fonctionnalités principales

- **Gestion des statuts utilisateur** :  
  Les utilisateurs peuvent choisir leur statut (disponible, absent, ne pas déranger, invisible) via un menu déroulant.  
  Le statut est envoyé au serveur sous forme de message JSON :

  ```json
  {
    "type": "status",
    "state": "invisible"
  }
  ```

  L'affichage de la liste des connectés est mis à jour dynamiquement, avec une icône colorée pour chaque statut.

- **Interface graphique Swing améliorée** :  
  L'interface a été retravaillée pour être plus lisible, moderne et agréable à utiliser (alignements, couleurs, marges, icônes, réactivité).

- **Tests et robustesse** :  
  Les fonctionnalités ont été testées entre plusieurs clients, avec gestion des cas limites (déconnexion, reconnexion, mauvais identifiants, envoi vide, etc.).

- **Documentation** :  
  Le projet est documenté :
  - README.md (ce fichier)
  - Commentaires dans le code source

---

## Architecture du projet

Le projet suit une architecture **MVC** :

- **Model** : gestion des données utilisateur, statuts, messages (ex : `User.java`)
- **View** : interfaces graphiques Swing (`AuthUI.java`, `UserUI.java`, `ClientInviteUI.java`)
- **Controller** : logique de communication réseau et gestion des événements (`ClientInvite.java`)

---

## Instructions de lancement

1. **Lancer le serveur**  
   (localement si vous avez un serveur ou sur une autre machine)

2. **Lancer le client**

Exécuter directement la classe principale depuis votre IDE.

3. **Se connecter**

   - Avec un compte existant (login)
   - En créant un nouveau compte (register)
   - Ou en mode invité

4. **Utiliser l'application**
   - Envoyer des messages globaux ou privés
   - Changer de statut et observer la propagation
   - Voir la liste des connectés et leur statut

---

## Livrables attendus

- Application complète, testée, stable et fonctionnelle
- Interface Swing finale intégrant la gestion des statuts
- Dossier de documentation (README.md)
- Projet archivable (Github)

---

## Protocole de démonstration

1. Lancer le serveur (local ou distant)
2. Lancer deux clients (comptes différents ou mode invité)
3. Montrer :
   - Connexion (identifiant ou invité)
   - Envoi de message global
   - Envoi de message privé
   - Changement de statut et propagation
   - Affichage dynamique de la liste des connectés avec statut
   - Distinction claire entre message global et MP
   - Aucune erreur lors de la déconnexion

---

## Auteurs

- Kevin Senasson

---

## Remarques

- Le code source est commenté pour faciliter la compréhension.

---
