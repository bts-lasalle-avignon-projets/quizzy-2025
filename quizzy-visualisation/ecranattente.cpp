#include "ecranattente.h"
#include "quizzygui.h"
#include <QDebug>

EcranAttente::EcranAttente(QuizzyGUI* parent) :
    QWidget(parent), ecranAttente(new QWidget(this)),
    layoutEcranAttente(new QVBoxLayout(ecranAttente)),
    titreEcranAttente(new QLabel(this)), messageConnexion(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    titreEcranAttente->setAlignment(Qt::AlignCenter);
    titreEcranAttente->setText("EcranAttente");
    messageConnexion->setText("En attente de connexion ...");
    messageConnexion->setAlignment(Qt::AlignCenter);
    layoutEcranAttente->addWidget(titreEcranAttente);
    layoutEcranAttente->addWidget(messageConnexion);
    parent->getEcrans()->addWidget(ecranAttente);
}

EcranAttente::~EcranAttente()
{
    qDebug() << Q_FUNC_INFO << this;
}

void EcranAttente::afficherMessageConnexion(QString message)
{
    messageConnexion->setText(message);
}
