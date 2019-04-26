
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * panel z biezacymi informacjami o grze
 *
 */
public class InformationPanel extends JPanel implements Runnable, GameActionObserverInterface{
	private int panelWidth;
	private int panelHeight;
	private Color color;

	private int level;
	private int deathCounter;
	private MenuWindow gameWindow;
	private float timer;
	private JLabel tLabel;
	private JLabel dLabel;
	private JLabel lLabel;
	private ActionListener actionListener;
	private boolean isTimeRunning;

	/**
	 * kontruktor z informacjami początkowymi
	 * @param width szerokosc panelu
	 * @param height wysokosc panelu
	 * @param gamewindow odwołanie do menu
	 * @param actionListener słuchacz zdarzeń
	 */
	public InformationPanel (int width, int height, MenuWindow gamewindow, ActionListener actionListener)
	{
		panelWidth = width;
		panelHeight = (int)height/10;
		color = new Color(255,255,255);
		level=1;
		deathCounter = 5;
		timer=0;
		isTimeRunning = true;
		gameWindow = gamewindow;
		this.setLayout(new GridLayout(1, 2));
		setPreferredSize(new Dimension(panelWidth,panelHeight));
		this.setBackground(color);
		this.actionListener = actionListener;
		tLabel = new JLabel();
		tLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		tLabel.setForeground(Color.white);

		lLabel = new JLabel();
		lLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lLabel.setForeground(Color.white);

		this.add(tLabel);
		this.add(lLabel);

		 dLabel = new JLabel();
		dLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		dLabel.setForeground(Color.WHITE);
		 lLabel.setText("LEVEL: "+level);
		 dLabel.setText("LIVES: "+deathCounter);
		this.add(dLabel);

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem exitToMenu = new JMenuItem("Back to Menu");
		menubar.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		menubar.setForeground(Color.WHITE);

		exitToMenu.setMnemonic(KeyEvent.VK_M);
		exitToMenu.setToolTipText("Exit to main menu (progress will be lost)");

		exitToMenu.addActionListener(actionListener);
		exitToMenu.setActionCommand("FROMGAME");
		menu.add(exitToMenu);

		JMenuItem exit = new JMenuItem("Exit game");
		exit.setToolTipText("Exit game (progress wil be lost)");
		exit.setMnemonic(KeyEvent.VK_E);
		exit.addActionListener( (ActionEvent event) -> {
			System.exit(0);
		});
		JMenuItem pauseBtn = new JMenuItem("Pause game");
		pauseBtn.setToolTipText("Pause game");
		pauseBtn.setMnemonic(KeyEvent.VK_E);
		pauseBtn.addActionListener( (ActionEvent event) -> {
			gameWindow.pause();
		});
		menu.add(exit);
		menu.add(pauseBtn);
		menubar.add(menu);
		add(menubar);
	};

	/**
	 * rysowanie tła
	 * @param g
	 */

	public void paintComponent(Graphics g){

		super.paintComponent(g);

		BufferedImage background = null;

		try{

			background = ImageIO.read(MenuWindow.class.getResource("images/background2.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		g.drawImage(background,0,0,getWidth(),getHeight(), this);

	}


	/**
	 * zmiana poziomu
	 */
	public void setLevel()
	{level++;
	lLabel.setText("LEVEL: "+level);
	
	}

	/**
	 * ilość pozostałych żyć- zmniejszanie ich
	 */
	public void setDeaths()
{--deathCounter;
if (deathCounter <0)
{
	deathCounter=0;
}
dLabel.setText("LIVES: "+deathCounter);
}

	@Override
	/**
	 * reakcje na zdarzenia na planszy gry
	 */
	public void gameActionPerformed(GameAction action) {
		String actionName=action.getActionName();

		switch(actionName) {
		case "DEATH" :

			setDeaths();

			break;

		case "NEXTLEVEL" :
			setLevel();
			break;
	
		default :break;
		}
	}


	/*@Override
	public void actionPerformed(ActionEvent action) {
		String actionName = action.getActionCommand();
		if (actionName == "Timer")
		{
			timer++;
			tLabel.setText(("TIME: "+timer));
		}


	}*/

	/**
	 * wątek liczący czas
	 */
	public void run() {
		// TODO Auto-generated method stub

		while (isTimeRunning) {

					try {
						Thread.sleep(1000);
						Thread.currentThread().interrupt();
					} catch (InterruptedException e) {
						timer++;
					}



			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException ie) {

				}
			}
		}
	}

	/**
	 * informacja o czasie w grze
	 * @return
	 */
	public float getTimer()

	{
		return timer;
	}

	/**
	 * pauza w grze
	 */
	public void pause()
	{
		if (isTimeRunning)
			isTimeRunning = false;
		else isTimeRunning = true;
	}

}


