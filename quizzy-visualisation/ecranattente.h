#ifndef ECRANATTENTE_H
#define ECRANATTENTE_H

#include <QtWidgets>

class QuizzyGUI;

class EcranAttente : public QWidget
{
    Q_OBJECT
  public:
    EcranAttente(QuizzyGUI* parent = nullptr);
    ~EcranAttente();

  private:
    QWidget*     ecranAttente;
    QVBoxLayout* layoutEcranAttente;
    QLabel*      titreEcranAttente;
    QLabel*      messageConnexion;

  public slots:
    void afficherMessageConnexion(QString message);
};

#endif // ECRANATTENTE_H
