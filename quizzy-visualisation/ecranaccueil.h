#ifndef ECRANACCUEIL_H
#define ECRANACCUEIL_H

#include <QtWidgets>

class QuizzyGUI;

class EcranAccueil : public QWidget
{
    Q_OBJECT
  public:
    EcranAccueil(QuizzyGUI* parent = nullptr);
    ~EcranAccueil();

  private:
    QWidget*     ecranAccueil;
    QVBoxLayout* layoutVEcranAccueil;
    QHBoxLayout* layoutHEcranAccueil;
    QVBoxLayout* layoutNomsJoueurs;
    QLabel*      messageThemeChoisi;
    QLabel*      nomJoueur1;
    QLabel*      nomJoueur2;
    QLabel*      nombreDeQuestions;
    QLabel*      tempsParQuestion;
    QLabel*      messagePreparation;

  public slots:
    void afficherThemeChoisi(QString theme);
    void afficherNombreDeQuestions(QString nbQuestion);
    void afficherNomsJoueurs(QString nomJoueur1, QString nomJoueur2);
    void afficherTempsParQuestion(QString temps);

  signals:
    void tempsEcoule();
};

#endif // ECRANACCUEIL_H
