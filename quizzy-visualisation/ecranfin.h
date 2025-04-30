#ifndef ECRANFIN_H
#define ECRANFIN_H

#include <QtWidgets>

class QuizzyGUI;

class EcranFin : public QWidget
{
    Q_OBJECT
  public:
    EcranFin(QuizzyGUI* parent = nullptr);
    ~EcranFin();

  private:
    QWidget*     ecranFin;
    QVBoxLayout* layoutEcranFin;
    QLabel*      titreEcranFin;
    QLabel*      messageEcranFin;
};

#endif // ECRANFIN_H
