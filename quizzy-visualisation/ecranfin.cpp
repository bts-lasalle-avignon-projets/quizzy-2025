#include "ecranfin.h"
#include "quizzygui.h"
#include <QDebug>

EcranFin::EcranFin(QuizzyGUI* parent) :
    QWidget(parent), ecranFin(new QWidget(this)),
    layoutEcranFin(new QVBoxLayout(ecranFin)),
    messageFelicitation(new QLabel(this)), messageGagnant(new QLabel(this)),
    scoreGagnant(new QLabel(this)), scorePerdant(new QLabel(this)),
    messageFinDePartie(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    ecranFin->setObjectName("ecranFin");

    messageFelicitation->setAlignment(Qt::AlignCenter);
    messageFelicitation->setText(
      "Bravo, vous avez répondu à toutes les questions !");
    layoutEcranFin->addWidget(messageFelicitation);

    messageGagnant->setAlignment(Qt::AlignCenter);
    layoutEcranFin->addWidget(messageGagnant);

    scoreGagnant->setAlignment(Qt::AlignCenter);
    layoutEcranFin->addWidget(scoreGagnant);

    scorePerdant->setAlignment(Qt::AlignCenter);
    layoutEcranFin->addWidget(scorePerdant);

    messageFinDePartie->setAlignment(Qt::AlignCenter);
    messageFinDePartie->setText("La partie va se terminer ...");
    layoutEcranFin->addWidget(messageFinDePartie);

    parent->getEcrans()->addWidget(ecranFin);
}

void EcranFin::afficherScores(QString nomJoueur1,
                              QString nomJoueur2,
                              QString scoreJoueur1,
                              QString scoreJoueur2)
{
    scoreGagnant->setText(nomJoueur1 + " : " + scoreJoueur1 + " points");
    scorePerdant->setText(nomJoueur2 + " : " + scoreJoueur2 + " points");
}

void EcranFin::afficherGagnant(QString texte)
{
    messageGagnant->setText(texte);
}

EcranFin::~EcranFin()
{
    qDebug() << Q_FUNC_INFO << this;
}
