package blackjack;

public class Main implements Runnable {
	
	long xTime = System.nanoTime();
	
	//screen refresh rate
	public int Hz = 100;
	
	GUI gui = new GUI();
	
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
	
	@Override
	public void run() {
		while(true) {
			if (System.nanoTime() - xTime >= 1000000000/Hz) {
				gui.refresher();
				gui.repaint();
				xTime = System.nanoTime();
			}
		}
	}
	
}
