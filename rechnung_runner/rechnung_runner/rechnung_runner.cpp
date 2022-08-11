// quittung.cpp : Diese Datei enthält die Funktion "main". Hier beginnt und endet die Ausführung des Programms.
//

#include <stdio.h>
#include <stdlib.h>
#include <Windows.h>
#include <iostream>
#include <string.h>

using namespace std;

int main(int argc, char* argv[])
{
    ShowWindow(::GetConsoleWindow(), SW_HIDE);
    //ShowWindow(::GetConsoleWindow(), SW_NORMAL);

    if (argc > 1) {
        
        string arg1 = argv[1];
        if (arg1 == "rechnung") {
            system(".\\java\\bin\\java.exe -cp \"rechnung_manager_app-0.0.1.jar;lib\\*\" --module-path java/fx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics de.seifi.rechnung_manager_app.RechnungManagerFxApp");
        }
        if (arg1 == "data_manager") {
            system(".\\java\\bin\\java.exe -cp \"data_manager-0.0.1.jar;lib\\*\" --module-path java/fx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics de.seifi.data_manager.DataManagerFxApp");
        }
        //".\\java\\bin\\java.exe -cp rechnung_manager_app-0.0.1-jar-with-dependencies.jar --module-path java/fx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics de.seifi.rechnung_manager_app.RechnungManagerFxApp"

        //cout << arg1 << "\n";
    }
    else {
        cout << "No argument\n";
    }

    return 0;
}

// Programm ausführen: STRG+F5 oder Menüeintrag "Debuggen" > "Starten ohne Debuggen starten"
// Programm debuggen: F5 oder "Debuggen" > Menü "Debuggen starten"

// Tipps für den Einstieg: 
//   1. Verwenden Sie das Projektmappen-Explorer-Fenster zum Hinzufügen/Verwalten von Dateien.
//   2. Verwenden Sie das Team Explorer-Fenster zum Herstellen einer Verbindung mit der Quellcodeverwaltung.
//   3. Verwenden Sie das Ausgabefenster, um die Buildausgabe und andere Nachrichten anzuzeigen.
//   4. Verwenden Sie das Fenster "Fehlerliste", um Fehler anzuzeigen.
//   5. Wechseln Sie zu "Projekt" > "Neues Element hinzufügen", um neue Codedateien zu erstellen, bzw. zu "Projekt" > "Vorhandenes Element hinzufügen", um dem Projekt vorhandene Codedateien hinzuzufügen.
//   6. Um dieses Projekt später erneut zu öffnen, wechseln Sie zu "Datei" > "Öffnen" > "Projekt", und wählen Sie die SLN-Datei aus.
