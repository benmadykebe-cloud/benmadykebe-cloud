package ca.kebe.centreti.dao;

import ca.kebe.centreti.model.Priorite;
import ca.kebe.centreti.model.Rapport;
import ca.kebe.centreti.model.Statut;
import ca.kebe.centreti.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

/** Accès à la table ticket : création, lecture, assignation et changement de statut. */
public final class TicketDao {

    private static final String LISTE = """
            SELECT k.id AS "N°", k.titre AS "Titre", k.priorite AS "Priorité", k.statut AS "Statut",
                   u.nom AS "Demandeur", COALESCE(t.nom, '-') AS "Technicien", k.date_creation AS "Créé (UTC)"
            FROM ticket k
            JOIN utilisateur u ON u.id = k.utilisateur_id
            LEFT JOIN technicien t ON t.id = k.technicien_id
            """;

    private final Connection connexion;

    public TicketDao(Connection connexion) {
        this.connexion = connexion;
    }

    /** Insère un ticket (statut OUVERT par défaut) et renvoie son numéro. */
    public int creer(String titre, String description, Priorite priorite, int utilisateurId,
                     Integer equipementId, String dateCreation) throws SQLException {
        String sql = "INSERT INTO ticket (titre, description, priorite, date_creation, utilisateur_id, equipement_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, titre);
            ps.setString(2, description);
            ps.setString(3, priorite.name());
            ps.setString(4, dateCreation);
            ps.setInt(5, utilisateurId);
            definirEntierOptionnel(ps, 6, equipementId);
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                cles.next();
                return cles.getInt(1);
            }
        }
    }

    public Optional<Ticket> trouver(int id) throws SQLException {
        try (PreparedStatement ps = connexion.prepareStatement("SELECT * FROM ticket WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Ticket(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        Priorite.valueOf(rs.getString("priorite")),
                        Statut.valueOf(rs.getString("statut")),
                        rs.getString("date_creation"),
                        rs.getString("date_resolution"),
                        rs.getInt("utilisateur_id"),
                        lireEntierOptionnel(rs, "technicien_id"),
                        lireEntierOptionnel(rs, "equipement_id")));
            }
        }
    }

    /** Liste les tickets, tous ou seulement ceux d'un statut (filtre null = tous). */
    public Rapport lister(Statut filtre) throws SQLException {
        if (filtre == null) {
            return RapportDao.executer(connexion, "Tickets", LISTE + "ORDER BY k.id");
        }
        return RapportDao.executer(connexion, "Tickets au statut " + filtre,
                LISTE + "WHERE k.statut = ? ORDER BY k.id", filtre.name());
    }

    public void assigner(int ticketId, int technicienId) throws SQLException {
        try (PreparedStatement ps = connexion.prepareStatement("UPDATE ticket SET technicien_id = ? WHERE id = ?")) {
            ps.setInt(1, technicienId);
            ps.setInt(2, ticketId);
            ps.executeUpdate();
        }
    }

    /** Change le statut et la date de résolution (null = pas de date de résolution). */
    public void mettreAJourStatut(int ticketId, Statut statut, String dateResolution) throws SQLException {
        String sql = "UPDATE ticket SET statut = ?, date_resolution = ? WHERE id = ?";
        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            if (dateResolution == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, dateResolution);
            }
            ps.setInt(3, ticketId);
            ps.executeUpdate();
        }
    }

    private static void definirEntierOptionnel(PreparedStatement ps, int position, Integer valeur)
            throws SQLException {
        if (valeur == null) {
            ps.setNull(position, Types.INTEGER);
        } else {
            ps.setInt(position, valeur);
        }
    }

    private static Integer lireEntierOptionnel(ResultSet rs, String colonne) throws SQLException {
        int valeur = rs.getInt(colonne);
        return rs.wasNull() ? null : valeur;
    }
}
