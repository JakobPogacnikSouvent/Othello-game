package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra{
	/*
	 * Neinteligentna inteligenca
	 */
	
	protected AI carlos;
	
	/* 
	 **************************************************************************	
	 *                         CONSTRUCTORS                                   *
	 **************************************************************************  
	 */
	
	public Inteligenca() {
		super("Jakob&Peter");
		// TODO Auto-generated constructor stub

		carlos = new AI(Igra.initialBoard(), 5000);
	}
	
	public Inteligenca(int thinkingTimeMS) {
		super("Jakob&Peter");
		
		carlos = new AI(Igra.initialBoard(), thinkingTimeMS);
	}
	
	public Inteligenca(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
		
		carlos = new AI(Igra.initialBoard(), 5000);
	}
	
	public Inteligenca(String ime, int thinkingTimeMS) {
		super(ime);
		
		carlos = new AI(Igra.initialBoard(), thinkingTimeMS);
	}
	
	/* 
	 **************************************************************************	
	 *                            PUBLIC METHODS                              *
	 **************************************************************************  
	 */
	
	public Poteza izberiPotezo(Igra i) {
		return carlos.MCTSFindPoteza(i);
	}
	
	public Poteza izberiNakljucnoPotezo(Igra i) {
		return carlos.randomFindPoteza(i);
	}

}
