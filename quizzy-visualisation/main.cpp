#include "quizzy.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    quizzy w;
    w.show();
    return a.exec();
}
