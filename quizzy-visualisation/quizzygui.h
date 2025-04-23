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

    enum Ecran
    {
        EcranAttente = 0,
        EcranAccueil,
        EcranQuestion,
        EcranReponse,
        EcranFin
    };

  private:
    Quizzy*         quizzy;
    QStackedWidget* ecrans;
    QLabel*         messageAttente;

    void initialiserEcrans();
    void creerEcrans();

    // Ecran d'attente

    void creerEcranAttente();

    QWidget* ecranAttente;
    QLabel*  titreEcranAttente;

    // Ecran d'accueil

    void creerEcranAccueil();

    QWidget* ecranAccueil;
    QLabel*  titreEcranAccueil;

    // Ecran question

    void creerEcranQuestion();

    QWidget* ecranQuestion;
    QLabel*  titreEcranQuestion;

    // Ecran reponse

    void creerEcranReponse();

    QWidget* ecranReponse;
    QLabel*  titreEcranReponse;

    // Ecran fin

    void creerEcranFin();

    QWidget* ecranFin;
    QLabel*  titreEcranFin;

  public slots:
    void afficherEcran(QuizzyGUI::Ecran ecran);
    void afficherEcranAttente();
    void ecranSuivant();
};

#endif // QUIZZYGUI_H
