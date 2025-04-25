#include "quizzy.h"
#include "communicationbluetooth.h"
#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    communicationBluetooth(new CommunicationBluetooth(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
}

Quizzy::~Quizzy()
{
    delete communicationBluetooth;
    qDebug() << Q_FUNC_INFO << this;
}
