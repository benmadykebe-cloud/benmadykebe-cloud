package ca.kebe.centreti;

import ca.kebe.centreti.db.Database;
import java.sql.Connection;
import java.sql.SQLException;

/** Crée une base SQLite en mémoire avec le schéma et les données d'exemple. */
final class BaseDeTest {

    private BaseDeTest() {
    }

    static Connection nouvelle() throws SQLException {
        Connection connexion = Database.ouvrir("jdbc:sqlite::memory:");
        Database.initialiserSchema(connexion);
        Database.chargerDonneesExemple(connexion);
        return connexion;
    }
}
