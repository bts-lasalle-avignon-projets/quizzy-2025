#include "ecranaccueil.h"
#include "quizzygui.h"
#include <QDebug>

EcranAccueil::EcranAccueil(QuizzyGUI* parent) :
    QWidget(parent), ecranAccueil(new QWidget(this)),
    layoutEcranAccueil(new QVBoxLayout(ecranAccueil)),
    titreEcranAccueil(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    titreEcranAccueil->setAlignment(Qt::AlignCenter);
    titreEcranAccueil->setText("EcranAccueil");
    layoutEcranAccueil->addWidget(titreEcranAccueil);
    parent->getEcrans()->addWidget(ecranAccueil);
}

EcranAccueil::~EcranAccueil()
{
    qDebug() << Q_FUNC_INFO << this;
}
