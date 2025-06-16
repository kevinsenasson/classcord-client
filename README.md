# Projet Classcord - Jour 1

## Fonctionnalités ajoutées

- **Classe `User`**

  - Attributs privés : `username`, `status`
  - Constructeur pour initialiser les attributs
  - Méthode `toString` pour un affichage lisible de l’utilisateur

- **Classe `Message`**

  - Attributs publics : `type`, `subtype`, `from`, `to`, `content`, `timestamp`
  - Constructeur pour initialiser tous les attributs
  - (À compléter : méthodes pour manipuler les messages)

- **Tests**
  - Création d’une classe de test `UserTest` avec deux tests simples sur la méthode `toString`
  - Vérification de l’affichage des objets `User` dans la console
  - Test manuel possible via la méthode `main` dans la classe `User`

## Ce qui reste à faire

- Ajouter des méthodes pour manipuler les messages
- Créer des interactions entre utilisateurs et messages
- Ajouter des tests unitaires pour la classe `Message`
- Améliorer la gestion des statuts utilisateurs

---

**Résumé du jour 1**

- Création des classes principales
- Mise en place des constructeurs et méthodes de base
- Premiers tests unitaires et vérifications manuelles

---

## Comment lancer le projet

1. Compiler le projet avec Maven :
   ```
   mvn compile
   ```
2. Lancer les tests :
   ```
   mvn test
   ```
