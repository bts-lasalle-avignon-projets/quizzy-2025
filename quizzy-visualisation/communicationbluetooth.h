#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>
#include <QMap>

#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothAddress>
#include <QBluetoothDeviceInfo>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>

// Trame pour la configuration :
//@@Thème;Tempsparquestion;Nombredequestion;NomJoueur1;NomJoueur2##

// Trame pour chaque question :
//@@Question;N°delaréponse;PropositionA;PropositionB;PropositionC;PropositionD##

// Trame pour les réponses des joueurs
//@@N°dujoueur;N°delapropositionchoisie##

#define DEBUT_TRAME "@@"
#define FIN_TRAME   "##"
#define SEPARATEUR  ';'

#define THEME              0;
#define TEMPS              1;
#define NOMBRE_DE_QUESTION 2;
#define NOM_JOUEUR_1       3;
#define NOM_JOUEUR_2       4;

#define QUESTION       0;
#define NUMERO_REPONSE 1;
#define PROPOSITION_A  2;
#define PROPOSITION_B  3;
#define PROPOSITION_C  4;
#define PROPOSITION_D  5;

#define NUMERO_DU_JOUEUR      0;
#define NUMERO_REPONSE_JOUEUR 1;

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();

  private:
    bool etatDeConnexion;

    QBluetoothLocalDevice appareil;
    QBluetoothAddress     addresseDeLappareil;
    QString               nomDeLappareil;

    QBluetoothServer*     serveur;
    QBluetoothSocket*     socketDeLAppareil;
    QBluetoothServiceInfo informationsDuService;

    bool traiterTrame(QString trameRecue);
    void lireTrame(QString trameTraitee);

  private slots:
    void connecterAppareil();
    void deconnecterAppareil();
    void recevoirTrame();

  signals:
    void appareilConnecte();
    void appareilDeconnecte();
    void sessionParametree(QString theme,
                           int     temps,
                           int     nombreDeQuestion,
                           QString nomJoueur1,
                           QString nomJoueur2);
    void questionRecue(QString     question,
                       int         numeroDeLaReponse,
                       QStringList propositions);
    void reponseJoueurRecue(int numeroDuJoueur, int numeroPropositionChoisie);
};

#endif // COMMUNICATIONBLUETOOTH_H
