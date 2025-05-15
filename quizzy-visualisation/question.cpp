#include "question.h"

void Question::setTheme(const QString& theme)
{
    this->theme = theme;
}

QString Question::getTheme() const
{
    return theme;
}

void Question::setTemps(int secondes)
{
    tempsParQuestion = secondes;
}

int Question::getTemps() const
{
    return tempsParQuestion;
}

void Question::setNombre(int nb)
{
    nombreDeQuestions = nb;
}

int Question::getNombre() const
{
    return nombreDeQuestions;
}
