#ifndef QUESTION_H
#define QUESTION_H

#include <QString>

class Question
{
  public:
    void    setTheme(const QString& theme);
    QString getTheme() const;

    void setTemps(int secondes);
    int  getTemps() const;

    void setNombre(int nb);
    int  getNombre() const;

  private:
    QString theme;
    int     tempsParQuestion  = 0;
    int     nombreDeQuestions = 0;
};

#endif // QUESTION_H

