#ifndef ECRANREPONSE_H
#define ECRANREPONSE_H

#include <QtWidgets>

class QuizzyGUI;

class EcranReponse : public QWidget
{
    Q_OBJECT
  public:
    EcranReponse(QuizzyGUI* parent = nullptr);
    ~EcranReponse();

  private:
    QWidget* ecranReponse;
    /*QVBoxLayout* layoutEcranReponse;
    QHBoxLayout* layoutTitre;
    QHBoxLayout* layoutThemeNbQuestion;
    QLabel*      titreQuestion;
    QLabel*      theme;
    QLabel*      nombreDeQuestion;
    QLabel*      messageReponse;
    QVBoxLayout* layoutPropositions;
    QHBoxLayout* layoutPropositionsAB;
    QHBoxLayout* layoutPropositionsCD;
    QLabel*      propositionA;
    QLabel*      propositionB;
    QLabel*      propositionC;
    QLabel*      propositionD;
    QHBoxLayout* layoutTimer;
    QLabel*      explication;
    QLabel*      tempsRestant;
    QTimer*      timer;*/

    int tempsMax;
    int tempsActuel;
};

#endif // ECRANREPONSE_H
