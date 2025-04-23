#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include "quizzy.h"
#include "quizzygui.h"
#include <QObject>

#include <QBluetoothUuid>
#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothAddress>
#include <QBluetoothDeviceInfo>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>

static const QString serviceUuid(
  QStringLiteral("00001101-0000-1000-8000-00805F9B34FB"));
static const QString serviceNom(QStringLiteral("Quizzy"));

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  public:
    explicit CommunicationBluetooth(QObject* parent = 0);
    ~CommunicationBluetooth();

    void                     demarrerServeur();
    void                     arreterServeur();
    void                     deconnecterAppareil();
    bool                     verifierLaConnexion();
    QString                  getNomAppareil();
    QString                  getAdresseAppareil();
    QList<QBluetoothAddress> getPeripheriquesDistants();

  public slots:
    void envoyer(QString trame);

  private slots:
    void socketDeconnecte();
    void socketPretALire();
    void nouveauClient();
    void recevoirTrame();
    void separerTrame(QString);
    void traiterTrame(QStringList);

  private:
    QBluetoothLocalDevice appareil;
    QBluetoothServer*     serveur;
    QBluetoothSocket*     socket;
    QBluetoothServiceInfo informationsDuService;
    QString               nomDeLAppareil;
    QString               adresseDeLAppareil;
    bool                  connecte;

  signals:
    void clientConnecte();
    void clientDeconnecte();
    void afficherMessage(QString message);
    void changerEcran(QuizzyGUI::Ecran ecran);
    void signalEcranSuivant();
};
#endif // COMMUNICATIONBLUETOOTH_H
