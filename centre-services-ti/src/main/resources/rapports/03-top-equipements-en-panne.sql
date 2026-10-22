-- Les 5 équipements qui reviennent le plus souvent dans les tickets.
-- Le tri secondaire par numéro de série rend l'ordre stable en cas d'égalité.
SELECT e.type         AS "Type",
       e.numero_serie AS "Numéro de série",
       COUNT(*)       AS "Tickets"
FROM ticket k
JOIN equipement e ON e.id = k.equipement_id
GROUP BY e.id
ORDER BY COUNT(*) DESC, e.numero_serie
LIMIT 5
