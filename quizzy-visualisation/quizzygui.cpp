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
#endif
}

QuizzyGUI::~QuizzyGUI()
{
    delete quizzy;
    qDebug() << Q_FUNC_INFO << this;
}

void QuizzyGUI::initialiserEcrans()
{
    ecrans                       = new QStackedWidget(this);
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
}

void QuizzyGUI::creerEcranQuestion()
{
}

void QuizzyGUI::creerEcranReponse()
{
}

void QuizzyGUI::creerEcranFin()
{
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
