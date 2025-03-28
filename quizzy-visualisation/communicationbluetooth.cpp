#include "communicationbluetooth.h"
#include <QDebug>
#include <unistd.h>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent), serveur(NULL), socket(NULL), nomDeLAppareil(""),
    adresseDeLAppareil(""), connecte(false)
{
    appareil.powerOn();

    nomDeLAppareil     = appareil.name();
    adresseDeLAppareil = appareil.address().toString();

    appareil.setHostMode(QBluetoothLocalDevice::HostDiscoverable);

    connect(&appareil,
            SIGNAL(deviceConnected(QBluetoothAddress)),
            this,
            SLOT(appareilConnecte(QBluetoothAddress)));

    connect(&appareil,
            SIGNAL(deviceDisconnected(QBluetoothAddress)),
            this,
            SLOT(appareilDeconnecte(QBluetoothAddress)));

    demarrerServeur();
}

void CommunicationBluetooth::demarrerServeur()
{
    if(serveur == NULL)
    {
        serveur =
          new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);
        connect(serveur, SIGNAL(newConnection()), this, SLOT(nouveauClient()));

        QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);
        informationsDuService = serveur->listen(uuid, serviceNom);
    }
}

void CommunicationBluetooth::arreterServeur()
{
    informationsDuService.unregisterService();

    if(socket)
    {
        if(socket->isOpen())
            socket->close();
        delete socket;
        socket = NULL;
    }

    delete serveur;
    serveur = NULL;
}

void CommunicationBluetooth::deconnecterAppareil()
{
    arreterServeur();
    demarrerServeur();
}

bool CommunicationBluetooth::verifierLaConnexion()
{
    qDebug() << Q_FUNC_INFO;
    return appareil.isValid();
}

QString CommunicationBluetooth::getNomAppareil()
{
    return nomDeLAppareil;
}

QString CommunicationBluetooth::getAdresseAppareil()
{
    return adresseDeLAppareil;
}

QList<QBluetoothAddress> CommunicationBluetooth::getPeripheriquesDistants()
{
    return appareil.connectedDevices();
}

void CommunicationBluetooth::appareilConnecte(const QBluetoothAddress& adresse)
{
    QString message =
      QString::fromUtf8("Demande connexion du client ") + adresse.toString();
    if(appareil.pairingStatus(adresse) == QBluetoothLocalDevice::Paired ||
       appareil.pairingStatus(adresse) ==
         QBluetoothLocalDevice::AuthorizedPaired)
        message += " [" + QString::fromUtf8("appairé") + "]";
    else
        message += " [" + QString::fromUtf8("non appairé") + "]";
    emit afficherMessage(message);
}

void CommunicationBluetooth::appareilDeconnecte(
  const QBluetoothAddress& adresse)
{
    QString message =
      QString::fromUtf8("Client déconnecté") + adresse.toString();
    emit afficherMessage(message);
}

void CommunicationBluetooth::socketDeconnecte()
{
    connecte = false;
    emit clientDeconnecte();
}

void CommunicationBluetooth::socketPretALire()
{
    QByteArray donnees;

    while(socket->bytesAvailable())
    {
        donnees += socket->readAll();
        sleep(150000); // cf. timeout
    }
    emit afficherMessage(QString::fromUtf8("Données reçues : ") +
                         QString(donnees));
}

void CommunicationBluetooth::envoyer(QString trame)
{
    if(socket == NULL || !socket->isOpen())
        return;

    emit afficherMessage(QString::fromUtf8("Données envoyées : ") + trame);
    trame += "\r\n";
    socket->write(trame.toLatin1());
}

void CommunicationBluetooth::nouveauClient()
{
    socket = serveur->nextPendingConnection();
    if(!socket)
        return;

    connect(socket,
            SIGNAL(appareilDeconnecte()),
            this,
            SLOT(socketDeconnecte()));

    connecte = true;
    emit    clientConnecte();
    QString message = QString::fromUtf8("Périphérique ") + socket->peerName() +
                      " [" + socket->peerAddress().toString() + "] " +
                      QString::fromUtf8("connecté ");
    emit afficherMessage(message);
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    arreterServeur();
    appareil.setHostMode(QBluetoothLocalDevice::HostPoweredOff);
}

/*
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
}*/
