package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import hefe.VodjaIgre;
import logika.Igra;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	
	private Platno platno;
	
	private JLabel status;
	private JLabel rez;
	
	private JMenuItem menuZazeni, menuOdpri, menuShrani, menuKoncaj;
	private JMenuItem menuBarvaIgralecEna, menuBarvaIgralecDve, menuBarvaPolja, menuBarvaPredzadje;
	
	public Okno() {
		setTitle("Othello 1.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Ureditev orodne vrstice
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
				
		JMenu menuDatoteka = dodajMenu(menubar, "Igra");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
				
		menuZazeni = dodajItem(menuDatoteka, "Zaženi");
		menuKoncaj = dodajItem(menuDatoteka, "Zapri program");
				
		menuBarvaIgralecEna = dodajItem(menuNastavitve, "Nastavi barvo prvega igralca.");
		menuBarvaIgralecDve = dodajItem(menuNastavitve, "Nastavi barvo drugega igralca.");
		menuBarvaPolja = dodajItem(menuNastavitve, "Nastavi barvo polj.");
		menuBarvaPredzadje = dodajItem(menuNastavitve, "Nastavi barvo mreže.");
		// Glavna plošča
		JPanel glavnaPlosca = new JPanel();
		glavnaPlosca.setLayout(new BoxLayout(glavnaPlosca, BoxLayout.Y_AXIS));
		this.add(glavnaPlosca);
		
		// JPanel, ki je nad igralnim poljem
		JPanel strop = new JPanel();
		
		// Sporočilo na stropu
		status = new JLabel();
		strop.add(status);
		
		glavnaPlosca.add(strop);
		
		// Igralno polje
		platno = new Platno(600, 600);
		glavnaPlosca.add(platno);
		
		// JPanel, ki je pod igralnim poljem
		JPanel temelj = new JPanel();
		
		// Števec točk
		rez = new JLabel();
		temelj.add(rez);
		
		
		
		glavnaPlosca.add(temelj);
		
		status.setText("Pozdravljeni!");
		rez.setText("Začnimo, ko boste pripravljeni");
		
		
	}
	
	public void osveziGUI() {
		if (VodjaIgre.igra == null) {
			status.setText("Nič ne dogaja...");
		}
		else {
			byte porocilo = VodjaIgre.igra.getWinner();
			if (porocilo == 0) {
				byte id = VodjaIgre.igra.getActivePlayer();
				status.setText("Na potezi je " + id);
				byte[][] board = VodjaIgre.igra.getBoard();
				int prvi = Igra.getScore((byte) 1, board);
				int drugi = Igra.getScore((byte)2, board);
				rez.setText(prvi + " || " + drugi);
			}
			else if (porocilo == 1) status.setText("Zmagal je igralec 1!");
			else if (porocilo == 2) status.setText("Zmagal je igralec 2!");
			else status.setText("Izenačenje?! :O");
		}
		platno.repaint();
	}
	
	
	private JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;
	}
	
	
	private JMenuItem dodajItem(JMenu dom, String ime) {
		JMenuItem menuItem = new JMenuItem(ime);
		dom.add(menuItem);
		menuItem.addActionListener(this);
		return menuItem;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == menuZazeni) {
			VodjaIgre.igramoNovoIgro();
		} 
		else if (source == menuOdpri) {
			//TODO
		}
		else if (source == menuShrani) {
			//TODO
		}
		else if (source == menuKoncaj) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if (source == menuBarvaIgralecEna) {
			Color izbira = JColorChooser.showDialog(this, "Barva kamenčkov prvega igralca", platno.barvaIgralecEna);
			if (izbira == null) return;
			platno.barvaIgralecEna = izbira;
			platno.repaint();
		}
		else if (source == menuBarvaIgralecDve) {
			Color izbira = JColorChooser.showDialog(this, "Barva kamenčkov drugega igralca", platno.barvaIgralecDve);
			if (izbira == null) return;
			platno.barvaIgralecDve = izbira;
			platno.repaint();
		}
		else if (source == menuBarvaPolja) {
			Color izbira = JColorChooser.showDialog(this, "Barva igralnih polj", platno.barvaPolje);
			if (izbira == null) return;
			platno.barvaPolje = izbira;
			platno.repaint();
		}
		else if (source == menuBarvaPredzadje) {
			Color izbira = JColorChooser.showDialog(this, "Barva mreže", platno.barvaPredzadje);
			if (izbira == null) return;
			platno.barvaPredzadje = izbira;
			platno.repaint();
		}
	}
}
