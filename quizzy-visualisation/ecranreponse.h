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
    QWidget*     ecranReponse;
    QVBoxLayout* layoutEcranReponse;
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

    int tempsMax;
    int tempsActuel;

  public slots:
    void afficherTitreQuestion(QString titre);
    void afficherThemeQuestion(QString themeQuestions);
    void afficherNbQuestions(QString nbQuestions, int indexQuestionActuelle);
    void afficherPropositions(QString propA,
                              QString propB,
                              QString propC,
                              QString propD);
    void afficherTempsRestant(int temps);
    void afficherExplication(QString texte);
    void afficherPropositionCorrecte(int idReponse);
};

#endif // ECRANREPONSE_H
