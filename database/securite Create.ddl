CREATE TABLE adresse (
  code               char(10) NOT NULL, 
  valeur_adresse     varchar(100) NOT NULL, 
  element_liaison    char(16) NOT NULL, 
  code_typeAdresse   char(10) NOT NULL, 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE droit (
  code              char(10) NOT NULL, 
  libelle           varchar(50) NOT NULL, 
  code_groupe       char(10) NOT NULL, 
  code_operation    char(10) NOT NULL, 
  description       text, 
  date_creation     date,
  heure_creation     time, 
  heure_modification time,  
  date_modification date, 
  user_createur     char(14), 
  user_modificateur char(14), 
  date_activation   date, 
  code_unique       char(16) DEFAULT '0' NOT NULL, 
  code_statut       char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE groupe (
  code               char(10) NOT NULL, 
  libelle            varchar(50) NOT NULL UNIQUE, 
  infos_groupe       text, 
  nombre_utilisateur int(10), 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  date_modification  date, 
  heure_modification time, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE localisation (
  code                 char(14) NOT NULL, 
  libelle              varchar(100) NOT NULL, 
  code_localite_parent char(14), 
  niveau_localite      int(10), 
  lattitude            double, 
  longitude            double, 
  description          text, 
  date_creation        date, 
  date_modification    date, 
  heure_creation       time, 
  heure_modification   time, 
  user_createur        char(14), 
  user_modificateur    char(14), 
  date_activation      date, 
  code_unique          char(16) DEFAULT '0' NOT NULL, 
  code_statut          char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE operation (
  code               char(10) NOT NULL, 
  code_rubrique      char(10) NOT NULL, 
  code_typeOperation char(10) NOT NULL, 
  libelle            varchar(50) NOT NULL UNIQUE, 
  description        text, 
  date_creation      date, 
  heure_creation       time, 
  heure_modification   time,
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE relations (
  code                      char(10) NOT NULL, 
  libelle                   varchar(50) NOT NULL, 
  code_rubrique_source      char(10) NOT NULL, 
  code_rubrique_destination char(10) NOT NULL, 
  table_source              varchar(50) NOT NULL, 
  table_destination         varchar(50) NOT NULL, 
  champ_source              varchar(50) NOT NULL, 
  champ_destination         varchar(50) NOT NULL, 
  description               text, 
  date_creation             date, 
  date_modification         date, 
  heure_creation            time, 
  heure_modification        time, 
  user_createur             char(14), 
  user_modificateur         char(14), 
  date_activation           date, 
  code_unique               char(16) DEFAULT '0' NOT NULL, 
  code_statut               char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE rubrique (
  code               char(10) NOT NULL, 
  nom_rubrique       varchar(50) NOT NULL, 
  code_typeRubrique  char(3) NOT NULL, 
  code_parent        char(10), 
  tableName          varchar(50), 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE sexe (
  code        char(2) NOT NULL, 
  libelle     varchar(20) NOT NULL UNIQUE, 
  description text, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE statut (
  code        char(4) NOT NULL, 
  libelle     varchar(20) NOT NULL UNIQUE, 
  description text, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE tableCode (
  code               char(8) NOT NULL, 
  tableName          varchar(50) NOT NULL UNIQUE, 
  length_code        int(10) NOT NULL, 
  prefix_code        char(2) NOT NULL, 
  suffix_code        char(2) NOT NULL, 
  index_code         varchar(20) NOT NULL, 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE typeAdresse (
  code               char(10) NOT NULL, 
  libelle            varchar(100) NOT NULL UNIQUE, 
  regex_pattern      text, 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE typeOperation (
  code              char(10) NOT NULL, 
  libelle           varchar(50) NOT NULL UNIQUE, 
  description       text, 
  date_creation     date, 
  date_modification date,
  heure_creation     time, 
  heure_modification time,
  user_createur     char(14), 
  user_modificateur char(14), 
  date_activation   date, 
  code_unique       char(16) DEFAULT '0' NOT NULL, 
  code_statut       char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE typeRubrique (
  code        char(3) NOT NULL, 
  libelle     varchar(50) NOT NULL UNIQUE, 
  description text, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE typeUtilisateur (
  code               char(10) NOT NULL, 
  libelle            varchar(50) NOT NULL UNIQUE, 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE usergroupe (
  code               char(10) NOT NULL, 
  code_utilisateur   char(14) NOT NULL, 
  code_groupe        char(10) NOT NULL, 
  description        text, 
  date_creation      date, 
  heure_creation     time, 
  heure_modification time, 
  date_modification  date, 
  user_createur      char(14), 
  user_modificateur  char(14), 
  date_activation    date, 
  code_unique        char(16) DEFAULT '0' NOT NULL, 
  code_statut        char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
CREATE TABLE utilisateur (
  code                 char(14) NOT NULL, 
  noms                 varchar(100) NOT NULL, 
  prenoms              varchar(100), 
  nom_utilisateur      varchar(30) NOT NULL UNIQUE, 
  password             text NOT NULL, 
  date_naissance       date, 
  code_lieu_naissance  char(14), 
  code_sexe            char(2), 
  description          text, 
  code_typeUtilisateur char(10), 
  date_creation        date, 
  heure_creation       time, 
  date_modification    date, 
  heure_modification   time, 
  user_createur        char(14), 
  user_modificateur    char(14), 
  date_activation      date, 
  code_unique          char(16) DEFAULT '0' NOT NULL, 
  code_statut          char(4) NOT NULL, 
  PRIMARY KEY (code)) CHARACTER SET UTF8;
ALTER TABLE utilisateur ADD CONSTRAINT FKutilisateu465786 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE utilisateur ADD CONSTRAINT FKutilisateu216618 FOREIGN KEY (code_sexe) REFERENCES sexe (code);
ALTER TABLE typeUtilisateur ADD CONSTRAINT FKtypeUtilis282564 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE utilisateur ADD CONSTRAINT FKutilisateu470849 FOREIGN KEY (code_typeUtilisateur) REFERENCES typeUtilisateur (code);
ALTER TABLE localisation ADD CONSTRAINT FKlocalisati462028 FOREIGN KEY (code_localite_parent) REFERENCES localisation (code);
ALTER TABLE localisation ADD CONSTRAINT FKlocalisati803148 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE utilisateur ADD CONSTRAINT FKutilisateu274136 FOREIGN KEY (code_lieu_naissance) REFERENCES localisation (code);
ALTER TABLE typeAdresse ADD CONSTRAINT FKtypeAdress751368 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE adresse ADD CONSTRAINT FKadresse45264 FOREIGN KEY (code_typeAdresse) REFERENCES typeAdresse (code);
ALTER TABLE adresse ADD CONSTRAINT FKadresse76225 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE groupe ADD CONSTRAINT FKgroupe752177 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE rubrique ADD CONSTRAINT FKrubrique286856 FOREIGN KEY (code_typeRubrique) REFERENCES typeRubrique (code);
ALTER TABLE rubrique ADD CONSTRAINT FKrubrique401981 FOREIGN KEY (code_parent) REFERENCES rubrique (code);
ALTER TABLE rubrique ADD CONSTRAINT FKrubrique947728 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE relations ADD CONSTRAINT FKrelations517739 FOREIGN KEY (code_rubrique_source) REFERENCES rubrique (code);
ALTER TABLE relations ADD CONSTRAINT FKrelations352120 FOREIGN KEY (code_rubrique_destination) REFERENCES rubrique (code);
ALTER TABLE relations ADD CONSTRAINT FKrelations407446 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE tableCode ADD CONSTRAINT FKtableCode179735 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE usergroupe ADD CONSTRAINT FKusergroupe31166 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE usergroupe ADD CONSTRAINT FKusergroupe599194 FOREIGN KEY (code_utilisateur) REFERENCES utilisateur (code);
ALTER TABLE usergroupe ADD CONSTRAINT FKusergroupe927501 FOREIGN KEY (code_groupe) REFERENCES groupe (code);
ALTER TABLE typeOperation ADD CONSTRAINT FKtypeOperat41097 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE operation ADD CONSTRAINT FKoperation946975 FOREIGN KEY (code_statut) REFERENCES statut (code);
ALTER TABLE operation ADD CONSTRAINT FKoperation529152 FOREIGN KEY (code_typeOperation) REFERENCES typeOperation (code);
ALTER TABLE operation ADD CONSTRAINT FKoperation420532 FOREIGN KEY (code_rubrique) REFERENCES rubrique (code);
ALTER TABLE droit ADD CONSTRAINT FKdroit113697 FOREIGN KEY (code_groupe) REFERENCES groupe (code);
ALTER TABLE droit ADD CONSTRAINT FKdroit340393 FOREIGN KEY (code_operation) REFERENCES operation (code);
ALTER TABLE droit ADD CONSTRAINT FKdroit899225 FOREIGN KEY (code_statut) REFERENCES statut (code);

