#ifndef QUIZZY_H
#define QUIZZY_H

#include <QMainWindow>

class quizzy : public QMainWindow
{
    Q_OBJECT

public:
    quizzy(QWidget *parent = nullptr);
    ~quizzy();
};
#endif // QUIZZY_H
