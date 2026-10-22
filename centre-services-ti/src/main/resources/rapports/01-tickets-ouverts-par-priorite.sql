-- Tickets ni résolus ni fermés, regroupés par priorité (du plus urgent au moins urgent).
SELECT priorite AS "Priorité",
       COUNT(*)  AS "Tickets ouverts"
FROM ticket
WHERE statut IN ('OUVERT', 'EN_COURS')
GROUP BY priorite
ORDER BY CASE priorite
             WHEN 'CRITIQUE' THEN 1
             WHEN 'HAUTE'    THEN 2
             WHEN 'NORMALE'  THEN 3
             ELSE 4
         END
