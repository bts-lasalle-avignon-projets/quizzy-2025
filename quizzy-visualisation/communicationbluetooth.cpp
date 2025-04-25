#include "communicationbluetooth.h"
#include <QDebug>
#include "quizzygui.h"
#include <unistd.h>
#include <stdio.h>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent), serveur(NULL), socket(NULL), nomDeLAppareil(""),
    adresseDeLAppareil(""), connecte(false)
{
    appareil.powerOn();

    nomDeLAppareil     = appareil.name();
    adresseDeLAppareil = appareil.address().toString();

    appareil.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
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
        qDebug() << getNomAppareil() << getAdresseAppareil();
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
    emit clientConnecte();
    qDebug() << Q_FUNC_INFO << "Appareil connecté !";
    QString message = QString::fromUtf8("Périphérique ") + socket->peerName() +
                      " [" + socket->peerAddress().toString() + "] " +
                      QString::fromUtf8("connecté ");
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    arreterServeur();
    appareil.setHostMode(QBluetoothLocalDevice::HostPoweredOff);
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees;

    donnees       = socket->readAll();
    QString trame = QString(donnees);
    qDebug() << Q_FUNC_INFO << "Trame reçue :" << trame;

    separerTrame(trame);
}

void CommunicationBluetooth::separerTrame(QString trame)
{
    trame.replace("\\n", "\n");
    trame                    = trame.mid(2, trame.length() - 3);
    QStringList trameSeparee = trame.split(";");
    traiterTrame(trameSeparee);
    // qDebug() << Q_FUNC_INFO << "Trame séparée" << trameSeparee;
}

void CommunicationBluetooth::traiterTrame(QStringList trameSeparee)
{
    QChar typeDeTrame = trameSeparee[TYPE_DE_TRAME].at(0);

    qDebug() << "Type de trame: " << typeDeTrame;

    switch(typeDeTrame.toLatin1())
    {
        case 'C':
        {
            QString theme       = trameSeparee[THEME];
            QString temps       = trameSeparee[TEMPS];
            QString nbQuestions = trameSeparee[NOMBRE_DE_QUESTION];

            qDebug() << "Thème: " << theme;
            qDebug() << "Temps: " << temps;
            qDebug() << "Nombre de questions: " << nbQuestions;
            break;
        }
        case 'J':
        {
            QString joueur1 = trameSeparee[NOM_JOUEUR_1];
            QString joueur2 = trameSeparee[NOM_JOUEUR_2];

            qDebug() << "Joueur 1: " << joueur1;
            qDebug() << "Joueur 2: " << joueur2;

            emit changerEcran(QuizzyGUI::EcranAccueil);
            break;
        }
        case 'Q':
        {
            QString titre       = trameSeparee[TITRE_QUESTION];
            QString propA       = trameSeparee[PROPOSITION_A];
            QString propB       = trameSeparee[PROPOSITION_B];
            QString propC       = trameSeparee[PROPOSITION_C];
            QString propD       = trameSeparee[PROPOSITION_D];
            QString idReponse   = trameSeparee[NUMERO_REPONSE];
            QString explication = trameSeparee[EXPLICATION];
            QString points      = trameSeparee[POINTS];

            qDebug() << "Titre: " << titre;
            qDebug() << "Propositions: " << propA << propB << propC << propD;
            qDebug() << "ID Réponse: " << idReponse;
            qDebug() << "Explication: " << explication;
            qDebug() << "Points: " << points;

            emit changerEcran(QuizzyGUI::EcranQuestion);
            break;
        }
        case 'S':
        {
            qDebug() << "Passer à la suite";
            emit signalEcranSuivant();
            break;
        }
        case 'R':
        {
            QString score1 = trameSeparee[NOM_JOUEUR_1];
            QString score2 = trameSeparee[NOM_JOUEUR_2];

            qDebug() << "Score Joueur 1: " << score1;
            qDebug() << "Score Joueur 2: " << score2;

            emit changerEcran(QuizzyGUI::EcranFin);
            break;
        }
        case 'T':
        {
            qDebug() << "Session terminée";
            emit changerEcran(QuizzyGUI::EcranFin);
            break;
        }
        case 'F':
        {
            qDebug() << "Quiz terminé";
            emit changerEcran(QuizzyGUI::EcranFin);
            break;
        }
        default:
        {
            qDebug() << "Trame invalide";
            break;
        }
    }
}
