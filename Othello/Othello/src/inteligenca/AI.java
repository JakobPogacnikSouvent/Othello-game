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
		
		root.cycle(); // To immediatelly add children so update root can immediatelly be called and he is able to start as second player
	}
	
	public AI(Tree t) {
		searchTree = t;
		root = searchTree;
		
		System.out.println(t.getnOfSimulations());
	}
	
	
	public Poteza makeMoveMCTS(Igra i) {
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
		if (! i.getIsOver()) {
			Poteza p = randomFindPoteza(i);
			System.out.println(p);
			i.odigraj(p);
			
			return p;
		} else {
			return null;
		}
	}
	
	private Poteza randomFindPoteza(Igra i) {
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
	}
	
	public Poteza MCTSFindPoteza(Igra i) {
		if (i.prejsnjiIgralci.size() > 0) {
			if (i.prejsnjiIgralci.get(i.prejsnjiIgralci.size() - 1) == root.getPlayer()) {
				updateRoot(i.prejsnjePoteze.get(i.prejsnjePoteze.size() - 1));
			}			
		}
		
		
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		int thinkingTimeMS = 1000; // Should be 5000 in final release
		
		while (currTime < startTime + thinkingTimeMS) {
			root.cycle();
			currTime = System.currentTimeMillis();

		}
		
		Poteza p = root.getWinningestMove();
		updateRoot(p);
		return p;
	}
	
	public void updateRoot(Poteza p) {
		root = root.getChildren().get(p);
	}
	
	public void saveRoot() {
		System.out.println("Saving root...");
		searchTree.saveTreeDepth10("Tree.txt");
	}
	
	public void resetRoot() {
		root = searchTree;
	}

}
