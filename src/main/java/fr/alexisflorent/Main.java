package fr.alexisflorent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Connexion à la base de données
        Connection con = connectToDatabase();

        // Création des tables si elles n'existent pas
        createTable(con);

        // Création d'un socket serveur
        int port = 5555;
        ServerSocket serverSocket = null;
        try {
            System.out.println("Création du socket réussi");
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Création du socket échouée");
            e.printStackTrace();
            return;
        }

        // Boucle infinie pour ne pas arrêter le serveur
        while (true) {
            try {
                // Attente d'une connexion entrante
                Socket socket = serverSocket.accept();
                System.out.println("Connexion entrante reçue de " + socket.getInetAddress().getHostAddress());

                // Réception des données
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object livres = ois.readObject();
                Object lecteurs = ois.readObject();

                ArrayList<String[]> livresList = (ArrayList<String[]>) livres;
                // Pour chaque livre dans la liste
                for (String[] livre : livresList) {
                    // On récupère les données : titre, auteur, nbPages
                    String titre = livre[0];
                    String auteur = livre[1];
                    int nbPages = 0;
                    try {
                        nbPages = Integer.parseInt(livre[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Le nombre de pages n'est pas un nombre, mais le livre est quand même enregistré (pages = 0)");
                    }
                    // On enregistre le livre dans la base de données
                    String sql = "INSERT INTO livres (titre, auteur, nb_pages) VALUES (?, ?, ?)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, titre);
                    statement.setString(2, auteur);
                    statement.setInt(3, nbPages);
                    statement.executeUpdate();
                }

                ArrayList<String[]> lecteursList = (ArrayList<String[]>) lecteurs;
                // Pour chaque lecteur dans la liste
                for (String[] lecteur : lecteursList) {
                    // On récupère les données : nom, prenom, email
                    String nom = lecteur[0];
                    String prenom = lecteur[1];
                    String email = lecteur[2];
                    // On enregistre le lecteur dans la base de données
                    String sql = "INSERT INTO lecteurs (nom, prenom, email) VALUES (?, ?, ?)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, nom);
                    statement.setString(2, prenom);
                    statement.setString(3, email);
                    statement.executeUpdate();
                }

                // Fermeture de la connexion
                ois.close();
                socket.close();
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connexion à la base de données
     * @return Connection
     */
    private static Connection connectToDatabase() {
        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/mabd";
        String user = "root";
//        String password = "root";
        String password = "iut";
        Connection con;
        try {
            System.out.println("Connexion à la base de données réussi");
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Connexion à la base de données échouée");
            e.printStackTrace();
            return null;
        }
        return con;
    }

    /**
     * Création des tables si elles n'existent pas
     * @param con Connection
     * @return void
     */
    private static void createTable(Connection con) {
        String sqlLivres = "CREATE TABLE IF NOT EXISTS livres " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "titre VARCHAR(255), " +
                "auteur VARCHAR(255), " +
                "nb_pages INT, PRIMARY KEY (id))";

        String sqlLecteurs = "CREATE TABLE IF NOT EXISTS lecteurs " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "nom VARCHAR(255), " +
                "prenom VARCHAR(255), " +
                "email VARCHAR(255), PRIMARY KEY (id))";

        try {
            con.prepareStatement(sqlLivres).executeUpdate();
            con.prepareStatement(sqlLecteurs).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}