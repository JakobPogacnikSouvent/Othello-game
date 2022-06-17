package hefe;

import gui.Okno;
import logika.Igra;
import splosno.Poteza;

public class VodjaIgre {
	
	public static Okno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	
	public static void igramoNovoIgro() {
		igra = new Igra();
		igramo();
	}
	
	public static void igramo() {
		okno.osveziGUI();
		byte p = igra.getWinner();
		if (p == 0) {
			clovekNaVrsti = true;
		}
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo();
	}
}
