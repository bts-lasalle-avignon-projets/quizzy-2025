package com.lasalle.quizzy;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;


public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String  TAG         = "_BaseDeDonnees";
    public static final String   NOM_BDD     = "quizzy.db";
    public static final int      VERSION_BDD = 1;
    private static BaseDeDonnees baseDeDonnees = null;
    private final SQLiteDatabase sqlite;

    public static final String TABLE_THEME       = "table_theme";
    public static final String TABLE_QUIZ        = "table_quiz";
    public static final String TABLE_QUESTION    = "table_question";
    public static final String TABLE_PARTICIPANT = "table_participant";
    public static final String TABLE_REPONSE     = "table_reponse";
    public static final String TABLE_RESULTATS   = "table_resultats";

    public static final String COLONNE_THEMEID       = "themeID";
    public static final String COLONNE_THEME         = "theme";
    public static final String COLONNE_QUIZZID       = "quizzID";
    public static final String COLONNE_HORODATAGE    = "horodatage";
    public static final String COLONNE_GAGNANTID     = "gagnantID";
    public static final String COLONNE_QUESTIONID    = "questionID";
    public static final String COLONNE_QUESTION      = "question";
    public static final String COLONNE_PROPOSITION1  = "proposition1";
    public static final String COLONNE_PROPOSITION2  = "proposition2";
    public static final String COLONNE_PROPOSITION3  = "proposition3";
    public static final String COLONNE_PROPOSITION4  = "proposition4";
    public static final String COLONNE_REPONSE       = "reponse";
    public static final String COLONNE_EXPLICATION   = "explication";
    public static final String COLONNE_PARTICIPANTID = "participantID";
    public static final String COLONNE_PRENOM        = "prenom";
    public static final String COLONNE_TEMPS         = "temps";
    public static final String COLONNE_SCORE         = "score";

    private static final String CREE_TABLE_THEME =
            "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + "(" + COLONNE_THEMEID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEME + " TEXT NOT NULL);";

    private static final String CREE_TABLE_QUIZZ =
            "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEMEID + " INTEGER, " + COLONNE_HORODATAGE +
                    " TEXT NOT NULL, " + COLONNE_GAGNANTID + " INTEGER DEFAULT 0, "
                    + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID +
                    ") ON DELETE CASCADE);";

    private static final String CREE_TABLE_QUESTION =
            "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                    + COLONNE_QUESTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLONNE_THEMEID + " INTEGER, "
                    + COLONNE_QUESTION + " TEXT NOT NULL, "
                    + COLONNE_PROPOSITION1 + " TEXT NOT NULL, "
                    + COLONNE_PROPOSITION2 + " TEXT NOT NULL, "
                    + COLONNE_PROPOSITION3 + " TEXT NOT NULL, "
                    + COLONNE_PROPOSITION4 + " TEXT NOT NULL, "
                    + COLONNE_EXPLICATION + " TEXT DEFAULT '', "
                    + COLONNE_REPONSE + " INTEGER NOT NULL,"
                    + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID + ")  ON DELETE CASCADE);";

    private static final String CREE_TABLE_PARTICIPANT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_PRENOM + " TEXT NOT NULL);";

    private static final String CREE_TABLE_REPONSE =
            "CREATE TABLE IF NOT EXISTS "
                    + TABLE_REPONSE + "("
                    + COLONNE_QUIZZID + " INTEGER, "
                    + COLONNE_PARTICIPANTID + " INTEGER, "
                    + COLONNE_QUESTIONID + " INTEGER, "
                    + COLONNE_TEMPS + " INTEGER DEFAULT NULL, "
                    + COLONNE_REPONSE + " INTEGER NOT NULL, "
                    + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + ", " + COLONNE_QUESTIONID + "), "
                    + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID + ") ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + COLONNE_QUESTIONID + ") REFERENCES " + TABLE_QUESTION + "(" + COLONNE_QUESTIONID + ") ON DELETE CASCADE);";

    private static final String CREE_TABLE_RESULTATS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RESULTATS + "(" + COLONNE_QUIZZID + " INTEGER, " +
                    COLONNE_PARTICIPANTID + " INTEGER, " + COLONNE_SCORE + " REAL, "
                    + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + "), "
                    + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
                    ") ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" +
                    COLONNE_PARTICIPANTID + ") ON DELETE CASCADE);";

    private static final String AJOUTE_PARTICIPANTS = "INSERT INTO " + TABLE_PARTICIPANT + "(" +
            COLONNE_PARTICIPANTID + "," + COLONNE_PRENOM +
            ") VALUES "
            + " (1,'ARIATI Axel'),\n"
            + " (2,'BERNARD Clément'),\n"
            + " (3,'BLONDEL Joshua'),\n"
            + " (4,'BOUSQUET SOLFRINI Valentin'),\n"
            + " (5,'CLÉMENT Aymeric'),\n"
            + " (6,'GASSE Lenny'),\n"
            + " (7,'MILLOT Pierre'),\n"
            + " (8,'NAVARRO Mattéo'),\n"
            + " (9,'PESSINA Nicolas'),\n"
            + " (10,'RAFFIN Louis'),\n"
            + " (11,'SORIA-BONET Enzo'),\n"
            + " (12,'VALOBRA Enzo'),\n"
            + " (13,'VANDENBROUCKE Théo'),\n"
            + " (14,'VAUDAINE Dylan');\n";

    private static final String AJOUTE_THEMES = "INSERT INTO " + TABLE_THEME + "(" +
            COLONNE_THEMEID + "," + COLONNE_THEME + ") VALUES "
            + " (1,'Informatique'),\n"
            + " (2,'Culture générale'),\n"
            + " (3,'Cinéma'),\n"
            + " (4,'Musique');";

    private static final String AJOUTE_QUESTIONS =
            "INSERT INTO " + TABLE_QUESTION +
                    "(themeID, question, proposition1, proposition2, proposition3, proposition4, reponse, explication) VALUES "
                    +
                    "(1,'Quel programmeur a créé et continue de diriger le développement du noyau de Linux ?','Steeve Jobs','Linus Torvalds','Larry Ellison','Bill Gates','Linus Torvalds a découvert l''informatique vers l''âge de 11 ans grâce à l''ordinateur de son grand-père, un Commodore VIC-20.'),\n"
                    +
                    "(1,'Quel est le principal atout de Linux, développé et maintenu par Linus Torvalds ?','Il est libre','Il est beau','Il est Finlandais','Il est amusant','Linux est un système d''exploitation open-source, gratuit, et modifiableIl est développé et maintenu par Torvalds avec l''aide de contributeurs.'),\n"
                    +
                    "(1,'Quel type de mémoire est volatile ?','ROM','SSD','RAM','CD','RAM','RAM est volatile, elle perd ses données lorsque l''appareil est éteint.'),\n"
                    +
                    "(1,'Quel langage est souvent enseigné en premier ?','Python','Java','Swift','Rust','Python','Python est réputé pour sa simplicité et sa lisibilité.'),\n"
                    +
                    "(1,'Quel composant exécute les instructions ?','RAM','CPU','GPU','ROM','CPU','Le CPU est le processeur qui exécute les instructions des programmes.'),\n"
                    +
                    "(1,'Quel est un protocole internet ?','USB','TCP','PDF','HTML','TCP','TCP est un protocole de communication sur Internet.'),\n"
                    +
                    "(1,'Quel système est open source ?','macOS','Windows','Linux','iOS','Linux','Linux est un système d''exploitation libre et open source.'),\n"
                    +
                    "(1,'Quelle unité mesure la vitesse CPU ?','Watt','Volt','Hertz','Ohm','Hertz','La fréquence d''un processeur est mesurée en Hertz.'),\n"
                    +
                    "(1,'Quel outil gère le code source ?','Excel','Git','Paint','CMD','Git','Git est un système de gestion de versions très utilisé.'),\n"
                    +
                    "(1,'Quel format est une image ?','PNG','TXT','CSV','MP3','PNG','PNG est un format courant pour les images numériques.'),\n"
                    +
                    "(1,'Quelle extension est exécutable ?','.txt','.doc','.exe','.jpg','.exe','.exe est l''extension d''un fichier exécutable sous Windows.'),\n"
                    +
                    "(1,'Quel appareil connecte un réseau ?','Scanner','Modem','Clé USB','Imprimante','Modem','Le modem permet de connecter un réseau à Internet.'),\n"
                    +
                    "(1,'Quel système d''exploitation est utilisé sur la majorité des serveurs web ?','Linux','Windows','macOS','Android','Linux','Linux est populaire sur les serveurs grâce à sa stabilité et son code libre.'),\n"
                    +
                    "(1,'Quel langage est utilisé pour les pages web ?','C++','Java','HTML','Python','HTML','HTML permet de structurer le contenu d''une page web.'),\n"
                    +
                    "(1,'Quelle commande permet de lister les fichiers sous Linux ?','ls','cd','pwd','mv','ls','La commande ls affiche les fichiers du répertoire courant.'),\n"
                    +
                    "(1,'Quel navigateur est développé par Mozilla ?','Safari','Chrome','Firefox','Edge','Firefox','Firefox est le navigateur open source développé par Mozilla.'),\n"
                    +
                    "(1,'Quel outil est utilisé pour compresser des fichiers ?','Paint','Zip','Excel','Notepad','Zip','Le format zip permet de compresser plusieurs fichiers.'),\n"
                    +
                    "(1,'Quel protocole sécurise un site web ?','HTTP','FTP','SMTP','HTTPS','HTTPS','HTTPS chiffre les échanges pour sécuriser la navigation.'),\n"
                    +
                    "(1,'Quelle technologie permet la virtualisation ?','Java','Docker','Excel','Ruby','Docker','Docker permet de créer des conteneurs virtuels pour les applications.'),\n"
                    +
                    "(1,'Quel logiciel permet de coder ?','Word','Photoshop','VS Code','Paint','VS Code','VS Code est un éditeur de code puissant et populaire.'),\n"
                    +
                    "(1,'Quel langage est utilisé en data science ?','Bash','Python','Java','HTML','Python','Python est largement utilisé en analyse de données et intelligence artificielle.'),\n"
                    +
                    "(1,'Quel mot désigne un virus déguisé ?','Trojan','Worm','Bug','Patch','Trojan','Un trojan semble inoffensif mais contient un code malveillant.'),\n"
                    +
                    "(1,'Quel type de mémoire conserve les données sans électricité ?','RAM','ROM','Cache','Flash','ROM','La mémoire ROM conserve les données même sans alimentation électrique.'),\n"
                    +
                    "(1,'Quel langage est utilisé pour créer des applications Android ?','Swift','Kotlin','Ruby','Go','Kotlin','Kotlin est le langage officiel pour le développement Android moderne.'),\n"
                    +
                    "(1,'Quel composant stocke temporairement les données en cours ?','GPU','RAM','SSD','ROM','RAM','La RAM stocke temporairement les données utilisées par le processeur.'),\n"
                    +
                    "(1,'Quel système de fichiers est utilisé par défaut sous Windows ?','NTFS','EXT4','FAT32','APFS','NTFS','NTFS est le système de fichiers principal des systèmes Windows modernes.'),\n"
                    +
                    "(1,'Quelle commande Linux permet de changer de dossier ?','ls','rm','cd','mv','cd','La commande cd permet de naviguer entre les répertoires.'),\n"
                    +
                    "(1,'Quelle norme sans fil permet de connecter des périphériques ?','USB','WiFi','Bluetooth','Ethernet','Bluetooth','Bluetooth connecte sans fil des appareils à courte distance.'),\n"
                    +
                    "(1,'Quel langage est principalement utilisé pour le développement web backend ?','PHP','HTML','CSS','SQL','PHP','PHP est un langage serveur populaire pour le développement web backend.'),\n"
                    +
                    "(1,'Quel protocole sert à transférer des fichiers sur Internet ?','HTTP','FTP','SMTP','IMAP','FTP','FTP est un protocole utilisé pour transférer des fichiers entre ordinateurs.'),\n"
                    +
                    "(1,'Quel composant calcule les graphismes dans un ordinateur ?','CPU','RAM','GPU','HDD','GPU','Le GPU traite et rend les images et vidéos pour l''affichage.'),\n"
                    +
                    "(1,'Quel est le format standard pour les pages web ?','XML','HTML','JSON','TXT','HTML','HTML est la base des pages web affichées dans les navigateurs.'),\n"
                    +
                    "(1,'Quel logiciel est utilisé pour créer des bases de données relationnelles ?','MySQL','Photoshop','Excel','Chrome','MySQL','MySQL est un système de gestion de bases de données relationnelles.'),\n"
                    +
                    "(1,'Quel est le langage principal pour programmer les applications iOS ?','Kotlin','Swift','Java','C#','Swift','Swift est le langage recommandé pour développer sur iOS.'),\n"
                    +
                    "(1,'Quel type de fichier contient du code source compilable ?','.exe','.dll','.class','.txt','.class','Les fichiers .class contiennent du bytecode Java compilé.'),\n"
                    +
                    "(1,'Quelle unité mesure la capacité de stockage ?','Hz','GB','W','V','GB','Le gigaoctet (GB) mesure la capacité de stockage des données.'),\n"
                    +
                    "(1,'Quel système d’exploitation est développé par Apple ?','Windows','Linux','macOS','Android','macOS','macOS est le système d’exploitation des ordinateurs Apple.'),\n"
                    +
                    "(1,'Quelle commande Linux supprime un fichier ?','rm','mv','cp','ls','rm','La commande rm supprime les fichiers dans un terminal Linux.'),\n"
                    +
                    "(1,'Quel composant contrôle les entrées et sorties ?','CPU','RAM','Chipset','SSD','Chipset','Le chipset gère la communication entre les composants matériels.'),\n"
                    +
                    "(1,'Quelle est la taille d’un octet ?','4 bits','8 bits','16 bits','32 bits','8 bits','Un octet correspond à 8 bits d’information binaire.'),\n"
                    +
                    "(1,'Quel langage est utilisé pour manipuler des bases de données ?','HTML','CSS','SQL','Java','SQL','SQL permet de gérer et interroger des bases de données.'),\n"
                    +
                    "(1,'Quel outil permet de créer des machines virtuelles ?','Docker','VirtualBox','Git','Node.js','VirtualBox','VirtualBox permet de créer et gérer des machines virtuelles.'),\n"
                    +
                    "(1,'Quelle commande affiche l’adresse IP sur Windows ?','ipconfig','ping','netstat','tracert','ipconfig','ipconfig affiche les informations réseau d’une machine Windows.'),\n"
                    +
                    "(1,'Quel protocole est utilisé pour envoyer des emails ?','HTTP','SMTP','FTP','IMAP','SMTP','SMTP est le protocole principal pour l’envoi d’emails.'),\n"
                    +
                    "(1,'Quel est le principal langage pour les scripts shell ?','Python','Bash','Java','Ruby','Bash','Bash est le langage principal pour les scripts shell Unix/Linux.'),\n"
                    +
                    "(1,'Quel élément est une adresse physique dans un réseau ?','IP','MAC','DNS','URL','MAC','L’adresse MAC identifie un appareil sur un réseau local.'),\n"
                    +
                    "(1,'Quel format est utilisé pour les images vectorielles ?','PNG','SVG','JPG','GIF','SVG','Le format SVG est utilisé pour les images vectorielles scalables.'),\n"
                    +
                    "(1,'Quel système permet de sécuriser les accès informatiques ?','VPN','Firewall','Antivirus','Proxy','Firewall','Un firewall filtre le trafic réseau pour la sécurité.'),\n"
                    +
                    "(1,'Quelle commande compile un programme en C ?','gcc','java','python','make','gcc','gcc est le compilateur GNU pour le langage C.'),\n"
                    +
                    "(1,'Quel est le langage de programmation des applications Android ?','Swift','Kotlin','Ruby','Go','Kotlin','Kotlin est officiellement supporté pour Android depuis 2017.'),\n"
                    +
                    "(1,'Quel protocole DNS sert à résoudre les noms en adresses IP ?','HTTP','FTP','DNS','SMTP','DNS','Le protocole DNS traduit les noms de domaines en adresses IP.'),\n"
                    +
                    "(1,'Quelle technologie permet la communication sans fil à courte distance ?','WiFi','Ethernet','Bluetooth','USB','Bluetooth','Bluetooth est une technologie de communication sans fil courte portée.'),\n"
                    +
                    "(1,'Quel langage est utilisé pour les styles CSS ?','HTML','Java','CSS','Python','CSS','CSS sert à styliser la présentation des pages web HTML.'),\n"
                    +
                    "(1,'Quel animal représentant Linux est aussi la mascotte de l''université d''Helsinki ?','Marmotte','Caribou','Gnou','Manchot','Le manchot est la mascotte de Linux, choisie pour son aspect sympathique et facile à identifier.'),\n"
                    +
                    "(1,'Laquelle de ces propositions désigne une distribution Linux fondée en 1993 ?','Zubuntu','Red Hat','Souze','Mandrika','Red Hat, fondée en 1993, est une distribution Linux très populaire dans le monde professionnel.'),\n"
                    +
                    "(1,'Quel système d''exploitation mobile majeur de l''industrie s''appuie sur un noyau Linux ?','iOS','Windows Phone','Android','BlackBerry 10','Android utilise le noyau Linux, ce qui en fait l''un des systèmes d''exploitation mobiles les plus utilisés.'),\n"
                    +
                    "(1,'Sous Linux, comment appelle-t-on les logiciels assemblés autour du noyau ?','Progiciel','Paquet','Distribution','Logithèque','Une distribution est un ensemble de logiciels qui fonctionne avec le noyau Linux.'),\n"
                    +
                    "(1,'Quel serveur web présent sous Linux est aussi présent sur les serveurs du monde entier ?','Apache','Comanche','Sioux','Mohican','Apache est l''un des serveurs web les plus utilisés au monde, y compris sur des systèmes Linux. Il est open-source et largement adopté pour héberger des sites web.'),\n"
                    +
                    "(1,'Quel est le nom de la mascotte de Linux, connue des mordus du système d''exploitation ?','Wilber','Tux','Gnu','Puffy','Tux est la mascotte officielle de Linux, un manchot qui représente le système d''exploitation open-source créé par Linus Torvalds.'),\n"
                    +
                    "(1,'En quelle année Linus Torvalds a-t-il livré la première version du noyau Linux ?','1991','1993','1995','1997','La première version du noyau Linux a été publiée en 1991 par Linus Torvalds, marquant le début du développement du système d''exploitation.'),\n"
                    +
                    "(1,'Quel ancien mot bantou désigne une célèbre distribution Linux ?','Umbro','Ubuntu','Uhura','Ursula','Ubuntu vient d''un mot bantou signifiant \"humanité envers les autres\".'),\n"
                    +
                    "(2,'Quelle espèce d''oiseaux, encore présente en Europe, gringotte, quiritte ou trille ?','Rossignol','Corbeau','Perroquet','Moineau','Le rossignol est un petit oiseau chanteur, célèbre pour son chant mélodieux, encore présent dans certaines régions d''Europe.'),\n"
                    +
                    "(2,'Laquelle de ces professions ne peut-on associer au grand Léonard de Vinci ?','Peintre','Sculpteur','Auteur dramatique','Botaniste','Léonard de Vinci était principalement connu comme peintre, sculpteur et inventeur, mais il n''était pas auteur dramatique.'),\n"
                    +
                    "(2,'Quelle capitale est surnommée la ville lumière ?','Paris','Rome','Londres','Berlin','Paris','Paris est connue comme la ville lumière grâce à son éclairage urbain.'),\n"
                    +
                    "(2,'Quel métal est utilisé pour fabriquer des fils électriques ?','Fer','Cuivre','Aluminium','Or','Cuivre','Le cuivre est un excellent conducteur utilisé dans les câbles électriques.'),\n"
                    +
                    "(2,'Quelle planète est la plus proche du Soleil ?','Terre','Mars','Vénus','Mercure','Mercure','Mercure est la planète la plus proche du Soleil.'),\n"
                    +
                    "(2,'Quelle est la langue officielle du Brésil ?','Espagnol','Anglais','Français','Portugais','Portugais','Le portugais est la langue officielle du Brésil.'),\n"
                    +
                    "(2,'Quel animal est symbole de sagesse ?','Chien','Chouette','Lion','Éléphant','Chouette','La chouette est traditionnellement associée à la sagesse.'),\n"
                    +
                    "(2,'Quelle est la plus grande île du monde ?','Madagascar','Groenland','Bali','Sumatra','Groenland','Le Groenland est la plus grande île du monde.'),\n"
                    +
                    "(2,'Quel est l’organe principal de la respiration ?','Cœur','Foie','Poumon','Rein','Poumon','Les poumons sont responsables des échanges gazeux respiratoires.'),\n"
                    +
                    "(2,'Quelle ville est célèbre pour sa tour penchée ?','Venise','Pise','Rome','Florence','Pise','La tour penchée de Pise est un monument célèbre en Italie.'),\n"
                    +
                    "(2,'Quel métal précieux est symbole de richesse ?','Argent','Or','Platine','Cuivre','Or','L''or est traditionnellement un symbole de richesse.'),\n"
                    +
                    "(2,'Quel continent est aussi un pays ?','Afrique','Europe','Australie','Amérique','Australie','L’Australie est à la fois un continent et un pays.'),\n"
                    +
                    "(2,'Quelle est la plus grande chaîne de montagnes ?','Alpes','Himalaya','Andes','Appalaches','Himalaya','L’Himalaya abrite le plus haut sommet du monde.'),\n"
                    +
                    "(2,'Quel océan borde la côte Est des États-Unis ?','Pacifique','Atlantique','Indien','Arctique','Atlantique','L’océan Atlantique borde la côte Est américaine.'),\n"
                    +
                    "(2,'Quel est l’élément chimique avec le symbole O ?','Or','Oxygène','Osmium','Ozone','Oxygène','Le symbole O désigne l’oxygène.'),\n"
                    +
                    "(2,'Qui a peint la Mona Lisa ?','Van Gogh','Michel-Ange','Léonard','Raphaël','Léonard','Léonard de Vinci a peint la Mona Lisa.'),\n"
                    +
                    "(2,'Quel est le plus grand désert chaud ?','Gobi','Sahara','Kalahari','Atacama','Sahara','Le Sahara est le plus grand désert chaud du monde.'),\n"
                    +
                    "(2,'Quelle année marque la chute du mur de Berlin ?','1987','1989','1991','1993','1989','Le mur de Berlin est tombé en 1989.'),\n"
                    +
                    "(2,'Quelle est la devise française ?','Liberté','Égalité','Fraternité','Liberté, égalité, fraternité','Liberté, égalité, fraternité','La devise officielle est « Liberté, égalité, fraternité ».'),\n"
                    +
                    "(2,'Quel est le plus grand pays du monde ?','Chine','Russie','Canada','États-Unis','Russie','La Russie est le pays le plus vaste.'),\n"
                    +
                    "(2,'Quelle ville est connue pour son carnaval annuel ?','Venise','Rio','Nice','Mardi Gras','Rio','Le carnaval de Rio est célèbre mondialement.'),\n"
                    +
                    "(2,'Quel scientifique a découvert la gravité ?','Newton','Einstein','Galilée','Curie','Newton','Isaac Newton a formulé la loi de la gravité.'),\n"
                    +
                    "(2,'Quelle est la capitale de l’Espagne ?','Madrid','Barcelone','Valence','Séville','Madrid','Madrid est la capitale espagnole.'),\n"
                    +
                    "(2,'Quelle invention est attribuée aux frères Lumière ?','Téléphone','Cinéma','Télévision','Radio','Cinéma','Ils sont considérés comme les inventeurs du cinéma.'),\n"
                    +
                    "(2,'Quelle est la langue la plus parlée dans le monde ?','Anglais','Mandarin','Espagnol','Français','Mandarin','Le mandarin est la langue la plus parlée.'),\n"
                    +
                    "(2,'Quel animal est le plus rapide sur terre ?','Lion','Guépard','Gazelle','Antilope','Guépard','Le guépard est l’animal terrestre le plus rapide.'),\n"
                    +
                    "(2,'Quel monument est situé à New York ?','Statue de la Liberté','Big Ben','Tour Eiffel','Colisée','Statue de la Liberté','La Statue de la Liberté est un symbole américain.'),\n"
                    +
                    "(2,'Quelle est la principale religion en Inde ?','Christianisme','Bouddhisme','Hindouisme','Islam','Hindouisme','L’hindouisme est la religion majoritaire en Inde.'),\n"
                    +
                    "(2,'Quel élément est le principal composant de l’eau ?','Hydrogène','Oxygène','Azote','Carbone','Hydrogène','L’eau est composée d’hydrogène et d’oxygène.'),\n"
                    +
                    "(2,'Quel est le plus grand fleuve du monde ?','Amazon','Nil','Yangtsé','Mississippi','Amazon','L’Amazone est le plus grand fleuve par débit.'),\n"
                    +
                    "(2,'Quel est le sport le plus populaire au monde ?','Basket','Football','Tennis','Rugby','Football','Le football est le sport le plus pratiqué globalement.'),\n"
                    +
                    "(2,'Quelle guerre a duré de 1914 à 1918 ?','Seconde Guerre mondiale','Première Guerre mondiale','Guerre froide','Guerre de Cent Ans','Première Guerre mondiale','La Première Guerre mondiale a duré de 1914 à 1918.'),\n"
                    +
                    "(2,'Quel est l’élément chimique symbolisé par Fe ?','Fer','Fluor','Francium','Fermium','Fer','Fe est le symbole chimique du fer.'),\n"
                    +
                    "(2,'Quel pays a inventé la pizza ?','France','Espagne','Italie','Grèce','Italie','La pizza est originaire d’Italie.'),\n"
                    +
                    "(2,'Quelle est la devise des États-Unis ?','In God We Trust','E pluribus unum','Liberté','Justice','E pluribus unum','« E pluribus unum » est la devise officielle américaine.'),\n"
                    +
                    "(2,'Quel est le plus haut sommet du monde ?','Mont Blanc','Everest','K2','Kangchenjunga','Everest','Le mont Everest est le plus haut sommet terrestre.'),\n"
                    +
                    "(2,'Quelle ville est célèbre pour ses gondoles ?','Venise','Amsterdam','Bruges','Florence','Venise','Venise est connue pour ses canaux et gondoles.'),\n"
                    +
                    "(2,'Quel océan est le plus vaste ?','Atlantique','Pacifique','Indien','Arctique','Pacifique','L’océan Pacifique est le plus vaste du globe.'),\n"
                    +
                    "(2,'Quel est le plus grand pays d’Afrique ?','Algérie','Égypte','Libye','Soudan','Algérie','L’Algérie est le plus grand pays africain.'),\n"
                    +
                    "(2,'Qui a écrit Les Misérables ?','Hugo','Balzac','Dumas','Zola','Hugo','Victor Hugo est l’auteur des Misérables.'),\n"
                    +
                    "(2,'Quel est l’animal emblématique de l’Australie ?','Kangourou','Koala','Émeu','Dingo','Kangourou','Le kangourou est le symbole animalier de l’Australie.'),\n"
                    +
                    "(2,'Quel métal est liquide à température ambiante ?','Mercure','Plomb','Aluminium','Zinc','Mercure','Le mercure est un métal liquide à température ambiante.'),\n"
                    +
                    "(2,'Quelle langue est parlée au Québec ?','Anglais','Français','Espagnol','Italien','Français','Le français est la langue principale au Québec.'),\n"
                    +
                    "(2,'Quel pays a gagné la Coupe du Monde 2018 ?','France','Brésil','Allemagne','Argentine','France','La France a remporté la Coupe du Monde 2018.'),\n"
                    +
                    "(2,'Quelle ville est la capitale du Japon ?','Kyoto','Osaka','Tokyo','Hiroshima','Tokyo','Tokyo est la capitale du Japon.'),\n"
                    +
                    "(2,'Quelle planète est surnommée la planète rouge ?','Mars','Jupiter','Saturne','Venus','Mars','Mars est surnommée la planète rouge à cause de son sol.'),\n"
                    +
                    "(2,'Fils de Laïos et de Jocaste, qui Oedipe a-t-il tué dans la mythologie grecque ?','Son père','Sa mère','Sa fille','Son fils','Oedipe, dans la mythologie grecque, tue son père Laïos sans savoir qui il est, accomplissant ainsi une prophétie tragique.'),\n"
                    +
                    "(2,'En France, à la Libération, combien se retrouvèrent à Notre-Dame de Paris ?','5 000','9 000','13 000','17 000','Lors de la Libération de Paris en 1944, environ 13 000 personnes se sont rassemblées à Notre-Dame pour célébrer la fin de l''occupation allemande.'),\n"
                    +
                    "(2,'En quelle année a été bu le premier Coca-Cola concocté par John Pemberton ?','1886','1906','1926','1946','Le premier Coca-Cola a été concocté en 1886 par le pharmacien John Pemberton, à Atlanta.'),\n"
                    +
                    "(2,'Dans quelle ville se sont déroulés les Jeux olympiques de 1900 ?','Athènes','Rome','Los Angeles','Paris','Les Jeux Olympiques de 1900 se sont déroulés à Paris, en France, et ont été les premiers à inclure des femmes comme participantes.'),\n"
                    +
                    "(2,'À quelle substance naturelle ou synthétique la pénicilline est-elle associée ?','Antibiotique','Anesthésique','Soporifique','Analgésique','La pénicilline est le premier antibiotique découvert par Alexander Fleming en 1928, révolutionnant le traitement des infections bactériennes.'),\n"
                    +
                    "(2,'Quel prix Nobel Winston Leonard Spencer-Churchill a-t-il reçu en 1953 ?','Physique','Littérature','Paix','Chimie','Winston Churchill a reçu le prix Nobel de littérature en 1953, en reconnaissance de ses écrits historiques et oratoires.'),\n"
                    +
                    "(2,'En quelle année a-t-on pu assister à l''inauguration du métro parisien ?','1880','1860','1840','1900','Le métro parisien a été inauguré en 1900 lors de l''Exposition universelle, marquant le début de l''urbanisation moderne à Paris.'),\n"
                    +
                    "(2,'Quel écrivain et journaliste littéraire français fut surnommé le taureau normand ?','Victor Hugo','Guy de Maupassant','Honoré de Balzac','Alfred de Musset','Guy de Maupassant, écrivain réaliste français, a été surnommé \"le taureau normand\" en raison de sa personnalité énergique et de son origine normande.'),\n"
                    +
                    "(3,'Quel film a remporté l''Oscar du meilleur film en 1994 ?','Forrest Gump','Pulp Fiction','The Shawshank Redemption','The Lion King','Forrest Gump a remporté l''Oscar du meilleur film en 1994, grâce à sa performance émotive et son message puissant.'),\n"
                    +
                    "(3,'Qui a réalisé le film \"Inception\" ?','Steven Spielberg','James Cameron','Christopher Nolan','Quentin Tarantino','Christopher Nolan a réalisé \"Inception\" en 2010, un film de science-fiction acclamé pour sa narration complexe.'),\n"
                    +
                    "(3,'Quel film a pour héros un lionceau ?','Bambi','Le Roi Lion','Dumbo','Shrek','Le Roi Lion','Le Roi Lion raconte l’histoire d’un lionceau nommé Simba.'),\n"
                    +
                    "(3,'Quel acteur joue Iron Man ?','Holland','Downey','Evans','Hemsworth','Downey','Robert Downey Jr incarne Iron Man.'),\n"
                    +
                    "(3,'Quel film est un classique de 1994 avec un lion ?','Gladiator','Le Roi Lion','Tarzan','Le Livre de la Jungle','Le Roi Lion','Le Roi Lion est sorti en 1994.'),\n"
                    +
                    "(3,'Quel réalisateur est célèbre pour \"Jurassic Park\" ?','Spielberg','Scorsese','Kubrick','Tarantino','Spielberg','Steven Spielberg a réalisé \"Jurassic Park\".'),\n"
                    +
                    "(3,'Quel film raconte l’histoire de pirates au trésor ?','Pirates des Caraïbes','Titanic','Les Dents de la Mer','Indiana Jones','Pirates des Caraïbes','Pirates des Caraïbes suit des aventures de pirates. '),\n"
                    +
                    "(3,'Quel acteur joue dans \"Pirates des Caraïbes\" ?','DiCaprio','Depp','Pitt','Ford','Depp','Johnny Depp joue Jack Sparrow dans \"Pirates des Caraïbes\".'),\n"
                    +
                    "(3,'Quel film a pour thème principal la guerre ?','Forrest Gump','Il faut sauver le soldat Ryan','Toy Story','Gladiator','Il faut sauver le soldat Ryan','Ce film traite de la Seconde Guerre mondiale.'),\n"
                    +
                    "(3,'Quelle actrice a joué dans \"Titanic\" ?','Winslet','Watson','Stone','Blanchett','Winslet','Kate Winslet est l’héroïne dans \"Titanic\".'),\n"
                    +
                    "(3,'Quel est le nom du sorcier dans \"Harry Potter\" ?','Ron','Harry','Hermione','Draco','Harry','Harry Potter est le personnage principal sorcier.'),\n"
                    +
                    "(3,'Quel film d’animation est produit par Pixar ?','Shrek','Toy Story','Kung Fu Panda','Moi, moche et méchant','Toy Story','Toy Story est un film d’animation Pixar.'),\n"
                    +
                    "(3,'Qui est le réalisateur de \"Pulp Fiction\" ?','Tarantino','Nolan','Spielberg','Scorsese','Tarantino','Quentin Tarantino a réalisé \"Pulp Fiction\".'),\n"
                    +
                    "(3,'Quel film met en scène un super-héros chauve ?','Batman','Deadpool','Dr. Manhattan','Spawn','Dr. Manhattan','Dr. Manhattan est chauve et super puissant.'),\n"
                    +
                    "(3,'Quel film est basé sur une histoire vraie de fuite de prison ?','La Ligne verte','Les Évadés','Inception','Shawshank','Les Évadés','\"Les Évadés\" raconte une évasion de prison réelle.'),\n"
                    +
                    "(3,'Qui joue le rôle principal dans \"Gladiator\" ?','Crowe','DiCaprio','Smith','Eastwood','Crowe','Russell Crowe joue Maximus dans \"Gladiator\".'),\n"
                    +
                    "(3,'Quel film met en scène un robot nommé WALL-E ?','Robots','WALL-E','Transformers','I, Robot','WALL-E','WALL-E est un robot dans le film du même nom.'),\n" + "(3,'Quel film est une saga sur des étoiles et des Jedi ?','Star Trek','Star Wars','Matrix','Avatar','Star Wars','Star Wars raconte la lutte entre Jedi et Sith.'),\n"
                    +
                    "(3,'Quel acteur joue dans la trilogie \"Matrix\" ?','Reeves','Smith','Pitt','Clooney','Reeves','Keanu Reeves incarne Neo dans \"Matrix\".'),\n"
                    +
                    "(3,'Quel film met en scène une épée laser ?','Harry Potter','Star Wars','Seigneur des Anneaux','Avengers','Star Wars','Les épées laser sont dans Star Wars.'),\n"
                    +
                    "(3,'Quel film raconte une invasion extraterrestre ?','Independence Day','Gravity','Titanic','Avatar','Independence Day','Ce film traite d’une attaque alien sur Terre.'),\n"
                    +
                    "(3,'Quel acteur joue Jack Sparrow ?','Depp','DiCaprio','Pitt','Hanks','Depp','Johnny Depp joue Jack Sparrow.'),\n"
                    +
                    "(3,'Quelle actrice joue dans \"Wonder Woman\" ?','Stone','Gadot','Watson','Johansson','Gadot','Gal Gadot incarne Wonder Woman.'),\n"
                    + "(3,'Quel film raconte l’histoire d’un magicien nommé Gandalf ?','Harry Potter','Le Hobbit','Le Seigneur des Anneaux','Narnia','Le Seigneur des Anneaux','Gandalf est un personnage de la saga du Seigneur des Anneaux.'),\n"
                    +
                    "(3,'Qui a réalisé \"Avatar\" ?','Nolan','Cameron','Spielberg','Tarantino','Cameron','James Cameron a réalisé \"Avatar\".'),\n"
                    +
                    "(3,'Quel film est célèbre pour la phrase \"Je suis ton père\" ?','Star Wars','Matrix','Gladiator','Titanic','Star Wars','Cette phrase est prononcée dans Star Wars.'),\n"
                    +
                    "(3,'Quel film d’animation met en scène des jouets ?','Toy Story','Monstres & Cie','Cars','Ratatouille','Toy Story','Toy Story raconte la vie des jouets.'),\n"
                    +
                    "(3,'Quel film raconte une histoire d’amour sur un paquebot ?','Titanic','La La Land','Casablanca','Avatar','Titanic','Titanic est centré sur une romance en mer.'),\n"
                    +
                    "(3,'Quel acteur joue dans \"Forrest Gump\" ?','Tom Hanks','Brad Pitt','Leonardo DiCaprio','Matt Damon','Tom Hanks','Tom Hanks joue Forrest Gump.'),\n"
                    +
                    "(3,'Quel film est une comédie romantique célèbre ?','Love Actually','Avengers','Inception','Gladiator','Love Actually','Love Actually est une célèbre comédie romantique.'),\n"
                    +
                    "(3,'Quel film met en scène un tueur à gages nommé John Wick ?','John Wick','Die Hard','Speed','Taken','John Wick','John Wick est un film centré sur un tueur à gages.'),\n"
                    +
                    "(3,'Quel film est basé sur un jeu vidéo célèbre ?','Resident Evil','Avatar','Inception','Titanic','Resident Evil','Resident Evil est une adaptation de jeu vidéo.'),\n"
                    +
                    "(3,'Quel acteur joue dans \"Iron Man\" ?','Evans','Downey','Holland','Hemsworth','Downey','Robert Downey Jr est Iron Man.'),\n"
                    +
                    "(3,'Quel film raconte une histoire de zombies ?','28 Jours Plus Tard','World War Z','Titanic','Forrest Gump','World War Z','World War Z est centré sur une pandémie zombie.'),\n"
                    +
                    "(3,'Quel réalisateur est célèbre pour \"Shining\" ?','Kubrick','Spielberg','Tarantino','Nolan','Kubrick','Stanley Kubrick a réalisé \"Shining\".'),\n"
                    +
                    "(3,'Quel film a pour thème la conquête spatiale ?','Interstellar','Gravity','Inception','Avatar','Interstellar','Interstellar traite de la conquête spatiale.'),\n"
                    +
                    "(3,'Quel film met en scène un détective nommé Sherlock Holmes ?','Sherlock','Se7en','Zodiac','Gone Girl','Sherlock','Sherlock Holmes est un célèbre détective fictif.'),\n"
                    +
                    "(3,'Quel film raconte la vie de la reine Victoria ?','The Queen','Victoria','Elizabeth','Marie Antoinette','Victoria','Ce film raconte la vie de la reine Victoria.'),\n"
                    +
                    "(3,'Quel acteur joue dans \"Les Avengers\" ?','Hawkeye','Downey','Evans','Tous','Tous','Tous ces acteurs jouent dans Les Avengers.'),\n"
                    +
                    "(3,'Quel film d’animation est produit par Disney ?','Shrek','La Reine des Neiges','Toy Story','Kung Fu Panda','La Reine des Neiges','Disney a produit La Reine des Neiges.'),\n"
                    +
                    "(3,'Quel film raconte une histoire d’espionnage avec James Bond ?','Mission Impossible','James Bond','Jason Bourne','Kingsman','James Bond','James Bond est célèbre pour ses missions d’espionnage.'),\n"
                    +
                    "(3,'Quel acteur joue dans \"Fight Club\" ?','Pitt','DiCaprio','Hanks','Crowe','Pitt','Brad Pitt est un des acteurs principaux de Fight Club.'),\n"
                    +
                    "(3,'Quel acteur incarne le personnage de Jack Dawson dans \"Titanic\" ?','Brad Pitt','Johnny Depp','Leonardo DiCaprio','Matt Damon','Leonardo DiCaprio joue le rôle de Jack Dawson, l''amoureux tragique dans \"Titanic\", réalisé par James Cameron.'),\n"
                    +
                    "(3,'Quel film est célèbre pour la réplique \"Que la Force soit avec toi\" ?','Star Wars','The Matrix','Jurassic Park','Indiana Jones','\"Star Wars\" est célèbre pour la réplique \"Que la Force soit avec toi\", un film de science-fiction emblématique de George Lucas.'),\n"
                    +
                    "(3,'Qui a interprété le rôle de The Joker dans \"The Dark Knight\" ?','Jack Nicholson','Heath Ledger','Jared Leto','Tom Hardy','Heath Ledger a interprété The Joker dans \"The Dark Knight\" (2008), un rôle qui lui a valu un Oscar posthume.'),\n"
                    +
                    "(3,'Dans quel film Tim Burton a-t-il mis en scène un personnage nommé Edward Scissorhands ?','Beetlejuice','Batman','Edward aux mains d''argent','Sweeney Todd','Tim Burton a réalisé \"Edward aux mains d''argent\" en 1990, un film où Johnny Depp incarne un personnage avec des mains en ciseaux.'),\n"
                    +
                    "(3,'Quel film d''animation de Pixar raconte l''histoire d''un robot appelé WALL-E ?','Toy Story','WALL-E','Monstres et Cie','Les Indestructibles','WALL-E est un film d''animation de Pixar de 2008 qui suit un robot solitaire dans un futur dystopique.'),\n"
                    +
                    "(3,'Qui a réalisé \"Pulp Fiction\" ?','Quentin Tarantino','Martin Scorsese','Francis Ford Coppola','Stanley Kubrick','Quentin Tarantino a réalisé \"Pulp Fiction\" en 1994, un film révolutionnaire connu pour sa narration non linéaire et son dialogue distinctif.'),\n"
                    +
                    "(3,'Quel acteur a incarné le rôle de Tony Stark / Iron Man dans l''univers Marvel ?','Robert Downey Jr.','Chris Hemsworth','Chris Evans','Mark Ruffalo','Robert Downey Jr. a incarné le personnage de Tony Stark / Iron Man dans l''univers Marvel, débutant avec \"Iron Man\" en 2008.'),\n"
                    +
                    "(3,'Dans quel film de science-fiction de 1999, Keanu Reeves joue-t-il un personnage nommé Neo ?','Matrix','Terminator','Starship Troopers','Blade Runner','\"Matrix\" est un film de science-fiction de 1999 où Keanu Reeves incarne Neo, un hacker qui découvre la réalité virtuelle.'),\n"
                    +
                    "(4,'Quel groupe a chanté \"Bohemian Rhapsody\" ?','The Beatles','Led Zeppelin','Queen','Pink Floyd','\"Bohemian Rhapsody\" est une chanson iconique de Queen, écrite par Freddie Mercury, et publiée en 1975.'),\n"
                    +
                    "(4,'Quel est le titre de l''album de Michael Jackson qui contient le single \"Thriller\" ?','Bad','Dangerous','Off the Wall','Thriller','L''album \"Thriller\", sorti en 1982, est l''album le plus vendu de tous les temps, avec la chanson \"Thriller\" comme emblématique.'),\n"
                    +
                    "(4,'Qui est surnommé le Roi du Pop ?','Elvis','Michael','Prince','Madonna','Michael','Michael Jackson est connu comme le Roi de la Pop.'),\n"
                    +
                    "(4,'Quel instrument a 6 cordes ?','Violon','Guitare','Piano','Flûte','Guitare','La guitare classique a 6 cordes.'),\n"
                    +
                    "(4,'Quel style musical vient de la Jamaïque ?','Jazz','Reggae','Blues','Rock','Reggae','Le reggae est un genre musical jamaïcain.'),\n"
                    +
                    "(4,'Quel groupe est célèbre pour \"Bohemian Rhapsody\" ?','Queen','Beatles','Pink Floyd','U2','Queen','Queen est célèbre pour cette chanson.'),\n"
                    +
                    "(4,'Quel instrument est soufflé ?','Guitare','Piano','Trompette','Batterie','Trompette','La trompette est un instrument à vent.'),\n"
                    +
                    "(4,'Qui a chanté \"Thriller\" ?','Prince','Michael','Elvis','David','Michael','Michael Jackson a chanté \"Thriller\".'),\n"
                    +
                    "(4,'Quel est le genre de musique classique ?','Jazz','Baroque','Pop','Rock','Baroque','La musique baroque est un style classique.'),\n"
                    +
                    "(4,'Quel artiste est connu pour \"Like a Virgin\" ?','Madonna','Beyoncé','Lady Gaga','Céline','Madonna','Madonna a popularisé \"Like a Virgin\".'),\n"
                    +
                    "(4,'Quel instrument a un clavier ?','Guitare','Piano','Batterie','Violon','Piano','Le piano possède un clavier.'),\n"
                    +
                    "(4,'Quel genre musical est originaire des États-Unis ?','Reggae','Jazz','Salsa','Flamenco','Jazz','Le jazz est né aux États-Unis.'),\n"
                    +
                    "(4,'Qui est le leader des Beatles ?','John','Paul','George','Ringo','John','John Lennon était le leader principal.'),\n"
                    +
                    "(4,'Quel groupe a chanté \"Hey Jude\" ?','Queen','Beatles','Rolling Stones','Pink Floyd','Beatles','\"Hey Jude\" est une chanson des Beatles.'),\n"
                    +
                    "(4,'Quel style musical est associé au rap ?','Classique','Jazz','Hip-hop','Country','Hip-hop','Le hip-hop est le style principal du rap.'),\n"
                    +
                    "(4,'Quel instrument est à percussion ?','Violon','Guitare','Batterie','Flûte','Batterie','La batterie est un instrument de percussion.'),\n"
                    +
                    "(4,'Quel chanteur est surnommé The Boss ?','Elvis','Bruce','Michael','Prince','Bruce','Bruce Springsteen est surnommé The Boss.'),\n"
                    +
                    "(4,'Quelle chanteuse est célèbre pour \"Rolling in the Deep\" ?','Adele','Beyoncé','Madonna','Taylor','Adele','Adele a chanté \"Rolling in the Deep\".'),\n"
                    +
                    "(4,'Quel instrument est joué avec un archet ?','Piano','Violon','Guitare','Batterie','Violon','Le violon se joue avec un archet.'),\n"
                    +
                    "(4,'Quel style musical vient de Louisiane ?','Jazz','Reggae','Rock','Classique','Jazz','Le jazz a des racines en Louisiane.'),\n"
                    +
                    "(4,'Quel groupe est connu pour \"Stairway to Heaven\" ?','Queen','Led Zeppelin','Pink Floyd','U2','Led Zeppelin','\"Stairway to Heaven\" est un tube de Led Zeppelin.'),\n"
                    +
                    "(4,'Quel chanteur est mort en 1977 ?','Elvis','Michael','Prince','Freddie','Elvis','Elvis Presley est décédé en 1977.'),\n"
                    +
                    "(4,'Quel instrument a des touches blanches et noires ?','Violon','Piano','Guitare','Flûte','Piano','Le piano possède des touches blanches et noires.'),\n"
                    +
                    "(4,'Quel genre musical est originaire du Brésil ?','Salsa','Bossa Nova','Flamenco','Reggae','Bossa Nova','La Bossa Nova vient du Brésil.'),\n"
                    +
                    "(4,'Qui a chanté \"Like a Rolling Stone\" ?','Bob Dylan','Elvis','Michael','Prince','Bob Dylan','Bob Dylan est l’auteur de cette chanson.'),\n"
                    +
                    "(4,'Quel instrument est en bois et à vent ?','Trompette','Flûte','Piano','Guitare','Flûte','La flûte est un instrument à vent en bois.'),\n"
                    +
                    "(4,'Quel genre musical est populaire en Jamaïque ?','Reggae','Jazz','Pop','Classique','Reggae','Le reggae est populaire en Jamaïque.'),\n"
                    +
                    "(4,'Quel groupe a chanté \"Hotel California\" ?','Beatles','Eagles','Queen','U2','Eagles','\"Hotel California\" est un tube des Eagles.'),\n"
                    +
                    "(4,'Quel chanteur est connu pour sa danse \"Moonwalk\" ?','Michael','Prince','Elvis','David','Michael','Michael Jackson a popularisé le moonwalk.'),\n"
                    +
                    "(4,'Quel instrument est électrique et à cordes ?','Batterie','Guitare','Piano','Flûte','Guitare','La guitare électrique est courante dans le rock.'),\n"
                    +
                    "(4,'Quelle chanteuse est surnommée Queen of Pop ?','Madonna','Beyoncé','Lady Gaga','Adele','Madonna','Madonna est appelée la reine de la pop.'),\n"
                    +
                    "(4,'Quel est le nom du chanteur principal du groupe The Rolling Stones ?','Mick Jagger','Keith Richards','Paul McCartney','Jim Morrison','Mick Jagger est le chanteur principal du groupe The Rolling Stones, formé en 1962 et toujours actif.'),\n"
                    +
                    "(4,'Quel genre musical est représenté par le groupe The Beatles ?','Jazz','Rock','Reggae','Classique','The Beatles sont un groupe de rock britannique légendaire, influençant profondément la musique populaire du 20ème siècle.'),\n"
                    +
                    "(4,'Qui est l''auteur-compositeur du tube \"Like a Rolling Stone\" ?','Bob Dylan','Jimi Hendrix','Bruce Springsteen','Neil Young','\"Like a Rolling Stone\" est une chanson de Bob Dylan, considérée comme l''une des plus grandes chansons de l''histoire de la musique rock.'),\n"
                    +
                    "(4,'Quel artiste a popularisé la chanson \"Like a Prayer\" en 1989 ?','Madonna','Cher','Cyndi Lauper','Janet Jackson','\"Like a Prayer\" est une chanson emblématique de Madonna, sortie en 1989, qui mélange pop et gospel.'),\n"
                    +
                    "(4,'Quel est le nom de l''album qui a propulsé Nirvana au sommet avec le titre \"Smells Like Teen Spirit\" ?','In Utero','Bleach','MTV Unplugged','Nevermind','\"Nevermind\" de Nirvana, sorti en 1991, a marqué un tournant dans le rock alternatif, avec \"Smells Like Teen Spirit\" comme chanson phare.'),\n"
                    +
                    "(4,'Quel genre musical est principalement associé à Elvis Presley ?','Rock & Roll','Pop','Jazz','Soul','Elvis Presley est une icône du rock & roll, fusionnant des éléments de musique noire et de la culture populaire des années 50.'),\n"
                    +
                    "(4,'Quel groupe a sorti l''album \"The Dark Side of the Moon\" en 1973 ?','Led Zeppelin','The Who','Pink Floyd','The Rolling Stones','\"The Dark Side of the Moon\" de Pink Floyd est un album conceptuel qui est devenu un classique de la musique rock progressif.'),\n"
                    +
                    "(4,'Quel musicien est surnommé \"le roi de la pop\" ?','Prince','Michael Jackson','Elton John','Justin Timberlake','Michael Jackson est surnommé \"le roi de la pop\" en raison de ses nombreuses contributions au genre pop et de son impact mondial.');";

    private BaseDeDonnees(Context context)
    {
        super(context, NOM_BDD, null, VERSION_BDD);
        Log.d(TAG, "BaseDeDonnees()");
        sqlite = this.getWritableDatabase();
    }

    public synchronized static BaseDeDonnees getInstance(Context context)
    {
        Log.d(TAG, "getInstance()");
        if(baseDeDonnees == null)
        {
            baseDeDonnees = new BaseDeDonnees(context);
        }
        return baseDeDonnees;
    }

    public void effacer()
    {
        Log.d(TAG, "effacer()");
        onUpgrade(sqlite, sqlite.getVersion(), sqlite.getVersion() + 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate()");
        db.execSQL(CREE_TABLE_PARTICIPANT);
        db.execSQL(CREE_TABLE_THEME);
        db.execSQL(CREE_TABLE_QUIZZ);
        db.execSQL(CREE_TABLE_QUESTION);
        db.execSQL(CREE_TABLE_RESULTATS);
        db.execSQL(CREE_TABLE_REPONSE);
        insererDonnees(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANT);
        db.setVersion(newVersion);
        onCreate(db);
    }

    private void insererDonnees(SQLiteDatabase db)
    {
        Log.d(TAG, "insererDonnees()");
        // Pour les tests
        db.execSQL(AJOUTE_PARTICIPANTS);
        db.execSQL(AJOUTE_THEMES);
        db.execSQL(AJOUTE_QUESTIONS);
    }

    public ArrayList<String> getThemes()
    {
        ArrayList<String> themes  = new ArrayList<String>();
        Cursor            curseur = sqlite.rawQuery("SELECT theme FROM table_theme", null);
        if(curseur.moveToFirst())
        {
            do
            {
                String theme = curseur.getString(0);
                themes.add(theme);
            } while(curseur.moveToNext());
        }

        curseur.close();
        Log.d(TAG, "getThemes() " + themes);
        return themes;
    }

    public ArrayList<String> getParticipants()
    {
        ArrayList<String> participants = new ArrayList<String>();
        Cursor curseur = sqlite.rawQuery("SELECT prenom FROM table_participant", null);

        if(curseur.moveToFirst())
        {
            do {
                String participant = curseur.getString(0);
                participants.add(participant);
            } while(curseur.moveToNext());
        }

        curseur.close();
        Log.d(TAG, "getParticipant() " + participants);

        return participants;
    }

    public String getQuestionAleatoireParTheme(int themeID)
    {
        String trameQuestion = "";
        Cursor curseur = sqlite.rawQuery(
                "SELECT question, proposition1, proposition2, proposition3, proposition4, reponse, explication " +
                        "FROM table_question WHERE themeID = ? ORDER BY RANDOM() LIMIT 1",
                new String[]{String.valueOf(themeID)}
        );

        if (curseur.moveToFirst())
        {
            trameQuestion = "@@Q;" +
                    curseur.getString(0) + ";" +  // question
                    curseur.getString(1) + ";" +  // prop1
                    curseur.getString(2) + ";" +  // prop2
                    curseur.getString(3) + ";" +  // prop3
                    curseur.getString(4) + ";" +  // prop4
                    curseur.getInt(5) + ";" +     // réponse (index)
                    curseur.getString(6) + "\n";  // explication
        }
        curseur.close();
        return trameQuestion;
    }
}