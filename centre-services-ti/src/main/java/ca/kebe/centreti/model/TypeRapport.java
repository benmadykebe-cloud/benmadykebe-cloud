package ca.kebe.centreti.model;

/** Les rapports disponibles. Chacun correspond à un fichier SQL dans src/main/resources/rapports. */
public enum TypeRapport {
    TICKETS_PAR_PRIORITE("Tickets ouverts par priorité", "01-tickets-ouverts-par-priorite.sql", false),
    DELAI_MOYEN("Délai moyen de résolution par technicien", "02-delai-moyen-par-technicien.sql", false),
    TOP_EQUIPEMENTS("Équipements les plus souvent en panne", "03-top-equipements-en-panne.sql", false),
    CHARGE_TECHNICIEN("Charge des techniciens sur 7 jours", "04-charge-technicien-7-jours.sql", true),
    TICKETS_EN_RETARD("Tickets en retard sur leur délai cible", "05-tickets-en-retard.sql", false),
    TICKETS_PAR_DEPARTEMENT("Tickets par département", "06-tickets-par-departement.sql", false);

    private final String titre;
    private final String fichier;
    private final boolean necessiteSeuil;

    TypeRapport(String titre, String fichier, boolean necessiteSeuil) {
        this.titre = titre;
        this.fichier = fichier;
        this.necessiteSeuil = necessiteSeuil;
    }

    public String titre() {
        return titre;
    }

    public String fichier() {
        return fichier;
    }

    /** Vrai si le rapport attend un seuil en minutes (paramètre ? de la requête). */
    public boolean necessiteSeuil() {
        return necessiteSeuil;
    }
}
