-- Nombre de tickets par département et part du total.
-- SUM(COUNT(*)) OVER () est une fonction de fenêtre : elle additionne tous les groupes.
SELECT u.departement AS "Département",
       COUNT(*)      AS "Tickets",
       ROUND(100.0 * COUNT(*) / SUM(COUNT(*)) OVER (), 1) AS "Part (%)"
FROM ticket k
JOIN utilisateur u ON u.id = k.utilisateur_id
GROUP BY u.departement
ORDER BY COUNT(*) DESC, u.departement
