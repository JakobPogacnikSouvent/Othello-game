package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import splosno.Poteza;

public class AI {
	
	Tree searchTree;
	Tree root;
	
	
	public AI(byte[][] zacetnoStanje) {
		searchTree = new Tree(zacetnoStanje);
		root = searchTree;
	}
	
	public AI(Tree t) {
		searchTree = t;
		root = searchTree;
	}
	
	
	public void makeMove(Igra i) {
		if (! i.getIsOver()) {
			Poteza p = randomFindPoteza(i);
			i.odigraj(p);
		}
	}
	
	private Poteza randomFindPoteza(Igra i) {
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());

		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}

}
