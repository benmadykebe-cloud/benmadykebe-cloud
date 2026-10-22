package ca.kebe.centreti.ui;

import ca.kebe.centreti.model.Rapport;

import java.util.List;

/** Met en forme un {@link Rapport} sous forme de tableau de texte aligné. */
public final class Tableau {

    private static final int LARGEUR_MAX_CELLULE = 44;

    private Tableau() {
    }

    public static String formater(Rapport rapport) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(rapport.titre()).append('\n');
        if (rapport.estVide()) {
            return sb.append("Aucun résultat.\n").toString();
        }

        List<String> colonnes = rapport.colonnes();
        int[] largeurs = new int[colonnes.size()];
        for (int i = 0; i < largeurs.length; i++) {
            largeurs[i] = colonnes.get(i).length();
        }
        for (List<String> ligne : rapport.lignes()) {
            for (int i = 0; i < largeurs.length; i++) {
                largeurs[i] = Math.max(largeurs[i], couper(ligne.get(i)).length());
            }
        }

        String separateur = separateur(largeurs);
        sb.append(separateur).append('\n');
        sb.append(ligne(colonnes, largeurs)).append('\n');
        sb.append(separateur).append('\n');
        for (List<String> donnees : rapport.lignes()) {
            sb.append(ligne(donnees, largeurs)).append('\n');
        }
        sb.append(separateur).append('\n');
        sb.append(rapport.lignes().size()).append(" ligne(s)\n");
        return sb.toString();
    }

    private static String separateur(int[] largeurs) {
        StringBuilder sb = new StringBuilder("+");
        for (int largeur : largeurs) {
            sb.append("-".repeat(largeur + 2)).append('+');
        }
        return sb.toString();
    }

    private static String ligne(List<String> cellules, int[] largeurs) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < largeurs.length; i++) {
            String texte = couper(cellules.get(i));
            sb.append(' ').append(texte).append(" ".repeat(largeurs[i] - texte.length())).append(" |");
        }
        return sb.toString();
    }

    static String couper(String texte) {
        if (texte.length() <= LARGEUR_MAX_CELLULE) {
            return texte;
        }
        return texte.substring(0, LARGEUR_MAX_CELLULE - 3) + "...";
    }
}
