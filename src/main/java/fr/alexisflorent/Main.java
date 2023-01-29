package fr.alexisflorent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "password";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Connexion à la base de données échouée");
            e.printStackTrace();
            return;
        }

        // Création d'un socket serveur
        int port = 5555;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Création du socket échouée");
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                // Attente d'une connexion entrante
                Socket socket = serverSocket.accept();
                System.out.println("Connexion entrante reçue de " + socket.getInetAddress().getHostAddress());

                // Réception des données
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object object = ois.readObject();

                // Stockage des données dans la base de données
                // if (object instanceof Book) {
                     String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
                     PreparedStatement statement = con.prepareStatement(sql);
                //     statement.setString(1, "booktitle");
                //     statement.setString(2, "bookauthor");
                     statement.executeUpdate();
                // } else if (object instanceof Reader) {
                //     Reader reader = (Reader) object;
                //     String sql = "INSERT INTO readers (name, email) VALUES (?, ?)";
                //     PreparedStatement statement = con.prepareStatement(sql);
                //     statement.setString(1, "lecteurname");
                //     statement.setString(2, "lecteurmail");
                //     statement.executeUpdate();
                // }

                // Fermeture de la connexion
                ois.close();
                socket.close();
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}