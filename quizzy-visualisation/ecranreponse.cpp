#include "ecranreponse.h"
#include "quizzygui.h"
#include <QDebug>

EcranReponse::EcranReponse(QuizzyGUI* parent) :
    QWidget(parent), ecranReponse(new QWidget(this)),
    layoutEcranReponse(new QVBoxLayout(ecranReponse)),
    layoutTitre(new QHBoxLayout()), layoutThemeNbQuestion(new QHBoxLayout()),
    titreQuestion(new QLabel(this)), theme(new QLabel(this)),
    nombreDeQuestion(new QLabel(this)), messageReponse(new QLabel(this)),
    layoutPropositions(new QVBoxLayout()),
    layoutPropositionsAB(new QHBoxLayout()),
    layoutPropositionsCD(new QHBoxLayout()), propositionA(new QLabel(this)),
    propositionB(new QLabel(this)), propositionC(new QLabel(this)),
    propositionD(new QLabel(this)), layoutTimer(new QHBoxLayout()),
    explication(new QLabel(this)), tempsRestant(new QLabel(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    ecranReponse->setObjectName("ecranReponse");

    titreQuestion->setText("Titre de la question");
    theme->setText("Thème");
    nombreDeQuestion->setText("Question x/y");
    messageReponse->setText("Voici la réponse !");
    propositionA->setText("A - Proposition A");
    propositionB->setText("B - Proposition B");
    propositionC->setText("C - Proposition C");
    propositionD->setText("D - Proposition D");
    explication->setText("Explication de la bonne réponse.");

    titreQuestion->setWordWrap(true);
    titreQuestion->setMaximumWidth(1200);
    titreQuestion->setAlignment(Qt::AlignCenter);
    theme->setAlignment(Qt::AlignCenter);
    nombreDeQuestion->setAlignment(Qt::AlignCenter);
    messageReponse->setAlignment(Qt::AlignCenter);
    explication->setWordWrap(true);
    explication->setMaximumWidth(1800);
    explication->setAlignment(Qt::AlignCenter);
    tempsRestant->setAlignment(Qt::AlignRight);
    layoutPropositions->setAlignment(Qt::AlignCenter);
    propositionA->setAlignment(Qt::AlignCenter);
    propositionB->setAlignment(Qt::AlignCenter);
    propositionC->setAlignment(Qt::AlignCenter);
    propositionD->setAlignment(Qt::AlignCenter);

    layoutEcranReponse->addLayout(layoutTitre);
    layoutTitre->addWidget(titreQuestion);
    layoutTitre->addLayout(layoutThemeNbQuestion);
    layoutThemeNbQuestion->addWidget(theme);
    layoutThemeNbQuestion->addWidget(nombreDeQuestion);

    layoutEcranReponse->addWidget(messageReponse);

    layoutEcranReponse->addLayout(layoutPropositions);
    layoutPropositions->addLayout(layoutPropositionsAB);
    layoutPropositionsAB->addWidget(propositionA);
    layoutPropositionsAB->addWidget(propositionB);
    layoutPropositions->addLayout(layoutPropositionsCD);
    layoutPropositionsCD->addWidget(propositionC);
    layoutPropositionsCD->addWidget(propositionD);

    layoutEcranReponse->addWidget(explication);

    layoutEcranReponse->addLayout(layoutTimer);
    layoutTimer->addStretch();
    layoutTimer->addWidget(tempsRestant);

    parent->getEcrans()->addWidget(ecranReponse);
}

void EcranReponse::afficherTitreQuestion(QString titre)
{
    titreQuestion->setText(titre);
}

void EcranReponse::afficherThemeQuestion(QString themeQuestions)
{
    theme->setText(themeQuestions);
}

void EcranReponse::afficherNbQuestions(QString nbQuestions,
                                       int     indexQuestionActuelle)
{
    QString texte =
      QString::number(indexQuestionActuelle) + " / " + nbQuestions;
    nombreDeQuestion->setText(texte);
}

void EcranReponse::afficherPropositions(QString propA,
                                        QString propB,
                                        QString propC,
                                        QString propD)
{
    propositionA->setText(propA);
    propositionB->setText(propB);
    propositionC->setText(propC);
    propositionD->setText(propD);
}

void EcranReponse::afficherPropositionCorrecte(int idReponse)
{
    QVector<QLabel*> propositions = { propositionA,
                                      propositionB,
                                      propositionC,
                                      propositionD };

    for(int i = 0; i < 4; ++i)
    {
        if(i == idReponse - 1)
        {
            propositions[i]->setObjectName("propositionCorrecte");
        }
        else
        {
            propositions[i]->setObjectName("propositionIncorrecte");
        }
        propositions[i]->style()->unpolish(propositions[i]);
        propositions[i]->style()->polish(propositions[i]);
        propositions[i]->update();
    }
}

void EcranReponse::afficherTempsRestant(int temps)
{
    tempsRestant->setText(QString::number(temps));
}

void EcranReponse::afficherExplication(QString texte)
{
    explication->setText(texte);
}

EcranReponse::~EcranReponse()
{
    qDebug() << Q_FUNC_INFO << this;
}
