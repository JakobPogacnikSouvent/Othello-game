package gui;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import logika.Igra;
import splosno.Poteza;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	protected Igra igra;
	
	protected Color barvaIgralecEna;
	protected Color barvaIgralecDve;
	protected Color barvaPolje;
	protected Color barvaPredzadje;
	protected Color barvaOzadje;
	
	protected int pod_not;
	protected int pod_zun;
	protected int polje;
	protected int sirina;
	protected int korak;
	protected int xStart;
	protected int yStart;
	protected int xst;
	protected int yst;
	
	protected boolean lezece;
	
	
	public Platno(int sirina, int visina) {
		this.setPreferredSize(new Dimension(sirina, visina));
		igra = null;
		
		barvaIgralecEna = Color.BLACK;
		barvaIgralecDve = Color.WHITE;
		barvaPolje = Color.GREEN;
		barvaPredzadje = Color.BLACK;
		barvaOzadje = new Color(17, 59, 8);
		
		
		setBackground(barvaOzadje);
		
		addMouseListener(this);
		setFocusable(true);
	}
	
	
	private final static double PADDING_ZUNANJI = 0.05;
	private final static double PADDING_NOTRANJI = 0.01;
	
	
	private boolean lezece() {
		return getWidth() > getHeight();
	}
	
	
	private double sirina() {
		return Math.min(getWidth(), getHeight());
	}
	
	
	private int sirinaPolja() {
		double s = sirina();
		return (int) (s * (1. - (2. * PADDING_ZUNANJI + 9. * PADDING_NOTRANJI)) / 8.);
	}
	
	private int podlogaNotranja() {
		double s = sirina();
		return (int) (s * (1. - 2. * PADDING_ZUNANJI) * PADDING_NOTRANJI);
	}
	
	private int podlogaZunanja() {
		double s = sirina();
		return (int) (s * PADDING_ZUNANJI);
	}
	
	private int sirinaIgre() {
		int polje = sirinaPolja();
		int podloga = podlogaNotranja();
		return 9 * podloga + 8 * polje;
	}
	
	private int getSirina() {
		return (int) getWidth();
	}
	
	private int getVisina() {
		return (int) getHeight();
	}
	
	// Da se izognem večjim nepravilnostim, vs porašunam v naprej.
	private void nastavi() {
		lezece = lezece();		
		sirina = sirinaIgre();
		pod_zun = podlogaZunanja();
		pod_not = podlogaNotranja();
		polje = sirinaPolja();
		korak = polje + pod_not;
		if (lezece) {
			xStart = (int) getSirina() / 2 - sirina / 2;
			yStart = pod_zun;
		} else {
			xStart = pod_zun;
			yStart = (int) getVisina() / 2 - sirina / 2;
		}
		xst = xStart + pod_not;
		yst = yStart + pod_not;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		nastavi();
		
		// Nariši ploščo
		g2.setColor(barvaPredzadje);
		g2.fillRect(xStart, yStart, sirina, sirina);
		
		// Nariši polja na ploščo
		g2.setColor(barvaPolje);
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				int iks = xst + i * (korak);
				int ips = yst + j * (korak);
				g2.fillRect(iks, ips, polje, polje);
			}
		}
		
		// Nariši kamenčke
		if (VodjaIgre.igra != null) {
			paintStones(g2, VodjaIgre.igra);
		}
		
	}
	
	private void paintStones(Graphics2D g2, Igra igra) {
		byte[][] plosca = igra.getBoard();
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (plosca[i][j] != 0) {
					// System.out.println("Printing stone " + i + " " + j);
					paintPlayer(g2, j, i, plosca[i][j]);
				}
			}
		}
	}
	
	private void paintPlayer(Graphics2D g2, int i, int j, int p) {
		if (p == 1) g2.setColor(barvaIgralecEna);
		else g2.setColor(barvaIgralecDve);
		g2.fillOval(xst + i * korak, yst + j * korak, polje, polje);
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int i = (x - xst) / korak;
		int j = (y - yst) / korak;
		VodjaIgre.igrajClovekovoPotezo(new Poteza(j, i));
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
