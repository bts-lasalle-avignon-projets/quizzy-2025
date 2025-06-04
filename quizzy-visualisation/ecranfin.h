#ifndef ECRANFIN_H
#define ECRANFIN_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>

class QuizzyGUI;

class EcranFin : public QWidget
{
    Q_OBJECT

  public:
    explicit EcranFin(QuizzyGUI* parent = nullptr);
    ~EcranFin();

    void afficherScores(QString nomJoueur1,
                        QString nomJoueur2,
                        QString scoreJoueur1,
                        QString scoreJoueur2);
    void afficherGagnant(QString texte);

  private:
    QWidget*     ecranFin;
    QVBoxLayout* layoutEcranFin;
    QLabel*      messageFelicitation;
    QLabel*      messageGagnant;
    QLabel*      scoreGagnant;
    QLabel*      scorePerdant;
    QLabel*      messageFinDePartie;
};

#endif // ECRANFIN_H
