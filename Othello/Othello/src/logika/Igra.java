package logika;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import splosno.Poteza;

public class Igra {
	/*
	 * Class to hold game state
	 */
	
	private static byte Player1; // The number we will use to mark P1 with
	private static byte Player2; // The number we will use to mark P2 with
	private static byte Empty; // The number we will mark empty spaces with	
	
	private int finalScoreP1;
	private int finalScoreP2;
	
	private byte activePlayer;
	private byte notActivePlayer;
	
	private byte[][] board;

	private boolean isOver;
	
	public List<Poteza> prejsnjePoteze;
	public List<Byte> prejsnjiIgralci; // Igralec lahko naredi 2 zaporedni potezi
	
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
		// Returns the winner of the game. If game is still in progress returns 0
		
		if (!isOver) return 0;
		
		if (finalScoreP1 > finalScoreP2) {
			return Player1;
		} else if (finalScoreP1 < finalScoreP2) {
			return Player2;
		} else {
			// IzenaÄ�eno
			return 3;
		}
	}
	
	public Igra() {
		innitStatics();
		
		activePlayer = Player1;
		notActivePlayer = Player2;
		
		board = innitBoard();
		
		isOver = false;
	}
	
	public Igra(byte[][] board, byte activePlayer) {
		// Create a game object from existing board state
		innitStatics();

		this.activePlayer = activePlayer;
		this.notActivePlayer = (byte) ((activePlayer % 2) + 1);			

		this.board = board;
		
		if (!hasLegalMoves(activePlayer, board)) {
			if (!hasLegalMoves(notActivePlayer, board)) {
				isOver = true;
			} else {
				flipPlayers();
			}
		}		
	}
	
	protected void innitStatics() {
		Player1 = 1; // IMPORTANT: used as a constant in Tree constructor
		Player2 = 2; 
		Empty = 0;
		
		prejsnjePoteze = new ArrayList<Poteza>();
		prejsnjiIgralci = new ArrayList<Byte>();
		
	}
	
	private byte[][] innitBoard() {
		/*
		 * Return board starting position
		 */
		byte [][] emptyBoard = new byte[8][8];
		emptyBoard[3][4] = Player1;
		emptyBoard[4][3] = Player1;
		emptyBoard[3][3] = Player2;
		emptyBoard[4][4] = Player2;
		 
		return emptyBoard;
	}
	
	public String toString() {
		return Arrays.deepToString(board).replace("], ", "],\n");
	}
	
	
	public static byte[][] makeMove (byte player, Poteza poteza, byte[][] board) {
		// Return copy of board after player player makes the move poteza on board board
		// If the move is deemed illegal throw an exception
		
		// TODO: Should copy board
	    byte[][] newBoard = new byte[board.length][];
	    // Copying elements of arr1[ ] to arr2[ ] using the clone() method
	       for(int i = 0; i < board.length; i++) {
	    	   newBoard[i] =  board[i].clone();	    	   
	       }
	       
		if (!((0 <= poteza.getX()) &&  (poteza.getX() < 8) && (0 <= poteza.getY()) && (poteza.getY() < 8))) throw new IllegalArgumentException("Illegal move");
		if (newBoard[poteza.getX()][poteza.getY()] != Empty) throw new IllegalArgumentException("Illegal move");
		
		List<Poteza> toFlip = findFlips(poteza, player, newBoard);
		
		if (toFlip.size() > 0 ) {

			newBoard[poteza.getX()][poteza.getY()] = player;

			for (Poteza p : toFlip) {
				turnStone(p, newBoard);
			}

			
		}
		
		return newBoard;

	}
	
	public boolean odigraj(Poteza poteza) {
		board = makeMove(activePlayer, poteza, board);
		
		prejsnjePoteze.add(poteza);
		prejsnjiIgralci.add(activePlayer);

		if (hasLegalMoves(notActivePlayer, board)) {
			
			flipPlayers();				
		} else if (! hasLegalMoves(activePlayer, board)) {
			// Ko se zgodi da noben igralec nima veljavnih potez je igre konec
			
			endGame();
		}			
		return true;
	}
	
	private static byte[][] turnStone(Poteza p, byte[][] board) {
		/*
		 * Changes stone on (x, y) from Player1 to Player2 or vice versa.
		 * Does nothing if (x, y) is empty.
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
	
	private byte flipPlayers() {
		/*
		 * Swaps active and not active player.
		 * Returns new active player.
		 */
		
		byte temp = activePlayer;
		activePlayer = notActivePlayer;
		notActivePlayer = temp;
		
		return activePlayer;
	}

	private static List<Poteza> findFlips(Poteza p, int player, byte[][] board) {
		/*
		 * Returns all stones that are to be flipped if player would play a move on (x, y)
		 */
		int x = p.getX();
		int y = p.getY();
		int notPlayer;
		
		if (player == 1) {
			notPlayer = 2;
		} else {
			notPlayer = 1;
		}
		
		List<Poteza> toFlip = new ArrayList<Poteza>();
		int[] t = {-1, 0, 1};
		for (int i : t) {
			for (int j : t) {
				
				List<Poteza> currentChain = new ArrayList<Poteza>();
				int k = 1;
				boolean finishedSuccessfully = false;
				
				while ((0 <= x + k*i) &&  (x + k*i < 8) && (0 <= y + k*j) && (y + k*j < 8)) {
					if (board[x + k*i][y + k*j] == notPlayer) {
						currentChain.add(new Poteza(x + k*i, y + k*j));						
						k++;
					} else if (board[x + k*i][y + k*j] == player) {
						finishedSuccessfully = true;
						break;
					} else if (board[x + k*i][y + k*j] == Empty) {
						// finishedSuccessfully = false
						break;
					}				
				}	
				
				if (finishedSuccessfully) {
					toFlip.addAll(currentChain);
				}
			}
		}
		return toFlip;
	}
	
	private static int[][][] numberOfValidMoves(byte[][] board) {
		/*
		 * Sestavi 8x8x2 matriko, ki ima na (i, j, k)-tem mestu Å¡tevilo, ki nam pove koliko kamenÄ�kov obrne poteza igralca (k + 1),
		 * Ä�e ta postavi kamenÄ�ek na (i, j)-to mesto.
		 * NE POZABI: Igralcema pripadata Å¡tevili 1 in 2, zato dobimo zamik pri tretji komponenti matrike.
		 */
		
		int[][][] boardNew = new int[8][8][2];
		byte[] igralca = {1, 2};
		
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				
				if (board[x][y] != 0) continue;
				
				for (int igr : igralca) {
					
					int n = findFlips(new Poteza(x, y), igr, board).size();
					boardNew[x][y][igr-1] = n;
					
				}
			}
		}
		return boardNew;
	}

	private static boolean[][] legalForPlayer(byte player, byte[][] board) {
		/*
		 * Sestavi 8x8 matriko boolean vrednosti, ki nam pove, Ä�e na doloÄ�eno mesto igralec "player" lahko igra potezo.
		 */
		
		boolean[][] boolBoard = new boolean[8][8];
		
		int[][][] validBoard = numberOfValidMoves(board);
		
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
				if (validBoard[i][j][player-1] != 0) boolBoard[i][j] = true;
			}
		}
		return boolBoard;
	}	
	
	public static boolean hasLegalMoves(byte player, byte[][] board) {
		/*
		 * Returns true if player has legal moves or false otherwise
		 */
		
		boolean[][] boolBoard = legalForPlayer(player, board);
				
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				
				if (boolBoard[i][j]) return true;
			}
		}
		return false;
		
	}

	private void endGame() {
		/*
		 * Nastavi final score in spremeni isOver
		 */
		setFinalScore();
		
		isOver = true;
	}
	
	private void setFinalScore() {
		finalScoreP1 = 0;
		finalScoreP2 = 0;
		
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
	
}