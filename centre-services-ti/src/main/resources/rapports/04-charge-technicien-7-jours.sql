-- Techniciens dont le temps d'intervention des 7 derniers jours dépasse un seuil (en minutes).
-- Le paramètre ? est le seuil : HAVING filtre les groupes après le calcul de la somme.
SELECT t.nom           AS "Technicien",
       COUNT(*)        AS "Interventions",
       SUM(i.minutes)  AS "Minutes (7 jours)"
FROM intervention i
JOIN technicien t ON t.id = i.technicien_id
WHERE i.date_intervention >= datetime('now', '-7 days')
GROUP BY t.id
HAVING SUM(i.minutes) > ?
ORDER BY SUM(i.minutes) DESC, t.nom
