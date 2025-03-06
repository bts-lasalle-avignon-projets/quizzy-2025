#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();
};

#endif // COMMUNICATIONBLUETOOTH_H
