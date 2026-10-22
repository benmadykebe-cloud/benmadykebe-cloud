package ca.kebe.centreti.model;

/**
 * Un ticket de support. Les champs optionnels (technicien, équipement, date de résolution)
 * valent null tant qu'ils ne sont pas connus.
 */
public record Ticket(
        int id,
        String titre,
        String description,
        Priorite priorite,
        Statut statut,
        String dateCreation,
        String dateResolution,
        int utilisateurId,
        Integer technicienId,
        Integer equipementId) {
}
