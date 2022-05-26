package tests;

import inteligenca.Inteligenca;
import logika.Igra;
import splosno.Poteza;

public class FinalTest {

	public static void main(String[] args) {
		
		int wins = 0;
		for (int i = 1; i < 100; i++) {
			Igra b = new Igra();
			
			Inteligenca j = new Inteligenca(5000);
			Inteligenca o = new Inteligenca("Idiots", 100);

			int carlosPid = 1 + (i % 2);

			while (! b.getIsOver()) {
				Poteza p;
				
				
				if (b.getActivePlayer() == carlosPid) {
					p = j.izberiPotezo(b);
				} else {
					p = o.izberiNakljucnoPotezo(b);
				}
				
				b.odigraj(p);
			}

			if (b.getWinner() == carlosPid) wins++;
			
			System.out.print(wins);
			System.out.print(" / ");
			System.out.println(i);
			
		}
		
	}

	@SuppressWarnings("unused")
	private static void print(Igra x) {
		System.out.println(x);
	}
}

