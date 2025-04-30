#ifndef ECRANQUESTION_H
#define ECRANQUESTION_H

#include <QtWidgets>

class QuizzyGUI;

class EcranQuestion : public QWidget
{
    Q_OBJECT
  public:
    EcranQuestion(QuizzyGUI* parent = nullptr);
    ~EcranQuestion();

  private:
    QWidget*     ecranQuestion;
    QVBoxLayout* layoutEcranQuestion;
    QLabel*      titreEcranQuestion;
};

#endif // ECRANQUESTION_H
