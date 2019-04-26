import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;

import java.io.IOException;
import javax.sound.sampled.*;
import java.io.File;

/**
 * klasa z całym oknem gry oraz menu rozpoczynającym grę
 */
public class MenuWindow extends JFrame implements ActionListener, GameActionObserverInterface{

	AppStart appStart;
	private int width;
	private int height;
	private int size;
	private JPanel menuPanel;

	private Clip music;
	public boolean isSound;
    private GameOverPanel gameOverPanel;
	private NickPanel nickpanel;
	private HighScoresPanel highscoresPanel;
	private OptionsPanel optionsPanel;
	private Color color;
	private String difficulty;
	private Image background;
	private InformationPanel infoPanel;
	private GamePanel gamePanel;
	public Thread gamePanThread;
	public Thread infoThread;

	private JButton startGameBtn;
	private JButton highScoreBtn;
	private JButton optionsBtn;
	private JButton exitBtn;
	private Clip clickSound;

	private Boolean isOffline;

	/**
	 * kontruktor z parametrami początkowymi całego okna gry
	 * @param app
	 */
	MenuWindow(AppStart app)
	{
		this.setFocusable(true);
		//this.addKeyListener(this);
		appStart=app;
		isOffline = false;
		width=appStart.frameWidth;
		height=appStart.frameHeight;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize( new Dimension(width, height));
		setTitle("Ball: The Game");
		size=height*width;
		color = new Color(255, 0, 0) ;
		difficulty = "EASY";
		isSound = true;
		showUI();
		loadMusic();
		playMusic();

	}

	/**
	 * rysowanie pełnego interfejsu menu
	 */
	void showUI()
	{

        menuPanel = new JPanel(){

			public void paintComponent(Graphics g){

				super.paintComponent(g);
				BufferedImage background = null;
				BufferedImage title = null;

				try{

					background = ImageIO.read(MenuWindow.class.getResource("images/background.jpg"));
					title = ImageIO.read(MenuWindow.class.getResource("images/title.jpg"));
				}catch(IOException e){
					e.printStackTrace();
				}
				g.drawImage(background,0,0,menuPanel.getWidth(),menuPanel.getHeight(), menuPanel);
				g.drawImage(title,(int)(menuPanel.getWidth()*0.3), (int)(menuPanel.getHeight()*0.12), (int)(menuPanel.getWidth()*0.4),
						(int)(menuPanel.getHeight()*0.16), menuPanel);
				size = menuPanel.getHeight()*menuPanel.getWidth();

				startGameBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, (int)(size/31000)));
				highScoreBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/31000));
				optionsBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/31000));
				exitBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/31000));

			}
		};

		startGameBtn = new JButton ("START GAME");
        startGameBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, (int)(size/32000)));
        startGameBtn.setActionCommand("STARTGAME");
        startGameBtn.addActionListener(this);
        startGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


		highScoreBtn = new JButton ("HIGHSCORES");
        highScoreBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/32000));
        highScoreBtn.setActionCommand("HIGHSCORES");
        highScoreBtn.addActionListener(this);
		highScoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

		optionsBtn = new JButton ("OPTIONS");
        optionsBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/32000));
        optionsBtn.setActionCommand("OPTIONS");
        optionsBtn.addActionListener(this);
		optionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

		exitBtn = new JButton ("EXIT");
       exitBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, size/32000));
       exitBtn.setActionCommand("EXIT");
       exitBtn.addActionListener(this);
		exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));


		menuPanel.add(Box.createVerticalStrut(90));
       menuPanel.add(startGameBtn);
		menuPanel.add(Box.createVerticalStrut(20));
       menuPanel.add(highScoreBtn);
		menuPanel.add(Box.createVerticalStrut(20));
       menuPanel.add(optionsBtn);
		menuPanel.add(Box.createVerticalStrut(20));
       menuPanel.add(exitBtn);
		pack();
       add(menuPanel);
			}


	@Override
	/**
	 * reakcje na zdarzenia związane z menu oraz wyborem parametrow, poruszaniem sie miedzy panelami gry
	 */
	public  void actionPerformed(ActionEvent action) {
		String actionName = action.getActionCommand();

		switch (actionName) {
		
		case "STARTGAME" :
			if(isSound) {loadClickSound();}
			setNickWindow();
			break;

		case "HIGHSCORES" :
			if(isSound) {loadClickSound();}
			highscoresPanel = new HighScoresPanel(width, height, this);
			highscoresPanel.getHighscores();
			this.remove(menuPanel);
			this.add(highscoresPanel);
            this.revalidate();
            this.repaint();
			break;

		case "OPTIONS" :
			if(isSound) {loadClickSound();}
			optionsPanel = new OptionsPanel(width, height, this);
			optionsPanel.addObserver(this);
			this.remove(menuPanel);
			this.add(optionsPanel);
			this.revalidate();
			this.repaint();
			break;

		case "EXIT" :
			if(isSound) {loadClickSound();}
			System.exit(0);
			break;

		case "FROMHIGHSCORES" :
			if(isSound) {loadClickSound();}
			this.remove(highscoresPanel);
            this.add(menuPanel);
            this.revalidate();
            this.repaint();
			break;

		case "FROMOPTIONS" :
			if(isSound) {loadClickSound();}
			this.remove(optionsPanel);
			this.add(menuPanel);
			this.revalidate();
			this.repaint();
			break;
			case "FROMGAME":
				if(isSound) {loadClickSound();}
				gamePanel.pause();
				infoPanel.pause();
				this.remove(gamePanel);
				this.remove(infoPanel);
				this.add(menuPanel);
				this.revalidate();
				this.repaint();
				break;
			case "PLAYAGAIN":
				if(isSound) {loadClickSound();}
				this.remove(gameOverPanel);
				setNickWindow();
				break;
			case "FROMGAMEOVER" :
				if(isSound) {loadClickSound();}
				this.remove(gameOverPanel);
				this.add(menuPanel);
				this.revalidate();
				this.repaint();
				break;

			default :
				break;

		}
	}

	/**
	 * uruchamanie panelu gry wraz z informacjami o niej
	 * @param nick
	 */
	public  void setGameWindow(String nick) {
		this.remove(menuPanel);
		infoPanel=new InformationPanel(width ,height,this, this );
		gamePanel = new GamePanel(this, "images/image.jpg",infoPanel, 1, nick);

		add(infoPanel,BorderLayout.NORTH);
		add(gamePanel);
		gamePanel.setVisible(true);
         infoPanel.setVisible(true);
		gamePanThread = new Thread(gamePanel);
		gamePanThread.start();
		infoThread = new Thread(infoPanel);
		infoThread.start();

		this.setVisible(true);
		pack();
	}

	/**
	 * tworzenie panelu poberającego nazwę gracza
	 */
	private void setNickWindow(){
		nickpanel = new NickPanel(width, height, this);
		remove(menuPanel);
		add(nickpanel);
		revalidate();
		repaint();

	}

	@Override
	/**
	 * reakcje na zdarzenia związane z opcjami gry
	 */
	public void gameActionPerformed(GameAction action) {
		String actionName = action.getActionName();

		switch(actionName) {

		case "RED" :
			color = new Color (255, 0, 0);
			 break;
			 
		case "ORANGE" : 
			color = new Color(255, 130, 75);
			break;
		
		case "BLUE" :
			color = new Color (1, 130, 223);
			break;

		case "EASY" : 
			difficulty = "EASY";
			break;

		case "MEDIUM" :
			difficulty = "MEDIUM";
			break;
		
		case "HARD" :
			difficulty = "HARD";
			break;

			case "SOUNDON" :
			isSound = true;
			playMusic();
			break;

			case "SOUNDOFF":
			isSound = false;
			stopMusic();
			break;

		}
	}

	/**
	 * pauza w grze
	 */
	public void pause() {
		infoPanel.pause();
		gamePanel.pause();
	}

	/**
	 * informacja o najwyzszym poziomie gry
	 * @return
	 */
	public int getMaxlvl() {
		return appStart.maxLvl;
	}

	/**
	 * informacja o kolorze
	 * @return
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * informacja o poziomie gry
	 * @return
	 */
	public String getDifficulty()
	{
		return difficulty;
	}

	/**
	 * przebieg zakończenia gry
	 */
	public void gameOver() {
		if(isSound){loadClickSound();}
		this.remove(gamePanel);
		gamePanel.pause();
		infoPanel.pause();
		this.remove(infoPanel);
		gameOverPanel = new GameOverPanel(width, height, this);
		this.add(gameOverPanel);
		this.revalidate();
		this.repaint();

	}

	/**
	 * muzyka w tle
	 */

	public void loadMusic(){


		try {

			File file = new File("./src/resources/sounds/music.wav");
			if (file.exists()) {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);

				music = AudioSystem.getClip();
				music.open(sound);
			} else {
				throw new RuntimeException("File not found");
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 	uruchomienie muzyki
	 */

	private void playMusic(){

		music.loop(-1);
	}

	/**
	 * wyłączenie muzyki
	 */

	public void stopMusic(){
		music.stop();
	}

	/**
	 * dźwięk związany z wciśnięciem przycisku
	 */

	public void loadClickSound(){

		try {

			File file = new File("./src/resources/sounds/click.wav");
			if (file.exists()) {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);

				clickSound = AudioSystem.getClip();
				clickSound.open(sound);
				clickSound.start();
			} else {
				throw new RuntimeException("File not found");
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * poinformowanie aplikacji o braku połączenia z serwerem
	 *
	 */
	public void setOffline() {
		isOffline = true;
	}
}
