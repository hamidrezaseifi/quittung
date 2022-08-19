// quittung.cpp : Diese Datei enthält die Funktion "main". Hier beginnt und endet die Ausführung des Programms.
//

#include <stdio.h>
#include <stdlib.h>
#include <Windows.h>
#include <iostream>
#include <string.h>
#include <fstream>
#include <sstream>
#
using namespace std;

static inline void ltrim(std::string& s) {
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), [](unsigned char ch) {
        return !std::isspace(ch);
    }));
}

// trim from end (in place)
static inline void rtrim(std::string& s) {
    s.erase(std::find_if(s.rbegin(), s.rend(), [](unsigned char ch) {
        return !std::isspace(ch);
    }).base(), s.end());
}

// trim from both ends (in place)
static inline void trim(std::string& s) {
    ltrim(s);
    rtrim(s);
}


int main(int argc, char* argv[])
{
    //ShowWindow(::GetConsoleWindow(), SW_HIDE);
    //ShowWindow(::GetConsoleWindow(), SW_NORMAL);

    if (argc <= 1) {
        cout << "No argument!!!!\n";
        return 0;
    }

    bool hideWindows = true;
    if (argc > 2) {
        string hideWindowsCommand = argv[2];
        hideWindows = (hideWindowsCommand == "hide");
    }

    string commandFolderPath = argv[1];
    string commandFilePath = commandFolderPath + "\\command.cfg";

    ifstream infile(commandFilePath); //"E:\\Hamid\\Projects\\java\\quittung\\command.txt"
    string line;

    cout << "======================================================== Start ======================================================= \n";

    string command = ".\\java\\bin\\java.exe ";

    while (getline(infile, line))
    {
        trim(line);

        int delimIndex = line.find("=");
        if (delimIndex < 1) {
            continue;
        }

        string key = line.substr(0, delimIndex);
        string value = line.substr(delimIndex + 1, line.size());

        trim(key);
        trim(value);

        cout << key << "  --> " << value << "\n";

        cout << " ---------------------- " << "\n";

        if (key != "main_class" && key != "") {
            command += "-" + key + " ";
        }

        if (value != "") {
            command +=  "\"" + value + "\" ";
        }

    }

    cout << "======================================================== End ======================================================= \n";

    if (hideWindows) {
        ShowWindow(::GetConsoleWindow(), SW_HIDE);
    }
    

    cout << command << "\n";

    system(command.c_str());


    return 0;

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

