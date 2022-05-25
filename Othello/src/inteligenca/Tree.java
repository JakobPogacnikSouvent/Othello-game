package inteligenca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	public int getnOfSimulations() {
		return nOfSimulations;
	}
	
	public int getnOfWins() {
		return nOfWins;
	}
	
	public float getWinPercentage() {
		return nOfWins / nOfSimulations;
	}
	
	public byte getPlayer() {
		return player;
	}
	
	public Poteza getWinningestMove() {
		// Returns child with highest win percentage
		Poteza best = null;

		for (Poteza p : children.keySet()) {
			if(children.get(p).getnOfSimulations() == 0) continue;
			
			if (best == null) {
				best = p;
			} else {
				
				float pWin = 1 - children.get(p).getWinPercentage(); // Assume child is different player
				if (children.get(p).getPlayer() == this.player) {
					pWin = 1 - pWin;
				}
				
				float bestWin = 1- children.get(best).getWinPercentage(); // Asusme child is different player
				if (children.get(p).getPlayer() == this.player) {
					bestWin = 1 - bestWin;
				}
				
				// move c is better if it has a higher win percentage or same win percentage with more simulations (means the tree is better explored)
				if (pWin > bestWin || (pWin == bestWin && children.get(p).getnOfSimulations() > children.get(best).getnOfSimulations())) {
					best = p;
				}
			}
		}
		
		if (best == null) {
			Random generator = new Random();
			Object[] values = children.keySet().toArray();
			return (Poteza) values[generator.nextInt(values.length)];
		}
		
		return best;
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
	
	protected Tree findBestChild() {
		
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
	
	protected boolean addAvailableMoves() {		
		
		if (Igra.hasLegalMoves(player, board)) {
			// If player has legal moves on the board
			
			List<Poteza> moves = Igra.getLegalMovesList(player, board);
			
			for (Poteza p : moves) {
				byte[][] childStanje = Igra.makeMove(player, p, board);
				
				byte childPlayer = (byte) ((player % 2) + 1);
				
				children.put(p, new Tree(childStanje, childPlayer));
			}
			
			return true;
		} else if (Igra.hasLegalMoves((byte) ((player % 2) + 1), board)) {
			// If not player has legal moves on board
			
			byte player = (byte) ((this.player % 2) + 1);
			
			List<Poteza> moves = Igra.getLegalMovesList(player, board);
			
			for (Poteza p : moves) {
				byte[][] childStanje = Igra.makeMove(player, p, board);
				
				byte childPlayer = (byte) ((player % 2) + 1);
				
				children.put(p, new Tree(childStanje, childPlayer));
			}
			
			return true;
		} else {
			// If no player has legal moves on board
			
			return false;
		}
	}
	
	protected byte runSimulation() {
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
	
	protected Poteza findRandomMove(Igra i) {
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());
		
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}
	
	protected Tree getRandomChild() {
		Random generator = new Random();
		
		Object[] values = children.values().toArray();
		
		return (Tree) values[generator.nextInt(values.length)];

	}
	
	public byte cycle() {
		
		// Expand when you reach node with no children
		if (children.size() == 0) {
			
			if (this.addAvailableMoves()) {
				// If there are available moves in this position
				
				// Get random child
				Tree child = getRandomChild();
				
				byte simulationWinner = child.runSimulation();
				
				this.updateStats(simulationWinner);
				return simulationWinner;				
			} else {
				return this.runSimulation();
			}
			
		} else {
			Tree bestChild = this.findBestChild();
			
			byte simulationWinner = bestChild.cycle();
			
			this.updateStats(simulationWinner);
			return simulationWinner;
		}
	}
	
	/*
	public Tree loadTree(String filename) {
		// TODO
	}
	*/
	
	public void saveTreeDepth10(String filename) {
		try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
			String s = this.toStringDepth10();
					
			out.write(s);
			out.close();
		} catch (IOException exn) {
			exn.printStackTrace();
		}

	}
	
	public void saveTree(String filename) {
		try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
			String s = this.toString();
		
			out.write(s);
			out.close();
		} catch (IOException exn) {
			exn.printStackTrace();
		}
	}
	
	public String toString(int zamik) {
		String z = new String(new char[zamik]).replace("\0", "-");
		
		String t = z + Arrays.deepToString(board) + " " + Integer.toString(nOfWins) + " / " + Integer.toString(nOfSimulations) + "\n";
		
		for (Tree c : children.values()) {
			t += c.toString(zamik + 1);
		}

		return t;

	}
	
	protected String toStringDepth10(int zamik, int depth) {
		if (depth >= 10) return "";
		
		// String z = new String(new char[zamik]).replace("\0", "-");
		String z = Integer.toString(zamik);
		
		String t = z + ";" + Arrays.deepToString(board) + ";" + Integer.toString(nOfWins) + ";" + Integer.toString(nOfSimulations) + "\n";
		
		for (Tree c : children.values()) {
			t += c.toStringDepth10(++zamik, ++depth);
		}

		return t;
	}
	
	public String toStringDepth10() {
		return toStringDepth10(0, 0);
	}
		
	public String toString() {
		return toString(0);
	}
	
	public static Tree loadTree(String filename) {
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {			
			
			// runaš stack
			
			while (in.ready()) {
				String[] line = in.readLine().split(";", 0);
				
				int zamik = Integer.parseInt(line[0]);
				int nOfWins = Integer.parseInt(line[2]);
				int nOfSimulations = Integer.parseInt(line[3]);
				
				String[] fields = line[1].replace("[", "").replace("]", "").split(", ", 0);
				System.out.println(fields[0]);
			}
			
			in.close();
		} catch (IOException exn) {
			exn.printStackTrace();
		}

		return null;
	}
}
