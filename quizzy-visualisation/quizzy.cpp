#include "quizzy.h"
#include "communicationbluetooth.h"
#include <QDebug>

Quizzy::Quizzy(QObject*                parent,
               CommunicationBluetooth* communicationBluetooth) :
    QObject(parent),
    communicationBluetooth(communicationBluetooth)
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
}

Quizzy::~Quizzy()
{
    qDebug() << Q_FUNC_INFO << this;
}
