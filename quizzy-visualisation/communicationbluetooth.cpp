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

void CommunicationBluetooth::verifierLaConnexion()
{
    if(appareil.isValid())
    {
        activerBluetooth();
        recupererInformationsAppareil();
        rendreAppareilVisible();
        qDebug() << Q_FUNC_INFO << "Is valid";
    }
}

void CommunicationBluetooth::activerBluetooth()
{
    appareil.powerOn();
    qDebug() << Q_FUNC_INFO << "Is on";
}

void CommunicationBluetooth::recupererInformationsAppareil()
{
    nomDeLappareil      = appareil.name();
    addresseDeLappareil = appareil.address();
    qDebug() << Q_FUNC_INFO << "Info OK";
}

void CommunicationBluetooth::rendreAppareilVisible()
{
    appareil.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
    qDebug() << Q_FUNC_INFO << "Is visible";
}

void CommunicationBluetooth::demarrerServeur()
{
    if(serveur == nullptr)
    {
    serveur = new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);

    connect(serveur, SIGNAL(newConnection()), this, SLOT(connecterAppareil()));

    QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);
    informationsDuService = serveur->listen(uuid, nomDuService);
    qDebug() << Q_FUNC_INFO << "Server On";
    }
}

void CommunicationBluetooth::arreterServeur()
{
    socketDeLAppareil->close();
    delete socketDeLAppareil;
    socketDeLAppareil = nullptr;

    delete serveur;
    serveur = nullptr;
}

void CommunicationBluetooth::connecterAppareil()
{
    socketDeLAppareil = serveur->nextPendingConnection();
    connect(socketDeLAppareil,
            SIGNAL(disconnected()),
            this,
            SLOT(deconnecterAppareil()));
    connect(socketDeLAppareil,
            SIGNAL(readyRead()),
            this,
            SLOT(recevoirTrame()));

    etatDeConnexion = true;

    emit appareilConnecte();
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees;

    donnees       = socketDeLAppareil->readAll();
    QString trame = QString(donnees);
    qDebug() << Q_FUNC_INFO << "trame" << trame;
}

void CommunicationBluetooth::deconnecterAppareil()
{
    if(etatDeConnexion)
        etatDeConnexion = false;
    emit appareilDeconnecte();
}

void CommunicationBluetooth::verifierLaConnexion()
{
    if(appareil.isValid())
    {
        activerBluetooth();
        recupererInformationsAppareil();
        rendreAppareilVisible();
    }
}

void CommunicationBluetooth::activerBluetooth()
{
    appareil.powerOn();
}

void CommunicationBluetooth::recupererInformationsAppareil()
{
    nomDeLappareil      = appareil.name();
    addresseDeLappareil = appareil.address();
}

void CommunicationBluetooth::rendreAppareilVisible()
{
    appareil.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
}

void CommunicationBluetooth::demarrerServeur()
{
    if(serveur == nullptr)
    {
        serveur =
          new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);

        connect(serveur,
                SIGNAL(newConnection()),
                this,
                SLOT(connecterAppareil()));

        QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);
        informationsDuService = serveur->listen(uuid, nomDuService);
    }
}

void CommunicationBluetooth::arreterServeur()
{
    socketDeLAppareil->close();
    delete socketDeLAppareil;
    socketDeLAppareil = nullptr;

    delete serveur;
    serveur = nullptr;
}

void CommunicationBluetooth::connecterAppareil()
{
    socketDeLAppareil = serveur->nextPendingConnection();

    connect(socketDeLAppareil,
            SIGNAL(disconnected()),
            this,
            SLOT(deconnecterAppareil()));
    connect(socketDeLAppareil,
            SIGNAL(readyRead()),
            this,
            SLOT(recevoirTrame()));

    etatDeConnexion = true;

    emit appareilConnecte();
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees;

    donnees       = socketDeLAppareil->readAll();
    QString trame = QString(donnees);
    qDebug() << Q_FUNC_INFO << "trame" << trame;
}

void CommunicationBluetooth::deconnecterAppareil()
{
    if(etatDeConnexion)
        etatDeConnexion = false;
    emit appareilDeconnecte();
}
