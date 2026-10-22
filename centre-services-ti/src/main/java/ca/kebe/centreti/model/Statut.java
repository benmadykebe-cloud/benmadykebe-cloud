package ca.kebe.centreti.model;

/** États possibles d'un ticket et transitions autorisées entre eux. */
public enum Statut {
    OUVERT, EN_COURS, RESOLU, FERME;

    /** Indique si un ticket dans cet état peut passer à l'état cible. */
    public boolean peutPasserA(Statut cible) {
        return switch (this) {
            case OUVERT -> cible == EN_COURS;
            case EN_COURS -> cible == RESOLU;
            case RESOLU -> cible == FERME || cible == EN_COURS; // réouverture possible
            case FERME -> false;
        };
    }
}
