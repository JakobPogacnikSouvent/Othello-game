package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import splosno.Poteza;

public class AI {
	
	Tree searchTree; // Monte Carlo search tree initial root
	Tree root;       // Monte Carlo search tree current root
	
	int thinkingTimeMS; // Time in ms allocated for each move in MCTS
	
	/* 
	 **************************************************************************	
	 *                            CONSTRUCTORS                                *
	 **************************************************************************  
	 */
	
	public AI(byte[][] zacetnoStanje, int thinkingTime) {
		/*
		 * Creates Monte Carlo search tree from zacetno stanje
		 */
		
		searchTree = new Tree(zacetnoStanje);
		root = searchTree;
		thinkingTimeMS = thinkingTime;
		
		// Immediately cycle tree. Also enables AI to start as Player2.
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		// Cycle tree until allocated thinking time is up
		while (currTime < startTime + thinkingTimeMS) {
			
			root.cycle();
			
			currTime = System.currentTimeMillis();

		}

	}
	
	public AI(Tree t, int thinkingTime) {
		/*
		 * Crates AI instance with given Monte Carlo search tree as root
		 */
		
		searchTree = t;
		root = searchTree;
		
		thinkingTimeMS = thinkingTime;
	}
	
	/* 
	 **************************************************************************	
	 *                            PUBLIC METHODS                              *
	 **************************************************************************  
	 */
	
	public Poteza makeMoveMCTS(Igra i) {
		/*
		 * Gets move with MCTS and plays it on Igra i
		 * Returns move that was played
		 */
		
		if (! i.getIsOver()) {
			Poteza p = MCTSFindPoteza(i);
			System.out.println(p);
			i.odigraj(p);
			
			return p;
		} else {
			return null;
		}
	}
	
	public Poteza makeMoveRandom(Igra i) {
		/*
		 * Gets random move and plays it on Igra i
		 * Returns move that was played
		 */
		
		if (! i.getIsOver()) {
			Poteza p = randomFindPoteza(i);
			System.out.println(p);
			i.odigraj(p);
			
			return p;
		} else {
			return null;
		}
	}
	
	public Poteza randomFindPoteza(Igra i) {
		/*
		 * Returns random legal move
		 */
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
	}
	
	public Poteza MCTSFindPoteza(Igra i) {
		/*
		 * Selects move with MCTS and returns it
		 */
		
		// If previous move was played by opponent update MCTS root 
		if (i.getPrejsnjiIgralci().size() > 0) {
			if (i.getPrejsnjiIgralci().get(i.getPrejsnjiIgralci().size() - 1) == root.getPlayer()) {
				updateRoot(i.getPrejsnjePoteze().get(i.getPrejsnjePoteze().size() - 1));
			}			
		}
		
		
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
				
		// Cycle tree until allocated thinking time is up
		while (currTime < startTime + thinkingTimeMS) {
			root.cycle();
			currTime = System.currentTimeMillis();
		}
		
		// Get the move with highest win percentage
		Poteza p = root.getWinningestMove();
		
		// Update MCTS root
		updateRoot(p);
		
		return p;
	}
	
	public void updateRoot(Poteza p) {
		/*
		 * Changes root to one of its children
		 */
		
		root = root.getChildren().get(p);
	}
	
	public void saveRoot(String filename) {
		/*
		 * Saves first 10 nodes of root to "filename"
		 */
		
		System.out.println("Saving root...");
		searchTree.saveTreeDepth10(filename);
	}
	
	public void saveRoot() {
		saveRoot("Tree.txt");
	}
	
	public void resetRoot() {
		/*
		 * Resets root to initial root.
		 * WARNING: Memory grows rapidly if root is constantly reset 
		 */
		
		root = searchTree;
	}

}
