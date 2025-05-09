# Simulateur QUIZZY 2025

## Présentation du protocole implanté dans le simulateur ESP'ACE

Ce document présente rapidement le fonctionnement du simulateur ainsi que le protocole implémenté.

> Le protocole complet est disponible dans Google Drive.

## Configuration du simulateur

Valeur par défaut :

```cpp
#define NB_BUZZERS 4
```

Pour le numéro de pupitre : voir platformio.ini (la constante `NUMERO_PUPITRE`)

> Pour répondre aux qustions, on utilise l'encodeur rotatif.

## Identification des périphériques

Nom des périphériques Bluetooth :

- \"quizzy-ecran\" pour la RPI et,
- \"quizzy-pn\" où \"n\" est le numéro de pupitre (par exemple `quizzy-p1` pour le pupitre 1)

## platform.ini

```ini
; PlatformIO Project Configuration File

[env]
platform = espressif32
;board = esp32dev
board = lolin32
framework = arduino
;platform_packages = platformio/framework-arduinoespressif32@^3.20006.0
lib_deps =
  thingpulse/ESP8266 and ESP32 OLED driver for SSD1306 displays @ ^4.2.0
upload_speed = 115200
monitor_speed = 115200

[env:esp32_pupitre_1]
build_flags = -D$PIOENV -DNUMERO_PUPITRE=1 -DDEBUG
upload_port = /dev/ttyUSB0
monitor_port = /dev/ttyUSB0

[env:esp32_pupitre_2]
build_flags = -D$PIOENV -DNUMERO_PUPITRE=2 -DDEBUG
upload_port = /dev/ttyUSB1
monitor_port = /dev/ttyUSB1
```

## Protocole

- Auteurs : Louis RAFFIN et Lenny GASSE
- Version : 0.1
- Trame ASCII
- Délimiteur de début : **$**
- Délimiteur de fin : **\\n**
- Séparateur de champs : **;**

Les messages échangés entre l'application mobile de gestion et les pupitres :

| Sens de communication |    Rôle de la trame    |        Format de la trame         |
| :-------------------: | :--------------------: | :-------------------------------: |
|  Gestion -\> Pupitre  | Configuration Question |   $C;temps;nombreDeQuestions\\n   |
| Gestion \-\> Pupitre  |     Finir le quiz      |               $F\\n               |
|  Gestion -\> Pupitre  |   Passer à la suite    |               $S\\n               |
| Pupitre \-\> Gestion  |     Appuis boutons     | $A;couleurBouton;numeroPupitre\\n |

Remarques :

- Les temps sont exprimés en seconde
- le champ couleurBouton peut prendre les valeurs suivantes : R|J|B|V pour Rouge|Jaune|Bleu|Vert

## Auteur

- Thierry Vaira <<tvaira@free.fr>>
