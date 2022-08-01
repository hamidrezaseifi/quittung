package de.seifi.rechnung_manager_module.models;

import javafx.print.Printer;

public class PrinterItem {

    private final Printer printer;


    public PrinterItem(Printer printer) {
        this.printer = printer;
    }

    @Override
    public String toString(){
        return this.printer.getName();
    }
}
