package inteligenca;

import java.util.HashMap;
import java.util.Map;

import splosno.Poteza;

public class Tree {
	
	protected byte[][] stanje;
	
	protected int nOfSimulations;
	protected int nOfWins;
	
	protected byte player;
	
	protected Map<Poteza, Tree> children;
	
	public Tree(byte[][] stanje) {
		this.stanje = stanje;
		nOfSimulations = 0;
		nOfWins = 0;
		
		children = new HashMap<Poteza, Tree>();
	}
	
	public Tree findLeaf() {
		
		Tree best = null;
		
		for (Poteza p : children.keySet()) {
			if (best == null) {
				best = children.get(p);
			} else if (UCT(children.get(p)) > UCT(best)) {
				best = children.get(p);
			}
		}
		
		return best;
	}
	
	protected int UCT(Tree child) {
		return (int) (child.nOfWins / child.nOfSimulations + 1.41 * Math.sqrt(Math.log(this.nOfSimulations) / child.nOfSimulations));
	}
	
	
}
