
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import java.io.*;
import java.net.*;
import java.util.Timer;
import javax.sound.sampled.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * panel, w którym rozgrywa się gra
 */
public class GamePanel extends JPanel implements Runnable,KeyListener, GameActionObserverInterface {

	private final String nick;
	public Ball ball;
	private MenuWindow gameWindow;
	private BufferedImage background;
	private int panelWidth;
	private int panelHeight;
	private CollisionSeeker collision;
	private int panelSize;
	private FinishPoint finish;
	private SuperPoint superpoint;
	private ArrayList<Obstacle> obstList;
	private InformationPanel infoPanel;
	private ArrayList<Obstacle> obstacleList;
	private float xPosBall;
	private float yPosBall;
	private float xPosFinish;
	private float yPosFinish;
	private float xPosSuperPoint;
	private float yPosSuperPoint;
	private int obstacleNumber;
	private int maxLvl;
	private int currentLvl;
	private boolean loadLvl = false;
	private int lives;
	private int isNext;
	private Boolean isPaused;
	private Socket socket;

	private Color color;
	private String difficulty;

	private boolean isPlaying;
	private Timer timer;

	/**
	 * kontruktor przyjmujacy i ustawiający paramtery początkowe
	 * @param game odwołanie do menu gry
	 * @param imgPath ścieżka do obrazka
	 * @param info odwołanie do panelu z informacjami o bieżącej grze
	 * @param currentLvl informacja o bieżącym poziomie gry
	 * @param nick nazwa użytkownika
	 */
	public GamePanel(MenuWindow game, String imgPath, InformationPanel info, int currentLvl, String nick) {

		gameWindow = game;
		infoPanel = info;
		panelWidth = gameWindow.getWidth();
		panelHeight = gameWindow.getHeight() * 9 / 10;
		maxLvl = game.getMaxlvl();
		isNext = 0;
		lives = 5;
		isPaused = false;
		color = gameWindow.getColor();
		difficulty = gameWindow.getDifficulty();
		this.nick = nick;
		this.currentLvl = currentLvl;
		getLevel(currentLvl);
		collision = new CollisionSeeker();
        isPlaying= true;
		addComponents();


		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
// ryswanie tla//
		try {
			background = ImageIO.read(getClass().getResource(imgPath));


		} catch (IOException ioe) {
			System.out.println("Could not read in the pic");

		}
		collision.addObserver(infoPanel);
		collision.addObserver(this);

		gameWindow.addKeyListener(this);

	}

	/**
	 * pobranie danego poziomu
	 */


	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		panelSize = getWidth() * getHeight();
		g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		for (int i = 0; i < (obstList.size()); i++) {
			obstList.get(i).draw(g, this);
		};

		drawSuperPoint(g2d);
		drawBall(g2d);
		drawFinish(g2d);
	}


	/**
	 *
	 * rysowanie kulki
	 * @param g2d
	 */
	private void drawBall(Graphics2D g2d) {

		ball.drawBall(g2d, panelSize, this);
		collision.check(ball, finish, obstList, superpoint);
		repaint();
	}

	/**
	 * rysowanie pola końcowego
	 * @param g2d
	 */

	private void drawFinish(Graphics2D g2d) {

		finish.paintComponent(g2d, this);
		repaint();
	}

	/**
	 *rysowanie miejsca z dodatkową zdolnością
	 * @param g2d
	 */
	private void drawSuperPoint(Graphics2D g2d) {

		superpoint.drawSuperPoint(g2d, panelSize, this);
		repaint();
	}

	/**
	 * tworzenie elementów znajdujących się na ekranie
	 */
	private void addComponents() {

		ball = new Ball(xPosBall, yPosBall, color, this);

		obstList = obstacleList;
		superpoint = new SuperPoint(xPosSuperPoint, yPosSuperPoint);
		finish = new FinishPoint(xPosFinish, yPosFinish, this);
		collision.addObserver(ball);
	}


	/**
	 * zdarzenie wciśnięcia klawisza
	 * @param evt
	 */
	public void keyPressed(KeyEvent evt) {
		ball.keyPressed(evt);
	}

	/**
	 * zdarzenie puszczenia klawisza
	 * @param evt
	 */

	public void keyReleased(KeyEvent evt) {
		ball.keyReleased(evt);
	}


	@Override
	/**
	 * stuknięcie w klawisz
	 */
	public void keyTyped(KeyEvent arg0) { }


	@Override
	/**
	 * wątek gry
	 */
	public  void run() {
		// TODO Auto-generated method stub

		while (isPlaying) {
			if (!loadLvl && !isPaused) {

				ball.gravity();
				collision.check(ball, finish, obstList, superpoint);

			}
			if (loadLvl){
				isNext = 1;
				if (isNext == 1) {
					try {
						Thread.sleep(200);
						Thread.currentThread().interrupt();
					} catch (InterruptedException e) {
						currentLvl++;
						getLevel(currentLvl);
						isNext = 0;
						addComponents();
					}
				}
			}

			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ie) {

				}
			}
		}
	}


	/**
	 * pobranie danego poziomu
	 *
	 * @param i numer poziomu
	 */

	private void getLevel(int i) {

		String host;

		loadLvl = false;
		obstacleList = new ArrayList<>();

		try {


			BufferedReader fileReader = new BufferedReader(new FileReader("./src/resources/client.txt"));
			String line = fileReader.readLine();

			String[] clientData = new String[2];
			clientData = line.split(" ");

			host = clientData[0];
			int port = Integer.parseInt(clientData[1]);

			socket = new Socket(host, port);
			DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
			ostream.writeInt(1);
			ostream.writeInt(currentLvl);


			DataInputStream istream = new DataInputStream(socket.getInputStream());
			xPosBall = istream.readFloat();

			yPosBall = istream.readFloat();

			xPosFinish = istream.readFloat();

			yPosFinish = istream.readFloat();

			xPosSuperPoint = istream.readFloat();
			yPosSuperPoint = istream.readFloat();

			obstacleNumber = istream.readInt();

			for (int n = 0; n < obstacleNumber; n++) {
				float xPos = istream.readFloat();
				float yPos = istream.readFloat();
				float height = istream.readFloat();
				float width = istream.readFloat();
				Obstacle tmpobst = new Obstacle(xPos, yPos, height, width);
				obstacleList.add(tmpobst);
			}

			socket.close();
		} catch (Exception e) {
			getOfflineLvl(i);
		}
	}

	/**
	 * funkcja pobierająca lokalne pliki, gdyby serwer nie działał
	 * @param i
	 */
	private void getOfflineLvl(int i) {
		String fileName = "./src/resources/levels/lvl" + i;
		fileName = fileName + ".xml";
		obstacleList = new ArrayList<>();

		try {
			File lvlSetFile = new File(fileName);
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(lvlSetFile);
			doc.getDocumentElement().normalize();

			NodeList ballList = doc.getElementsByTagName("Ball");
			Node ballNode = ballList.item(0);

			if (ballNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) ballNode;

				xPosBall = Float.parseFloat(element.getElementsByTagName("xPos").item(0).getTextContent());
				yPosBall = Float.parseFloat(element.getElementsByTagName("yPos").item(0).getTextContent());
			}
			NodeList node = doc.getElementsByTagName("Color");


			for (int p = 0; p < node.getLength(); p++) {
				Node tmpNode = node.item(p);

				if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) tmpNode;

					int r = Integer.parseInt(element.getElementsByTagName("r").item(0).getTextContent());
					int g = Integer.parseInt(element.getElementsByTagName("g").item(0).getTextContent());
					int b = Integer.parseInt(element.getElementsByTagName("b").item(0).getTextContent());

					Color tmpcolor = new Color(r, g, b);

				}
			}

			NodeList obstNumber = doc.getElementsByTagName("ObstaclesNumber");
			Node obstNumberNode = obstNumber.item(0);
			if (obstNumberNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) obstNumberNode;

				obstacleNumber = Integer.parseInt(element.getElementsByTagName("number").item(0).getTextContent());
			}


			NodeList finList = doc.getElementsByTagName("Finish");
			Node finishNode = finList.item(0);
			if (finishNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) finishNode;

				xPosFinish = Float.parseFloat(element.getElementsByTagName("xPos").item(0).getTextContent());
				yPosFinish = Float.parseFloat(element.getElementsByTagName("yPos").item(0).getTextContent());

			}

			NodeList superList = doc.getElementsByTagName("SuperPoint");
			Node superNode = superList.item(0);
			if (superNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) superNode;

				xPosSuperPoint = Float.parseFloat(element.getElementsByTagName("xPos").item(0).getTextContent());
				yPosSuperPoint = Float.parseFloat(element.getElementsByTagName("yPos").item(0).getTextContent());
			}


			NodeList nodeList = doc.getElementsByTagName("Obstacle");

			for (int p = 0; p < nodeList.getLength(); p++) {
				Node tmpNode = nodeList.item(p);

				if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) tmpNode;

					float xPos = Float.parseFloat(element.getElementsByTagName("xPos").item(0).getTextContent());
					float yPos = Float.parseFloat(element.getElementsByTagName("yPos").item(0).getTextContent());
					float height = Float.parseFloat(element.getElementsByTagName("hight").item(0).getTextContent());
					float width = Float.parseFloat(element.getElementsByTagName("weidth").item(0).getTextContent());
					Obstacle tmpobst = new Obstacle(xPos, yPos, height, width);
					obstacleList.add(tmpobst);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		;


	}


	@Override
	/**
	 * reakcje na zdarzenia w gze
	 */
	public void gameActionPerformed(GameAction action) {
		String gameAction = action.getActionName();

		switch (gameAction) {

			case "NEXTLEVEL":
				if(gameWindow.isSound)loadFinishSounds();
				if (currentLvl < maxLvl - 1) {

					loadLvl = true;

				} else {
					gameOver();
				}

				break;

			case "DEATH":
				lostLife();
				if(gameWindow.isSound && !ball.invincibility) loadDamageSounds();
				if (lives < 0) {

					/*lives++;
					* gameOver(); - to wysyłało dane na serwer*/
					gameWindow.gameOver();
				}
				break;

		}
	}

	/**
	 * zakończenie gry
	 */

	private void gameOver() {
		InetAddress IP;
		String host;

		try {
			IP = InetAddress.getLocalHost();
			host = IP.getHostName();
			socket = new Socket(host, 12129);
			DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
			ostream.writeInt(3);
			ostream.writeInt(lives);
			ostream.writeFloat(infoPanel.getTimer());
			byte[] nameData = nick.getBytes("UTF-8");
			ostream.writeInt(nameData.length);
			ostream.write(nameData);
			socket.close();
			gameWindow.gameOver();
		} catch (Exception e) {
/**
 * zabezpieczenie w razie niedziałającego serwera
 */
			gameWindow.setOffline();
			gameWindow.gameOver();
		}

	}

	/**
	 * informacja o poziomie trudności
	 * @return
	 */
	public String getDifficulty() {

		return difficulty;
	}

	/**
	 * utrata życia
	 */
	private void lostLife() {

		lives = --lives;

	}

	/**
	 * pauza w grze
 	 */
	public void pause() {
		if (isPaused) {
			isPaused = false;
		} else {
			isPaused = true;
		}
	}

	/**
	 * dźwięk zderzenia z obiektem
	 */
	private void loadDamageSounds(){
		Clip damageSound;
		try {

			File file = new File("./src/resources/sounds/damage.wav");
			if (file.exists()) {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);

				damageSound = AudioSystem.getClip();
				damageSound.open(sound);
				damageSound.start();
			} else {
				throw new RuntimeException("File not found");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * dźwięk kończący poziom
	 */
	private void loadFinishSounds(){
		Clip finishSound;

		try {
			File file = new File("./src/resources/sounds/finish.wav");
			if (file.exists()) {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);

				finishSound = AudioSystem.getClip();
				finishSound.open(sound);
				finishSound.start();
			} else {
				throw new RuntimeException("File not found");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}



}