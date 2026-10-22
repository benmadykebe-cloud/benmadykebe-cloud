-- Délai moyen entre la création et la résolution d'un ticket, par technicien (en heures).
-- julianday() donne un nombre de jours : on multiplie par 24 pour obtenir des heures.
SELECT t.nom AS "Technicien",
       COUNT(*) AS "Tickets résolus",
       ROUND(AVG((julianday(k.date_resolution) - julianday(k.date_creation)) * 24), 1) AS "Délai moyen (h)"
FROM ticket k
JOIN technicien t ON t.id = k.technicien_id
WHERE k.date_resolution IS NOT NULL
GROUP BY t.id
ORDER BY AVG(julianday(k.date_resolution) - julianday(k.date_creation)), t.nom
