package ca.kebe.centreti;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.kebe.centreti.model.Priorite;
import ca.kebe.centreti.model.Statut;
import ca.kebe.centreti.model.Ticket;
import ca.kebe.centreti.service.RegleMetierException;
import ca.kebe.centreti.service.TicketService;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Les numéros de tickets, d'utilisateurs et de techniciens viennent de donnees-exemple.sql. */
class TicketServiceTest {

    private Connection connexion;
    private TicketService service;

    @BeforeEach
    void preparer() throws SQLException {
        connexion = BaseDeTest.nouvelle();
        service = new TicketService(connexion);
    }

    @AfterEach
    void nettoyer() throws SQLException {
        connexion.close();
    }

    @Test
    void cycleDeVieComplet() throws SQLException {
        int id = service.creerTicket("Souris sans fil muette", "Ne répond plus.", Priorite.BASSE, 1, null);
        assertEquals(Statut.OUVERT, service.chargerTicket(id).statut());

        service.assigner(id, 1);
        service.changerStatut(id, Statut.EN_COURS);
        service.ajouterIntervention(id, 1, 15, "Remplacement des piles");
        service.changerStatut(id, Statut.RESOLU);

        Ticket resolu = service.chargerTicket(id);
        assertEquals(Statut.RESOLU, resolu.statut());
        assertNotNull(resolu.dateResolution());

        service.changerStatut(id, Statut.FERME);
        Ticket ferme = service.chargerTicket(id);
        assertEquals(Statut.FERME, ferme.statut());
        assertEquals(resolu.dateResolution(), ferme.dateResolution());
    }

    @Test
    void refuseLaPriseEnChargeSansTechnicien() {
        // Le ticket 9 est OUVERT et n'a pas de technicien.
        assertThrows(RegleMetierException.class, () -> service.changerStatut(9, Statut.EN_COURS));
    }

    @Test
    void refuseUneTransitionInterdite() {
        assertThrows(RegleMetierException.class, () -> service.changerStatut(9, Statut.RESOLU));
    }

    @Test
    void reouvertureEffaceLaDateDeResolution() throws SQLException {
        // Le ticket 1 est RESOLU avec un technicien.
        service.changerStatut(1, Statut.EN_COURS);
        Ticket rouvert = service.chargerTicket(1);
        assertEquals(Statut.EN_COURS, rouvert.statut());
        assertNull(rouvert.dateResolution());
    }

    @Test
    void refuseUneInterventionSurUnTicketNonEnCours() {
        assertThrows(RegleMetierException.class,
                () -> service.ajouterIntervention(9, 1, 10, "Test"));
    }

    @Test
    void refuseUneInterventionParUnAutreTechnicien() {
        // Le ticket 5 est EN_COURS et assigné au technicien 1.
        assertThrows(RegleMetierException.class,
                () -> service.ajouterIntervention(5, 2, 10, "Test"));
    }

    @Test
    void refuseUneDureeNulle() {
        assertThrows(RegleMetierException.class,
                () -> service.ajouterIntervention(5, 1, 0, "Test"));
    }

    @Test
    void refuseUnTitreVideOuUnUtilisateurInconnu() {
        assertThrows(RegleMetierException.class,
                () -> service.creerTicket("  ", "", Priorite.NORMALE, 1, null));
        assertThrows(RegleMetierException.class,
                () -> service.creerTicket("Titre", "", Priorite.NORMALE, 999, null));
    }

    @Test
    void refuseUnTicketInexistant() {
        assertThrows(RegleMetierException.class, () -> service.chargerTicket(999));
    }

    @Test
    void laBaseRefuseUnEtatIncoherentMemeSansPasserParLeService() {
        // Le ticket 8 est OUVERT sans technicien : le CHECK du schéma interdit EN_COURS.
        assertThrows(SQLException.class, () -> {
            try (Statement s = connexion.createStatement()) {
                s.executeUpdate("UPDATE ticket SET statut = 'EN_COURS' WHERE id = 8");
            }
        });
    }
}
