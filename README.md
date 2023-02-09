# Installation

### Pour chacun des projets, deux solutions sont possibles :

- Télécharger le projet ou clonez-le.

Une fois le projet récupéré :

- Lancer le projet avec la commande suivante : 
```java -jar Java_V3_Serveur-1.0-SNAPSHOT.jar```
*(Le fichier jar sera déplacé à la racine du projet pour éviter d'avoir à aller les chercher dans le dossier target)*

**OU**

- Ouvrir le projet sur IntelliJ et le lancer normalement.

Pour la base de données, les tables devraient se créer automatiquement au lancement du serveur si elles n'existent pas. Au cas où un problème empêcherait la création, nous fournissons un .sql contenant les tables.

# Utilisation

Une fois le serveur lancé,  des messages confirmant la création du socket et la connexion à la base de données devraient apparaitre. Les tables ainsi créées, vous pouvez le laisser tourner en fond et utiliser le [Client](https://github.com/ShAd-x/Java_client/tree/main) :

À chaque fois que le client communique avec le serveur, un message indiquant une connexion entrante devrait apparaitre dans la console.
