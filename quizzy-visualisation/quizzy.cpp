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
    QStringList nomsJoueurs;

    switch(typeDeTrame.unicode())
    {
            // TODO
        case 'J':
            // joueursConfigures;
            break;
        case 'S':
            // suivant;
            break;
        case 'T':
            // teminerLaSession;
            break;
        case 'F':
            // finirLeQuiz;
            break;
        case 'C':
            // TODO
            emit sessionParametree(trameTraitee[THEME],
                                   trameTraitee[TEMPS],
                                   trameTraitee[NOMBRE_DE_QUESTION],
                                   nomsJoueurs);
            break;
        case 'Q':
            emit questionRecue(trameTraitee[TITRE_QUESTION],
                               trameTraitee[NUMERO_REPONSE],
                               trameTraitee[PROPOSITION_A],
                               trameTraitee[PROPOSITION_B],
                               trameTraitee[PROPOSITION_C],
                               trameTraitee[PROPOSITION_D]);
            break;
        case 'R':
            emit reponseJoueurRecue(trameTraitee[NUMERO_DU_JOUEUR],
                                    trameTraitee[NUMERO_REPONSE_JOUEUR]);
            break;
        default:
            qDebug() << Q_FUNC_INFO << "typeDeTrame inconnu"
                     << typeDeTrame.unicode();
    }
}
