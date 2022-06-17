package tests;

import gui.Okno;
import hefe.VodjaIgre;

public class GuiTest {
	
	public static void main(String[] args) {
		Okno okno = new Okno();
		okno.pack();
		okno.setVisible(true);
		VodjaIgre.okno = okno;
	
	}
}
