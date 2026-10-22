package ca.kebe.centreti.dao;

import ca.kebe.centreti.model.Rapport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accès aux tables de référence : utilisateurs, techniciens et équipements. */
public final class ReferenceDao {

    private final Connection connexion;

    public ReferenceDao(Connection connexion) {
        this.connexion = connexion;
    }

    public Rapport utilisateurs() throws SQLException {
        return RapportDao.executer(connexion, "Utilisateurs",
                "SELECT id AS \"N°\", nom AS \"Nom\", departement AS \"Département\" FROM utilisateur ORDER BY id");
    }

    public Rapport techniciens() throws SQLException {
        return RapportDao.executer(connexion, "Techniciens",
                "SELECT id AS \"N°\", nom AS \"Nom\", specialite AS \"Spécialité\" FROM technicien ORDER BY id");
    }

    public Rapport equipements() throws SQLException {
        return RapportDao.executer(connexion, "Équipements",
                "SELECT id AS \"N°\", type AS \"Type\", numero_serie AS \"Numéro de série\" FROM equipement ORDER BY id");
    }

    public boolean utilisateurExiste(int id) throws SQLException {
        return existe("SELECT 1 FROM utilisateur WHERE id = ?", id);
    }

    public boolean technicienExiste(int id) throws SQLException {
        return existe("SELECT 1 FROM technicien WHERE id = ?", id);
    }

    public boolean equipementExiste(int id) throws SQLException {
        return existe("SELECT 1 FROM equipement WHERE id = ?", id);
    }

    private boolean existe(String sql, int id) throws SQLException {
        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
