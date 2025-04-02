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
    void creerEcranAttente();
    void creerEcranAccueil();
    void creerEcranQuestion();
    void creerEcranReponse();
    void creerEcranFin();

    // Ecran d'attente

    QWidget* ecranAttente;
    QLabel*  titreEcranAttente;

  public slots:
    void afficherEcran(QuizzyGUI::Ecran ecran);
    void afficherEcranAttente();
};

#endif // QUIZZYGUI_H
