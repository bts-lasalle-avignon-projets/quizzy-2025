#ifndef QUIZZY_H
#define QUIZZY_H
#include <QObject>
#include <QVector>

#include "joueur.h"
#include "question.h"

#define SEPARATEUR ';'

#define TYPE_DE_TRAME 0

#define THEME              1
#define TEMPS              2
#define NOMBRE_DE_QUESTION 3

#define NOM_JOUEUR_1 1
#define NOM_JOUEUR_2 2

#define TITRE_QUESTION 1
#define PROPOSITION_A  2
#define PROPOSITION_B  3
#define PROPOSITION_C  4
#define PROPOSITION_D  5
#define NUMERO_REPONSE 6
#define EXPLICATION    7
#define POINTS         8

#define NUMERO_DU_JOUEUR      0
#define NUMERO_REPONSE_JOUEUR 1

class CommunicationBluetooth;
class Joueur;
class Question;

class Quizzy : public QObject
{
    Q_OBJECT
  public:
    Quizzy(QObject* parent = nullptr);
    virtual ~Quizzy();

    enum Etat
    {
        Initial,
        ConfigurationChoisie,
        JoueursAjoutes,
        QuizDemarre,
        QuestionCommencee,
        QuestionFinie,
        AnnonceDesResultats,
        QuizTermine
    };

    char stringToChar(QString chaine);

  private:
    CommunicationBluetooth*
      communicationBluetooth; //!< association vers CommunicationBluetooth

    int  IDQuestionAffichee;
    bool timerTermine = false;

    Etat               etat;
    QString            trame;
    QVector<Joueur*>   joueurs;
    QVector<Question*> question;

    QVector<QString> traiterTrame(QString trameRecue);
    void             lireTrame(QVector<QString> trameTraitee);

    void initialiserCommunicationBluetooth();
    void initialiserQuiz();

  signals:

    void sessionParametree(QString theme,
                           QString temps,
                           QString nombreDeQuestion,
                           QString nomJoueur1,
                           QString nomJoueur2);
    void questionRecue(QString titreQuestion,
                       QString IDReponse,
                       QString propositionA,
                       QString propositionB,
                       QString propositionC,
                       QString propositionD);
    void reponseJoueurRecue(QString IDPupitre, QString choixJoueur);
};
#endif // QUIZZY_H
