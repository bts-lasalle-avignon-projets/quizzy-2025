#include "ecranaccueil.h"
#include "quizzygui.h"
#include <QDebug>

EcranAccueil::EcranAccueil(QuizzyGUI* parent) :
    QWidget(parent), ecranAccueil(new QWidget(this)),
    layoutVEcranAccueil(new QVBoxLayout(ecranAccueil)),
    layoutHEcranAccueil(new QHBoxLayout(ecranAccueil)),
    layoutNomsJoueurs(new QVBoxLayout(ecranAccueil)),
    messageThemeChoisi(new QLabel(this)), nomJoueur1(new QLabel(this)),
    nomJoueur2(new QLabel(this)), nombreDeQuestions(new QLabel(this)),
    tempsParQuestion(new QLabel(this)), messagePreparation(new QLabel(this))

{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    ecranAccueil->setObjectName("ecranAccueil");
    messageThemeChoisi->setObjectName("messageThemeChoisi");

    messageThemeChoisi->setAlignment(Qt::AlignCenter);
    messageThemeChoisi->setText("message theme choisi");

    messagePreparation->setAlignment(Qt::AlignCenter);
    messagePreparation->setText("La partie va bientôt commencer !");

    nomJoueur1->setAlignment(Qt::AlignHCenter);
    nomJoueur1->setText("Joueur 1 :");
    nomJoueur2->setAlignment(Qt::AlignHCenter);
    nomJoueur2->setText("Joueur 2 :");

    nombreDeQuestions->setAlignment(Qt::AlignCenter);
    nombreDeQuestions->setText("Le quiz comporte \n x questions");

    tempsParQuestion->setAlignment(Qt::AlignCenter);
    tempsParQuestion->setText("Vous avez x secondes par question.");

    layoutVEcranAccueil->addWidget(messageThemeChoisi);
    layoutVEcranAccueil->addLayout(layoutHEcranAccueil);
    layoutVEcranAccueil->addWidget(tempsParQuestion);
    layoutVEcranAccueil->addWidget(messagePreparation);

    layoutHEcranAccueil->addLayout(layoutNomsJoueurs);
    layoutHEcranAccueil->addWidget(nombreDeQuestions);

    layoutNomsJoueurs->addWidget(nomJoueur1);
    layoutNomsJoueurs->addWidget(nomJoueur2);

    parent->getEcrans()->addWidget(ecranAccueil);
}

EcranAccueil::~EcranAccueil()
{
    qDebug() << Q_FUNC_INFO << this;
}

void EcranAccueil::afficherThemeChoisi(QString theme)
{
    QString message = "Le thème choisi est " + theme;
    messageThemeChoisi->setText(message);

    auto gui = qobject_cast<QuizzyGUI*>(parent());
    if(gui)
    {
        gui->getQuestion()->setTheme(theme);
    }
}

void EcranAccueil::afficherNombreDeQuestions(QString nbQuestions)
{
    QString message = "Le quiz comporte \n" + nbQuestions + " questions";
    nombreDeQuestions->setText(message);
}

void EcranAccueil::afficherTempsParQuestion(QString temps)
{
    QString message = "Vous avez " + temps + " secondes par question.";
    tempsParQuestion->setText(message);
}

void EcranAccueil::afficherNomsJoueurs(QString nomDuJoueur1,
                                       QString nomDuJoueur2)
{
    QString nomJ1 = "Joueur 1 : " + nomDuJoueur1;
    QString nomJ2 = "Joueur 2 : " + nomDuJoueur2;
    nomJoueur1->setText(nomJ1);
    nomJoueur2->setText(nomJ2);
}
