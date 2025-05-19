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

class QuizzyGUI;

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  public:
    explicit CommunicationBluetooth(QuizzyGUI* gui = nullptr);
    ~CommunicationBluetooth();

    void demarrerServeur();
    void arreterServeur();
    void reconnecterAppareil();
    bool verifierLaConnexion();

    QString                  getNomAppareil();
    QString                  getAdresseAppareil();
    QList<QBluetoothAddress> getPeripheriquesDistants();

  public slots:
    void envoyer(QString trame);

  private slots:
    void deconnecterAppareil();
    void connecterAppareil();
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
    void appareilConnecte(QString nom, QString adresse);
    void appareilDeconnecte(QString nom, QString adresse);
    void signalEcranSuivant();
    void signalConfiguration();
    void signalNomsJoueurs();
    void signalQuestion();
};

#endif // COMMUNICATIONBLUETOOTH_H
