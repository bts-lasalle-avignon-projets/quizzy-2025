#include "quizzy.h"
#include "communicationbluetooth.h"
#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth)
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
}

Quizzy::~Quizzy()
{
    delete communicationBluetooth;
    qDebug() << Q_FUNC_INFO << this;
}

CommunicationBluetooth* Quizzy::getCommunication()
{
    return communicationBluetooth;
}
