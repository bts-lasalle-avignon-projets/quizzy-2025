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

void CommunicationBluetooth::demarrerServeur()
{
    // TODO
}

void CommunicationBluetooth::arreterServeur()
{
    // TODO
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
    // TODO
}
