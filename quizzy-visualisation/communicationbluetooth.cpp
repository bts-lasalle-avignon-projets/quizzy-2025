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
    qDebug() << Q_FUNC_INFO << "Données reçues : " << QString(donnees);
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

    connect(socket, SIGNAL(disconnected()), this, SLOT(socketDeconnecte()));
    connect(socket, SIGNAL(readyRead()), this, SLOT(recevoirTrame()));

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

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees = socket->readAll();
    QString    trame   = QString(donnees);

    qDebug() << Q_FUNC_INFO << "Trame reçue : " << trame;

    if(trame.startsWith("@@") && trame.endsWith("\n"))
    {
        trame = trame.mid(2, trame.length() - 4);

        QStringList parties = trame.split(";");

        QString typeDeTrame = parties[0];

        qDebug() << "Type de trame: " << typeDeTrame;

        if(typeDeTrame == "C")
        {
            QString theme       = parties[1];
            QString temps       = parties[2];
            QString nbQuestions = parties[3];

            qDebug() << "Thème: " << theme;
            qDebug() << "Temps: " << temps;
            qDebug() << "Nombre de questions: " << nbQuestions;
        }
        else if(typeDeTrame == "J")
        {
            QString joueur1 = parties[1];
            QString joueur2 = parties[2];

            qDebug() << "Joueur 1: " << joueur1;
            qDebug() << "Joueur 2: " << joueur2;

            emit changerEcran(QuizzyGUI::EcranAccueil);
        }
        else if(typeDeTrame == "Q")
        {
            QString titre       = parties[1];
            QString prop1       = parties[2];
            QString prop2       = parties[3];
            QString prop3       = parties[4];
            QString prop4       = parties[5];
            QString idReponse   = parties[6];
            QString explication = parties[7];
            QString points      = parties[8];

            qDebug() << "Titre: " << titre;
            qDebug() << "Propositions: " << prop1 << prop2 << prop3 << prop4;
            qDebug() << "ID Réponse: " << idReponse;
            qDebug() << "Explication: " << explication;
            qDebug() << "Points: " << points;

            emit changerEcran(QuizzyGUI::EcranQuestion);
        }
        else if(typeDeTrame == "S")
        {
            qDebug() << "Passer à la suite";
            emit changerEcran(QuizzyGUI::EcranReponse);
        }
        else if(typeDeTrame == "R")
        {
            QString score1 = parties[1];
            QString score2 = parties[2];

            qDebug() << "Score Joueur 1: " << score1;
            qDebug() << "Score Joueur 2: " << score2;

            emit changerEcran(QuizzyGUI::EcranFin);
        }
        else if(typeDeTrame == "T")
        {
            qDebug() << "Session terminée";
            emit changerEcran(QuizzyGUI::EcranFin);
        }
        else if(typeDeTrame == "F")
        {
            qDebug() << "Quiz terminé";
            emit changerEcran(QuizzyGUI::EcranFin);
        }
    }
    else
    {
        qDebug() << "Trame invalide";
    }
}
