#ifndef QUIZZYGUI_H
#define QUIZZYGUI_H

#include <QtWidgets>

#include "joueur.h"
#include "question.h"

#define CHEMIN_LOGO ":/images/logo.png"

#define TEST_ECRANS

class CommunicationBluetooth;

/**
 * @def NOM_APPLICATION
 * @brief Le nom de l'application
 */
#define NOM_APPLICATION "Quizzy"

/**
 * @def VERSION_APPLICATION
 * @brief La version de l'application
 */
#define VERSION_APPLICATION "1.0"

class Quizzy;
class EcranAttente;
class EcranAccueil;
class EcranQuestion;
class EcranReponse;
class EcranFin;

/**
 * @class QuizzyGUI
 * @brief Déclaration de la classe QuizzyGUI
 * @details Cette classe gère l'interface graphique de l'application Quizzy
 */
class QuizzyGUI : public QMainWindow
{
    Q_OBJECT

  public:
    QuizzyGUI(QWidget* parent = nullptr);
    ~QuizzyGUI();
    QStackedWidget* getEcrans();

    enum Ecran
    {
        idEcranAttente = 0,
        idEcranAccueil,
        idEcranQuestion,
        idEcranReponse,
        idEcranFin,
        NbEcrans
    };

  private:
    CommunicationBluetooth* communication;
    Quizzy*                 quizzy;
    Joueur                  joueur1;
    Joueur                  joueur2;
    Question                question;

    QStackedWidget* ecrans;
    QVBoxLayout*    layoutPrincipal;
    EcranAttente*   ecranAttente;
    EcranAccueil*   ecranAccueil;
    EcranQuestion*  ecranQuestion;
    EcranReponse*   ecranReponse;
    EcranFin*       ecranFin;

    void initialiserEvenements();
    void initialiserEcrans();
    void creerEcrans();
    void creerEcranAttente();
    void creerEcranAccueil();
    void creerEcranQuestion();
    void creerEcranReponse();
    void creerEcranFin();

  public:
    Joueur* getJoueur1()
    {
        return &joueur1;
    }
    Joueur* getJoueur2()
    {
        return &joueur2;
    }
    Question* getQuestion()
    {
        return &question;
    }

  public slots:
    void afficherEcran(QuizzyGUI::Ecran ecran);
    void afficherEcranAttente();
    void afficherEcranSuivant();
    void afficherMessageConnexion(QString nom, QString adresse);
    void afficherMessageDeconnexion(QString nom, QString adresse);
    void afficherConfiguration();
    void afficherNomsJoueurs();
    void afficherQuestion();
    void afficherScores();
    void determinerGagnant();
    void finirPartie();
};

#endif // QUIZZYGUI_H
