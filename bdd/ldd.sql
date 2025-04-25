-- LDD (langage de définition de données)

--- Suppression des tables

DROP TABLE IF EXISTS "table_resultats";
DROP TABLE IF EXISTS "table_reponse";
DROP TABLE IF EXISTS "table_question";
DROP TABLE IF EXISTS "table_quiz";
DROP TABLE IF EXISTS "table_theme";
DROP TABLE IF EXISTS "table_participant";

--- Création des tables

CREATE TABLE IF NOT EXISTS table_quiz(
    quizzID INTEGER PRIMARY KEY AUTOINCREMENT, 
    themeID INTEGER, 
    horodatage TEXT NOT NULL, 
    gagnantID INTEGER DEFAULT 0, 
    FOREIGN KEY (themeID) REFERENCES table_theme(themeID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS table_resultats(
    quizzID INTEGER, 
    participantID INTEGER, 
    score REAL, 
    PRIMARY KEY (quizzID, participantID),
    FOREIGN KEY (quizzID) REFERENCES table_quiz(quizzID) ON DELETE CASCADE,
    FOREIGN KEY (participantID) REFERENCES table_participant(participantID) ON DELETE CASCADE
);

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

CREATE TABLE IF NOT EXISTS table_reponse(
    quizzID INTEGER,
    participantID INTEGER,
    questionID INTEGER,
    temps INTEGER DEFAULT NULL,
    reponse INTEGER NOT NULL,
    PRIMARY KEY (quizzID, participantID, questionID),
    FOREIGN KEY (quizzID) REFERENCES table_quiz(quizzID) ON DELETE CASCADE,
    FOREIGN KEY (participantID) REFERENCES table_participant(participantID) ON DELETE CASCADE,
    FOREIGN KEY (questionID) REFERENCES table_question(questionID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS table_participant(
    participantID INTEGER PRIMARY KEY AUTOINCREMENT,
    prenom TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS table_theme(
    themeID INTEGER PRIMARY KEY AUTOINCREMENT,
    theme TEXT NOT NULL
);
