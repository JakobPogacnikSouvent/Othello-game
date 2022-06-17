package app;

import gui.Okno;
import gui.VodjaIgre;

public class App {
	
	public static void main(String[] args) {
		Okno okno = new Okno();
		okno.pack();
		okno.setVisible(true);
		VodjaIgre.okno = okno;
	
	}
}
