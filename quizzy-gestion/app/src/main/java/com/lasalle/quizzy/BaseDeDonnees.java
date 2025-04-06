package com.lasalle.quizzy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;

public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String  TAG         = "_BaseDeDonnees"; //!< TAG pour les logs (cf. Logcat)
    public static final String   NOM_BDD     = "quizzy.db";
    public static final int      VERSION_BDD = 1;
    private static BaseDeDonnees baseDeDonnees =
      null;                              //!< L'instance unique de BaseDeDonnees (singleton)
    private final SQLiteDatabase sqlite; //<! L'accès à la base de données SQLite

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
    public static final String COLONNE_POINTS        = "points";
    public static final String COLONNE_EXPLICATION   = "explication";
    public static final String COLONNE_PARTICIPANTID = "participantID";
    public static final String COLONNE_PRENOM        = "prenom";
    public static final String COLONNE_TEMPS         = "temps";
    public static final String COLONNE_SCORE         = "score";

    /*
        CREATE TABLE IF NOT EXISTS table_theme(
            themeID INTEGER PRIMARY KEY AUTOINCREMENT,
            theme TEXT NOT NULL
        );
    */
    private static final String CREE_TABLE_THEME =
      "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + "(" + COLONNE_THEMEID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEME + " TEXT NOT NULL);";

    /*
        CREATE TABLE IF NOT EXISTS table_quiz(
            quizzID INTEGER PRIMARY KEY AUTOINCREMENT,
            themeID INTEGER,
            horodatage TEXT NOT NULL,
            gagnantID INTEGER DEFAULT 0,
            FOREIGN KEY (themeID) REFERENCES table_theme(themeID) ON DELETE CASCADE
        );
    */
    private static final String CREE_TABLE_QUIZZ =
      "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEMEID + " INTEGER, " + COLONNE_HORODATAGE +
      " TEXT NOT NULL, " + COLONNE_GAGNANTID + " INTEGER DEFAULT 0, "
      + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID +
      ") ON DELETE CASCADE);";

    /*
        CREATE TABLE IF NOT EXISTS table_question(
            questionID INTEGER PRIMARY KEY AUTOINCREMENT,
            themeID INTEGER,
            question TEXT NOT NULL,
            proposition1 TEXT NOT NULL,
            proposition2 TEXT NOT NULL,
            proposition3 TEXT NOT NULL,
            proposition4 TEXT NOT NULL,
            explication TEXT DEFAULT '',
            reponse INTEGER NOT NULL,
            points	REAL DEFAULT 1,
            FOREIGN KEY (themeID) REFERENCES table_theme(themeID) ON DELETE CASCADE
        );
    */
    private static final String CREE_TABLE_QUESTION =
      "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "(" + COLONNE_QUESTIONID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEMEID + " INTEGER, " + COLONNE_QUESTION +
      " TEXT NOT NULL, " + COLONNE_PROPOSITION1 + " TEXT NOT NULL, " + COLONNE_PROPOSITION2 +
      " TEXT NOT NULL, " + COLONNE_PROPOSITION3 + " TEXT NOT NULL, " + COLONNE_PROPOSITION4 +
      " TEXT NOT NULL, " + COLONNE_EXPLICATION + " TEXT DEFAULT '', " + COLONNE_REPONSE +
      " INTEGER NOT NULL," + COLONNE_POINTS + " REAL DEFAULT 1,"
      + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID +
      ")  ON DELETE CASCADE);";

    /*
        CREATE TABLE IF NOT EXISTS table_participant(
            participantID INTEGER PRIMARY KEY AUTOINCREMENT,
            prenom TEXT NOT NULL
        );
    */
    private static final String CREE_TABLE_PARTICIPANT =
      "CREATE TABLE IF NOT EXISTS " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_PRENOM + " TEXT NOT NULL);";

    /*
        CREATE TABLE IF NOT EXISTS table_reponse(
            quizzID INTEGER,
            participantID INTEGER,
            questionID INTEGER,
            temps INTEGER DEFAULT NULL,
            reponse INTEGER NOT NULL,
            PRIMARY KEY (quizzID, participantID, questionID),
            FOREIGN KEY (quizzID) REFERENCES table_quiz(quizzID) ON DELETE CASCADE,
            FOREIGN KEY (participantID) REFERENCES table_participant(participantID) ON DELETE
       CASCADE, FOREIGN KEY (questionID) REFERENCES table_question(questionID) ON DELETE CASCADE
        );
    */
    private static final String CREE_TABLE_REPONSE =
      "CREATE TABLE IF NOT EXISTS " + TABLE_REPONSE + "(" + COLONNE_QUIZZID + " INTEGER, " +
      COLONNE_PARTICIPANTID + " INTEGER, " + COLONNE_QUESTIONID + " INTEGER, " + COLONNE_TEMPS +
      " INTEGER DEFAULT NULL, " + COLONNE_REPONSE + " INTEGER NOT NULL, "
      + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + ", " +
      COLONNE_QUESTIONID + "), "
      + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
      ") ON DELETE CASCADE, "
      + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" +
      COLONNE_PARTICIPANTID + ") ON DELETE CASCADE, "
      + "FOREIGN KEY (" + COLONNE_QUESTIONID + ") REFERENCES " + TABLE_QUESTION + "(" +
      COLONNE_QUESTIONID + ") ON DELETE CASCADE);";

    /*
        CREATE TABLE IF NOT EXISTS table_resultats(
            quizzID INTEGER,
            participantID INTEGER,
            score REAL,
            PRIMARY KEY (quizzID, participantID),
            FOREIGN KEY (quizzID) REFERENCES table_quiz(quizzID) ON DELETE CASCADE,
            FOREIGN KEY (participantID) REFERENCES table_participant(participantID) ON DELETE
       CASCADE
        );
    */
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
      "(themeID, question, proposition1, proposition2, proposition3, proposition4, explication, reponse) VALUES "
      +
      "(1,'Quel programmeur a créé et continue de diriger le développement du noyau de Linux ?','Steeve Jobs','Linus Torvalds','Larry Ellison','Bill Gates','Linus Torvalds a découvert l''informatique vers l''âge de 11 ans grâce à l''ordinateur de son grand-père, un Commodore VIC-20.',2),\n"
      +
      "(1,'Quel est le principal atout de Linux, développé et maintenu par Linus Torvalds ?','Il est libre','Il est beau','Il est Finlandais','Il est amusant','Linux est un système d''exploitation open-source, gratuit, et modifiableIl est développé et maintenu par Torvalds avec l''aide de contributeurs.',1),\n"
      +
      "(1,'Quel animal représentant Linux est aussi la mascotte de l''université d''Helsinki ?','Marmotte','Caribou','Gnou','Manchot','Le manchot est la mascotte de Linux, choisie pour son aspect sympathique et facile à identifier.',4),\n"
      +
      "(1,'Laquelle de ces propositions désigne une distribution Linux fondée en 1993 ?','Zubuntu','Red Hat','Souze','Mandrika','Red Hat, fondée en 1993, est une distribution Linux très populaire dans le monde professionnel.',2),\n"
      +
      "(1,'Quel système d''exploitation mobile majeur de l''industrie s''appuie sur un noyau Linux ?','iOS','Windows Phone','Android','BlackBerry 10','Android utilise le noyau Linux, ce qui en fait l''un des systèmes d''exploitation mobiles les plus utilisés.',3),\n"
      +
      "(1,'Sous Linux, comment appelle-t-on les logiciels assemblés autour du noyau ?','Progiciel','Paquet','Distribution','Logithèque','Une distribution est un ensemble de logiciels qui fonctionne avec le noyau Linux.',3),\n"
      +
      "(1,'Quel serveur web présent sous Linux est aussi présent sur les serveurs du monde entier ?','Apache','Comanche','Sioux','Mohican','Apache est l''un des serveurs web les plus utilisés au monde, y compris sur des systèmes Linux. Il est open-source et largement adopté pour héberger des sites web.',1),\n"
      +
      "(1,'Quel est le nom de la mascotte de Linux, connue des mordus du système d''exploitation ?','Wilber','Tux','Gnu','Puffy','Tux est la mascotte officielle de Linux, un manchot qui représente le système d''exploitation open-source créé par Linus Torvalds.',2),\n"
      +
      "(1,'En quelle année Linus Torvalds a-t-il livré la première version du noyau Linux ?','1991','1993','1995','1997','La première version du noyau Linux a été publiée en 1991 par Linus Torvalds, marquant le début du développement du système d''exploitation.',1),\n"
      +
      "(1,'Quel ancien mot bantou désigne une célèbre distribution Linux ?','Umbro','Ubuntu','Uhura','Ursula','Ubuntu vient d''un mot bantou signifiant \"humanité envers les autres\".',2),\n"
      +
      "(2,'Quelle espèce d''oiseaux, encore présente en Europe, gringotte, quiritte ou trille ?','Rossignol','Corbeau','Perroquet','Moineau','Le rossignol est un petit oiseau chanteur, célèbre pour son chant mélodieux, encore présent dans certaines régions d''Europe.',1),\n"
      +
      "(2,'Laquelle de ces professions ne peut-on associer au grand Léonard de Vinci ?','Peintre','Sculpteur','Auteur dramatique','Botaniste','Léonard de Vinci était principalement connu comme peintre, sculpteur et inventeur, mais il n''était pas auteur dramatique.',3),\n"
      +
      "(2,'Fils de Laïos et de Jocaste, qui Oedipe a-t-il tué dans la mythologie grecque ?','Son père','Sa mère','Sa fille','Son fils','Oedipe, dans la mythologie grecque, tue son père Laïos sans savoir qui il est, accomplissant ainsi une prophétie tragique.',1),\n"
      +
      "(2,'En France, à la Libération, combien se retrouvèrent à Notre-Dame de Paris ?','5 000','9 000','13 000','17 000','Lors de la Libération de Paris en 1944, environ 13 000 personnes se sont rassemblées à Notre-Dame pour célébrer la fin de l''occupation allemande.',3),\n"
      +
      "(2,'En quelle année a été bu le premier Coca-Cola concocté par John Pemberton ?','1886','1906','1926','1946','Le premier Coca-Cola a été concocté en 1886 par le pharmacien John Pemberton, à Atlanta.',1),\n"
      +
      "(2,'Dans quelle ville se sont déroulés les Jeux olympiques de 1900 ?','Athènes','Rome','Los Angeles','Paris','Les Jeux Olympiques de 1900 se sont déroulés à Paris, en France, et ont été les premiers à inclure des femmes comme participantes.',4),\n"
      +
      "(2,'À quelle substance naturelle ou synthétique la pénicilline est-elle associée ?','Antibiotique','Anesthésique','Soporifique','Analgésique','La pénicilline est le premier antibiotique découvert par Alexander Fleming en 1928, révolutionnant le traitement des infections bactériennes.',1),\n"
      +
      "(2,'Quel prix Nobel Winston Leonard Spencer-Churchill a-t-il reçu en 1953 ?','Physique','Littérature','Paix','Chimie','Winston Churchill a reçu le prix Nobel de littérature en 1953, en reconnaissance de ses écrits historiques et oratoires.',2),\n"
      +
      "(2,'En quelle année a-t-on pu assister à l''inauguration du métro parisien ?','1880','1860','1840','1900','Le métro parisien a été inauguré en 1900 lors de l''Exposition universelle, marquant le début de l''urbanisation moderne à Paris.',4),\n"
      +
      "(2,'Quel écrivain et journaliste littéraire français fut surnommé le taureau normand ?','Victor Hugo','Guy de Maupassant','Honoré de Balzac','Alfred de Musset','Guy de Maupassant, écrivain réaliste français, a été surnommé \"le taureau normand\" en raison de sa personnalité énergique et de son origine normande.',2),\n"
      +
      "(3,'Quel film a remporté l''Oscar du meilleur film en 1994 ?','Forrest Gump','Pulp Fiction','The Shawshank Redemption','The Lion King','Forrest Gump a remporté l''Oscar du meilleur film en 1994, grâce à sa performance émotive et son message puissant.',1),\n"
      +
      "(3,'Qui a réalisé le film \"Inception\" ?','Steven Spielberg','James Cameron','Christopher Nolan','Quentin Tarantino','Christopher Nolan a réalisé \"Inception\" en 2010, un film de science-fiction acclamé pour sa narration complexe.',3),\n"
      +
      "(3,'Quel acteur incarne le personnage de Jack Dawson dans \"Titanic\" ?','Brad Pitt','Johnny Depp','Leonardo DiCaprio','Matt Damon','Leonardo DiCaprio joue le rôle de Jack Dawson, l''amoureux tragique dans \"Titanic\", réalisé par James Cameron.',3),\n"
      +
      "(3,'Quel film est célèbre pour la réplique \"Que la Force soit avec toi\" ?','Star Wars','The Matrix','Jurassic Park','Indiana Jones','\"Star Wars\" est célèbre pour la réplique \"Que la Force soit avec toi\", un film de science-fiction emblématique de George Lucas.',1),\n"
      +
      "(3,'Qui a interprété le rôle de The Joker dans \"The Dark Knight\" ?','Jack Nicholson','Heath Ledger','Jared Leto','Tom Hardy','Heath Ledger a interprété The Joker dans \"The Dark Knight\" (2008), un rôle qui lui a valu un Oscar posthume.',2),\n"
      +
      "(3,'Dans quel film Tim Burton a-t-il mis en scène un personnage nommé Edward Scissorhands ?','Beetlejuice','Batman','Edward aux mains d''argent','Sweeney Todd','Tim Burton a réalisé \"Edward aux mains d''argent\" en 1990, un film où Johnny Depp incarne un personnage avec des mains en ciseaux.',3),\n"
      +
      "(3,'Quel film d''animation de Pixar raconte l''histoire d''un robot appelé WALL-E ?','Toy Story','WALL-E','Monstres et Cie','Les Indestructibles','WALL-E est un film d''animation de Pixar de 2008 qui suit un robot solitaire dans un futur dystopique.',2),\n"
      +
      "(3,'Qui a réalisé \"Pulp Fiction\" ?','Quentin Tarantino','Martin Scorsese','Francis Ford Coppola','Stanley Kubrick','Quentin Tarantino a réalisé \"Pulp Fiction\" en 1994, un film révolutionnaire connu pour sa narration non linéaire et son dialogue distinctif.',1),\n"
      +
      "(3,'Quel acteur a incarné le rôle de Tony Stark / Iron Man dans l''univers Marvel ?','Robert Downey Jr.','Chris Hemsworth','Chris Evans','Mark Ruffalo','Robert Downey Jr. a incarné le personnage de Tony Stark / Iron Man dans l''univers Marvel, débutant avec \"Iron Man\" en 2008.',1),\n"
      +
      "(3,'Dans quel film de science-fiction de 1999, Keanu Reeves joue-t-il un personnage nommé Neo ?','Matrix','Terminator','Starship Troopers','Blade Runner','\"Matrix\" est un film de science-fiction de 1999 où Keanu Reeves incarne Neo, un hacker qui découvre la réalité virtuelle.',1),\n"
      +
      "(4,'Quel groupe a chanté \"Bohemian Rhapsody\" ?','The Beatles','Led Zeppelin','Queen','Pink Floyd','\"Bohemian Rhapsody\" est une chanson iconique de Queen, écrite par Freddie Mercury, et publiée en 1975.',3),\n"
      +
      "(4,'Quel est le titre de l''album de Michael Jackson qui contient le single \"Thriller\" ?','Bad','Dangerous','Off the Wall','Thriller','L''album \"Thriller\", sorti en 1982, est l''album le plus vendu de tous les temps, avec la chanson \"Thriller\" comme emblématique.',4),\n"
      +
      "(4,'Quel est le nom du chanteur principal du groupe The Rolling Stones ?','Mick Jagger','Keith Richards','Paul McCartney','Jim Morrison','Mick Jagger est le chanteur principal du groupe The Rolling Stones, formé en 1962 et toujours actif.',1),\n"
      +
      "(4,'Quel genre musical est représenté par le groupe The Beatles ?','Jazz','Rock','Reggae','Classique','The Beatles sont un groupe de rock britannique légendaire, influençant profondément la musique populaire du 20ème siècle.',2),\n"
      +
      "(4,'Qui est l''auteur-compositeur du tube \"Like a Rolling Stone\" ?','Bob Dylan','Jimi Hendrix','Bruce Springsteen','Neil Young','\"Like a Rolling Stone\" est une chanson de Bob Dylan, considérée comme l''une des plus grandes chansons de l''histoire de la musique rock.',1),\n"
      +
      "(4,'Quel artiste a popularisé la chanson \"Like a Prayer\" en 1989 ?','Madonna','Cher','Cyndi Lauper','Janet Jackson','\"Like a Prayer\" est une chanson emblématique de Madonna, sortie en 1989, qui mélange pop et gospel.',1),\n"
      +
      "(4,'Quel est le nom de l''album qui a propulsé Nirvana au sommet avec le titre \"Smells Like Teen Spirit\" ?','In Utero','Bleach','MTV Unplugged','Nevermind','\"Nevermind\" de Nirvana, sorti en 1991, a marqué un tournant dans le rock alternatif, avec \"Smells Like Teen Spirit\" comme chanson phare.',4),\n"
      +
      "(4,'Quel genre musical est principalement associé à Elvis Presley ?','Rock & Roll','Pop','Jazz','Soul','Elvis Presley est une icône du rock & roll, fusionnant des éléments de musique noire et de la culture populaire des années 50.',1),\n"
      +
      "(4,'Quel groupe a sorti l''album \"The Dark Side of the Moon\" en 1973 ?','Led Zeppelin','The Who','Pink Floyd','The Rolling Stones','\"The Dark Side of the Moon\" de Pink Floyd est un album conceptuel qui est devenu un classique de la musique rock progressif.',3),\n"
      +
      "(4,'Quel musicien est surnommé \"le roi de la pop\" ?','Prince','Michael Jackson','Elton John','Justin Timberlake','Michael Jackson est surnommé \"le roi de la pop\" en raison de ses nombreuses contributions au genre pop et de son impact mondial.',2);";

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
}
