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
			Poteza p = MCTSFindPoteza(i);
			System.out.println(p);
			i.odigraj(p);
		}
	}
	
	private Poteza randomFindPoteza(Igra i) {
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}
	
	private Poteza MCTSFindPoteza(Igra i) {
		// TODO: Problem: need to update root after opponent move
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		while (currTime < startTime + 500) {
			root.cycle();
			currTime = System.currentTimeMillis();

		}
		
		Poteza p = root.getWinningestMove();
		updateRoot(p);
		return p;
	}
	
	private void updateRoot(Poteza p) {
		root = root.getChildren().get(p);
	}
	
	public void saveTree() {
		searchTree.saveTree("Tree.txt");
	}

}
