INSERT INTO usergroupe (code, code_utilisateur, code_groupe, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut)
VALUES
  ('2100000321', '1', '1200000012', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1800000000003718', '1000');


INSERT INTO groupe (code, libelle, infos_groupe, nombre_utilisateur, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut) 
VALUES ('1200000012', 'PUBLIC', null, '0', null, null, null, null, null, null, '0', '1000');


INSERT INTO rubrique (code, nom_rubrique, code_typeRubrique, code_parent, tableName, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut) 
VALUES 	('1100000011', 'securite', '100', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'), 
	 	('1100000111', 'rubrique', '101', '1100000011', 'rubrique', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'), 
	 	('1100000211', 'groupe', '101', '1100000011', 'groupe', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'), 
	 	('1100000311', 'table de code', '101', '1100000011', 'tableCode', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
	 	('1100000411', 'utilisateur', '101', '1100000011', 'utilisateur', NULL, NULL, NULL, NULL, null, null, '0', '1000'), 
	 	('1100000511', 'references', '100', NULL, NULL, NULL, NULL, NULL, NULL, null, null, '0', '1000'), 
	 	('1100000611', "type d'utilisateur", '101', '1100000511', 'typeUtilisateur', NULL, NULL, NULL, NULL, null, null, '0', '1000'), 
	 	('1100000711', 'type adresse', '101', '1100000511', 'typeAdresse', NULL, NULL, NULL, NULL, null, null, '0', '1000');

INSERT INTO sexe (code, libelle, description) VALUES 
		('10', 'Masculin', 'Masculin'),
		('11', 'Feminin', 'Feminin'),
		('13', 'Inconnu', "L'individu a un sexe mais il est inconnu"), 
		('14', 'Non applicable', "L'individu concerné n'a pas un sexe"),
		('15', 'Autre', "L'individu peut avoir plusieurs sexe");
		
INSERT INTO statut (code, libelle, description) VALUES 
('1000', 'ACTIF', "actif est l'état dans lequel tout opération est possible sur un élément"), 
('1001', 'BLOQUE', "bolqué est l'état dans lequel un élément ne peut subir aucun traitement, ce statut n'est pas définitif"),
('1002', 'CLOTURE', "cloturé est l'état dans lequel un élément ne pourra plus jamais être utilisé dans l'application mais n'est pas supprimé de la base de données"), 
('1003', 'INCONNU', "inconnu est un statut non définitif"), 
('1004', 'EN TRAITEMENT', "la decision du statut est en cours de traitement");

INSERT INTO tableCode
  (code,  tableName, length_code, prefix_code, suffix_code, index_code, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut) VALUES 
('10000000'	,'tableCode','8'	,'10',	'10',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000001'	,'rubrique'	,'10'	,'11'	,'11'	,'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000002'	,'groupe'	,'10',	'12',	'12',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000003'	,'adresse'	,'10'	,'13'	,'13'	,'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000004'	,'localisation',	'14',	'14',	'14', '20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000005'	,'utilisateur',	'14',	'15',	'15',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000006'	,'typeAdresse',	'10',	'16',	'16',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000007'	,'typeUtilisateur',	'10',	'17',	'17',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000008'	,'code_unique',	'16',	'18',	'18',	'20', NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000'),
('10000009' ,'relations', '10', '17', '17', '2', NULL, NULL, NULL, NULL, NULL, NULL, 'se0', '1000'),


INSERT INTO typeRubrique (code, libelle, description) VALUES 
  ('100', 'menu', 'rubrique menu'),
  ('101', 'feuille', 'rubrique feuille');


INSERT INTO utilisateur (code, noms, prenoms, nom_utilisateur, password, date_naissance, code_lieu_naissance, code_sexe, description, code_typeUtilisateur, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut) 
VALUES 
  ('1', 'KWATE DASSI LOIC', NULL, 'system', 'lado', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000');
