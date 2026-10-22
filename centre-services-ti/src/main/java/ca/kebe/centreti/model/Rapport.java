package ca.kebe.centreti.model;

import java.util.List;

/**
 * Résultat d'une requête SQL prêt à afficher : un titre, des noms de colonnes
 * et des lignes de texte. Utilisé pour les listes et les rapports.
 */
public record Rapport(String titre, List<String> colonnes, List<List<String>> lignes) {

    public boolean estVide() {
        return lignes.isEmpty();
    }
}
