package tests;

import inteligenca.AI;
import logika.Igra;
import splosno.Poteza;

public class IgraTest {

	public static void main(String[] args) {
		Igra b = new Igra();
		print(b);
		
		AI carlos = new AI(b.getBoard());
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
		
		while (! b.getIsOver()) {
			carlos.makeMove(b);
			print(b);
		}
		System.out.println(b.getWinner());
		carlos.saveTree();
	}
	
	private static void print(Igra x) {
		System.out.println(x);
	}
	
	private static void igraj(int x, int y, Igra b) {
		try {
			b.odigraj(new Poteza(x, y));
			System.out.println("Successful move");			
		} catch (IllegalArgumentException e){
			System.out.println("Illegal move");
		}
	}
}
