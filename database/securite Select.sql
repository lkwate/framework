SELECT code, valeur_adresse, element_liaison, code_typeAdresse, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM adresse;
SELECT code, libelle, code_groupe, code_operation, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM droit;
SELECT code, libelle, infos_groupe, nombre_utilisateur, description, date_creation, heure_creation, date_modification, heure_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM groupe;
SELECT code, libelle, niveau_localite, lattitude, longitude, description, date_creation, date_modification, heure_creation, heure_modification, user_createur, user_modificateur, date_activation, code_unique, code_localite_parent, code_statut 
  FROM localisation;
SELECT code, code_rubrique, code_typeOperation, libelle, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM operation;
SELECT code, code_rubrique_source, code_rubrique_destination, table_source, table_destination, champ_source, champ_destination, description, date_creation, date_modification, heure_creation, heure_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM relations;
SELECT code, nom_rubrique, code_typeRubrique, code_parent, tableName, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM rubrique;
SELECT code, libelle, description 
  FROM sexe;
SELECT code, libelle, description 
  FROM statut;
SELECT code, tableName, length_code, prefix_code, suffix_code, index_code, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM tableCode;
SELECT code, libelle, regex_pattern, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM typeAdresse;
SELECT code, libelle, description, date_creation, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM typeOperation;
SELECT code, libelle, description 
  FROM typeRubrique;
SELECT code, libelle, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM typeUtilisateur;
SELECT code, code_utilisateur, code_groupe, description, date_creation, heure_creation, heure_modification, date_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM usergroupe;
SELECT code, noms, prenoms, nom_utilisateur, password, date_naissance, code_lieu_naissance, code_sexe, description, code_typeUtilisateur, date_creation, heure_creation, date_modification, heure_modification, user_createur, user_modificateur, date_activation, code_unique, code_statut 
  FROM utilisateur;

