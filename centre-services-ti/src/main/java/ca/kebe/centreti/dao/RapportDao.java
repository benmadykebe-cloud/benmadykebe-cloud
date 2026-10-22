package ca.kebe.centreti.dao;

import ca.kebe.centreti.db.Database;
import ca.kebe.centreti.model.Rapport;
import ca.kebe.centreti.model.TypeRapport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Exécute les rapports SQL et transforme n'importe quel résultat en {@link Rapport}. */
public final class RapportDao {

    private final Connection connexion;

    public RapportDao(Connection connexion) {
        this.connexion = connexion;
    }

    /** Lit le fichier SQL du rapport, l'exécute et renvoie le résultat. */
    public Rapport generer(TypeRapport type, Object... parametres) throws SQLException {
        String sql = Database.decouperScript(Database.lireRessource("/rapports/" + type.fichier())).get(0);
        return executer(connexion, type.titre(), sql, parametres);
    }

    /** Exécute une requête SELECT avec des paramètres (PreparedStatement : pas d'injection SQL). */
    static Rapport executer(Connection connexion, String titre, String sql, Object... parametres)
            throws SQLException {
        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            for (int i = 0; i < parametres.length; i++) {
                ps.setObject(i + 1, parametres[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int nombreColonnes = meta.getColumnCount();
                List<String> colonnes = new ArrayList<>();
                for (int i = 1; i <= nombreColonnes; i++) {
                    colonnes.add(meta.getColumnLabel(i));
                }
                List<List<String>> lignes = new ArrayList<>();
                while (rs.next()) {
                    List<String> ligne = new ArrayList<>();
                    for (int i = 1; i <= nombreColonnes; i++) {
                        String valeur = rs.getString(i);
                        ligne.add(valeur == null ? "-" : valeur);
                    }
                    lignes.add(ligne);
                }
                return new Rapport(titre, colonnes, lignes);
            }
        }
    }
}
