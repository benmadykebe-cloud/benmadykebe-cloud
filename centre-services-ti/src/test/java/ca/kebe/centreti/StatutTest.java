package ca.kebe.centreti;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.kebe.centreti.model.Statut;
import org.junit.jupiter.api.Test;

class StatutTest {

    @Test
    void cheminNormalEstAutorise() {
        assertTrue(Statut.OUVERT.peutPasserA(Statut.EN_COURS));
        assertTrue(Statut.EN_COURS.peutPasserA(Statut.RESOLU));
        assertTrue(Statut.RESOLU.peutPasserA(Statut.FERME));
    }

    @Test
    void reouvertureDepuisResoluEstAutorisee() {
        assertTrue(Statut.RESOLU.peutPasserA(Statut.EN_COURS));
    }

    @Test
    void sautsEtRetoursInterditsSontRefuses() {
        assertFalse(Statut.OUVERT.peutPasserA(Statut.RESOLU));
        assertFalse(Statut.OUVERT.peutPasserA(Statut.FERME));
        assertFalse(Statut.EN_COURS.peutPasserA(Statut.OUVERT));
        assertFalse(Statut.EN_COURS.peutPasserA(Statut.FERME));
    }

    @Test
    void ticketFermeNeBougePlus() {
        for (Statut cible : Statut.values()) {
            assertFalse(Statut.FERME.peutPasserA(cible));
        }
    }
}
