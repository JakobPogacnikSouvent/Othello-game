package inteligenca;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logika.Igra;
import splosno.Poteza;

public class Tree {
	
	protected byte[][] board;
	
	protected int nOfSimulations;
	protected int nOfWins;
	
	protected byte player;
	
	protected Map<Poteza, Tree> children;
	
	public Map<Poteza, Tree> getChildren() {
		// For testing purposes only
		
		return children;
	}
	
	public float getRatio() {
		return nOfWins / nOfSimulations;
	}
	
	public Tree(byte[][] board, byte player) {
		this.board = board;
		nOfSimulations = 0;
		nOfWins = 0;
		
		this.player = player;
		
		children = new HashMap<Poteza, Tree>();
	}
	
	public Tree(byte[][] board) {
		this.board = board;
		nOfSimulations = 0;
		nOfWins = 0;
		
		this.player = 1; // Player 1 default
		
		children = new HashMap<Poteza, Tree>();
	}
	
	public Tree findBestChild() {
		
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
		if (child.nOfSimulations == 0) {
			return Integer.MAX_VALUE;
		} else {
			return (int) (child.nOfWins / child.nOfSimulations + 1.41 * Math.sqrt(Math.log(this.nOfSimulations) / child.nOfSimulations));			
		}
	}
	
	protected void addAvailableMoves() {
		System.out.println("Getting available moves");
		List<Poteza> moves = Igra.getLegalMovesList(player, board);
		
		
		System.out.println(Arrays.deepToString(board).replace("], ", "],\n"));
		
		
		for (Poteza p : moves) {
			byte[][] childStanje = Igra.makeMove(player, p, board);

			System.out.println(Arrays.deepToString(childStanje).replace("], ", "],\n"));
			
			byte childPlayer = (byte) ((player % 2) + 1);
			
			children.put(p, new Tree(childStanje, childPlayer));
		}
	}
	
	protected byte runSimulation() {
		System.out.println("Running sim");
		Igra simulation = new Igra(this.board, this.player);
		
		while (! simulation.getIsOver()) {
			simulation.odigraj(findRandomMove(simulation));
		}
		
		updateStats(simulation.getWinner());
		
		return simulation.getWinner();
	}
	
	protected void updateStats(byte simulationWinner) {
		nOfSimulations++;
		if (this.player == simulationWinner) {
			nOfWins++;
		}
	}
	
	private Poteza findRandomMove(Igra i) {
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());

		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}
	
	public Tree getRandomChild() {
		Random generator = new Random();
		Object[] values = children.values().toArray();
		return (Tree) values[generator.nextInt(values.length)];

	}
	
	public byte cycle() {
		System.out.println("Cycle");
		
		// Expand when you reach node with no children
		if (children.size() == 0) {
		
			System.out.println(children.size());
			this.addAvailableMoves();	
			System.out.println(children.size());
			
			// Get random child
			Tree child = getRandomChild();
			
			byte simulationWinner = child.runSimulation();
						
			this.updateStats(simulationWinner);
			return simulationWinner;
			
		} else {
			Tree bestChild = this.findBestChild();
			
			byte simulationWinner = bestChild.cycle();
			
			this.updateStats(simulationWinner);
			return simulationWinner;
		}
	}
	
}
