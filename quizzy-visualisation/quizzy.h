#ifndef QUIZZY_H
#define QUIZZY_H

#include <QObject>

class CommunicationBluetooth;

class Quizzy : public QObject
{
    Q_OBJECT

  private:
    CommunicationBluetooth*
      communicationBluetooth; //!< association vers CommunicationBluetooth

  public:
    Quizzy(QObject* parent = nullptr);
    virtual ~Quizzy();
};

#endif // QUIZZY_H
