#include "ecranreponse.h"
#include "quizzygui.h"
#include <QDebug>

EcranReponse::EcranReponse(QuizzyGUI* parent) :
    QWidget(parent), ecranReponse(new QWidget(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    parent->getEcrans()->addWidget(ecranReponse);
}

EcranReponse::~EcranReponse()
{
    qDebug() << Q_FUNC_INFO << this;
}
