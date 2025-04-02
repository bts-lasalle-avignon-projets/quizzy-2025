#include "quizzygui.h"
#include <QApplication>

#include "quizzy.h"
#include "communicationbluetooth.h"
/**
 * @file main.cpp
 * @brief Programme principal
 * @details Crée et affiche la fenêtre principale de l'application Quizzy
 * @author Louis Raffin
 * @version 1.0
 *
 * @param argc
 * @param argv[]
 * @return int
 *
 */

int main(int argc, char* argv[])
{
    QApplication           a(argc, argv);
    QuizzyGUI              quizzyGUI;
    CommunicationBluetooth com;

    quizzyGUI.show();
    com.demarrerServeur();

    if(com.verifierLaConnexion())
    {
        quizzyGUI.afficherEcranAttente();
    }
    else
    {
        qDebug() << "Pas de périphérique Bluetooth connecté.";
    }

    return a.exec();
}
