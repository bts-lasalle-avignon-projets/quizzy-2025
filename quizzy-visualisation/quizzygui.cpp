/**
 * @file quizzygui.cpp
 *
 * @brief Définition de la classe QuizzyGUI
 * @author Louis Raffin
 * @version 1.0
 */

#include "communicationbluetooth.h"
#include "quizzygui.h"
#include "quizzy.h"
#include <QDebug>

/**
 * @brief Constructeur de la classe QuizzyGUI
 *
 * @fn QuizzyGUI::QuizzyGUI
 * @param parent L'adresse de l'objet parent, si nullptr QuizzyGUI sera la
 * fenêtre principale de l'application
 */

QuizzyGUI::QuizzyGUI(QWidget* parent) : QMainWindow(parent)
{
    quizzy        = new Quizzy(this);
    communication = quizzy->getCommunication();

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

    creerEcrans();
    initialiserEvenements();
}

QuizzyGUI::~QuizzyGUI()
{
    delete quizzy;
    qDebug() << Q_FUNC_INFO << this;
}

EcranAttente::EcranAttente(QuizzyGUI* parent)
{
    ecranAttente       = new QWidget(this);
    layoutEcranAttente = new QVBoxLayout(ecranAttente);

    titreEcranAttente = new QLabel(this);
    titreEcranAttente->setAlignment(Qt::AlignCenter);

    messageConnexion = new QLabel(this);
    messageConnexion->setText("En attente de connexion ...");
    messageConnexion->setAlignment(Qt::AlignCenter);

    layoutEcranAttente->addWidget(titreEcranAttente);
    layoutEcranAttente->addWidget(messageConnexion);

    parent->ecrans->addWidget(ecranAttente);
}

EcranAttente::~EcranAttente()
{
    delete ecranAttente;
    qDebug() << Q_FUNC_INFO << this;
}

EcranAccueil::EcranAccueil(QuizzyGUI* parent)
{
    ecranAccueil       = new QWidget(this);
    layoutEcranAccueil = new QVBoxLayout(ecranAccueil);
    titreEcranAccueil  = new QLabel(this);

    titreEcranAccueil->setAlignment(Qt::AlignCenter);

    layoutEcranAccueil->addWidget(titreEcranAccueil);
    parent->ecrans->addWidget(ecranAccueil);
}

EcranAccueil::~EcranAccueil()
{
    delete ecranAccueil;
    qDebug() << Q_FUNC_INFO << this;
}

EcranQuestion::EcranQuestion(QuizzyGUI* parent)
{
    ecranQuestion       = new QWidget(this);
    layoutEcranQuestion = new QVBoxLayout(ecranQuestion);
    titreEcranQuestion  = new QLabel(this);

    titreEcranQuestion->setAlignment(Qt::AlignCenter);

    layoutEcranQuestion->addWidget(titreEcranQuestion);
    parent->ecrans->addWidget(ecranQuestion);
}

EcranQuestion::~EcranQuestion()
{
    delete ecranQuestion;
    qDebug() << Q_FUNC_INFO << this;
}

EcranReponse::EcranReponse(QuizzyGUI* parent)
{
    ecranReponse       = new QWidget(this);
    layoutEcranReponse = new QVBoxLayout(ecranReponse);
    titreEcranReponse  = new QLabel(this);

    titreEcranReponse->setAlignment(Qt::AlignCenter);

    layoutEcranReponse->addWidget(titreEcranReponse);
    parent->ecrans->addWidget(ecranReponse);
}

EcranReponse::~EcranReponse()
{
    delete ecranReponse;
    qDebug() << Q_FUNC_INFO << this;
}

EcranFin::EcranFin(QuizzyGUI* parent)
{
    ecranFin       = new QWidget(this);
    layoutEcranFin = new QVBoxLayout(ecranFin);
    titreEcranFin  = new QLabel(this);

    titreEcranFin->setAlignment(Qt::AlignCenter);

    layoutEcranFin->addWidget(titreEcranFin);
    parent->ecrans->addWidget(ecranFin);
}

EcranFin::~EcranFin()
{
    delete ecranFin;
    qDebug() << Q_FUNC_INFO << this;
}

void QuizzyGUI::initialiserEcrans()
{
    ecrans = new QStackedWidget(this);
    ecrans->setCurrentIndex(idEcranAttente);

    layoutPrincipal = new QVBoxLayout();

    layoutPrincipal->addWidget(ecrans);
    QWidget* centralWidget = new QWidget(this);
    centralWidget->setLayout(layoutPrincipal);
    setCentralWidget(centralWidget);
}

void QuizzyGUI::creerEcrans()
{
    initialiserEcrans();
    creerEcranAttente();
    creerEcranAccueil();
    creerEcranQuestion();
    creerEcranReponse();
    creerEcranFin();
}

void QuizzyGUI::changerEtatConnexion()
{
    ecranAttente->messageConnexion->setText("Appareil connecté !");
}

void QuizzyGUI::creerEcranAttente()
{
    ecranAttente = new EcranAttente(this);
}

void QuizzyGUI::creerEcranAccueil()
{
    ecranAccueil = new EcranAccueil(this);
}

void QuizzyGUI::creerEcranQuestion()
{
    ecranQuestion = new EcranQuestion(this);
}

void QuizzyGUI::creerEcranReponse()
{
    ecranReponse = new EcranReponse(this);
}

void QuizzyGUI::creerEcranFin()
{
    ecranFin = new EcranFin(this);
}

void QuizzyGUI::afficherEcran(QuizzyGUI::Ecran ecran)
{
    qDebug() << Q_FUNC_INFO << "Ecran affiché" << ecran;
    ecrans->setCurrentIndex(ecran);
}

void QuizzyGUI::afficherEcranAttente()
{
    afficherEcran(Ecran::idEcranAttente);
}

void QuizzyGUI::ecranSuivant()
{
    qDebug() << "ecran suivant";
    int indexCourant = ecrans->currentIndex();
    if(indexCourant < ecrans->count() - 1)
    {
        ecrans->setCurrentIndex(indexCourant + 1);
    }
}

void QuizzyGUI::initialiserEvenements()
{
    connect(communication,
            &CommunicationBluetooth::signalEcranSuivant,
            this,
            &QuizzyGUI::ecranSuivant);
}
