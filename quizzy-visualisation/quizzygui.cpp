/**
 * @file quizzygui.cpp
 *
 * @brief Définition de la classe QuizzyGUI
 * @author Louis Raffin
 * @version 1.0
 */

#include "communicationbluetooth.h"
#include "quizzygui.h"
#include "ecranattente.h"
#include "ecranaccueil.h"
#include "ecranquestion.h"
#include "ecranreponse.h"
#include "ecranfin.h"
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
    QMainWindow(parent), communication(new CommunicationBluetooth(this)),
    quizzy(new Quizzy(this, communication))
{
    qDebug() << Q_FUNC_INFO << this;

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

    creerEcrans();
    initialiserEvenements();
#ifdef RASPBERRY_PI
    showFullScreen();
#else
    showMaximized();
#endif
    afficherEcranAttente();
}

QuizzyGUI::~QuizzyGUI()
{
    qDebug() << Q_FUNC_INFO << this;
}

QStackedWidget* QuizzyGUI::getEcrans()
{
    return ecrans;
}

void QuizzyGUI::initialiserEcrans()
{
    ecrans          = new QStackedWidget(this);
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

void QuizzyGUI::afficherMessageConnexion(QString nom, QString adresse)
{
    ecranAttente->afficherMessageConnexion(QString("Appareil ") + nom +
                                           QString(" connecté !"));
}

void QuizzyGUI::afficherMessageDeconnexion(QString nom, QString adresse)
{
    ecranAttente->afficherMessageConnexion(QString("Appareil ") + nom +
                                           QString(" déconnecté !"));
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
    qDebug() << Q_FUNC_INFO << "ecran" << ecran;
    ecrans->setCurrentIndex(ecran);
}

void QuizzyGUI::afficherEcranAttente()
{
    afficherEcran(Ecran::idEcranAttente);
}

void QuizzyGUI::afficherEcranSuivant()
{
    int indexCourant = ecrans->currentIndex();
    if(indexCourant < ecrans->count() - 1)
    {
        afficherEcran(QuizzyGUI::Ecran(indexCourant + 1));
    }
}

void QuizzyGUI::initialiserEvenements()
{
    connect(communication,
            SIGNAL(appareilConnecte(QString, QString)),
            this,
            SLOT(afficherMessageConnexion(QString, QString)));
    connect(communication,
            SIGNAL(appareilDeconnecte(QString, QString)),
            this,
            SLOT(afficherMessageDeconnexion(QString, QString)));
    connect(communication,
            &CommunicationBluetooth::signalEcranSuivant,
            this,
            &QuizzyGUI::afficherEcranSuivant);
#ifdef TEST_ECRANS
    // Flèche droite pour écran suivant
    QAction* actionAllerDroite = new QAction(this);
    actionAllerDroite->setShortcut(QKeySequence(Qt::Key_Right));
    addAction(actionAllerDroite);
    connect(actionAllerDroite,
            SIGNAL(triggered()),
            this,
            SLOT(afficherEcranSuivant()));
#endif
}
