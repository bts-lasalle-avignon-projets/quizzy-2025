#include "communicationbluetooth.h"
#include <QDebug>
#include "quizzygui.h"
#include <unistd.h>
#include <stdio.h>

CommunicationBluetooth::CommunicationBluetooth(QuizzyGUI* gui) :
    QObject(gui), serveur(nullptr), socket(nullptr), nomDeLAppareil(""),
    adresseDeLAppareil(""), connecte(false)
{
    qDebug() << Q_FUNC_INFO << this;

    if(appareil.isValid())
    {
        appareil.powerOn();

        nomDeLAppareil     = appareil.name();
        adresseDeLAppareil = appareil.address().toString();

        demarrerServeur();

        appareil.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
    }
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    arreterServeur();
    appareil.setHostMode(QBluetoothLocalDevice::HostPoweredOff);
    qDebug() << Q_FUNC_INFO << this;
}

void CommunicationBluetooth::demarrerServeur()
{
    if(serveur == nullptr)
    {
        qDebug() << Q_FUNC_INFO << getNomAppareil() << getAdresseAppareil();
        serveur =
          new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);
        connect(serveur,
                SIGNAL(newConnection()),
                this,
                SLOT(connecterAppareil()));

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
        socket = nullptr;
    }

    delete serveur;
    serveur = nullptr;
}

void CommunicationBluetooth::reconnecterAppareil()
{
    arreterServeur();
    demarrerServeur();
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

void CommunicationBluetooth::deconnecterAppareil()
{
    if(connecte)
    {
        qDebug() << Q_FUNC_INFO << socket->peerName()
                 << socket->peerAddress().toString();
        connecte = false;
        emit appareilDeconnecte(socket->peerName(),
                                socket->peerAddress().toString());
    }
}

void CommunicationBluetooth::envoyer(QString trame)
{
    if(socket == nullptr || !socket->isOpen())
        return;

    qDebug() << Q_FUNC_INFO << "trame" << QString(trame);
    trame += "\r\n";
    socket->write(trame.toLatin1());
}

void CommunicationBluetooth::connecterAppareil()
{
    socket = serveur->nextPendingConnection();
    if(!socket)
        return;

    qDebug() << Q_FUNC_INFO << socket->peerName()
             << socket->peerAddress().toString();
    connect(socket, SIGNAL(disconnected()), this, SLOT(deconnecterAppareil()));
    connect(socket, SIGNAL(readyRead()), this, SLOT(recevoirTrame()));

    connecte = true;
    emit appareilConnecte(socket->peerName(), socket->peerAddress().toString());
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donnees;

    donnees       = socket->readAll();
    QString trame = QString(donnees);
    qDebug() << Q_FUNC_INFO << "trame" << trame;

    separerTrame(trame);
}

void CommunicationBluetooth::separerTrame(QString trame)
{
    trame.replace("\\n", "\n");
    trame                    = trame.mid(2, trame.length() - 3);
    QStringList trameSeparee = trame.split(";");
    traiterTrame(trameSeparee);
}

void CommunicationBluetooth::traiterTrame(QStringList trameSeparee)
{
    QChar typeDeTrame = trameSeparee[TYPE_DE_TRAME].at(0);

    qDebug() << Q_FUNC_INFO << "typeDeTrame" << typeDeTrame;

    switch(typeDeTrame.toLatin1())
    {
        case 'C':
        {
            QString theme       = trameSeparee[THEME];
            QString temps       = trameSeparee[TEMPS];
            QString nbQuestions = trameSeparee[NOMBRE_DE_QUESTION];

            auto gui = qobject_cast<QuizzyGUI*>(parent());
            if(!gui)
                return;

            gui->getQuestion()->setTheme(theme);
            gui->getQuestion()->setTemps(temps.toInt());
            gui->getQuestion()->setNombre(nbQuestions.toInt());

            emit signalConfiguration();

            qDebug() << Q_FUNC_INFO << "theme" << theme << "temps" << temps
                     << "nbQuestions" << nbQuestions;

            break;
        }
        case 'J':
        {
            QString joueur1 = trameSeparee[NOM_JOUEUR_1];
            QString joueur2 = trameSeparee[NOM_JOUEUR_2];

            auto gui = qobject_cast<QuizzyGUI*>(parent());
            if(!gui)
                return;

            gui->getJoueur1()->setNom(joueur1);
            gui->getJoueur2()->setNom(joueur2);

            emit signalNomsJoueurs();

            qDebug() << Q_FUNC_INFO << "joueur1" << joueur1 << "joueur2"
                     << joueur2;
            break;
        }
        case 'Q':
        {
            QString titre        = trameSeparee[TITRE_QUESTION];
            QString propositionA = trameSeparee[PROPOSITION_A];
            QString propositionB = trameSeparee[PROPOSITION_B];
            QString propositionC = trameSeparee[PROPOSITION_C];
            QString propositionD = trameSeparee[PROPOSITION_D];
            QString idReponse    = trameSeparee[NUMERO_REPONSE];
            QString explication  = trameSeparee[EXPLICATION];

            qDebug() << Q_FUNC_INFO << "titre" << titre << "propositions"
                     << propositionA << propositionB << propositionC
                     << propositionD << "idReponse" << idReponse
                     << "explication" << explication;

            auto gui = qobject_cast<QuizzyGUI*>(parent());
            if(!gui)
                return;

            gui->getQuestion()->setTitre(titre);
            gui->getQuestion()->setPropA(propositionA);
            gui->getQuestion()->setPropB(propositionB);
            gui->getQuestion()->setPropC(propositionC);
            gui->getQuestion()->setPropD(propositionD);
            gui->getQuestion()->setIdReponse(idReponse.toInt());
            gui->getQuestion()->setExplication(explication);

            emit signalQuestion();

            break;
        }
        case 'S':
        {
            emit signalEcranSuivant();
            break;
        }
        case 'R':
        {
            QString score1 = trameSeparee[NOM_JOUEUR_1];
            QString score2 = trameSeparee[NOM_JOUEUR_2];

            auto gui = qobject_cast<QuizzyGUI*>(parent());
            if(!gui)
                return;

            gui->getJoueur1()->setScore(score1.toInt());
            gui->getJoueur2()->setScore(score2.toInt());

            emit signalScore();

            qDebug() << Q_FUNC_INFO << "score1" << score1 << "score2" << score2;
            break;
        }
        case 'T':
        {
            break;
        }
        case 'F':
        {
            break;
        }
        default:
        {
            qDebug() << Q_FUNC_INFO << "typeDeTrame inconnue !";
            break;
        }
    }
}
