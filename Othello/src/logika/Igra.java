package logika;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import splosno.Poteza;

public class Igra {
	/*
	 * Class to hold board state
	 */
	
	private static int Player1; // The number we will use to mark P1 with
	private static int Player2; // The number we will use to mark P2 with
	private static int Empty; // The number we will mark empty spaces with	
	
	private int activePlayer;
	private int notActivePlayer;
	
	private int[][] board;

	
	public Igra() {
		Player1 = 1;
		Player2 = 2;
		Empty = 0;
		
		activePlayer = Player1;
		notActivePlayer = Player2;
		
		board = innitBoard();
	}
	
	private int[][] innitBoard() {
		/*
		 * Return board starting position
		 */
		 int [][] emptyBoard = new int[8][8];
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
		/*
		 * TODO: If on your turn you cannot outflank and flip at least one opposing disk, your turn is forfeited and your opponent moves again
		 * na koncu naredi check for available moves
		 */		
		if (!((0 <= poteza.getX()) &&  (poteza.getX() < 8) && (0 <= poteza.getY()) && (poteza.getY() < 8))) return false;
		if (board[poteza.getX()][poteza.getY()] != Empty) return false;
		
		List<Poteza> toFlip = findFlips(poteza);
		if (toFlip.size() > 0 ) {

			board[poteza.getX()][poteza.getY()] = activePlayer;

			for (Poteza p : toFlip) {
				turnStone(p);
			}

			// TODO: Check if new current player has available moves.
			// TODO: Check if board is full
			flipPlayers();
			
			return true;
		}
		
		return false;
	}
	
	private boolean[][] legalForPlayer(int player, int[][][] board) {
		/*
		 * Sestavi 8x8 matriko boolean vrednosti, ki nam pove, če na določeno mesto igralec "player" lahko igra potezo.
		 * POZOR: Ta kot argument vzame 8x8x2 matriko, ki jo ustvari funkcija "numberOfValidMoves."
		 */
		boolean[][] boolBoard = new boolean[8][8];
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (board[i][j][player-1] != 0) boolBoard[i][j] = true;
			}
		}
		return boolBoard;
	}
	
	private int[][][] numberOfValidMoves(int[][] board) {
		/*
		 * Sestavi 8x8x2 matriko, ki ima na (i, j, k)-tem mestu število, ki nam pove koliko kamenčkov obrne poteza igralca (k + 1),
		 * če ta postavi kamenček na (i, j)-to mesto.
		 * NE POZABI: Igralcema pripadata števili 1 in 2, zato dobimo zamik pri tretji komponenti matrike.
		 */
		int[][][] boardNew = new int[8][8][2];
		int[] smeri = {-1, 0, 1};
		int[] igralca = {1, 2};
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (board[i][j] != 0) continue;
				for (int igr : igralca) {
					for (int k : smeri) {
						for (int h : smeri) {
							if (k == 0 && h == 0) continue; // Če se ne premaknemo, nadaljuj.
							int q = 1; 						// Večkratnik koraka
							int counter = 0;					
							while (0 <= i + q*k && i + q*k < 8 && 0 <= j + q*h && j + q*h < 8) {
								if (board[i+q*k][j+q*h] == (igr + 1)%2) {
									counter += 1;
									q++;
								}
								else if (board[i+q*k][j+q*h] == igr && counter > 0) {
									boardNew[i][j][igr-1] += counter;
									break;
								}
								else break;
							}
						}
					}
				}
			}
		}
		return boardNew;
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
		
		int temp = activePlayer;
		activePlayer = notActivePlayer;
		notActivePlayer = temp;
		
		return activePlayer;
	}
	
	private List<Poteza> findFlips(Poteza p) {
		/*
		 * Returns all stones that are to be flipped if activePlayer would play a move on (x, y)
		 */
		int x = p.getX();
		int y = p.getY();
		
		List<Poteza> toFlip = new ArrayList<Poteza>();
		int[] t = {-1, 0, 1};
		for (int i : t) {
			for (int j : t) {
				
				List<Poteza> currentChain = new ArrayList<Poteza>();
				int k = 1;
				boolean finishedSuccessfully = false;
				
				while ((0 <= x + k*i) &&  (x + k*i < 8) && (0 <= y + k*j) && (y + k*j < 8)) {
					if (board[x + k*i][y + k*j] == notActivePlayer) {
						currentChain.add(new Poteza(x + k*i, y + k*j));						
						k++;
					} else if (board[x + k*i][y + k*j] == activePlayer) {
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
}
