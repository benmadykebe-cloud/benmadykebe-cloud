-- Tickets encore actifs dont l'âge dépasse le délai cible de leur priorité (table delai_cible).
SELECT k.id        AS "N°",
       k.titre     AS "Titre",
       k.priorite  AS "Priorité",
       ROUND((julianday('now') - julianday(k.date_creation)) * 24, 1) AS "Âge (h)",
       d.delai_heures AS "Délai cible (h)"
FROM ticket k
JOIN delai_cible d ON d.priorite = k.priorite
WHERE k.statut IN ('OUVERT', 'EN_COURS')
  AND (julianday('now') - julianday(k.date_creation)) * 24 > d.delai_heures
ORDER BY (julianday('now') - julianday(k.date_creation)) * 24 - d.delai_heures DESC, k.id
