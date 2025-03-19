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

QVector<QString> Quizzy::traiterTrame(QString trameRecue)
{
    // TODO
    QVector<QString> trameTraitee;
    return trameTraitee;
}

void Quizzy::lireTrame(QVector<QString> trameTraitee)
{
    // TODO differencier l'envoie selon la trame recue
    QChar typeDeTrame;
    typeDeTrame = trameTraitee[TYPE_DE_TRAME].at(0);

    switch(typeDeTrame.unicode())
    {
            // TODO
        case 'J':
            // emit joueursConfigures;
        case 'S':
            // suivant;
        case 'T':
            // teminerLaSession;
        case 'F':
            // finirLeQuiz;

        case 'C':
            emit sessionParametree(trameTraitee[THEME],
                                   trameTraitee[TEMPS],
                                   trameTraitee[NOMBRE_DE_QUESTION],
                                   trameTraitee[NOM_JOUEUR_1],
                                   trameTraitee[NOM_JOUEUR_2]);
        case 'Q':
            emit questionRecue(trameTraitee[TITRE_QUESTION],
                               trameTraitee[NUMERO_REPONSE],
                               trameTraitee[PROPOSITION_A],
                               trameTraitee[PROPOSITION_B],
                               trameTraitee[PROPOSITION_C],
                               trameTraitee[PROPOSITION_D]);
        case 'R':
            emit reponseJoueurRecue(trameTraitee[NUMERO_DU_JOUEUR],
                                    trameTraitee[NUMERO_REPONSE_JOUEUR]);
    }
}
