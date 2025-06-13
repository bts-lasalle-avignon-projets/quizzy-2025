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
    public static final int      VERSION_BDD = 7;
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
            + " (4,'Musique'),\n"
            + " (5,'Geek')";

    private static final String AJOUTE_QUESTIONS =
            "INSERT INTO " + TABLE_QUESTION +
                    "(themeID, question, proposition1, proposition2, proposition3, proposition4, reponse, explication) VALUES \n" +
                    "(1,'Quel programmeur a créé et continue de diriger le développement du noyau de Linux ?','Steeve Jobs','Linus Torvalds','Larry Ellison','Bill Gates',2,'Linus Torvalds a découvert l''informatique vers l''âge de 11 ans grâce à l''ordinateur de son grand-père, un Commodore VIC-20.'),\n" +
                    "(1,'Quel est le principal atout de Linux, développé et maintenu par Linus Torvalds ?','Il est libre','Il est beau','Il est Finlandais','Il est amusant',1,'Linux est un système d''exploitation open-source, gratuit, et modifiable. Il est développé et maintenu par Torvalds avec l''aide de contributeurs.'),\n" +
                    "(1,'Quel animal représentant Linux est aussi la mascotte de l''université d''Helsinki ?','Marmotte','Caribou','Gnou','Manchot',4,'Le manchot est la mascotte de Linux, choisie pour son aspect sympathique et facile à identifier.'),\n" +
                    "(1,'Laquelle de ces propositions désigne une distribution Linux fondée en 1993 ?','Zubuntu','Red Hat','Souze','Mandrika',2,'Red Hat, fondée en 1993, est une distribution Linux très populaire dans le monde professionnel.'),\n" +
                    "(1,'Quel système d''exploitation mobile majeur de l''industrie s''appuie sur un noyau Linux ?','iOS','Windows Phone','Android','BlackBerry 10',3,'Android utilise le noyau Linux, ce qui en fait l''un des systèmes d''exploitation mobiles les plus utilisés.'),\n" +
                    "(1,'Sous Linux, comment appelle-t-on les logiciels assemblés autour du noyau ?','Progiciel','Paquet','Distribution','Logithèque',3,'Une distribution est un ensemble de logiciels qui fonctionne avec le noyau Linux.'),\n" +
                    "(1,'Quel serveur web présent sous Linux est aussi présent sur les serveurs du monde entier ?','Apache','Comanche','Sioux','Mohican',1,'Apache est l''un des serveurs web les plus utilisés au monde, y compris sur des systèmes Linux. Il est open-source et largement adopté pour héberger des sites web.'),\n" +
                    "(1,'Quel est le nom de la mascotte de Linux, connue des mordus du système d''exploitation ?','Wilber','Tux','Gnu','Puffy',2,'Tux est la mascotte officielle de Linux, un manchot qui représente le système d''exploitation open-source créé par Linus Torvalds.'),\n" +
                    "(1,'En quelle année Linus Torvalds a-t-il livré la première version du noyau Linux ?','1991','1993','1995','1997',1,'La première version du noyau Linux a été publiée en 1991 par Linus Torvalds, marquant le début du développement du système d''exploitation.'),\n" +
                    "(1,'Quel ancien mot bantou désigne une célèbre distribution Linux ?','Umbro','Ubuntu','Uhura','Ursula',2,'Ubuntu vient d''un mot bantou signifiant \"humanité envers les autres\".'),\n" +
                    "(2,'Quelle espèce d''oiseaux, encore présente en Europe, gringotte, quiritte ou trille ?','Rossignol','Corbeau','Perroquet','Moineau',1,'Le rossignol est un petit oiseau chanteur, célèbre pour son chant mélodieux, encore présent dans certaines régions d''Europe.'),\n" +
                    "(2,'Laquelle de ces professions ne peut-on associer au grand Léonard de Vinci ?','Peintre','Sculpteur','Auteur dramatique','Botaniste',3,'Léonard de Vinci était principalement connu comme peintre, sculpteur et inventeur, mais il n''était pas auteur dramatique.'),\n" +
                    "(2,'Fils de Laïos et de Jocaste, qui Oedipe a-t-il tué dans la mythologie grecque ?','Son père','Sa mère','Sa fille','Son fils',1,'Oedipe, dans la mythologie grecque, tue son père Laïos sans savoir qui il est, accomplissant ainsi une prophétie tragique.'),\n" +
                    "(2,'En France, à la Libération, combien se retrouvèrent à Notre-Dame de Paris ?','5 000','9 000','13 000','17 000',3,'Lors de la Libération de Paris en 1944, environ 13 000 personnes se sont rassemblées à Notre-Dame pour célébrer la fin de l''occupation allemande.'),\n" +
                    "(2,'En quelle année a été bu le premier Coca-Cola concocté par John Pemberton ?','1886','1906','1926','1946',1,'Le premier Coca-Cola a été concocté en 1886 par le pharmacien John Pemberton, à Atlanta.'),\n" +
                    "(2,'Dans quelle ville se sont déroulés les Jeux olympiques de 1900 ?','Athènes','Rome','Los Angeles','Paris',4,'Les Jeux Olympiques de 1900 se sont déroulés à Paris, en France, et ont été les premiers à inclure des femmes comme participantes.'),\n" +
                    "(2,'À quelle substance naturelle ou synthétique la pénicilline est-elle associée ?','Antibiotique','Anesthésique','Soporifique','Analgésique',1,'La pénicilline est le premier antibiotique découvert par Alexander Fleming en 1928, révolutionnant le traitement des infections bactériennes.'),\n" +
                    "(2,'Quel prix Nobel Winston Leonard Spencer-Churchill a-t-il reçu en 1953 ?','Physique','Littérature','Paix','Chimie',2,'Winston Churchill a reçu le prix Nobel de littérature en 1953, en reconnaissance de ses écrits historiques et oratoires.'),\n" +
                    "(2,'En quelle année a-t-on pu assister à l''inauguration du métro parisien ?','1880','1860','1840','1900',4,'Le métro parisien a été inauguré en 1900 lors de l''Exposition universelle, marquant le début de l''urbanisation moderne à Paris.'),\n" +
                    "(2,'Quel écrivain et journaliste littéraire français fut surnommé le taureau normand ?','Victor Hugo','Guy de Maupassant','Honoré de Balzac','Alfred de Musset',2,'Guy de Maupassant, écrivain réaliste français, a été surnommé \"le taureau normand\" en raison de sa personnalité énergique et de son origine normande.'),\n" +
                    "(3,'Quel film a remporté l''Oscar du meilleur film en 1994 ?','Forrest Gump','Pulp Fiction','The Shawshank Redemption','The Lion King',1,'Forrest Gump a remporté l''Oscar du meilleur film en 1994, grâce à sa performance émotive et son message puissant.'),\n" +
                    "(3,'Qui a réalisé le film \"Inception\" ?','Steven Spielberg','James Cameron','Christopher Nolan','Quentin Tarantino',3,'Christopher Nolan a réalisé \"Inception\" en 2010, un film de science-fiction acclamé pour sa narration complexe.'),\n" +
                    "(3,'Quel acteur incarne le personnage de Jack Dawson dans \"Titanic\" ?','Brad Pitt','Johnny Depp','Leonardo DiCaprio','Matt Damon',3,'Leonardo DiCaprio joue le rôle de Jack Dawson, l''amoureux tragique dans \"Titanic\", réalisé par James Cameron.'),\n" +
                    "(3,'Quel film est célèbre pour la réplique \"Que la Force soit avec toi\" ?','Star Wars','The Matrix','Jurassic Park','Indiana Jones',1,'\"Star Wars\" est célèbre pour la réplique \"Que la Force soit avec toi\", un film de science-fiction emblématique de George Lucas.'),\n" +
                    "(3,'Qui a interprété le rôle de The Joker dans \"The Dark Knight\" ?','Jack Nicholson','Heath Ledger','Jared Leto','Tom Hardy',2,'Heath Ledger a interprété The Joker dans \"The Dark Knight\" (2008), un rôle qui lui a valu un Oscar posthume.'),\n" +
                    "(3,'Dans quel film Tim Burton a-t-il mis en scène un personnage nommé Edward Scissorhands ?','Beetlejuice','Batman','Edward aux mains d''argent','Sweeney Todd',3,'Tim Burton a réalisé \"Edward aux mains d''argent\" en 1990, un film où Johnny Depp incarne un personnage avec des mains en ciseaux.'),\n" +
                    "(3,'Quel film d''animation de Pixar raconte l''histoire d''un robot appelé WALL-E ?','Toy Story','WALL-E','Monstres et Cie','Les Indestructibles',2,'WALL-E est un film d''animation de Pixar de 2008 qui suit un robot solitaire dans un futur dystopique.'),\n" +
                    "(3,'Qui a réalisé \"Pulp Fiction\" ?','Quentin Tarantino','Martin Scorsese','Francis Ford Coppola','Stanley Kubrick',1,'Quentin Tarantino a réalisé \"Pulp Fiction\" en 1994, un film révolutionnaire connu pour sa narration non linéaire et son dialogue distinctif.'),\n" +
                    "(3,'Quel acteur a incarné le rôle de Tony Stark / Iron Man dans l''univers Marvel ?','Robert Downey Jr.','Chris Hemsworth','Chris Evans','Mark Ruffalo',1,'Robert Downey Jr. a incarné le personnage de Tony Stark / Iron Man dans l''univers Marvel, débutant avec \"Iron Man\" en 2008.'),\n" +
                    "(3,'Dans quel film de science-fiction de 1999, Keanu Reeves joue-t-il un personnage nommé Neo ?','Matrix','Terminator','Starship Troopers','Blade Runner',1,'\"Matrix\" est un film de science-fiction de 1999 où Keanu Reeves incarne Neo, un hacker qui découvre la réalité virtuelle.'),\n" +
                    "(4,'Quel groupe a chanté \"Bohemian Rhapsody\" ?','The Beatles','Led Zeppelin','Queen','Pink Floyd',3,'\"Bohemian Rhapsody\" est une chanson iconique de Queen, écrite par Freddie Mercury, et publiée en 1975.'),\n" +
                    "(4,'Quel est le titre de l''album de Michael Jackson qui contient le single \"Thriller\" ?','Bad','Dangerous','Off the Wall','Thriller',4,'L''album \"Thriller\", sorti en 1982, est l''album le plus vendu de tous les temps, avec la chanson \"Thriller\" comme emblématique.'),\n" +
                    "(4,'Quel est le nom du chanteur principal du groupe The Rolling Stones ?','Mick Jagger','Keith Richards','Paul McCartney','Jim Morrison',1,'Mick Jagger est le chanteur principal du groupe The Rolling Stones, formé en 1962 et toujours actif.'),\n" +
                    "(4,'Quel genre musical est représenté par le groupe The Beatles ?','Jazz','Rock','Reggae','Classique',2,'The Beatles sont un groupe de rock britannique légendaire, influençant profondément la musique populaire du 20ème siècle.'),\n" +
                    "(4,'Qui est l''auteur-compositeur du tube \"Like a Rolling Stone\" ?','Bob Dylan','Jimi Hendrix','Bruce Springsteen','Neil Young',1,'\"Like a Rolling Stone\" est une chanson de Bob Dylan, considérée comme l''une des plus grandes chansons de l''histoire de la musique rock.'),\n" +
                    "(4,'Quel artiste a popularisé la chanson \"Like a Prayer\" en 1989 ?','Madonna','Cher','Cyndi Lauper','Janet Jackson',1,'\"Like a Prayer\" est une chanson emblématique de Madonna, sortie en 1989, qui mélange pop et gospel.'),\n" +
                    "(4,'Quel est le nom de l''album qui a propulsé Nirvana au sommet avec le titre \"Smells Like Teen Spirit\" ?','In Utero','Bleach','MTV Unplugged','Nevermind',4,'\"Nevermind\" de Nirvana, sorti en 1991, a marqué un tournant dans le rock alternatif, avec \"Smells Like Teen Spirit\" comme chanson phare.'),\n" +
                    "(4,'Quel genre musical est principalement associé à Elvis Presley ?','Rock & Roll','Pop','Jazz','Soul',1,'Elvis Presley est une icône du rock & roll, fusionnant des éléments de musique noire et de la culture populaire des années 50.'),\n" +
                    "(4,'Quel groupe a sorti l''album \"The Dark Side of the Moon\" en 1973 ?','Led Zeppelin','The Who','Pink Floyd','The Rolling Stones',3,'\"The Dark Side of the Moon\" de Pink Floyd est un album conceptuel qui est devenu un classique de la musique rock progressif.'),\n" +
                    "(4,'Quel musicien est surnommé \"le roi de la pop\" ?','Prince','Michael Jackson','Elton John','Justin Timberlake',2,'Michael Jackson est surnommé \"le roi de la pop\" en raison de ses nombreuses contributions au genre pop et de son impact mondial.'),\n" +
                    "(5,'Dans quel jeu vidéo peut-on trouver la Triforce ?','The Witcher','Final Fantasy','The Legend of Zelda','Dark Souls',3,'La Triforce est un artefact emblématique de la série The Legend of Zelda.'),\n" +
                    "(5,'Quel est le nom du personnage principal de la série Halo ?','Marcus Fenix','Master Chief','Duke Nukem','Shepard',2,'Master Chief est le protagoniste emblématique de la série Halo.'),\n" +
                    "(5,'Quel est le premier jeu vidéo commercialisé ?','Space Invaders','Pong','Pac-Man','Tennis for Two',2,'Pong est considéré comme le premier jeu vidéo commercialisé à grande échelle en 1972.'),\n" +
                    "(5,'Dans quel jeu incarne-t-on un chasseur de monstres nommé Geralt de Riv ?','Dark Souls','The Witcher','Dragon Age','Skyrim',2,'Geralt de Riv est le personnage principal de la série The Witcher.'),\n" +
                    "(5,'Quel studio a développé la série Dark Souls ?','Capcom','FromSoftware','Ubisoft','Square Enix',2,'FromSoftware est le développeur derrière Dark Souls.'),\n" +
                    "(5,'Dans quel jeu trouve-t-on la ville de Rapture ?','BioShock','Fallout','Mass Effect','Half-Life',1,'Rapture est une ville sous-marine dystopique dans BioShock.'),\n" +
                    "(5,'Quel est le nom du plombier le plus célèbre des jeux vidéo ?','Luigi','Wario','Sonic','Mario',4,'Mario est la mascotte emblématique de Nintendo.'),\n" +
                    "(5,'Dans quel jeu vidéo peut-on trouver un creeper ?','Roblox','Terraria','Minecraft','Rust',3,'Les creepers sont des ennemis emblématiques de Minecraft.'),\n" +
                    "(5,'Qui est le créateur de Minecraft ?','Gabe Newell','Markus Persson','Elon Musk','Tim Sweeney',2,'Markus Persson, alias Notch, a créé Minecraft.'),\n" +
                    "(5,'Quel est le jeu vidéo le plus vendu de tous les temps ?','Tetris','Grand Theft Auto V','Minecraft','Fortnite',3,'Minecraft est le jeu vidéo le plus vendu dans l''histoire.'),\n" +
                    "(5,'Quel est le studio à l’origine de la série The Elder Scrolls ?','Rockstar Games','Bethesda','Bioware','Valve',2,'Bethesda Game Studios est le développeur de la série The Elder Scrolls.'),\n" +
                    "(5,'Quel personnage féminin est l’héroïne de la série Tomb Raider ?','Jill Valentine','Samus Aran','Lara Croft','Chun-Li',3,'Lara Croft est l’héroïne emblématique de la série Tomb Raider.'),\n" +
                    "(5,'Dans quel jeu incarne-t-on un tueur à gages nommé Agent 47 ?','Dishonored','Hitman','Max Payne','Watch Dogs',2,'Agent 47 est le personnage principal de la série Hitman.'),\n" +
                    "(5,'Quel jeu vidéo se déroule à Vice City ?','GTA San Andreas','GTA III','GTA Vice City','GTA IV',3,'Vice City est une ville fictive apparaissant dans le jeu GTA Vice City.'),\n" +
                    "(5,'Dans quel jeu incarne-t-on un chevalier mort-vivant cherchant à briser une malédiction ?','Dark Souls','Elden Ring','Bloodborne','Demon''s Souls',1,'Dark Souls met en scène un élu mort-vivant dans un monde en ruine.'),\n" +
                    "(5,'Dans quel jeu faut-il attraper tous les Pokémon ?','Pokémon','Digimon','Monster Rancher','Yu-Gi-Oh!',1,'Attraper tous les Pokémon est l’objectif principal dans les jeux Pokémon.'),\n" +
                    "(5,'Quelle console a popularisé le stick analogique ?','PlayStation','Sega Saturn','Nintendo 64','Dreamcast',3,'La manette de la Nintendo 64 introduisait un stick analogique central.'),\n" +
                    "(5,'Qui est le fondateur de Valve Corporation ?','Todd Howard','Gabe Newell','Hideo Kojima','Phil Spencer',2,'Gabe Newell a cofondé Valve et est célèbre pour la série Half-Life.'),\n" +
                    "(5,'Dans quel jeu joue-t-on une simulation de ferme avec des graphismes rétro ?','Harvest Moon','Stardew Valley','Animal Crossing','Rune Factory',2,'Stardew Valley est un jeu de ferme inspiré de Harvest Moon.'),\n" +
                    "(5,'Quel jeu de tir en ligne est célèbre pour ses skins et ses headshots ?','Overwatch','Apex Legends','Call of Duty','Counter-Strike: Global Offensive',4,'CS:GO est célèbre pour son gameplay compétitif et ses skins d’armes.'),\n" +
                    "(5,'Dans quel jeu incarne-t-on Kratos, le dieu de la guerre ?','God of War','Assassin''s Creed','Devil May Cry','Bayonetta',1,'Kratos est le protagoniste de la série God of War.'),\n" +
                    "(5,'Quel jeu indépendant met en scène un petit chevalier en quête dans un royaume souterrain ?','Dead Cells','Celeste','Hollow Knight','Ori and the Blind Forest',3,'Hollow Knight est un jeu d’action-exploration dans un monde souterrain.'),\n" +
                    "(5,'Quel est le nom de la princesse souvent sauvée par Mario ?','Daisy','Zelda','Peach','Pauline',3,'Peach est la princesse du Royaume Champignon souvent capturée.'),\n" +
                    "(5,'Quel jeu d’horreur se déroule dans une pizzeria avec des animatroniques ?','Outlast','Five Nights at Freddy’s','Silent Hill','Resident Evil',2,'Five Nights at Freddy’s met en scène des animatroniques hostiles.'),\n" +
                    "(5,'Dans quel jeu les joueurs s’affrontent pour être le dernier survivant ?','League of Legends','Among Us','Fortnite','Overwatch',3,'Fortnite est un battle royale populaire avec construction.'),\n" +
                    "(5,'Quel jeu consiste à identifier l’imposteur à bord d’un vaisseau spatial ?','Overcooked','Among Us','Destiny','Dead Space',2,'Among Us oppose imposteurs et coéquipiers dans une station spatiale.'),\n" +
                    "(5,'Quel est le nom du personnage principal dans la série Metal Gear ?','Big Boss','Liquid Snake','Raiden','Solid Snake',4,'Solid Snake est le héros emblématique de la saga Metal Gear.'),\n" +
                    "(5,'Quel jeu est célèbre pour la ligne Its dangerous to go alone! Take this. ?','Zelda II','Final Fantasy','The Legend of Zelda','Chrono Trigger',3,'Cette phrase culte vient du tout premier The Legend of Zelda.'),\n" +
                    "(5,'Quel jeu utilise des portails pour résoudre des énigmes ?','Quantum Conundrum','Half-Life','Portal','The Talos Principle',3,'Portal est un jeu de réflexion basé sur la création de portails.'),\n" +
                    "(5,'Dans quel jeu contrôle-t-on une boule bleue nommée Sonic ?','Mega Man','Sonic the Hedgehog','Crash Bandicoot','Rayman',2,'Sonic est la mascotte de SEGA depuis les années 1990.'),\n" +
                    "(5,'Quel jeu vidéo a popularisé le mode Battle Royale ?','H1Z1','PUBG','Call of Duty','Apex Legends',2,'PlayerUnknown''s Battlegrounds a été un pionnier du battle royale.'),\n" +
                    "(5,'Quel jeu propose de gérer une ville avec des Sims ?','City Skylines','The Sims','RollerCoaster Tycoon','SimCity',2,'The Sims permet de contrôler la vie quotidienne de personnages.'),\n" +
                    "(5,'Qui marche à 30° vers l avant?','City Skylines','The AA','RollerCoaster Tycoon','SimCity',2,'AA va très très vite'),\n" +
                    "(5,'Quel jeu d’aventure narratif a été développé par Quantic Dream ?','Life is Strange','Heavy Rain','Until Dawn','Firewatch',2,'Heavy Rain est un thriller interactif développé par Quantic Dream.'),\n" +
                    "(5,'Quel jeu de rythme utilise un sabre laser pour trancher des cubes ?','Dance Central','Just Dance','Beat Saber','Guitar Hero',3,'Beat Saber est un jeu VR où l’on tranche des blocs au rythme de la musique.'),\n" +
                    "(5,'Quel jeu de rôle japonais met en scène Cloud Strife ?','Persona 5','Final Fantasy VII','Dragon Quest','Xenoblade Chronicles',2,'Cloud est le héros emblématique de Final Fantasy VII.'),\n" +
                    "(5,'Quel jeu de survie prend place dans un monde cubique ?','Rust','Terraria','Ark: Survival Evolved','Minecraft',4,'Minecraft combine exploration, survie et construction dans un monde cubique.'),\n" +
                    "(5,'Quel jeu célèbre fête chaque année une convention nommée BlizzCon ?','League of Legends','StarCraft','World of Warcraft','Overwatch',3,'World of Warcraft est au cœur de la communauté Blizzard.'),\n" +
                    "(5,'Quel jeu multijoueur oppose des terroristes à des contre-terroristes ?','Rainbow Six Siege','Call of Duty','Battlefield','Counter-Strike',4,'Counter-Strike est un jeu tactique opposant deux équipes avec des objectifs.'),\n" +
                    "(5,'Quel jeu a pour personnage principal un bandicoot ?','Spyro','Ratchet & Clank','Crash Bandicoot','Jak and Daxter',3,'Crash Bandicoot est la mascotte emblématique de Naughty Dog.'),\n" +
                    "(5,'Quel jeu en ligne consiste à capturer des points de contrôle et des charges utiles ?','Overwatch','Team Fortress 2','Valorant','Apex Legends',2,'Team Fortress 2 a popularisé ce mode avec ses classes de personnages.'),\n" +
                    "(5,'Quel jeu vous plonge dans la Grèce antique en tant que mercenaire ?','The Witcher 3','Assassin''s Creed Odyssey','Skyrim','Immortals Fenyx Rising',2,'Assassin''s Creed Odyssey vous met dans la peau d’un mercenaire grec.'),\n" +
                    "(5,'Quel jeu met en scène un enfant accompagné d’une créature géante nommée Trico ?','ICO','The Last Guardian','Shadow of the Colossus','Ori and the Will of the Wisps',2,'The Last Guardian est centré sur la relation entre un enfant et Trico.'),\n" +
                    "(5,'Quel jeu se déroule sur l’île tropicale de Yara ?','Far Cry 5','Just Cause 4','Far Cry 6','Call of Juarez',3,'Far Cry 6 se déroule sur l’île fictive de Yara, inspirée de Cuba.');";

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
                    curseur.getString(0) + ";" + 
                    curseur.getString(1) + ";" + 
                    curseur.getString(2) + ";" +  
                    curseur.getString(3) + ";" +  
                    curseur.getString(4) + ";" +  
                    curseur.getInt(5) + ";" +    
                    curseur.getString(6) + "\n";  
        }
        curseur.close();
        return trameQuestion;
    }
}