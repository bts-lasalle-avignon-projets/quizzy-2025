#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include "quizzy.h"

#include <QObject>

#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothAddress>
#include <QBluetoothDeviceInfo>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>

static const QString uuidDuService(
  QStringLiteral("00001101-0000-1000-8000-00805F9B34FB"));
static const QString nomDuService(QStringLiteral("Quizzy"));

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    bool etatDeConnexion;

    QBluetoothLocalDevice appareil;
    QBluetoothAddress     addresseDeLappareil;
    QString               nomDeLappareil;

    QBluetoothServer*     serveur;
    QBluetoothSocket*     socketDeLAppareil;
    QBluetoothServiceInfo informationsDuService;

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();

    void demarrerServeur();
    void arreterServeur();
    void activerBluetooth();
    void verifierLaConnexion();
    void recupererInformationsAppareil();
    void rendreAppareilVisible();

  private slots:

    void connecterAppareil();
    void deconnecterAppareil();
    void recevoirTrame();

  signals:
    void appareilConnecte();
    void appareilDeconnecte();
};

#endif // COMMUNICATIONBLUETOOTH_H
