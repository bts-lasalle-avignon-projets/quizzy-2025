#include "ecranquestion.h"
#include "quizzygui.h"
#include <QDebug>

EcranQuestion::EcranQuestion(QuizzyGUI* parent) :
    QWidget(parent), ecranQuestion(new QWidget(this)),
    layoutEcranQuestion(new QVBoxLayout(ecranQuestion)),
    layoutTitre(new QHBoxLayout()), layoutThemeNbQuestion(new QHBoxLayout()),
    titreQuestion(new QLabel(this)), theme(new QLabel(this)),
    nombreDeQuestion(new QLabel(this)), messageChoix(new QLabel(this)),
    layoutPropositions(new QVBoxLayout()),
    layoutPropositionsAB(new QHBoxLayout()),
    layoutPropositionsCD(new QHBoxLayout()), propositionA(new QLabel(this)),
    propositionB(new QLabel(this)), propositionC(new QLabel(this)),
    propositionD(new QLabel(this)), layoutTimer(new QHBoxLayout()),
    barreDeProgression(new QProgressBar(this)), tempsRestant(new QLabel(this)),
    timer(new QTimer(this))
{
    {
        qDebug() << Q_FUNC_INFO << this << "parent" << parent;

        titreQuestion->setText("Titre de la question");
        theme->setText("Thème");
        nombreDeQuestion->setText("Question x/y");
        messageChoix->setText("Faites votre choix !");
        propositionA->setText("A - Proposition A");
        propositionB->setText("B - Proposition B");
        propositionC->setText("C - Proposition C");
        propositionD->setText("D - Proposition D");
        tempsRestant->setText(QString::number(tempsActuel) + " s");

        titreQuestion->setAlignment(Qt::AlignLeft);
        theme->setAlignment(Qt::AlignRight);
        nombreDeQuestion->setAlignment(Qt::AlignRight);
        messageChoix->setAlignment(Qt::AlignCenter);
        tempsRestant->setAlignment(Qt::AlignRight);
        layoutPropositions->setAlignment(Qt::AlignCenter);
        propositionA->setAlignment(Qt::AlignLeft);
        propositionB->setAlignment(Qt::AlignRight);
        propositionC->setAlignment(Qt::AlignLeft);
        propositionD->setAlignment(Qt::AlignRight);

        layoutEcranQuestion->addLayout(layoutTitre);
        layoutTitre->addWidget(titreQuestion);
        layoutTitre->addLayout(layoutThemeNbQuestion);
        layoutThemeNbQuestion->addWidget(theme);
        layoutThemeNbQuestion->addWidget(nombreDeQuestion);

        layoutEcranQuestion->addWidget(messageChoix);

        layoutEcranQuestion->addLayout(layoutPropositions);
        layoutPropositions->addLayout(layoutPropositionsAB);
        layoutPropositionsAB->addWidget(propositionA);
        layoutPropositionsAB->addWidget(propositionB);
        layoutPropositions->addLayout(layoutPropositionsCD);
        layoutPropositionsCD->addWidget(propositionC);
        layoutPropositionsCD->addWidget(propositionD);

        layoutEcranQuestion->addLayout(layoutTimer);
        layoutTimer->addWidget(barreDeProgression);
        layoutTimer->addWidget(tempsRestant);

        parent->getEcrans()->addWidget(ecranQuestion);

        connect(timer,
                &QTimer::timeout,
                this,
                &EcranQuestion::mettreAJourCompteARebours);
        timer->start(1000);
    }
}

void EcranQuestion::mettreAJourCompteARebours()
{
    if(tempsActuel > 0)
    {
        tempsActuel--;
        barreDeProgression->setValue((tempsActuel * 100) / tempsMax);
        tempsRestant->setText(QString::number(tempsActuel) + " s");
    }
    else
    {
        timer->stop();
        tempsRestant->setText("Temps écoulé !");
    }
}

void EcranQuestion::afficherTitreQuestion(QString titre)
{
    titreQuestion->setText(titre);
}

void EcranQuestion::afficherPropositions(QString propA,
                                         QString propB,
                                         QString propC,
                                         QString propD)
{
    propositionA->setText(propA);
    propositionB->setText(propB);
    propositionC->setText(propC);
    propositionD->setText(propD);
}

EcranQuestion::~EcranQuestion()
{
    qDebug() << Q_FUNC_INFO << this;
}
