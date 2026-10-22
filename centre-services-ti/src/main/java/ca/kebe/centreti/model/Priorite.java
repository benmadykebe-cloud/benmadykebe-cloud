package ca.kebe.centreti.model;

/** Niveaux d'urgence d'un ticket. Chaque niveau a un délai cible dans la table delai_cible. */
public enum Priorite {
    BASSE, NORMALE, HAUTE, CRITIQUE
}
