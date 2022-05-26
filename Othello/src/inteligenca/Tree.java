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
import java.util.Stack;

import logika.Igra;
import splosno.Poteza;

public class Tree {
	/*
	 * Each node in tree is a representation of a game board state.
	 * Each also has number of simulations run from said board state and
	 * the number of those that have been victories for the active player of current board state
	 */
	
	
	protected byte[][] board; // Board state. Value doesn't change once assigned
	
	protected int nOfSimulations;
	protected int nOfWins;
	
	protected byte player; // Active player
	
	protected Map<Poteza, Tree> children;
	
	/* 
	 **************************************************************************	
	 *                         GETTERS AND SETTERS                            *
	 **************************************************************************  
	 */
	
	public Map<Poteza, Tree> getChildren() {		
		return children;
	}
	
	public int getnOfSimulations() {
		return nOfSimulations;
	}
	
	protected void setnOfSimulations(int x) {
		nOfSimulations = x;
	}
	
	public int getnOfWins() {
		return nOfWins;
	}
	
	protected void setnOfWins(int x) {
		nOfWins = x;
	}
	
	public float getWinPercentage() {
		return nOfWins / nOfSimulations;
	}
	
	public byte getPlayer() {
		return player;
	}
	
	public Poteza getWinningestMove() {
		/*
		 * Returns child with highest win percentage according to this.player
		 * Meaning if children have different player returns child with lowest win percentage
		 * if children have sam player returns child with highest win percentage
		 */

		Poteza best = null;

		for (Poteza p : children.keySet()) {
			
			if(children.get(p).getnOfSimulations() == 0) continue;
			
			if (best == null) {
				best = p;
			} else {
				
				float pWin = 1 - children.get(p).getWinPercentage(); // Assume child is different player
				if (children.get(p).getPlayer() == this.player) {
					// Check if child is same player
					pWin = 1 - pWin;
				}
				
				float bestWin = 1- children.get(best).getWinPercentage(); // Assume child is different player
				if (children.get(p).getPlayer() == this.player) {
					// Check if child is same player
					bestWin = 1 - bestWin;
				}
				
				// Move c is better if it has a higher win percentage or same win percentage with more simulations (means the tree is better explored)
				if (pWin > bestWin || (pWin == bestWin && children.get(p).getnOfSimulations() > children.get(best).getnOfSimulations())) {
					best = p;
				}
			}
		}
		
		// If there is no best move (no children have been explored yet) return random child
		if (best == null) {
			Random generator = new Random();
			Object[] values = children.keySet().toArray();
			return (Poteza) values[generator.nextInt(values.length)];
		}
		
		return best;
	}
	
	/* 
	 **************************************************************************	
	 *                         CONSTRUCTORS                                   *
	 **************************************************************************  
	 */
	
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
		
		this.player = 1; // Igra.Player1 is default
		
		children = new HashMap<Poteza, Tree>();
	}
	
	/* 
	 **************************************************************************	
	 *                         STATIC FUNCTIONS                               *
	 **************************************************************************  
	 */
	
	public static Tree loadTree(String filename) {
		/*
		 * Loads tree from file
		 * 
		 * IMPORTANT: Tree must be saved in format of saveTreeDepth10
		 *            This method is not compatible with saveTree
		 */
		
		try (BufferedReader in = new BufferedReader(new FileReader(filename))) {			
						
			Stack<Tree> stck = new Stack<Tree>();
			
			while (in.ready()) {
				String[] line = in.readLine().split(";", 0);
				
				Poteza p = new Poteza(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
				
				int zamik = Integer.parseInt(line[2]);
				byte player = Byte.parseByte(line[3]);
				int nOfWins = Integer.parseInt(line[5]);
				int nOfSimulations = Integer.parseInt(line[6]);
				
				
				String[] fields = line[4].replace("[", "").replace("]", "").split(", ", 0);
				byte[][] board = new byte[8][8];
				
				for (int i = 0; i < fields.length; i++) {
					board[i / 8][i % 8] = Byte.parseByte(fields[i]);
				}
				
				Tree t = new Tree(board, player);
				t.setnOfSimulations(nOfSimulations);
				t.setnOfWins(nOfWins);
				
				while (zamik < stck.size()) {
					stck.pop();
				}
				
				
				if (!stck.isEmpty()) {
					Tree parent = stck.lastElement();
					parent.addChild(p, t);
					
				}
				
				stck.push(t);				
				
			}
			
			in.close();

			return stck.firstElement();
		} catch (IOException exn) {
			exn.printStackTrace();

			return null;
		}

	}

	/* 
	 **************************************************************************	
	 *                 PRIVATE / PROTECTED METHODS                            *
	 **************************************************************************  
	 */
	
	protected void addChild(Poteza p, Tree child) {
		this.children.put(p, child);
	}
	
	protected int UCT(Tree child) {
		/*
		 * Calculates the UCT of Tree
		 */
		
		if (child.nOfSimulations == 0) {
			// If child has not yet been explored he has highest priority
			return Integer.MAX_VALUE;
		} else {
			// Otherwise return UCT
			return (int) (child.nOfWins / child.nOfSimulations + 1.41 * Math.sqrt(Math.log(this.nOfSimulations) / child.nOfSimulations));			
		}
	}
	
	protected boolean addAvailableMoves() {		
		/*
		 * Adds all legal (available) moves and corresponding board states as children
		 * 
		 * Returns false if there are no legal moves on board and true otherwise
		 */
		
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
				
				this.addChild(p, new Tree(childStanje, childPlayer));
			}
			
			return true;
		} else {
			// If no player has legal moves on board
			
			return false;
		}
	}
	
	protected byte runSimulation() {
		/*
		 * Runs a simulation from current tree by playing a game with random moves
		 * Updates this.nOfSimluations and this.nOfWins after finishing the simulation
		 * 
		 * Returns winner of simulation
		 */
		
		Igra simulation = new Igra(this.board, this.player);
		
		while (! simulation.getIsOver()) {
			
			simulation.odigraj(findRandomMove(simulation));
		}
		
		updateStats(simulation.getWinner());
		
		return simulation.getWinner();
	}
	
	protected void updateStats(byte simulationWinner) {
		/*
		 * Updates this.nOfSimluations and this.nOfWins
		 */
		
		nOfSimulations++;
		if (this.player == simulationWinner) {
			nOfWins++;
		}
	}
	
	protected Poteza findRandomMove(Igra i) {
		/*
		 * Returns random move on current state of Igra i
		 */
		
		List<Poteza> possible = Igra.getLegalMovesList(i.getActivePlayer(), i.getBoard());
		
		Random rand = new Random();
	    return possible.get(rand.nextInt(possible.size()));
		
	}
	
	protected Tree getRandomChild() {
		/*
		 * Returns random child of this 
		 */
		
		Random generator = new Random();
		
		Object[] values = children.values().toArray();
		
		return (Tree) values[generator.nextInt(values.length)];

	}
	
	
	protected Tree findBestChild() {
		/*
		 * Returns child with highest UCT score
		 */
		
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
	
	/* 
	 **************************************************************************	
	 *                            PUBLIC METHODS                              *
	 **************************************************************************  
	 */
	
	public byte cycle() {
		/*
		 * Performs a single cycle of Monte Carlo tree search
		 */
		
		// Expand when you reach node with no children
		if (children.size() == 0) {
			
			// If there are available moves in this position
			if (this.addAvailableMoves()) {
				
				// Get random child
				Tree child = getRandomChild();
				
				// Run simulation
				byte simulationWinner = child.runSimulation();
				
				this.updateStats(simulationWinner);
				
				return simulationWinner;				
			} else {
				// If there are no available moves in this position
				return this.runSimulation();
			}
			
		} else {
			// If there are children available
			
			// Find best child
			Tree bestChild = this.findBestChild();
			
			// Recursively call on him
			byte simulationWinner = bestChild.cycle();
			
			this.updateStats(simulationWinner);
			return simulationWinner;
		}
	}

	
	public void saveTreeDepth10(String filename) {
		/*
		 * Saves first 10 layers of the tree in machine readable format.
		 * Compatible with loadTree
		 */
		
		try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
			String s = this.toStringDepth10();
					
			out.write(s);
			out.close();
		} catch (IOException exn) {
			exn.printStackTrace();
		}

	}
	
	public void saveTree(String filename) {
		/*
		 * Saves the entire tree in human readable format
		 * WARNING: Incompatible with loadTree 
		 */
		
		try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
			String s = this.toString();
		
			out.write(s);
			out.close();
		} catch (IOException exn) {
			exn.printStackTrace();
		}
	}
	
	public String toString(int zamik) {
		/*
		 * Turns the entire tree into human readable string
		 */
		
		String z = new String(new char[zamik]).replace("\0", "-");
		
		String t = z + Arrays.deepToString(board) + " " + Integer.toString(nOfWins) + " / " + Integer.toString(nOfSimulations) + "\n";
		
		for (Tree c : children.values()) {
			t +=  c.toString(zamik + 1);
		}

		return t;

	}
	
	public String toStringDepth10(int zamik) {
		/*
		 * Turns first 10 layers of the tree into machine readable string
		 */
		
		String z;
		
		if (zamik == 0) {
			z = "-1;-1;" + Integer.toString(zamik) + ";" + Integer.toString(player);
		} else {
			z = Integer.toString(zamik) + ";" + Integer.toString(player);			
		}
		
		String t = z + ";" + Arrays.deepToString(board) + ";" + Integer.toString(nOfWins) + ";" + Integer.toString(nOfSimulations) + "\n";
		
		if (zamik < 10) {
			for (Poteza p : children.keySet()) {
				t += Integer.toString(p.getX()) + ";" + Integer.toString(p.getY()) + ";" + this.children.get(p).toStringDepth10(zamik + 1);
			}
		}

		return t;
	}
	
	public String toStringDepth10() {
		return toStringDepth10(0);
	}
		
	public String toString() {
		return toString(0);
	}
	
}
