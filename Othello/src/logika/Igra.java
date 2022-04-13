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
		boolean isValidMove = false; // Flip flag when we flip an opposing disc. If we check all directions without flip the move is not valid.
		
		List<Poteza> toFlip = findFlips(poteza);
		if (toFlip.size() > 0) {
			isValidMove = true;
			
			for (Poteza p : toFlip) {
				turnStone(p);
			}
		}
		
		return isValidMove;
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
					
				if (finishedSuccessfully) {
					toFlip.addAll(currentChain);
				}
				
				}	
			}
		}
		
		return toFlip;
	}
}
