#include "communicationbluetooth.h"
#include <QDebug>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent)
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO << this;
}

void CommunicationBluetooth::connecterAppareil()
{
    // TODO
    etatDeConnexion = true;
    emit appareilConnecte();
}

void CommunicationBluetooth::deconnecterAppareil()
{
    if(etatDeConnexion)
        etatDeConnexion = false;
    emit appareilDeconnecte();
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees;

    donnees            = socketDeLAppareil->readAll();
    QString trameRecue = QString(donnees);

    traiterTrame(trameRecue);
}

bool CommunicationBluetooth::traiterTrame(QString trameRecue)
{
    // TODO
    return true;
}

void CommunicationBluetooth::lireTrame(QString trameTraitee)
{
    // TODO
    if(traiterTrame())
    {
        // TODO differencier l'envoie selon la trame recue
        emit sessionParametree(trameTraitee[THEME],
                               trameTraitee[TEMPS],
                               trameTraitee[NOMBRE_DE_QUESTION),
                               trameTraitee[NOM_JOUEUR_1],
                               trameTraitee[NOM_JOUEUR_2]);
        emit questionRecue(trameTraitee[QUESTION],
                           trameTraitee[NUMERO_REPONSE],
                           trameTraitee[PROPOSITION_A],
                           trameTraitee[PROPOSITION_B],
                           trameTraitee[PROPOSITION_C],
                           trameTraitee[PROPOSITION_D]);
        emit reponseJoueurRecue(trameTraitee[NUMERO_DU_JOUEUR],
                                trameTraitee[NUMERO_REPONSE_JOUEUR]);
    }
}
