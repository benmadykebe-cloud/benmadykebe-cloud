package ca.kebe.centreti.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Ouverture de la connexion SQLite, création du schéma et chargement des données d'exemple. */
public final class Database {

    private Database() {
    }

    /** Ouvre une connexion et active les clés étrangères (désactivées par défaut dans SQLite). */
    public static Connection ouvrir(String url) throws SQLException {
        Connection connexion = DriverManager.getConnection(url);
        try (Statement s = connexion.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        }
        return connexion;
    }

    public static void initialiserSchema(Connection connexion) throws SQLException {
        executerScript(connexion, "/schema.sql");
    }

    public static void chargerDonneesExemple(Connection connexion) throws SQLException {
        executerScript(connexion, "/donnees-exemple.sql");
    }

    public static boolean aucunTicket(Connection connexion) throws SQLException {
        try (Statement s = connexion.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM ticket")) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    /** Exécute toutes les requêtes d'un script dans une seule transaction (tout ou rien). */
    private static void executerScript(Connection connexion, String ressource) throws SQLException {
        boolean autoCommitInitial = connexion.getAutoCommit();
        connexion.setAutoCommit(false);
        try (Statement s = connexion.createStatement()) {
            for (String requete : decouperScript(lireRessource(ressource))) {
                s.execute(requete);
            }
            connexion.commit();
        } catch (SQLException e) {
            connexion.rollback();
            throw e;
        } finally {
            connexion.setAutoCommit(autoCommitInitial);
        }
    }

    /**
     * Sépare un script SQL en requêtes : retire les lignes de commentaire ("--")
     * puis coupe sur les points-virgules. Les scripts du projet ne contiennent
     * aucun point-virgule à l'intérieur d'un texte.
     */
    public static List<String> decouperScript(String script) {
        StringBuilder sansCommentaires = new StringBuilder();
        for (String ligne : script.split("\\R")) {
            if (!ligne.strip().startsWith("--")) {
                sansCommentaires.append(ligne).append('\n');
            }
        }
        List<String> requetes = new ArrayList<>();
        for (String morceau : sansCommentaires.toString().split(";")) {
            String requete = morceau.strip();
            if (!requete.isEmpty()) {
                requetes.add(requete);
            }
        }
        return requetes;
    }

    /** Lit un fichier du classpath (src/main/resources) en UTF-8. */
    public static String lireRessource(String chemin) {
        try (InputStream in = Database.class.getResourceAsStream(chemin)) {
            if (in == null) {
                throw new IllegalStateException("Ressource introuvable : " + chemin);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Lecture impossible : " + chemin, e);
        }
    }
}
