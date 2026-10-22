package ca.kebe.centreti.service;

/** Une règle de gestion n'est pas respectée (le message est destiné à l'utilisateur). */
public class RegleMetierException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegleMetierException(String message) {
        super(message);
    }
}
