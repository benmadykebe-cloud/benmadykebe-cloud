package ca.kebe.centreti;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.kebe.centreti.model.Rapport;
import ca.kebe.centreti.ui.Tableau;
import java.util.List;
import org.junit.jupiter.api.Test;

class TableauTest {

    @Test
    void alignesLesColonnesSurLaCelluleLaPlusLarge() {
        Rapport rapport = new Rapport("Exemple", List.of("Nom", "Nb"),
                List.of(List.of("Alexandre", "12"), List.of("Zoé", "3")));

        String[] lignes = Tableau.formater(rapport).split("\n");

        // lignes[0] est vide, lignes[1] est le titre, puis séparateur, en-tête, séparateur, données
        assertEquals("Exemple", lignes[1]);
        assertEquals("+-----------+----+", lignes[2]);
        assertEquals("| Nom       | Nb |", lignes[3]);
        assertEquals("| Alexandre | 12 |", lignes[5]);
        assertEquals("| Zoé       | 3  |", lignes[6]);
    }

    @Test
    void indiqueQuAucunResultatEstTrouve() {
        Rapport vide = new Rapport("Vide", List.of("A"), List.of());
        assertTrue(Tableau.formater(vide).contains("Aucun résultat."));
    }

    @Test
    void coupeLesTextesTropLongs() {
        String tresLong = "x".repeat(100);
        Rapport rapport = new Rapport("Long", List.of("Texte"), List.of(List.of(tresLong)));
        assertTrue(Tableau.formater(rapport).contains("..."));
    }
}
