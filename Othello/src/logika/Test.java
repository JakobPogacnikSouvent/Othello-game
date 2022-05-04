package logika;

import inteligenca.AI;
import splosno.Poteza;

public class Test {

	public static void main(String[] args) {
		Igra b = new Igra();
		print(b);
		
		AI george = new AI();
		
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
		
		System.out.println("========================");
		
		while (! b.getIsOver()) {
			george.makeMove(b);			
		}
		print(b);
		
	}
	
	private static void print(Igra x) {
		System.out.println(x);
	}
	
	private static void igraj(int x, int y, Igra b) {
		boolean valid = b.odigraj(new Poteza(x, y));
		System.out.println(valid);
	}
}
