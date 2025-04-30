#include "ecranquestion.h"
#include "quizzygui.h"
#include <QDebug>

EcranQuestion::EcranQuestion(QuizzyGUI* parent) :
    QWidget(parent), ecranQuestion(new QWidget(this)),
    layoutEcranQuestion(new QVBoxLayout(ecranQuestion)),
    titreEcranQuestion(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    titreEcranQuestion->setAlignment(Qt::AlignCenter);
    titreEcranQuestion->setText("EcranQuestion");
    layoutEcranQuestion->addWidget(titreEcranQuestion);
    parent->getEcrans()->addWidget(ecranQuestion);
}

EcranQuestion::~EcranQuestion()
{
    qDebug() << Q_FUNC_INFO << this;
}
