#include "ecranattente.h"
#include "quizzygui.h"
#include <QDebug>

EcranAttente::EcranAttente(QuizzyGUI* parent) :
    QWidget(parent), ecranAttente(new QWidget(this)),
    layoutEcranAttente(new QVBoxLayout(ecranAttente)),
    messageConnexion(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    ecranAttente->setObjectName("ecranAttente");
    messageConnexion->setObjectName("messageConnexion");

    messageConnexion->setText("En attente de connexion ...");
    messageConnexion->setAlignment(Qt::AlignCenter);

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
