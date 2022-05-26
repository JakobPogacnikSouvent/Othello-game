package logika;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import splosno.Poteza;

public class Igra {
	
	// IMPORTANT: Player1, Player2 and Empty are constants shared with other classes and should not be modified
	protected final static byte Player1 = 1; // Number used to mark P1 (black) with
	protected final static byte Player2 = 2; // Number to mark P2 (white) with
	protected final static byte Empty = 0; // Number used to mark empty spaces	
	
	protected byte activePlayer; // Player currently on the move
	protected byte notActivePlayer; // Player not currently on the move
	
	protected byte[][] board; // Current board state

	// The following variables are assigned when game ends
	protected boolean isOver; // true if game has ended
	protected int finalScoreP1;  // Sum of P1 chips on board
	protected int finalScoreP2; // Sum of P2 ships on board
	
	// Memorisation of previous moves
	protected List<Poteza> prejsnjePoteze; // Moves in order they happened
	protected List<Byte> prejsnjiIgralci; // The order of players making the moves (it's possible for a player can make 2 moves in a row)
	
	/* 
	 **************************************************************************	
	 *                         GETTERS AND SETTERS                            *
	 **************************************************************************  
	 */
	
	public List<Poteza> getPrejsnjePoteze() {
		return prejsnjePoteze;
	}
	
	public List<Byte> getPrejsnjiIgralci() {
		return prejsnjiIgralci;
	}
	
	public byte[][] getBoard() {
		return board;
	}

	public boolean[][] getLegalMoves(byte player) {
		return legalForPlayer(player, board);
	}
	
	public static boolean[][] getLegalMoves(byte player, byte[][] board) {
		return legalForPlayer(player, board);
	}
	
	public static List<Poteza> getLegalMovesList(byte player, byte[][] board) {
		/*
		 * Returns list of all legal moves for player "player" on board "board" 
		 */
		
		boolean[][] legalMoves = getLegalMoves(player, board);
		
		List<Poteza> possible = new ArrayList<Poteza>();

		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if (legalMoves[x][y]) {
					possible.add(new Poteza(x, y));
				}
			}
		}
		return possible;
	}
	
	public byte getActivePlayer() {
		return activePlayer;
	}
	
	public byte getNotActivePlayer() {
		return notActivePlayer;
	}
	
	public boolean getIsOver() {
		return isOver;
	}
	
	public byte getWinner() {
		/*
		 * Returns the winner of the game. If game is still in progress returns 0
		 * Return values by winner:
		 * - Player1 : 1
		 * - Player2 : 2
		 * - Draw    : 3
		 */
		
		if (!isOver) return 0;
		
		if (finalScoreP1 > finalScoreP2) {
			return Player1;
		} else if (finalScoreP1 < finalScoreP2) {
			return Player2;
		} else {
			// Draw
			return 3;
		}
	}
	
	/* 
	 **************************************************************************	
	 *                         CONSTRUCTORS                                   *
	 **************************************************************************  
	 */
	
	public Igra() {
		/*
		 * Default constructor
		 */
		
		prejsnjePoteze = new ArrayList<Poteza>();
		prejsnjiIgralci = new ArrayList<Byte>();
		
		// Player1 (black) starts the game
		activePlayer = Player1;
		notActivePlayer = Player2;
		
		board = initialBoard();
		
		isOver = false;
	}
	
	public Igra(byte[][] board, byte activePlayer) {
		/*
		 * Create a Igra object from specific board state
		 */
		
		prejsnjePoteze = new ArrayList<Poteza>();
		prejsnjiIgralci = new ArrayList<Byte>();

		this.activePlayer = activePlayer;
		this.notActivePlayer = (byte) ((activePlayer % 2) + 1);			

		this.board = board;
		
		// Check if game is over
		if (!hasLegalMoves(activePlayer, board)) {
			if (!hasLegalMoves(notActivePlayer, board)) {
				isOver = true;
			} else {
				flipPlayers();
			}
		}		
	}
	
	/* 
	 **************************************************************************	
	 *                         STATIC FUNCTIONS                               *
	 **************************************************************************  
	 */
	
	public static byte[][] initialBoard() {
		/*
		 * Returns starting board
		 */
		
		byte [][] emptyBoard = new byte[8][8];
		emptyBoard[3][4] = Player1;
		emptyBoard[4][3] = Player1;
		emptyBoard[3][3] = Player2;
		emptyBoard[4][4] = Player2;
		 
		return emptyBoard;
	}
	
	public static byte[][] makeMove (byte player, Poteza poteza, byte[][] board) {
		/*
		 * Returns a copy of the board after poteza is played on it by player
		 * Raises IllegalArgumentException if the move is illegal
		 */
		
		// Create copy of board
	    byte[][] newBoard = new byte[board.length][];
	       for(int i = 0; i < board.length; i++) {
	    	   newBoard[i] =  board[i].clone();	    	   
	       }
	       
	    // Raise exception if move is illegal (out of bounds or played on a nonempty square)
		if (!((0 <= poteza.getX()) &&  (poteza.getX() < 8) && (0 <= poteza.getY()) && (poteza.getY() < 8))) throw new IllegalArgumentException("Illegal move");
		if (newBoard[poteza.getX()][poteza.getY()] != Empty) throw new IllegalArgumentException("Illegal move");
		
		
		// Get stones to flip
		List<Poteza> toFlip = findFlips(player, poteza, newBoard);		
		
		if (toFlip.size() > 0 ) {
			
			newBoard[poteza.getX()][poteza.getY()] = player;

			// Turn stones
			for (Poteza p : toFlip) {
				turnStone(p, newBoard);
			}

			
		} else {
			// A move that doesn't flip any stones is illegal
			throw new IllegalArgumentException("Illegal move");
		}
		
		return newBoard;

	}
	
	protected static byte[][] turnStone(Poteza p, byte[][] board) {
		/*
		 * Changes stone on Poteza(x, y) from Player1 to Player2 or vice versa.
		 * Does nothing if Poteza(x, y) is empty.
		 */
		
		int x = p.getX();
		int y = p.getY();
		
		if (board[x][y] == Player1) {
			
			board[x][y] = Player2;
			
			return board;
			
		} else if (board[x][y] == Player2) {
			
			board[x][y] = Player1;
			
			return board;
		}
		
		return board;
	}
		
	protected static List<Poteza> findFlips(int player, Poteza p, byte[][] board) {
		/*
		 * Returns coordinates of all stones that are to be flipped after a player would play a move on Poteza(x, y)
		 */
		
		int x = p.getX();
		int y = p.getY();
		int notPlayer;
		
		if (player == 1) {
			notPlayer = 2;
		} else {
			notPlayer = 1;
		}
		
		// Create list to save stones to be flipped in
		List<Poteza> toFlip = new ArrayList<Poteza>();
		
		int[] t = {-1, 0, 1};
		
		// For every direction (up, right, down, left, up-right,...)
		for (int i : t) {
			for (int j : t) {
				
				
				List<Poteza> currentChain = new ArrayList<Poteza>();
				
				int k = 1;
				boolean finishedSuccessfully = false;
				
				// While in bounds
				while ((0 <= x + k*i) &&  (x + k*i < 8) && (0 <= y + k*j) && (y + k*j < 8)) {
					
					if (board[x + k*i][y + k*j] == notPlayer) {

						// If you get on notPlayer save it to current chain
						currentChain.add(new Poteza(x + k*i, y + k*j));						
						k++;
						
					} else if (board[x + k*i][y + k*j] == player) {
						
						// If you get on player current chain is finished successfully
						finishedSuccessfully = true;
						break;
						
					} else if (board[x + k*i][y + k*j] == Empty) {
						
						// If you get on empty space current chain is not finished successfully
						break;
					}				
				}	
				
				// All stones in successfull chain should be flipped
				if (finishedSuccessfully) {
					toFlip.addAll(currentChain);
				}
			}
		}
		
		return toFlip;
	}
	
	protected static int[][][] numberOfValidMoves(byte[][] board) {		
		/*
		 * Creates an 8x8x2 matrix. Value at (i, j, k) represents number of flips a move on (i, j) would create if played by player k+1
		 * 
		 */
		
		
		int[][][] boardNew = new int[8][8][2];
		byte[] igralca = {Player1, Player2};
		
		// For every space on board
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				
				// If space is not empty pass
				if (board[x][y] != Empty) continue;
				
				// Get number of flips for each player
				for (int igr : igralca) {
					
					int n = findFlips(igr, new Poteza(x, y), board).size();
					boardNew[x][y][igr-1] = n;
					
				}
			}
		}
		
		return boardNew;
	}

	protected static boolean[][] legalForPlayer(byte player, byte[][] board) {
		/*
		 * Creates 8x8 matrix. Value at (i, j) is true if player "player" can legally play a move there and false otherwise.
		 */
		
		boolean[][] boolBoard = new boolean[8][8];
		
		int[][][] validBoard = numberOfValidMoves(board);
		
		// For every space on board
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
				// Check if number of valid moves on that space is more than 0
				if (validBoard[i][j][player-1] != 0) boolBoard[i][j] = true;
			}
		}
		return boolBoard;
	}	
	
	public static boolean hasLegalMoves(byte player, byte[][] board) {
		/*
		 * Returns true if player has legal (possible) moves on board and false otherwise
		 */
		
		boolean[][] boolBoard = legalForPlayer(player, board);
			
		// Check matrix until you find a legal move
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
				if (boolBoard[i][j]) return true;
			}
		}
		return false;
		
	}

	/* 
	 **************************************************************************	
	 *                 PRIVATE / PROTECTED METHODS                            *
	 **************************************************************************  
	 */
	
	
	protected void endGame() {
		/*
		 * Assigns final score. Assigns isOver to true
		 */
		assignFinalScore();
		
		isOver = true;
	}
	
	
	protected void assignFinalScore() {
		/*
		 * Assigns values to finalScoreP1 and finalScoreP2
		 */
		
		finalScoreP1 = 0;
		finalScoreP2 = 0;
		
		// For every space on board increase final score for that player
		
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
				if (board[i][j] == Player1) {
					finalScoreP1++;
					
				} else if (board[i][j] == Player2) {
					finalScoreP2++;
				}
			}
		}

	}
	
	protected byte flipPlayers() {
		/*
		 * Swaps active and not active player.
		 * Returns new active player.
		 */
		
		byte temp = activePlayer;
		activePlayer = notActivePlayer;
		notActivePlayer = temp;
		
		return activePlayer;
	}
	
	/* 
	 **************************************************************************	
	 *                            PUBLIC METHODS                              *
	 **************************************************************************  
	 */
	
	public boolean odigraj(Poteza poteza) {
		/*
		 * Plays poteza "poteza" on this.board
		 * Returns true if it was successful or false if poteza was deemed illegal
		 */
		
		try {
			
			this.board = makeMove(activePlayer, poteza, this.board);		
			
		} catch (IllegalArgumentException e) {
			
			// makeMove raises IllegalArgumentException if poteza was deemed illegal
			return false;
		}
		
		
		// Update memorisation of previous moves
		prejsnjePoteze.add(poteza);
		prejsnjiIgralci.add(activePlayer);

		// Change game state
		if (hasLegalMoves(notActivePlayer, board)) {
			// Only flip players if not active players has legal (available) moves
			
			flipPlayers();				
			
		} else if (! hasLegalMoves(activePlayer, board)) {
			// End game when no player has legal (available) moves
			
			endGame();
		}		
		
		return true;
	}



	public String toString() {
		/*
		 * Returns board as human readable string
		 */
		
		return Arrays.deepToString(board).replace("], ", "],\n");
	}
}