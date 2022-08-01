package de.seifi.rechnung_common.utils;

public class GerldCalculator {
	
	public static float nettoToBrutto(float netto) {
		float brutto = (netto * 119) / 100;
		return brutto;
	}
	
	public static float bruttoToNetto(float brutto) {
		float netto = (brutto * 100) / 119;
		return netto;
	}

}
