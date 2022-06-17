package gui;

import inteligenca.AI;
import logika.Igra;
import splosno.Poteza;

public class VodjaIgre {
	
	public static Okno okno;
	public static Igra igra;
	
	public static byte CPU_Player;
	
	public static boolean clovekNaVrsti = false;
	
	private static AI carlos;
	
	public static void igramoNovoIgro() {
		igra = new Igra();
		CPU_Player = -1;
		igramo();
	}
	
	public static void igramoNovoIgroCPU(byte CPU) {
		igra = new Igra();
		carlos = new AI(Igra.initialBoard(), 500);
		CPU_Player = CPU;
		igramo();
	}
	
	public static void igramo() {
		okno.osveziGUI();
		
		byte ActivePlayer = igra.getActivePlayer();
		boolean isOver = igra.getIsOver();
		
		if (ActivePlayer == CPU_Player && !isOver) {
			igrajCPUPotezo();
		} else {
			clovekNaVrsti = true;
		}
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (clovekNaVrsti) {
			if (igra.odigraj(poteza)) {
				
				clovekNaVrsti = false;
				if (carlos != null) {
					carlos.updateRoot(poteza);					
				}
				igramo();
			}
		}
	}
	
	private static void igrajCPUPotezo() {
		carlos.makeMoveMCTS(igra);
		clovekNaVrsti = true;
		igramo();
	}
}
