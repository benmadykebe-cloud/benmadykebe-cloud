package ca.kebe.centreti;

import ca.kebe.centreti.db.Database;
import ca.kebe.centreti.ui.Console;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

/** Point d'entrée : java -jar centre-services-ti.jar [--demo] [--base fichier.db] */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        String fichier = "centre_ti.db";
        boolean demo = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--demo")) {
                demo = true;
            } else if (args[i].equals("--base") && i + 1 < args.length) {
                fichier = args[++i];
            } else {
                System.out.println("Usage : java -jar centre-services-ti.jar [--demo] [--base fichier.db]");
                return;
            }
        }

        // Affichage en UTF-8 pour que les accents s'affichent correctement.
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        try (Connection connexion = Database.ouvrir("jdbc:sqlite:" + fichier)) {
            Database.initialiserSchema(connexion);
            if (demo && Database.aucunTicket(connexion)) {
                Database.chargerDonneesExemple(connexion);
                System.out.println("Données d'exemple chargées dans " + fichier + ".");
            }
            new Console(connexion).lancer();
        } catch (SQLException e) {
            System.err.println("Erreur de base de données : " + e.getMessage());
            System.exit(1);
        }
    }
}
