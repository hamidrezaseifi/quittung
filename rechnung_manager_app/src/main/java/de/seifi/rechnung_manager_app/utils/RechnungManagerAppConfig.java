package de.seifi.rechnung_manager_app.utils;

import de.seifi.rechnung_common.config.ConfigItem;
import de.seifi.rechnung_common.config.ConfigReader;
import de.seifi.rechnung_common.config.IConfigItem;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.PrinterItem;
import javafx.print.Printer;

import java.util.Arrays;
import java.util.List;

public class RechnungManagerAppConfig extends ConfigReader {

    private static final String CONFIG_KEY_GENERAL_SECTOR = "general";

	private static final String CONFIG_KEY_SELECTED_PRINTER = "selected_pribter";

	public static final String CONFIG_FILE_NAME = "rechnung_manager_config.cfg";


	private static final List<IConfigItem> configItemList = Arrays.asList(
			new ConfigItem(CONFIG_KEY_GENERAL_SECTOR, CONFIG_KEY_SELECTED_PRINTER, ""));


	public RechnungManagerAppConfig() {
		super(CONFIG_FILE_NAME, configItemList);
		
	}
	
	public String getSelectedPrinterName() {
		return getValue(CONFIG_KEY_SELECTED_PRINTER);
	}
	
	public void setSelectedPrinterName(String path) {
		setValue(CONFIG_KEY_SELECTED_PRINTER, path);
		
	}

	public Printer getSelectedPrinter(boolean getDefault){
		String selectedPrinter = RechnungManagerFxApp.appConfig.getSelectedPrinterName();
		if(selectedPrinter.isEmpty() && getDefault){
			selectedPrinter = Printer.getDefaultPrinter().getName();
		}
		for(Printer p: Printer.getAllPrinters()){

			if(p.getName().equals(selectedPrinter)){
				return p;
			}
		}

		return null;
	}

}
