package com.lasalle.quizzy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class BaseDeDonnee extends SQLiteOpenHelper {

    public static final String NOM_BDD = "serveurs.db";
    public static final int    VERSION_BDD = 1;

    public static final String TABLE_RESULTATS = "table_resultats";
    public static final String TABLE_QUIZ = "table_quiz";
    public static final String TABLE_QUESTION = "table_question";
    public static final String TABLE_REPONSE = "table_reponse";
    public static final String TABLE_THEME = "table_theme";
    public static final String TABLE_PARTICIPANT = "table_participant";

    public static final String COLONNE_QUESTIONID = "questionID";
    public static final String COLONNE_QUESTION = "question";
    public static final String COLONNE_PROPOSITION1 = "proposition1";
    public static final String COLONNE_PROPOSITION2 = "proposition2";
    public static final String COLONNE_PROPOSITION3 = "proposition3";
    public static final String COLONNE_PROPOSITION4 = "proposition4";
    public static final String COLONNE_REPONSE = "reponse";
    public static final String COLONNE_THEMEID = "themeID";
    public static final String COLONNE_THEME = "theme";
    public static final String COLONNE_QUIZZID = "quizzID";
    public static final String COLONNE_HORODATAGE= "horodatage";
    public static final String COLONNE_GAGNANTID = "gagnantID";
    public static final String COLONNE_TEMPS = "temps";
    public static final String COLONNE_PARTICIPANTID = "participantID";
    public static final String COLONNE_PRENOM = "prenom";
    public static final String COLONNE_SCORE = "score";
    public static final String COLONNE_EXPLICATION = "explication";

    private static final String CREE_TABLE_QUIZZ = "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ + "("
            + COLONNE_QUIZZID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLONNE_THEMEID + " INTEGER, "
            + COLONNE_HORODATAGE + " TEXT NOT NULL, "
            + COLONNE_GAGNANTID + " INTEGER, "
            + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID + "));";

    private static final String CREE_TABLE_RESULTATS = "CREATE TABLE IF NOT EXISTS " + TABLE_RESULTATS + "("
            + COLONNE_QUIZZID + " INTEGER, "
            + COLONNE_PARTICIPANTID + " INTEGER, "
            + COLONNE_SCORE + " INTEGER, "
            + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + "), "
            + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID + "), "
            + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID + "));";

    private static final String CREE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
            + COLONNE_QUESTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLONNE_QUESTION + " TEXT NOT NULL, "
            + COLONNE_PROPOSITION1 + " TEXT NOT NULL, "
            + COLONNE_PROPOSITION2 + " TEXT NOT NULL, "
            + COLONNE_PROPOSITION3 + " TEXT NOT NULL, "
            + COLONNE_PROPOSITION4 + " TEXT NOT NULL, "
            + COLONNE_EXPLICATION + " TEXT NOT NULL, "
            + COLONNE_REPONSE + " TEXT NOT NULL, "
            + COLONNE_THEMEID + " INTEGER, "
            + "FOREIGN KEY (" + COLONNE_THEMEID + ") REFERENCES " + TABLE_THEME + "(" + COLONNE_THEMEID + "));";

    private static final String CREE_TABLE_REPONSE = "CREATE TABLE IF NOT EXISTS " + TABLE_REPONSE + "("
            + COLONNE_QUIZZID + " INTEGER, "
            + COLONNE_PARTICIPANTID + " INTEGER, "
            + COLONNE_QUESTIONID + " INTEGER, "
            + COLONNE_TEMPS + " INTEGER, "
            + COLONNE_REPONSE + " TEXT NOT NULL, "
            + "PRIMARY KEY (" + COLONNE_QUIZZID + ", " + COLONNE_PARTICIPANTID + ", " + COLONNE_QUESTIONID + "), "
            + "FOREIGN KEY (" + COLONNE_QUIZZID + ") REFERENCES " + TABLE_QUIZ + "(" + COLONNE_QUIZZID + "), "
            + "FOREIGN KEY (" + COLONNE_PARTICIPANTID + ") REFERENCES " + TABLE_PARTICIPANT + "(" + COLONNE_PARTICIPANTID + "), "
            + "FOREIGN KEY (" + COLONNE_QUESTIONID + ") REFERENCES " + TABLE_QUESTION + "(" + COLONNE_QUESTIONID + "));";

    private static final String CREE_TABLE_PARTICIPANT = "CREATE TABLE IF NOT EXISTS " + TABLE_PARTICIPANT + "("
            + COLONNE_PARTICIPANTID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLONNE_PRENOM + " TEXT NOT NULL);";

    private static final String CREE_TABLE_THEME = "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + "("
            + COLONNE_THEMEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLONNE_THEME + " TEXT NOT NULL);";

    private static final String AJOUTER_PARTICIPANT = "INSERT INTO " + TABLE_PARTICIPANT
            + "(prenom) VALUES " +
            " (1,'ARIATI Axel'),\n" +
            " (2,'BERNARD Clément'),\n" +
            " (3,'BLONDEL Joshua'),\n" +
            " (4,'BOUSQUET SOLFRINI Valentin'),\n" +
            " (5,'CLÉMENT Aymeric'),\n" +
            " (6,'GASSE Lenny'),\n" +
            " (7,'MILLOT Pierre'),\n" +
            " (8,'NAVARRO Mattéo'),\n" +
            " (9,'PESSINA Nicolas'),\n" +
            " (10,'RAFFIN Louis'),\n" +
            " (11,'SORIA-BONET Enzo'),\n" +
            " (12,'VALOBRA Enzo'),\n" +
            " (13,'VANDENBROUCKE Théo'),\n" +
            " (14,'VAUDAINE Dylan');\n";

    private static final String AJOUTER_THEME = "INSERT INTO " + TABLE_THEME
            + "(theme) VALUES " +
            " (1,'Informatique'),\n" +
            " (2,'Culture générale'),\n" +
            " (3,'Cinéma'),\n" +
            " (4,'Musique');";

    private static final String AJOUTER_QUESTION = "INSERT INTO " + TABLE_QUESTION
            + "(themeID, Question, Proposition1, Proposition2, Proposition3, Proposition4, Reponse, Explication) VALUES " +
            ;


    public BaseDeDonnee(Context context)
    {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREE_TABLE_PARTICIPANT);
        db.execSQL(CREE_TABLE_QUIZZ);
        db.execSQL(CREE_TABLE_QUESTION);
        db.execSQL(CREE_TABLE_THEME);
        db.execSQL(CREE_TABLE_RESULTATS);
        db.execSQL(CREE_TABLE_REPONSE);
        db.execSQL(AJOUTER_PARTICIPANT);
        db.execSQL(AJOUTER_THEME);
        db.execSQL(AJOUTER_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE);
        onCreate(db);

    }
}
