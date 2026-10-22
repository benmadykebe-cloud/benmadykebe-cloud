package ca.kebe.centreti.service;

import ca.kebe.centreti.dao.InterventionDao;
import ca.kebe.centreti.dao.ReferenceDao;
import ca.kebe.centreti.dao.TicketDao;
import ca.kebe.centreti.model.Priorite;
import ca.kebe.centreti.model.Statut;
import ca.kebe.centreti.model.Ticket;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Règles de gestion des tickets. Les DAO s'occupent du SQL ; ce service décide
 * ce qui est permis (transitions de statut, technicien obligatoire, etc.).
 */
public final class TicketService {

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TicketDao tickets;
    private final InterventionDao interventions;
    private final ReferenceDao references;
    private final Clock horloge;

    public TicketService(Connection connexion) {
        this(connexion, Clock.systemUTC());
    }

    public TicketService(Connection connexion, Clock horloge) {
        this.tickets = new TicketDao(connexion);
        this.interventions = new InterventionDao(connexion);
        this.references = new ReferenceDao(connexion);
        this.horloge = horloge;
    }

    /** Crée un ticket au statut OUVERT et renvoie son numéro. */
    public int creerTicket(String titre, String description, Priorite priorite,
                           int utilisateurId, Integer equipementId) throws SQLException {
        if (titre == null || titre.isBlank()) {
            throw new RegleMetierException("Le titre est obligatoire.");
        }
        if (priorite == null) {
            throw new RegleMetierException("La priorité est obligatoire.");
        }
        if (!references.utilisateurExiste(utilisateurId)) {
            throw new RegleMetierException("Utilisateur introuvable : " + utilisateurId);
        }
        if (equipementId != null && !references.equipementExiste(equipementId)) {
            throw new RegleMetierException("Équipement introuvable : " + equipementId);
        }
        String texte = description == null ? "" : description.strip();
        return tickets.creer(titre.strip(), texte, priorite, utilisateurId, equipementId, maintenant());
    }

    /** Assigne un technicien à un ticket encore actif (ouvert ou en cours). */
    public void assigner(int ticketId, int technicienId) throws SQLException {
        Ticket ticket = chargerTicket(ticketId);
        if (ticket.statut() == Statut.RESOLU || ticket.statut() == Statut.FERME) {
            throw new RegleMetierException("Impossible d'assigner un ticket " + ticket.statut() + ".");
        }
        if (!references.technicienExiste(technicienId)) {
            throw new RegleMetierException("Technicien introuvable : " + technicienId);
        }
        tickets.assigner(ticketId, technicienId);
    }

    /** Change le statut en respectant {@link Statut#peutPasserA(Statut)}. */
    public void changerStatut(int ticketId, Statut cible) throws SQLException {
        Ticket ticket = chargerTicket(ticketId);
        if (!ticket.statut().peutPasserA(cible)) {
            throw new RegleMetierException(
                    "Passage interdit : " + ticket.statut() + " vers " + cible + ".");
        }
        if (cible == Statut.EN_COURS && ticket.technicienId() == null) {
            throw new RegleMetierException("Assignez un technicien avant de prendre le ticket en charge.");
        }
        String dateResolution = switch (cible) {
            case RESOLU -> maintenant();
            case FERME -> ticket.dateResolution(); // la date de résolution est conservée
            default -> null;                       // réouverture : plus de date de résolution
        };
        tickets.mettreAJourStatut(ticketId, cible, dateResolution);
    }

    /** Enregistre du temps de travail : seulement sur un ticket en cours et par son technicien. */
    public void ajouterIntervention(int ticketId, int technicienId, int minutes, String note)
            throws SQLException {
        Ticket ticket = chargerTicket(ticketId);
        if (ticket.statut() != Statut.EN_COURS) {
            throw new RegleMetierException("Le ticket doit être EN_COURS (statut actuel : " + ticket.statut() + ").");
        }
        if (ticket.technicienId() == null || ticket.technicienId() != technicienId) {
            throw new RegleMetierException("Seul le technicien assigné peut ajouter une intervention.");
        }
        if (minutes <= 0) {
            throw new RegleMetierException("La durée doit être supérieure à 0 minute.");
        }
        if (note == null || note.isBlank()) {
            throw new RegleMetierException("La note est obligatoire.");
        }
        interventions.ajouter(ticketId, technicienId, minutes, note.strip(), maintenant());
    }

    public Ticket chargerTicket(int ticketId) throws SQLException {
        return tickets.trouver(ticketId)
                .orElseThrow(() -> new RegleMetierException("Ticket introuvable : " + ticketId));
    }

    private String maintenant() {
        return LocalDateTime.now(horloge).format(FORMAT_DATE);
    }
}
