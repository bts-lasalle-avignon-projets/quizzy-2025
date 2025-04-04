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

    public static final String TABLE_RESULTATS   = "table_resultats";
    public static final String TABLE_QUIZ        = "table_quiz";
    public static final String TABLE_QUESTION    = "table_question";
    public static final String TABLE_REPONSE     = "table_reponse";
    public static final String TABLE_THEME       = "table_theme";
    public static final String TABLE_PARTICIPANT = "table_participant";

    public static final String COLONNE_QUESTIONID    = "questionID";
    public static final String COLONNE_THEMEID       = "themeID";
    public static final String COLONNE_QUESTION      = "question";
    public static final String COLONNE_PROPOSITION1  = "proposition1";
    public static final String COLONNE_PROPOSITION2  = "proposition2";
    public static final String COLONNE_PROPOSITION3  = "proposition3";
    public static final String COLONNE_PROPOSITION4  = "proposition4";
    public static final String COLONNE_REPONSE       = "reponse";
    public static final String COLONNE_EXPLICATION   = "explication";
    public static final String COLONNE_THEME         = "theme";
    public static final String COLONNE_QUIZZID       = "quizzID";
    public static final String COLONNE_HORODATAGE    = "horodatage";
    public static final String COLONNE_GAGNANTID     = "gagnantID";
    public static final String COLONNE_TEMPS         = "temps";
    public static final String COLONNE_PARTICIPANTID = "participantID";
    public static final String COLONNE_PRENOM        = "prenom";
    public static final String COLONNE_SCORE         = "score";

    private static final String CREE_TABLE_QUIZZ =
      "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEMEID + " INTEGER, " + COLONNE_HORODATAGE +
      " TEXT NOT NULL, " + COLONNE_GAGNANTID + " INTEGER, "
      + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID +
      "));";

    private static final String CREE_TABLE_RESULTATS =
      "CREATE TABLE IF NOT EXISTS " + TABLE_RESULTATS + "(" + COLONNE_QUIZZID + " INTEGER, " +
      COLONNE_PARTICIPANTID + " INTEGER, " + COLONNE_SCORE + " INTEGER, "
      + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + "), "
      + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
      "), "
      + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" +
      COLONNE_PARTICIPANTID + "));";

    private static final String CREE_TABLE_QUESTION =
      "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "(" + COLONNE_QUESTIONID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEMEID + " INTEGER, " + COLONNE_QUESTION +
      " TEXT NOT NULL, " + COLONNE_PROPOSITION1 + " TEXT NOT NULL, " + COLONNE_PROPOSITION2 +
      " TEXT NOT NULL, " + COLONNE_PROPOSITION3 + " TEXT NOT NULL, " + COLONNE_PROPOSITION4 +
      " TEXT NOT NULL, " + COLONNE_EXPLICATION + " TEXT NOT NULL, " + COLONNE_REPONSE +
      " TEXT NOT NULL, "
      + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID +
      "));";

    private static final String CREE_TABLE_REPONSE =
      "CREATE TABLE IF NOT EXISTS " + TABLE_REPONSE + "(" + COLONNE_QUIZZID + " INTEGER, " +
      COLONNE_PARTICIPANTID + " INTEGER, " + COLONNE_QUESTIONID + " INTEGER, " + COLONNE_TEMPS +
      " INTEGER, " + COLONNE_REPONSE + " TEXT NOT NULL, "
      + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + ", " +
      COLONNE_QUESTIONID + "), "
      + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID +
      "), "
      + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" +
      COLONNE_PARTICIPANTID + "), "
      + "FOREIGN KEY (" + COLONNE_QUESTIONID + ") REFERENCES " + TABLE_QUESTION + "(" +
      COLONNE_QUESTIONID + "));";

    private static final String CREE_TABLE_PARTICIPANT =
      "CREATE TABLE IF NOT EXISTS " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_PRENOM + " TEXT NOT NULL);";

    private static final String CREE_TABLE_THEME =
      "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + "(" + COLONNE_THEMEID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_THEME + " TEXT NOT NULL);";

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

    private static final
      String
        AJOUTE_QUESTIONS =
          "INSERT INTO " + TABLE_QUESTION +
          "(themeID, question, proposition1, proposition2, proposition3, proposition4, reponse, explication) VALUES "
          +
          "(1,'Quel programmeur a créé et continue de diriger le développement du noyau de Linux ?','Linus Torvalds','Steeve Jobs','Larry Ellison','Bill Gates','Linus Torvalds','Linus Torvalds a découvert l''informatique vers l''âge de 11 ans grâce à l''ordinateur de son grand-père, un Commodore VIC-20.'),\n"
          +
          "(1,'Quel est le principal atout de Linux, développé et maintenu par Linus Torvalds ?','Il est libre','Il est beau','Il est Finlandais','Il est amusant','Il est libre','Linux est un système d''exploitation open-source, gratuit, et modifiableIl est développé et maintenu par Torvalds avec l''aide de contributeurs.'),\n"
          +
          "(1,'Quel animal représentant Linux est aussi la mascotte de l''université d''Helsinki ?','Manchot','Marmotte','Caribou','Gnou','Manchot','Le manchot est la mascotte de Linux, choisie pour son aspect sympathique et facile à identifier.'),\n"
          +
          "(1,'Laquelle de ces propositions désigne une distribution Linux fondée en 1993 ?','Red Hat','Zubuntu','Souze','Mandrika','Red Hat','Red Hat, fondée en 1993, est une distribution Linux très populaire dans le monde professionnel.'),\n"
          +
          "(1,'Quel système d''exploitation mobile majeur de l''industrie s''appuie sur un noyau Linux ?','Android','iOS','Windows Phone','BlackBerry 10','Android','Android utilise le noyau Linux, ce qui en fait l''un des systèmes d''exploitation mobiles les plus utilisés.'),\n"
          +
          "(1,'Sous Linux, comment appelle-t-on les logiciels assemblés autour du noyau ?','Distribution','Progiciel','Paquet','Logithèque','Distribution','Une distribution est un ensemble de logiciels qui fonctionne avec le noyau Linux.'),\n"
          +
          "(1,'Quel serveur web présent sous Linux est aussi présent sur les serveurs du monde entier ?','Apache','Comanche','Sioux','Mohican','Apache','Apache est l''un des serveurs web les plus utilisés au monde, y compris sur des systèmes Linux. Il est open-source et largement adopté pour héberger des sites web.'),\n"
          +
          "(1,'Quel est le nom de la mascotte de Linux, connue des mordus du système d''exploitation ?','Tux','Wilber','Gnu','Puffy','Tux','Tux est la mascotte officielle de Linux, un manchot qui représente le système d''exploitation open-source créé par Linus Torvalds.'),\n"
          +
          "(1,'En quelle année Linus Torvalds a-t-il livré la première version du noyau Linux ?','1991','1993','1995','1997','1991','La première version du noyau Linux a été publiée en 1991 par Linus Torvalds, marquant le début du développement du système d''exploitation.'),\n"
          +
          "(1,'Quel ancien mot bantou désigne une célèbre distribution Linux ?','Ubuntu','Umbro','Uhura','Ursula','Ubuntu','Ubuntu vient d''un mot bantou signifiant \"humanité envers les autres\".'),\n"
          +
          "(2,'Quelle espèce d''oiseaux, encore présente en Europe, gringotte, quiritte ou trille ?','Rossignol','Corbeau','Perroquet','Moineau','Rossignol','Le rossignol est un petit oiseau chanteur, célèbre pour son chant mélodieux, encore présent dans certaines régions d''Europe.'),\n"
          +
          "(2,'Laquelle de ces professions ne peut-on associer au grand Léonard de Vinci ?','Auteur dramatique','Peintre','Sculpteur','Botaniste','Auteur dramatique','Léonard de Vinci était principalement connu comme peintre, sculpteur et inventeur, mais il n''était pas auteur dramatique.'),\n"
          +
          "(2,'Fils de Laïos et de Jocaste, qui Oedipe a-t-il tué dans la mythologie grecque ?','Son père','Sa mère','Sa fille','Son fils','Son père','Oedipe, dans la mythologie grecque, tue son père Laïos sans savoir qui il est, accomplissant ainsi une prophétie tragique.'),\n"
          +
          "(2,'En France, à la Libération, combien se retrouvèrent à Notre-Dame de Paris ?','13 000','9 000','5 000','17 000','13 000','Lors de la Libération de Paris en 1944, environ 13 000 personnes se sont rassemblées à Notre-Dame pour célébrer la fin de l''occupation allemande.'),\n"
          +
          "(2,'En quelle année a été bu le premier Coca-Cola concocté par John Pemberton ?','1886','1906','1926','1946','1886','Le premier Coca-Cola a été concocté en 1886 par le pharmacien John Pemberton, à Atlanta.'),\n"
          +
          "(2,'Dans quelle ville se sont déroulés les Jeux olympiques de 1900 ?','Paris','Athènes','Rome','Los Angeles','Paris','Les Jeux Olympiques de 1900 se sont déroulés à Paris, en France, et ont été les premiers à inclure des femmes comme participantes.'),\n"
          +
          "(2,'À quelle substance naturelle ou synthétique la pénicilline est-elle associée ?','Antibiotique','Anesthésique','Soporifique','Analgésique','Antibiotique','La pénicilline est le premier antibiotique découvert par Alexander Fleming en 1928, révolutionnant le traitement des infections bactériennes.'),\n"
          +
          "(2,'Quel prix Nobel Winston Leonard Spencer-Churchill a-t-il reçu en 1953 ?','Littérature','Physique','Paix','Chimie','Littérature','Winston Churchill a reçu le prix Nobel de littérature en 1953, en reconnaissance de ses écrits historiques et oratoires.'),\n"
          +
          "(2,'En quelle année a-t-on pu assister à l''inauguration du métro parisien ?','1900','1880','1860','1840','1900','Le métro parisien a été inauguré en 1900 lors de l''Exposition universelle, marquant le début de l''urbanisation moderne à Paris.'),\n"
          +
          "(2,'Quel écrivain et journaliste littéraire français fut surnommé le taureau normand ?','Guy de Maupassant','Victor Hugo','Honoré de Balzac','Alfred de Musset','Guy de Maupassant','Guy de Maupassant, écrivain réaliste français, a été surnommé \"le taureau normand\" en raison de sa personnalité énergique et de son origine normande.'),\n"
          +
          "(3,'Quel film a remporté l''Oscar du meilleur film en 1994 ?','Forrest Gump','Pulp Fiction','The Shawshank Redemption','The Lion King','Forrest Gump','Forrest Gump a remporté l''Oscar du meilleur film en 1994, grâce à sa performance émotive et son message puissant.'),\n"
          +
          "(3,'Qui a réalisé le film \"Inception\" ?','Steven Spielberg','James Cameron','Christopher Nolan','Quentin Tarantino','Christopher Nolan','Christopher Nolan a réalisé \"Inception\" en 2010, un film de science-fiction acclamé pour sa narration complexe.'),\n"
          +
          "(3,'Quel acteur incarne le personnage de Jack Dawson dans \"Titanic\" ?','Brad Pitt','Johnny Depp','Leonardo DiCaprio','Matt Damon','Leonardo DiCaprio','Leonardo DiCaprio joue le rôle de Jack Dawson, l''amoureux tragique dans \"Titanic\", réalisé par James Cameron.'),\n"
          +
          "(3,'Quel film est célèbre pour la réplique \"Que la Force soit avec toi\" ?','Star Wars','The Matrix','Jurassic Park','Indiana Jones','Star Wars','\"Star Wars\" est célèbre pour la réplique \"Que la Force soit avec toi\", un film de science-fiction emblématique de George Lucas.'),\n"
          +
          "(3,'Qui a interprété le rôle de The Joker dans \"The Dark Knight\" ?','Jack Nicholson','Heath Ledger','Jared Leto','Tom Hardy','Heath Ledger','Heath Ledger a interprété The Joker dans \"The Dark Knight\" (2008), un rôle qui lui a valu un Oscar posthume.'),\n"
          +
          "(3,'Dans quel film Tim Burton a-t-il mis en scène un personnage nommé Edward Scissorhands ?','Beetlejuice','Batman','Edward aux mains d''argent','Sweeney Todd','Edward aux mains d''argent','Tim Burton a réalisé \"Edward aux mains d''argent\" en 1990, un film où Johnny Depp incarne un personnage avec des mains en ciseaux.'),\n"
          +
          "(3,'Quel film d''animation de Pixar raconte l''histoire d''un robot appelé WALL-E ?','Toy Story','WALL-E','Monstres et Cie','Les Indestructibles','WALL-E','WALL-E est un film d''animation de Pixar de 2008 qui suit un robot solitaire dans un futur dystopique.'),\n"
          +
          "(3,'Qui a réalisé \"Pulp Fiction\" ?','Quentin Tarantino','Martin Scorsese','Francis Ford Coppola','Stanley Kubrick','Quentin Tarantino','Quentin Tarantino a réalisé \"Pulp Fiction\" en 1994, un film révolutionnaire connu pour sa narration non linéaire et son dialogue distinctif.'),\n"
          +
          "(3,'Quel acteur a incarné le rôle de Tony Stark / Iron Man dans l''univers Marvel ?','Robert Downey Jr.','Chris Hemsworth','Chris Evans','Mark Ruffalo','Robert Downey Jr.','Robert Downey Jr. a incarné le personnage de Tony Stark / Iron Man dans l''univers Marvel, débutant avec \"Iron Man\" en 2008.'),\n"
          +
          "(3,'Dans quel film de science-fiction de 1999, Keanu Reeves joue-t-il un personnage nommé Neo ?','Matrix','Terminator','Starship Troopers','Blade Runner','Matrix','\"Matrix\" est un film de science-fiction de 1999 où Keanu Reeves incarne Neo, un hacker qui découvre la réalité virtuelle.'),\n"
          +
          "(4,'Quel groupe a chanté \"Bohemian Rhapsody\" ?','The Beatles','Queen','Led Zeppelin','Pink Floyd','Queen','\"Bohemian Rhapsody\" est une chanson iconique de Queen, écrite par Freddie Mercury, et publiée en 1975.'),\n"
          +
          "(4,'Quel est le titre de l''album de Michael Jackson qui contient le single \"Thriller\" ?','Bad','Dangerous','Off the Wall','Thriller','Thriller','L''album \"Thriller\", sorti en 1982, est l''album le plus vendu de tous les temps, avec la chanson \"Thriller\" comme emblématique.'),\n"
          +
          "(4,'Quel est le nom du chanteur principal du groupe The Rolling Stones ?','Mick Jagger','Keith Richards','Paul McCartney','Jim Morrison','Mick Jagger','Mick Jagger est le chanteur principal du groupe The Rolling Stones, formé en 1962 et toujours actif.'),\n"
          +
          "(4,'Quel genre musical est représenté par le groupe The Beatles ?','Jazz','Rock','Reggae','Classique','Rock','The Beatles sont un groupe de rock britannique légendaire, influençant profondément la musique populaire du 20ème siècle.'),\n"
          +
          "(4,'Qui est l''auteur-compositeur du tube \"Like a Rolling Stone\" ?','Bob Dylan','Jimi Hendrix','Bruce Springsteen','Neil Young','Bob Dylan','\"Like a Rolling Stone\" est une chanson de Bob Dylan, considérée comme l''une des plus grandes chansons de l''histoire de la musique rock.'),\n"
          +
          "(4,'Quel artiste a popularisé la chanson \"Like a Prayer\" en 1989 ?','Madonna','Cher','Cyndi Lauper','Janet Jackson','Madonna','\"Like a Prayer\" est une chanson emblématique de Madonna, sortie en 1989, qui mélange pop et gospel.'),\n"
          +
          "(4,'Quel est le nom de l''album qui a propulsé Nirvana au sommet avec le titre \"Smells Like Teen Spirit\" ?','In Utero','Nevermind','Bleach','MTV Unplugged','Nevermind','\"Nevermind\" de Nirvana, sorti en 1991, a marqué un tournant dans le rock alternatif, avec \"Smells Like Teen Spirit\" comme chanson phare.'),\n"
          +
          "(4,'Quel genre musical est principalement associé à Elvis Presley ?','Pop','Rock & Roll','Jazz','Soul','Rock & Roll','Elvis Presley est une icône du rock & roll, fusionnant des éléments de musique noire et de la culture populaire des années 50.'),\n"
          +
          "(4,'Quel groupe a sorti l''album \"The Dark Side of the Moon\" en 1973 ?','Led Zeppelin','Pink Floyd','The Who','The Rolling Stones','Pink Floyd','\"The Dark Side of the Moon\" de Pink Floyd est un album conceptuel qui est devenu un classique de la musique rock progressif.'),\n"
          +
          "(4,'Quel musicien est surnommé \"le roi de la pop\" ?','Prince','Michael Jackson','Elton John','Justin Timberlake','Michael Jackson','Michael Jackson est surnommé \"le roi de la pop\" en raison de ses nombreuses contributions au genre pop et de son impact mondial.');";

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
        db.execSQL(CREE_TABLE_QUIZZ);
        db.execSQL(CREE_TABLE_QUESTION);
        db.execSQL(CREE_TABLE_THEME);
        db.execSQL(CREE_TABLE_RESULTATS);
        db.execSQL(CREE_TABLE_REPONSE);
        insererDonnees(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE);
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
