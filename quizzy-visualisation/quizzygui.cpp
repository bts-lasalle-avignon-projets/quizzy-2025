/**
 * @file quizzygui.cpp
 *
 * @brief Définition de la classe QuizzyGUI
 * @author Louis Raffin
 * @version 1.0
 */

#include "quizzygui.h"
#include "quizzy.h"
#include <QDebug>

/**
 * @brief Constructeur de la classe QuizzyGUI
 *
 * @fn QuizzyGUI::QuizzyGUI
 * @param parent L'adresse de l'objet parent, si nullptr QuizzyGUI sera la
 * fenêtre principale de l'application
 */
QuizzyGUI::QuizzyGUI(QWidget* parent) :
    QMainWindow(parent), quizzy(new Quizzy(this))
{
    qDebug() << Q_FUNC_INFO << this;

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

#ifdef RASPBERRY_PI
    showFullScreen();
#endif
}

QuizzyGUI::~QuizzyGUI()
{
    delete quizzy;
    qDebug() << Q_FUNC_INFO << this;
}
