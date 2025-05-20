#include "joueur.h"

Joueur::Joueur(const QString& nom) : nom(nom), score(0)
{
}

void Joueur::setNom(const QString& nom)
{
    this->nom = nom;
}

QString Joueur::getNom() const
{
    return nom;
}

void Joueur::setScore(int score)
{
    this->score = score;
}

int Joueur::getScore()
{
    return score;
}
