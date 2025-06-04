#ifndef ECRANQUESTION_H
#define ECRANQUESTION_H

#include <QtWidgets>

class QuizzyGUI;

class EcranQuestion : public QWidget
{
    Q_OBJECT
  public:
    EcranQuestion(QuizzyGUI* parent = nullptr);
    ~EcranQuestion();

  private:
    QWidget*      ecranQuestion;
    QVBoxLayout*  layoutEcranQuestion;
    QHBoxLayout*  layoutTitre;
    QHBoxLayout*  layoutThemeNbQuestion;
    QLabel*       titreQuestion;
    QLabel*       theme;
    QLabel*       nombreDeQuestion;
    QLabel*       messageChoix;
    QVBoxLayout*  layoutPropositions;
    QHBoxLayout*  layoutPropositionsAB;
    QHBoxLayout*  layoutPropositionsCD;
    QLabel*       propositionA;
    QLabel*       propositionB;
    QLabel*       propositionC;
    QLabel*       propositionD;
    QHBoxLayout*  layoutTimer;
    QProgressBar* barreDeProgression;
    QLabel*       tempsRestant;
    QTimer*       timer;

    int tempsMax;
    int tempsActuel = 0;

  public slots:
    void afficherTitreQuestion(QString titre);
    void afficherThemeQuestion(QString theme);
    void afficherNbQuestions(QString nbQuestions, int indexQuestionActuelle);
    void afficherPropositions(QString propA,
                              QString propB,
                              QString propC,
                              QString propD);
    void afficherTempsRestant(int temps);
    void mettreAJourCompteARebours();
    void demarrerCompteARebours(int tempsDepart);
};

#endif // ECRANQUESTION_H
