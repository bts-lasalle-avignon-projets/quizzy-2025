#include "question.h"

void Question::setTheme(const QString& theme)
{
    this->theme = theme;
}

QString Question::getTheme() const
{
    return theme;
}

void Question::setTitre(QString& titre)
{
    this->titre = titre;
}

QString Question::getTitre()
{
    return titre;
}

void Question::setPropA(QString& propositionA)
{
    this->propositionA = propositionA;
}

QString Question::getPropA()
{
    return propositionA;
}

void Question::setPropB(QString& propositionB)
{
    this->propositionB = propositionB;
}

QString Question::getPropB()
{
    return propositionB;
}

void Question::setPropC(QString& propositionC)
{
    this->propositionC = propositionC;
}

QString Question::getPropC()
{
    return propositionC;
}

void Question::setPropD(QString& propositionD)
{
    this->propositionD = propositionD;
}

QString Question::getPropD()
{
    return propositionD;
}

void Question::setExplication(QString& explication)
{
    this->explication = explication;
}

QString Question::getExplication()
{
    return explication;
}

void Question::setIdReponse(int idReponse)
{
    this->idReponse = idReponse;
}

int Question::getIdReponse()
{
    return idReponse;
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
