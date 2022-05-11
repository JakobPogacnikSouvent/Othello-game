package inteligenca;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logika.Igra;
import splosno.Poteza;

public class AI {
	
	Tree d;
	
	
	public AI(byte[][] zacetnoStanje) {
		d = new Tree(zacetnoStanje);
	}
	
	public void makeMove(Igra i) {
		if (! i.getIsOver()) {
			Poteza p = randomFindPoteza(i);
			i.odigraj(p);
		}
	}
	
	private Poteza randomFindPoteza(Igra i) {
		boolean[][] legalMoves = i.getLegalMoves();
		
		List<Poteza> possible = new ArrayList<Poteza>();
		
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if (legalMoves[x][y]) {
					possible.add(new Poteza(x, y));
				}
			}
		}
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}

}
