#ifndef QUIZZYGUI_H
#define QUIZZYGUI_H

#include <QtWidgets>
#include <QMessageBox>
#include <QDebug>
#include <QBluetoothAddress>
#include <QBluetoothDeviceInfo>

#define CHEMIN_LOGO ":/images/logo.png"

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
/*
class EcranAttente : public QWidget
{
}*/

class QuizzyGUI : public QMainWindow
{
    Q_OBJECT

  public:
    QuizzyGUI(QWidget* parent = nullptr);
    ~QuizzyGUI();

    enum Ecran
    {
        idEcranAttente = 0,
        idEcranAccueil,
        idEcranQuestion,
        idEcranReponse,
        idEcranFin
    };

    QStackedWidget* ecrans;
    QVBoxLayout*    layoutPrincipal;
    void            changerEtatConnexion();

  private:
    Quizzy*                 quizzy;
    CommunicationBluetooth* communication;

    EcranAttente*  ecranAttente;
    EcranAccueil*  ecranAccueil;
    EcranQuestion* ecranQuestion;
    EcranReponse*  ecranReponse;
    EcranFin*      ecranFin;

    void initialiserEvenements();
    void initialiserEcrans();

    void creerEcrans();
    void creerEcranAttente();
    void creerEcranAccueil();
    void creerEcranQuestion();
    void creerEcranReponse();
    void creerEcranFin();

  public slots:
    void afficherEcran(QuizzyGUI::Ecran ecran);
    void afficherEcranAttente();
    void ecranSuivant();
};

class EcranAttente : public QWidget
{
    Q_OBJECT
  public:
    EcranAttente(QuizzyGUI* parent = nullptr);
    ~EcranAttente();

    QWidget* ecranAttente;
    QLabel*  titreEcranAttente;
    QLabel*  messageConnexion;

    QVBoxLayout* layoutEcranAttente;
};

class EcranAccueil : public QWidget
{
    Q_OBJECT
  public:
    EcranAccueil(QuizzyGUI* parent = nullptr);
    ~EcranAccueil();

    QWidget* ecranAccueil;
    QLabel*  titreEcranAccueil;

    QVBoxLayout* layoutEcranAccueil;
};

class EcranQuestion : public QWidget
{
    Q_OBJECT
  public:
    EcranQuestion(QuizzyGUI* parent = nullptr);
    ~EcranQuestion();

    QWidget* ecranQuestion;
    QLabel*  titreEcranQuestion;

    QVBoxLayout* layoutEcranQuestion;
};

class EcranReponse : public QWidget
{
    Q_OBJECT
  public:
    EcranReponse(QuizzyGUI* parent = nullptr);
    ~EcranReponse();

    QWidget* ecranReponse;
    QLabel*  titreEcranReponse;

    QVBoxLayout* layoutEcranReponse;
};

class EcranFin : public QWidget
{
    Q_OBJECT
  public:
    EcranFin(QuizzyGUI* parent = nullptr);
    ~EcranFin();

    QWidget* ecranFin;
    QLabel*  titreEcranFin;
    QLabel*  messageEcranFin;

    QVBoxLayout* layoutEcranFin;
};

#endif // QUIZZYGUI_H
