package ca.kebe.centreti;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.kebe.centreti.dao.RapportDao;
import ca.kebe.centreti.model.Rapport;
import ca.kebe.centreti.model.TypeRapport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Vérifie les rapports SQL sur les données d'exemple (dates relatives à l'heure de chargement). */
class RapportDaoTest {

    private Connection connexion;
    private RapportDao dao;

    @BeforeEach
    void preparer() throws SQLException {
        connexion = BaseDeTest.nouvelle();
        dao = new RapportDao(connexion);
    }

    @AfterEach
    void nettoyer() throws SQLException {
        connexion.close();
    }

    @Test
    void ticketsOuvertsParPrioriteDuPlusUrgentAuMoins() throws SQLException {
        Rapport r = dao.generer(TypeRapport.TICKETS_PAR_PRIORITE);
        assertEquals(List.of(
                List.of("CRITIQUE", "1"),
                List.of("HAUTE", "2"),
                List.of("NORMALE", "3"),
                List.of("BASSE", "1")), r.lignes());
    }

    @Test
    void delaiMoyenParTechnicienTrieDuPlusRapide() throws SQLException {
        Rapport r = dao.generer(TypeRapport.DELAI_MOYEN);
        assertEquals(3, r.lignes().size());
        assertEquals("Sofia Gagnon", r.lignes().get(0).get(0));
        assertEquals("Alex Roy", r.lignes().get(1).get(0));
        assertEquals("5", r.lignes().get(1).get(1));
        assertEquals("Karim Diallo", r.lignes().get(2).get(0));
    }

    @Test
    void equipementLePlusEnPanneEstLImprimante() throws SQLException {
        Rapport r = dao.generer(TypeRapport.TOP_EQUIPEMENTS);
        assertEquals(5, r.lignes().size());
        assertEquals(List.of("IMPRIMANTE", "IMP-2020-002", "3"), r.lignes().get(0));
    }

    @Test
    void chargeDesTechniciensFiltreParLeSeuil() throws SQLException {
        Rapport r = dao.generer(TypeRapport.CHARGE_TECHNICIEN, 180);
        assertEquals(1, r.lignes().size());
        assertEquals("Sofia Gagnon", r.lignes().get(0).get(0));
        assertEquals("395", r.lignes().get(0).get(2));

        Rapport tous = dao.generer(TypeRapport.CHARGE_TECHNICIEN, 0);
        assertEquals(3, tous.lignes().size());
    }

    @Test
    void deuxTicketsSontEnRetard() throws SQLException {
        Rapport r = dao.generer(TypeRapport.TICKETS_EN_RETARD);
        Set<String> numeros = new HashSet<>();
        for (List<String> ligne : r.lignes()) {
            numeros.add(ligne.get(0));
        }
        assertEquals(Set.of("6", "16"), numeros);
    }

    @Test
    void ticketsParDepartementTotalisentSeizeTickets() throws SQLException {
        Rapport r = dao.generer(TypeRapport.TICKETS_PAR_DEPARTEMENT);
        assertEquals("Ventes", r.lignes().get(0).get(0));
        int total = 0;
        for (List<String> ligne : r.lignes()) {
            total += Integer.parseInt(ligne.get(1));
        }
        assertEquals(16, total);
    }
}
