#include "ecranfin.h"
#include "quizzygui.h"
#include <QDebug>

EcranFin::EcranFin(QuizzyGUI* parent) :
    QWidget(parent), ecranFin(new QWidget(this)),
    layoutEcranFin(new QVBoxLayout(ecranFin)), titreEcranFin(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    titreEcranFin->setAlignment(Qt::AlignCenter);
    titreEcranFin->setText("EcranFin");
    layoutEcranFin->addWidget(titreEcranFin);
    parent->getEcrans()->addWidget(ecranFin);
}

EcranFin::~EcranFin()
{
    qDebug() << Q_FUNC_INFO << this;
}
