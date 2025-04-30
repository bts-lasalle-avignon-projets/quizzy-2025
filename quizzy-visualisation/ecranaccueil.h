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
    QVBoxLayout* layoutEcranAccueil;
    QLabel*      titreEcranAccueil;
};

#endif // ECRANACCUEIL_H
