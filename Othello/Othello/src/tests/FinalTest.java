package tests;

import inteligenca.AI;
import inteligenca.Inteligenca;
import inteligenca.Tree;
import logika.Igra;
import splosno.Poteza;

public class FinalTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Igra b = new Igra();
		Inteligenca j = new Inteligenca();
		Inteligenca o = new Inteligenca();
		
		
		while (! b.getIsOver()) {
			Poteza p;
			
			if (b.getActivePlayer() == 1) {
				p = j.izberiPotezo(b);
			} else {
				p = o.izberiPotezo(b);
			}
			
			b.odigraj(p);
			
			print(b);
		}
		
		System.out.println(b.getWinner());
	}

	private static void print(Igra x) {
		System.out.println(x);
	}
}

