-- Schéma du centre de services TI (SQLite)
-- Toutes les dates sont stockées en UTC, au format 'AAAA-MM-JJ HH:MM:SS'.
-- Les règles importantes sont aussi garanties par la base (CHECK, clés étrangères),
-- pas seulement par le code Java.

CREATE TABLE IF NOT EXISTS utilisateur (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nom          TEXT NOT NULL,
    departement  TEXT NOT NULL,
    courriel     TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS technicien (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nom         TEXT NOT NULL,
    specialite  TEXT NOT NULL CHECK (specialite IN ('SUPPORT', 'RESEAU', 'SYSTEMES'))
);

CREATE TABLE IF NOT EXISTS equipement (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    type            TEXT NOT NULL CHECK (type IN ('PORTABLE', 'POSTE', 'IMPRIMANTE', 'ROUTEUR', 'COMMUTATEUR', 'TELEPHONE')),
    numero_serie    TEXT NOT NULL UNIQUE,
    date_achat      TEXT NOT NULL,
    utilisateur_id  INTEGER REFERENCES utilisateur (id)
);

-- Délai de résolution visé pour chaque priorité, utilisé par le rapport des tickets en retard.
CREATE TABLE IF NOT EXISTS delai_cible (
    priorite      TEXT PRIMARY KEY CHECK (priorite IN ('BASSE', 'NORMALE', 'HAUTE', 'CRITIQUE')),
    delai_heures  INTEGER NOT NULL CHECK (delai_heures > 0)
);

CREATE TABLE IF NOT EXISTS ticket (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    titre            TEXT NOT NULL,
    description      TEXT NOT NULL DEFAULT '',
    priorite         TEXT NOT NULL REFERENCES delai_cible (priorite),
    statut           TEXT NOT NULL DEFAULT 'OUVERT' CHECK (statut IN ('OUVERT', 'EN_COURS', 'RESOLU', 'FERME')),
    date_creation    TEXT NOT NULL,
    date_resolution  TEXT,
    utilisateur_id   INTEGER NOT NULL REFERENCES utilisateur (id),
    technicien_id    INTEGER REFERENCES technicien (id),
    equipement_id    INTEGER REFERENCES equipement (id),
    -- Un ticket pris en charge a forcément un technicien.
    CHECK (statut = 'OUVERT' OR technicien_id IS NOT NULL),
    -- Un ticket a une date de résolution si et seulement s'il est résolu ou fermé.
    CHECK ((statut IN ('RESOLU', 'FERME')) = (date_resolution IS NOT NULL)),
    CHECK (date_resolution IS NULL OR date_resolution >= date_creation)
);

CREATE TABLE IF NOT EXISTS intervention (
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    ticket_id          INTEGER NOT NULL REFERENCES ticket (id) ON DELETE CASCADE,
    technicien_id      INTEGER NOT NULL REFERENCES technicien (id),
    minutes            INTEGER NOT NULL CHECK (minutes > 0),
    note               TEXT NOT NULL,
    date_intervention  TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_ticket_statut ON ticket (statut);
CREATE INDEX IF NOT EXISTS idx_ticket_technicien ON ticket (technicien_id);
CREATE INDEX IF NOT EXISTS idx_ticket_equipement ON ticket (equipement_id);
CREATE INDEX IF NOT EXISTS idx_intervention_ticket ON intervention (ticket_id);
CREATE INDEX IF NOT EXISTS idx_intervention_date ON intervention (date_intervention);

INSERT OR IGNORE INTO delai_cible (priorite, delai_heures) VALUES ('CRITIQUE', 4);
INSERT OR IGNORE INTO delai_cible (priorite, delai_heures) VALUES ('HAUTE', 8);
INSERT OR IGNORE INTO delai_cible (priorite, delai_heures) VALUES ('NORMALE', 24);
INSERT OR IGNORE INTO delai_cible (priorite, delai_heures) VALUES ('BASSE', 72);
