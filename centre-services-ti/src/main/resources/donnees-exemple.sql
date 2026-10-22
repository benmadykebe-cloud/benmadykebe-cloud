-- Données d'exemple (personnes et équipements fictifs).
-- Les dates sont relatives à l'heure du chargement : 'now' = maintenant (UTC).

INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (1, 'Julie Bouchard', 'Comptabilité', 'julie.bouchard@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (2, 'Marc-André Lavoie', 'Ventes', 'marc-andre.lavoie@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (3, 'Nadia Benali', 'Ressources humaines', 'nadia.benali@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (4, 'Samuel Côté', 'Entrepôt', 'samuel.cote@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (5, 'Hélène Fortin', 'Direction', 'helene.fortin@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (6, 'Pierre Gauthier', 'Entrepôt', 'pierre.gauthier@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (7, 'Amina Traoré', 'Comptabilité', 'amina.traore@exemple.ca');
INSERT INTO utilisateur (id, nom, departement, courriel) VALUES (8, 'David Lemieux', 'Ventes', 'david.lemieux@exemple.ca');

INSERT INTO technicien (id, nom, specialite) VALUES (1, 'Alex Roy', 'SUPPORT');
INSERT INTO technicien (id, nom, specialite) VALUES (2, 'Sofia Gagnon', 'RESEAU');
INSERT INTO technicien (id, nom, specialite) VALUES (3, 'Karim Diallo', 'SYSTEMES');

INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (1, 'PORTABLE', 'LT-2023-001', '2023-03-14', 1);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (2, 'PORTABLE', 'LT-2023-002', '2023-03-14', 3);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (3, 'POSTE', 'PC-2021-014', '2021-09-02', 4);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (4, 'POSTE', 'PC-2022-007', '2022-05-19', 7);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (5, 'IMPRIMANTE', 'IMP-2020-002', '2020-11-23', NULL);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (6, 'IMPRIMANTE', 'IMP-2022-001', '2022-02-08', NULL);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (7, 'PORTABLE', 'LT-2024-003', '2024-01-30', 2);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (8, 'PORTABLE', 'LT-2024-004', '2024-01-30', 8);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (9, 'ROUTEUR', 'RT-2021-001', '2021-06-10', NULL);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (10, 'COMMUTATEUR', 'SW-2021-002', '2021-06-10', NULL);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (11, 'TELEPHONE', 'TEL-2022-011', '2022-08-15', 8);
INSERT INTO equipement (id, type, numero_serie, date_achat, utilisateur_id) VALUES (12, 'POSTE', 'PC-2023-021', '2023-10-04', 5);

INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (1, 'Imprimante du 2e étage bloquée', 'Plus aucune impression depuis ce matin.', 'HAUTE', 'RESOLU', datetime('now', '-50 hours'), datetime('now', '-45 hours'), 2, 1, 5);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (2, 'Portable qui ne démarre plus', 'Écran noir au démarrage.', 'CRITIQUE', 'FERME', datetime('now', '-120 hours'), datetime('now', '-112 hours'), 1, 3, 1);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (3, 'Wifi instable en salle de conférence', 'Coupures fréquentes pendant les réunions.', 'HAUTE', 'RESOLU', datetime('now', '-30 hours'), datetime('now', '-20 hours'), 5, 2, 9);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (4, 'Mot de passe oublié', 'Compte verrouillé après plusieurs essais.', 'BASSE', 'FERME', datetime('now', '-100 hours'), datetime('now', '-99 hours'), 3, 1, NULL);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (5, 'Écran qui clignote', 'Scintillement toutes les quelques minutes.', 'NORMALE', 'EN_COURS', datetime('now', '-20 hours'), NULL, 4, 1, 3);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (6, 'Ports hors service sur le commutateur de l''entrepôt', 'Six postes sans réseau.', 'CRITIQUE', 'EN_COURS', datetime('now', '-6 hours'), NULL, 6, 2, 10);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (7, 'Installation du logiciel comptable', 'Nouvelle version à installer.', 'NORMALE', 'EN_COURS', datetime('now', '-10 hours'), NULL, 7, 3, 4);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (8, 'Téléphone IP sans tonalité', 'Aucun appel entrant ni sortant.', 'HAUTE', 'OUVERT', datetime('now', '-3 hours'), NULL, 8, NULL, 11);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (9, 'Accès au dossier partagé des ventes', 'Demande d''accès en lecture.', 'BASSE', 'OUVERT', datetime('now', '-2 hours'), NULL, 2, NULL, NULL);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (10, 'Portable lent au démarrage', 'Plus de cinq minutes avant le bureau.', 'NORMALE', 'RESOLU', datetime('now', '-60 hours'), datetime('now', '-50 hours'), 3, 1, 2);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (11, 'Imprimante du 2e étage : bourrages répétés', 'Papier coincé à chaque gros tirage.', 'NORMALE', 'FERME', datetime('now', '-200 hours'), datetime('now', '-190 hours'), 2, 1, 5);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (12, 'Imprimante du 2e étage : toner non reconnu', 'Message d''erreur après le changement.', 'BASSE', 'FERME', datetime('now', '-150 hours'), datetime('now', '-140 hours'), 2, 1, 5);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (13, 'Coupure réseau au bureau de la direction', 'Plus aucune connexion.', 'CRITIQUE', 'FERME', datetime('now', '-90 hours'), datetime('now', '-87 hours'), 5, 2, 9);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (14, 'Batterie du portable défectueuse', 'Se décharge en moins de 30 minutes.', 'HAUTE', 'FERME', datetime('now', '-250 hours'), datetime('now', '-240 hours'), 1, 3, 1);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (15, 'Poste qui ne se connecte plus au domaine', 'Message d''erreur à l''ouverture de session.', 'HAUTE', 'OUVERT', datetime('now', '-1 hours'), NULL, 4, NULL, 3);
INSERT INTO ticket (id, titre, description, priorite, statut, date_creation, date_resolution, utilisateur_id, technicien_id, equipement_id) VALUES (16, 'Connexion VPN impossible en télétravail', 'Échec d''authentification.', 'NORMALE', 'EN_COURS', datetime('now', '-26 hours'), NULL, 7, 2, NULL);

INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (1, 1, 30, 'Diagnostic : bourrage dans le bac 2', datetime('now', '-48 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (1, 1, 45, 'Remplacement du rouleau et test d''impression', datetime('now', '-46 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (2, 3, 120, 'Remplacement du disque et réinstallation du système', datetime('now', '-114 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (3, 2, 90, 'Changement du canal Wi-Fi et mise à jour du micrologiciel', datetime('now', '-24 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (3, 2, 60, 'Contrôle de la couverture dans la salle', datetime('now', '-21 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (4, 1, 10, 'Réinitialisation du mot de passe', datetime('now', '-99 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (5, 1, 25, 'Changement du câble vidéo', datetime('now', '-18 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (6, 2, 45, 'Test des ports et diagnostic du commutateur', datetime('now', '-5 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (7, 3, 60, 'Installation du logiciel et configuration des accès', datetime('now', '-8 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (10, 1, 40, 'Nettoyage du démarrage et mises à jour', datetime('now', '-55 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (11, 1, 35, 'Nettoyage du chemin papier', datetime('now', '-195 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (12, 1, 20, 'Remplacement de la cartouche de toner', datetime('now', '-145 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (13, 2, 150, 'Remplacement de l''alimentation du routeur', datetime('now', '-88 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (14, 3, 75, 'Remplacement de la batterie', datetime('now', '-245 hours'));
INSERT INTO intervention (ticket_id, technicien_id, minutes, note, date_intervention) VALUES (16, 2, 50, 'Vérification du client VPN et des règles du pare-feu', datetime('now', '-25 hours'));
