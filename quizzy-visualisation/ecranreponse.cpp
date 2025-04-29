#include "ecranreponse.h"
#include "quizzygui.h"
#include <QDebug>

EcranReponse::EcranReponse(QuizzyGUI* parent) :
    QWidget(parent), ecranReponse(new QWidget(this)),
    layoutEcranReponse(new QVBoxLayout(ecranReponse)),
    titreEcranReponse(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    titreEcranReponse->setAlignment(Qt::AlignCenter);
    titreEcranReponse->setText("EcranReponse");
    layoutEcranReponse->addWidget(titreEcranReponse);
    parent->getEcrans()->addWidget(ecranReponse);
}

EcranReponse::~EcranReponse()
{
    qDebug() << Q_FUNC_INFO << this;
}
