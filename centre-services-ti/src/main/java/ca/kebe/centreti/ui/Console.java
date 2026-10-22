package ca.kebe.centreti.ui;

import ca.kebe.centreti.dao.InterventionDao;
import ca.kebe.centreti.dao.RapportDao;
import ca.kebe.centreti.dao.ReferenceDao;
import ca.kebe.centreti.dao.TicketDao;
import ca.kebe.centreti.model.Priorite;
import ca.kebe.centreti.model.Statut;
import ca.kebe.centreti.model.TypeRapport;
import ca.kebe.centreti.service.RegleMetierException;
import ca.kebe.centreti.service.TicketService;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Interface en ligne de commande : menu, saisies et affichage. Aucune règle de gestion ici. */
public final class Console {

    private final TicketService service;
    private final TicketDao tickets;
    private final InterventionDao interventions;
    private final ReferenceDao references;
    private final RapportDao rapports;
    private final Scanner entree = new Scanner(System.in, StandardCharsets.UTF_8);

    public Console(Connection connexion) {
        this.service = new TicketService(connexion);
        this.tickets = new TicketDao(connexion);
        this.interventions = new InterventionDao(connexion);
        this.references = new ReferenceDao(connexion);
        this.rapports = new RapportDao(connexion);
    }

    public void lancer() {
        System.out.println("=== Centre de services TI ===");
        try {
            boolean continuer = true;
            while (continuer) {
                afficherMenu();
                int choix = lireEntier("Votre choix");
                try {
                    switch (choix) {
                        case 1 -> creerTicket();
                        case 2 -> listerTickets();
                        case 3 -> assignerTicket();
                        case 4 -> changerStatut();
                        case 5 -> ajouterIntervention();
                        case 6 -> voirInterventions();
                        case 7 -> menuRapports();
                        case 0 -> continuer = false;
                        default -> System.out.println("Choix invalide.");
                    }
                } catch (RegleMetierException e) {
                    System.out.println("Refusé : " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("Erreur de base de données : " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nFin de la saisie.");
        }
        System.out.println("Au revoir.");
    }

    private void afficherMenu() {
        System.out.println("""

                1. Créer un ticket
                2. Lister les tickets
                3. Assigner un technicien
                4. Changer le statut d'un ticket
                5. Ajouter une intervention
                6. Voir les interventions d'un ticket
                7. Rapports
                0. Quitter""");
    }

    private void creerTicket() throws SQLException {
        System.out.print(Tableau.formater(references.utilisateurs()));
        int utilisateurId = lireEntier("N° du demandeur");

        String titre = lireTexte("Titre");
        String description = lireTexte("Description (facultative)");

        Priorite[] priorites = Priorite.values();
        for (int i = 0; i < priorites.length; i++) {
            System.out.println("  " + (i + 1) + ". " + priorites[i]);
        }
        int choixPriorite = lireEntier("Priorité (1 à " + priorites.length + ")");
        if (choixPriorite < 1 || choixPriorite > priorites.length) {
            System.out.println("Priorité invalide.");
            return;
        }

        System.out.print(Tableau.formater(references.equipements()));
        int equipement = lireEntier("N° de l'équipement concerné (0 = aucun)");

        int id = service.creerTicket(titre, description, priorites[choixPriorite - 1], utilisateurId,
                equipement == 0 ? null : equipement);
        System.out.println("Ticket n° " + id + " créé.");
    }

    private void listerTickets() throws SQLException {
        Statut[] statuts = Statut.values();
        System.out.println("  0. Tous");
        for (int i = 0; i < statuts.length; i++) {
            System.out.println("  " + (i + 1) + ". " + statuts[i]);
        }
        int choix = lireEntier("Filtrer par statut");
        if (choix < 0 || choix > statuts.length) {
            System.out.println("Choix invalide.");
            return;
        }
        System.out.print(Tableau.formater(tickets.lister(choix == 0 ? null : statuts[choix - 1])));
    }

    private void assignerTicket() throws SQLException {
        int ticketId = lireEntier("N° du ticket");
        System.out.print(Tableau.formater(references.techniciens()));
        int technicienId = lireEntier("N° du technicien");
        service.assigner(ticketId, technicienId);
        System.out.println("Ticket assigné.");
    }

    private void changerStatut() throws SQLException {
        int ticketId = lireEntier("N° du ticket");
        System.out.println("Statut actuel : " + service.chargerTicket(ticketId).statut());
        Statut[] statuts = Statut.values();
        for (int i = 0; i < statuts.length; i++) {
            System.out.println("  " + (i + 1) + ". " + statuts[i]);
        }
        int choix = lireEntier("Nouveau statut (1 à " + statuts.length + ")");
        if (choix < 1 || choix > statuts.length) {
            System.out.println("Choix invalide.");
            return;
        }
        service.changerStatut(ticketId, statuts[choix - 1]);
        System.out.println("Statut mis à jour.");
    }

    private void ajouterIntervention() throws SQLException {
        int ticketId = lireEntier("N° du ticket");
        int technicienId = lireEntier("N° du technicien");
        int minutes = lireEntier("Durée en minutes");
        String note = lireTexte("Note");
        service.ajouterIntervention(ticketId, technicienId, minutes, note);
        System.out.println("Intervention enregistrée.");
    }

    private void voirInterventions() throws SQLException {
        int ticketId = lireEntier("N° du ticket");
        System.out.print(Tableau.formater(interventions.lister(ticketId)));
    }

    private void menuRapports() throws SQLException {
        TypeRapport[] types = TypeRapport.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println("  " + (i + 1) + ". " + types[i].titre());
        }
        int choix = lireEntier("Rapport (1 à " + types.length + ")");
        if (choix < 1 || choix > types.length) {
            System.out.println("Choix invalide.");
            return;
        }
        TypeRapport type = types[choix - 1];
        if (type.necessiteSeuil()) {
            int seuil = lireEntier("Seuil en minutes (par exemple 180)");
            System.out.print(Tableau.formater(rapports.generer(type, seuil)));
        } else {
            System.out.print(Tableau.formater(rapports.generer(type)));
        }
    }

    private String lireTexte(String question) {
        System.out.print(question + " : ");
        return entree.nextLine().strip();
    }

    private int lireEntier(String question) {
        while (true) {
            String texte = lireTexte(question);
            try {
                return Integer.parseInt(texte);
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre entier.");
            }
        }
    }
}
