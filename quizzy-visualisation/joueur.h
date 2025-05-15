#ifndef JOUEUR_H
#define JOUEUR_H

#include <QString>

class Joueur
{
  public:
    Joueur(const QString& nom = "");

    void    setNom(const QString& nom);
    QString getNom() const;

    void setScore(int score);
    int  getScore() const;

  private:
    QString nom;
    int     score;
};

#endif // JOUEUR_H

