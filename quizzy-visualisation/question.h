#ifndef QUESTION_H
#define QUESTION_H

#include <QString>

class Question
{
  public:
    void    setTheme(const QString& theme);
    QString getTheme() const;

    void    setTitre(QString& titre);
    QString getTitre();

    void    setPropA(QString& propositionA);
    QString getPropA();

    void    setPropB(QString& propositionB);
    QString getPropB();

    void    setPropC(QString& propositionC);
    QString getPropC();

    void    setPropD(QString& propositionD);
    QString getPropD();

    void    setExplication(QString& explication);
    QString getExplication();

    void setIdReponse(int idReponse);
    int  getIdReponse();

    void setTemps(int secondes);
    int  getTemps() const;

    void setNombre(int nb);
    int  getNombre() const;

  private:
    QString theme;
    QString titre;
    QString propositionA;
    QString propositionB;
    QString propositionC;
    QString propositionD;
    QString explication;

    int idReponse;
    int tempsParQuestion  = 0;
    int nombreDeQuestions = 0;
};

#endif // QUESTION_H
