QT       += core gui bluetooth

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

DEFINES += QT_DEPRECATED_WARNINGS
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    communicationbluetooth.cpp \
    joueur.cpp \
    question.cpp \
    quizzygui.cpp \
    quizzy.cpp \
    main.cpp

HEADERS += \
    communicationbluetooth.h \
    joueur.h \
    question.h \
    quizzy.h \
    quizzygui.h

COPIES += ressources

ressources.files = ressources
ressources.path = $$OUT_PWD/
ressources.base = $$PWD/

# Les defines pour la version release (sans debug)
#CONFIG(release, debug|release):DEFINES+=QT_NO_DEBUG_OUTPUT RASPBERRY_PI
# Les defines pour la version debug
CONFIG(debug, debug|release):DEFINES+=DEBUG_QuizzyGUI
