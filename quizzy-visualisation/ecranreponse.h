#ifndef ECRANREPONSE_H
#define ECRANREPONSE_H

#include <QtWidgets>

class QuizzyGUI;

class EcranReponse : public QWidget
{
    Q_OBJECT
  public:
    EcranReponse(QuizzyGUI* parent = nullptr);
    ~EcranReponse();

  private:
    QWidget*     ecranReponse;
    QVBoxLayout* layoutEcranReponse;
    QLabel*      titreEcranReponse;
};

#endif // ECRANREPONSE_H
