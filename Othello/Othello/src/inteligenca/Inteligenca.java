package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra{
	
	protected AI carlos;
	
	
	public Inteligenca() {
		super("Jakob&Peter");
		// TODO Auto-generated constructor stub
		
		carlos = new AI(Tree.loadTree("Tree.txt"));
	}
	
	public Inteligenca(String ime) {
		super(ime);
		// TODO Auto-generated constructor stub
		
		carlos = new AI(Tree.loadTree("Tree.txt"));
	}
	
	public Poteza izberiPotezo(Igra i) {
		return carlos.MCTSFindPoteza(i);
	}

}
