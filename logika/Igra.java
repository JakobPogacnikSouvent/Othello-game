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
	
	public byte[][] getBoard() {
		return board;
	}

	public boolean[][] getLegalMoves() {
		return legalForPlayer(board);
	}
	
	public boolean[][] getLegalMoves(byte[][] b) {
		return legalForPlayer(b);
	}
	
	public int getActivePlayer() {
		return activePlayer;
	}
	
	public int getNotActivePlayer() {
		return notActivePlayer;
	}
	
	public boolean getIsOver() {
		return isOver;
	}
	
	public Igra() {
		Player1 = 1;
		Player2 = 2;
		Empty = 0;
		
		activePlayer = Player1;
		notActivePlayer = Player2;
		
		board = innitBoard();
		
		isOver = false;
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
	
	public boolean odigraj(Poteza poteza) {	
		if (!((0 <= poteza.getX()) &&  (poteza.getX() < 8) && (0 <= poteza.getY()) && (poteza.getY() < 8))) return false;
		if (board[poteza.getX()][poteza.getY()] != Empty) return false;
		
		List<Poteza> toFlip = findFlips(poteza);
		if (toFlip.size() > 0 ) {

			board[poteza.getX()][poteza.getY()] = activePlayer;

			for (Poteza p : toFlip) {
				turnStone(p);
			}

			if (hasLegalMoves(notActivePlayer, board)) {
				
				flipPlayers();				
			} else if (! hasLegalMoves(board)) {
				// Ko se zgodi da noben igralec nima veljavnih potez je igre konec
				
				endGame(board);
			}			
			return true;
		}
		
		return false;
	}
	
	private boolean turnStone(Poteza p) {
		/*
		 * Changes stone on (x, y) from Player1 to Player2 or vice versa.
		 * Does nothing if (x, y) is empty.
		 * Returns true if it flipped a stone, false otherwise.
		 */
		
		int x = p.getX();
		int y = p.getY();
		
		if (board[x][y] == Player1) { 
			board[x][y] = Player2;
			return true;
		} else if (board[x][y] == Player2) { 
			board[x][y] = Player1;
			return true;
		}
		
		return false;
	}
	
	private int flipPlayers() {
		/*
		 * Swaps active and not active player.
		 * Returns new active player.
		 */
		
		byte temp = activePlayer;
		activePlayer = notActivePlayer;
		notActivePlayer = temp;
		
		return activePlayer;
	}

	private List<Poteza> findFlips(Poteza p, int player) {
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
	
	private List<Poteza> findFlips(Poteza p) {
		/*
		 * Default to active player
		 */
		return findFlips(p, activePlayer);
	}
	
	private int[][][] numberOfValidMoves(byte[][] board) {
		/*
		 * Sestavi 8x8x2 matriko, ki ima na (i, j, k)-tem mestu število, ki nam pove koliko kamenčkov obrne poteza igralca (k + 1),
		 * če ta postavi kamenček na (i, j)-to mesto.
		 * NE POZABI: Igralcema pripadata števili 1 in 2, zato dobimo zamik pri tretji komponenti matrike.
		 */
		
		int[][][] boardNew = new int[8][8][2];
		byte[] igralca = {1, 2};
		
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				
				if (board[x][y] != 0) continue;
				
				for (int igr : igralca) {
					
					int n = findFlips(new Poteza(x, y), igr).size();
					boardNew[x][y][igr-1] = n;
					
				}
			}
		}
		return boardNew;
	}

	private boolean[][] legalForPlayer(byte player, byte[][] board) {
		/*
		 * Sestavi 8x8 matriko boolean vrednosti, ki nam pove, če na določeno mesto igralec "player" lahko igra potezo.
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

	private boolean[][] legalForPlayer(byte[][] board) {
		return legalForPlayer(activePlayer, board);
	}

	
	private boolean hasLegalMoves(byte player, byte[][] board) {
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
	
	private boolean hasLegalMoves(byte[][] board) {
		return hasLegalMoves(activePlayer, board);
	}

	private int endGame(byte[][] board) {
		/*
		 * Nastavi final score in vrne zmagovalca
		 */
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

		isOver = true;
		
		if (finalScoreP1 > finalScoreP2) {
			return Player1;
		} else if (finalScoreP1 < finalScoreP2) {
			return Player2;
		} else {
			// Izenačeno
			return 3;
		}
		
	}
	
}