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

QuizzyGUI::QuizzyGUI(QWidget* parent) :
    QMainWindow(parent), quizzy(new Quizzy(this))
{
    qDebug() << Q_FUNC_INFO << this;

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

    creerEcrans();

#ifdef RASPBERRY_PI
    showFullScreen();
#else
    showFullScreen();
#endif
}

QuizzyGUI::~QuizzyGUI()
{
    delete quizzy;
    qDebug() << Q_FUNC_INFO << this;
}

void QuizzyGUI::initialiserEcrans()
{
    ecrans = new QStackedWidget(this);
    ecrans->setCurrentIndex(EcranAttente);

    QVBoxLayout* layoutPrincipal = new QVBoxLayout();

    layoutPrincipal->addWidget(ecrans);
    QWidget* centralWidget = new QWidget(this);
    centralWidget->setLayout(layoutPrincipal);
    setCentralWidget(centralWidget);

    CommunicationBluetooth* com = new CommunicationBluetooth(this);
    connect(com,
            SIGNAL(changerEcran(QuizzyGUI::Ecran)),
            this,
            SLOT(afficherEcran(QuizzyGUI::Ecran)));
    connect(com, SIGNAL(signalEcranSuivant()), this, SLOT(ecranSuivant()));
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

void QuizzyGUI::creerEcranAttente()
{
    ecranAttente                    = new QWidget(this);
    QVBoxLayout* layoutEcranAttente = new QVBoxLayout(ecranAttente);
    titreEcranAttente               = new QLabel(this);

    titreEcranAttente->setAlignment(Qt::AlignCenter);

    messageAttente = new QLabel("", this);
    messageAttente->setText("En attente de connexion ...");
    messageAttente->setAlignment(Qt::AlignCenter);

    layoutEcranAttente->addWidget(titreEcranAttente);
    layoutEcranAttente->addWidget(messageAttente);
    ecrans->addWidget(ecranAttente);
}

void QuizzyGUI::creerEcranAccueil()
{
    ecranAccueil                    = new QWidget(this);
    QVBoxLayout* layoutEcranAccueil = new QVBoxLayout(ecranAccueil);
    titreEcranAccueil               = new QLabel(this);

    titreEcranAccueil->setAlignment(Qt::AlignCenter);

    layoutEcranAccueil->addWidget(titreEcranAccueil);
    ecrans->addWidget(ecranAccueil);
}

void QuizzyGUI::creerEcranQuestion()
{
    ecranQuestion                    = new QWidget(this);
    QVBoxLayout* layoutEcranQuestion = new QVBoxLayout(ecranQuestion);
    titreEcranQuestion               = new QLabel(this);

    titreEcranQuestion->setAlignment(Qt::AlignCenter);

    layoutEcranQuestion->addWidget(titreEcranQuestion);
    ecrans->addWidget(ecranQuestion);
}

void QuizzyGUI::creerEcranReponse()
{
    ecranReponse                    = new QWidget(this);
    QVBoxLayout* layoutEcranReponse = new QVBoxLayout(ecranReponse);
    titreEcranReponse               = new QLabel(this);

    titreEcranReponse->setAlignment(Qt::AlignCenter);

    layoutEcranReponse->addWidget(titreEcranReponse);
    ecrans->addWidget(ecranReponse);
}

void QuizzyGUI::creerEcranFin()
{
    ecranFin                    = new QWidget(this);
    QVBoxLayout* layoutEcranFin = new QVBoxLayout(ecranFin);
    titreEcranFin               = new QLabel(this);

    titreEcranFin->setAlignment(Qt::AlignCenter);

    layoutEcranFin->addWidget(titreEcranFin);
    ecrans->addWidget(ecranFin);
}

void QuizzyGUI::afficherEcran(QuizzyGUI::Ecran ecran)
{
    qDebug() << Q_FUNC_INFO << "Ecran affiché" << ecran;
    ecrans->setCurrentIndex(ecran);
}

void QuizzyGUI::afficherEcranAttente()
{
    afficherEcran(Ecran::EcranAttente);
}

void QuizzyGUI::ecranSuivant()
{
    int indexCourant = ecrans->currentIndex();
    if(indexCourant < ecrans->count() - 1)
    {
        ecrans->setCurrentIndex(indexCourant + 1);
    }
}
