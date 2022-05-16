package tests;

import inteligenca.Tree;
import logika.Igra;

public class TreeTest {
	public static void main(String[] args) {
		Igra b = new Igra();
		Tree t = new Tree(b.getBoard(), b.getActivePlayer());
			
		t.cycle();
		System.out.println(t.getChildren().size());
		
		printr(t);
		
		System.out.println(t.getChildren());
		t.cycle();
		
	}
	
	public static void printr(Tree t) {		
		System.out.print("Ratio of tree: ");
		System.out.println(t.getRatio());
	}
}
