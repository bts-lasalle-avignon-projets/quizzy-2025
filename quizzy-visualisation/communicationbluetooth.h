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

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();

    void demarrerServeur();
    void arreterServeur();

  private:
    bool etatDeConnexion;

    QBluetoothLocalDevice appareil;
    QBluetoothAddress     addresseDeLappareil;
    QString               nomDeLappareil;

    QBluetoothServer*     serveur;
    QBluetoothSocket*     socketDeLAppareil;
    QBluetoothServiceInfo informationsDuService;

  private slots:
    void connecterAppareil();
    void deconnecterAppareil();
    void recevoirTrame();

  signals:
    void appareilConnecte();
    void appareilDeconnecte();
};

#endif // COMMUNICATIONBLUETOOTH_H
