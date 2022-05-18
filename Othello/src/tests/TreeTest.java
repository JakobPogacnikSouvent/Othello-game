package tests;

import inteligenca.Tree;
import logika.Igra;

public class TreeTest {
	public static void main(String[] args) {
		Igra b = new Igra();
		Tree t = new Tree(b.getBoard(), b.getActivePlayer());
			
		t.cycle();
		
		printr(t);
		
		printchld(t);
		
		t.cycle();
		t.cycle();
		t.cycle();
		t.cycle();
		t.cycle();

		printr(t);
		
		printchld(t);
		
		System.out.println(t.toString());
		t.saveTree("TreeTest.txt");
		
	}
	
	public static void printr(Tree t) {		
		System.out.print("Ratio of tree: ");
		System.out.print(t.getnOfWins());
		System.out.print(" / ");
		System.out.println(t.getnOfSimulations());
		
	}
	
	public static void printchld(Tree t) {
		// Prints rations of immediate children
		
		for (Tree c : t.getChildren().values()) {
			System.out.print("Ratio of child: ");
			System.out.print(c.getnOfWins());
			System.out.print(" / ");
			System.out.println(c.getnOfSimulations());
		}
	}
}
