package tests;

import inteligenca.AI;
import inteligenca.Tree;
import logika.Igra;
import splosno.Poteza;

public class IgraTest {

	public static void main(String[] args) {
		/*
		Igra b = new Igra();
		print(b);
		
		AI carlos = new AI(b.getBoard());
		*/
		
		/*
		System.out.println(b.odigraj(new Poteza(3,2)));
		print(b);
		
		b.odigraj(new Poteza(2,2));
		print(b);
		
		igraj(2,2,b);
		print(b);
		
		igraj(3,1,b);
		print(b);
		
		igraj(7,7,b);
		print(b);

		igraj(-20,20,b);
		print(b);
		*/
		System.out.println("========================");
		
		

		int wins = 0;
		for (int i = 1; i < 11; i++) {
			Igra b = new Igra();			
			AI carlos = new AI(Tree.loadTree("Tree.txt"), 1000);
			
			while (! b.getIsOver()) {
				if (b.getActivePlayer() == 1 + (i % 2)) {
					carlos.makeMoveMCTS(b);
				} else if (b.getActivePlayer() == (1 + ((i + 1) % 2))) {
					Poteza lastMove = carlos.makeMoveRandom(b);
					carlos.updateRoot(lastMove);
				}
			}
			if (b.getWinner() == 1 + (i % 2)) wins++;
			
			System.out.print(wins);
			System.out.print(" / ");
			System.out.println(i);

			carlos.saveRoot();
		}
		// System.out.println(b.getWinner());
	}
	
	@SuppressWarnings("unused")
	private static void print(Igra x) {
		System.out.println(x);
	}
	
	@SuppressWarnings("unused")
	private static void igraj(int x, int y, Igra b) {
		try {
			b.odigraj(new Poteza(x, y));
			System.out.println("Successful move");			
		} catch (IllegalArgumentException e){
			System.out.println("Illegal move");
		}
	}
}
