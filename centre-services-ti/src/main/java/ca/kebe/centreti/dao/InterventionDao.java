package ca.kebe.centreti.dao;

import ca.kebe.centreti.model.Rapport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Accès à la table intervention : le travail réalisé par un technicien sur un ticket. */
public final class InterventionDao {

    private final Connection connexion;

    public InterventionDao(Connection connexion) {
        this.connexion = connexion;
    }

    public void ajouter(int ticketId, int technicienId, int minutes, String note, String date)
            throws SQLException {
        String sql = "INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.setInt(2, technicienId);
            ps.setInt(3, minutes);
            ps.setString(4, note);
            ps.setString(5, date);
            ps.executeUpdate();
        }
    }

    public Rapport lister(int ticketId) throws SQLException {
        String sql = """
                SELECT i.date_intervention AS "Date (UTC)", t.nom AS "Technicien",
                       i.minutes AS "Minutes", i.note AS "Note"
                FROM intervention i
                JOIN technicien t ON t.id = i.technicien_id
                WHERE i.ticket_id = ?
                ORDER BY i.date_intervention
                """;
        return RapportDao.executer(connexion, "Interventions du ticket " + ticketId, sql, ticketId);
    }
}
