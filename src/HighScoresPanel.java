import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * klasa opisująca panel z najlepszymi wynikami
 */
public class HighScoresPanel extends JPanel {
	private final ActionListener actionListener;
	private ArrayList<PlayersScore> scoresList;
	private int width, height;
private Boolean isOffline;

	/**
	 * klasa gracza przechowująca informacje o nim
	 */
	private class PlayersScore {
		private int deaths;
		private float time;
		private String name;

		/**
		 * kontruktor przyjmujący informacje o graczu, który właśnie zakończył grę
		 * @param death ilość żyć, które pozostały graczowi
		 * @param timer czas, w który gracz przeszedł grę
		 * @param namie nazwa gracza
		 */
		public PlayersScore(int death, float timer, String namie) {
			deaths = death;
			time = timer;
			name = namie;
		}

		/**
		 * informacja o ilość żyć, które pozostały graczowi
		 * @return
		 */
		public int getDeaths() {

			return (deaths);
		}

		/**
		 * informacja o czasie, który był potrzebny, by przejść grę
		 * @return
		 */
		public float getTime() {

			return (time);
		}
		/**
		 *informacja o nazwie gracza
		 */
		public String getName() {

			return (name);
		}
	}

	/**
	 * kontruktor z parametrami początkowymi
	 * @param width szerokość okna
	 * @param height wysokość okna
	 * @param actionListener słuchacz zdarzeń
	 */
	public HighScoresPanel(int width, int height, ActionListener actionListener) {

		this.width = width;
		this.height = height;
		this.actionListener = actionListener;
		scoresList = new ArrayList<>();
		isOffline = false;
		setPreferredSize(new Dimension(width, height));
		JButton back = makeBackButton(actionListener);
		getHighscores();
		add(back);
		addInterface();

		setVisible(true);

	}

	/**
	 * rysowanie interfejsu
	 */
	private void addInterface() {


		add(Box.createVerticalStrut(50));
		JLabel highscoreLabel = new JLabel("HIGHSCORES");
		highscoreLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		highscoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		highscoreLabel.setForeground(Color.WHITE);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(highscoreLabel);
		if (!isOffline) {
			add(makeTable());

		}
		else
		{

			JLabel offlineLabel = new JLabel("SEVER IS UNREACHABLE, YOU CAN'T DOWNLOAD HIGHSCORES");
			offlineLabel.setForeground(Color.magenta);
			offlineLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
			offlineLabel.setAlignmentX(CENTER_ALIGNMENT);
			add(offlineLabel);
		}
	}

	/**
	 * rysowanie tabeli z wynikami
	 * @return
	 */
	private Component makeTable() {



        Container container = new Container();
		GridLayout tableLayout = new GridLayout(scoresList.size()+1,4);
		container.setLayout(tableLayout);

        JLabel placeLabel = new JLabel("");
       placeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        placeLabel.setForeground(Color.WHITE);
        container.add(placeLabel);

		JLabel NameLabel = new JLabel("NAME");
		NameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		NameLabel.setForeground(Color.magenta);
		container.add(NameLabel);

        JLabel LiveLabel = new JLabel("LIVE");
        LiveLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        LiveLabel.setForeground(Color.magenta);
        container.add(LiveLabel);

        JLabel TimeLabel = new JLabel("TIME");
        TimeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        TimeLabel.setForeground(Color.magenta);
        container.add(TimeLabel);





        JLabel nameLabelArray[] = new JLabel[scoresList.size()];
        JLabel timeLabelArray[] = new JLabel[scoresList.size()];
        JLabel liveLabelArray[] = new JLabel[scoresList.size()];
        JLabel placeLabelArray[] = new JLabel[scoresList.size()];


        for (int i = 0; i< scoresList.size(); i++)
        {   nameLabelArray[i] = new JLabel();
            timeLabelArray[i] = new JLabel();
            liveLabelArray[i] = new JLabel();
            placeLabelArray[i] = new JLabel();

            placeLabelArray[i].setText(Integer.toString(i+1));
            placeLabelArray[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
            placeLabelArray[i].setForeground(Color.white);
            container.add( placeLabelArray[i]);

            nameLabelArray[i].setText(scoresList.get(i).getName());
            nameLabelArray[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
            nameLabelArray[i].setForeground(Color.white);
            container.add(nameLabelArray[i]);


            liveLabelArray[i].setText(Integer.toString(scoresList.get(i).getDeaths()));
            liveLabelArray[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
            liveLabelArray[i].setForeground(Color.white);
            container.add(liveLabelArray[i]);
			timeLabelArray[i].setText(Float.toString(scoresList.get(i).getTime()));
			timeLabelArray[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
			timeLabelArray[i].setForeground(Color.white);
			container.add(timeLabelArray[i]);


        }






return(container);
	}

	/**
	 * tworzenie guzika powrotu do menu
	 * @param actionListener
	 * @return
	 */
	private JButton makeBackButton(ActionListener actionListener) {


		JButton backBtn = new JButton("BACK");
		backBtn.setAlignmentX(CENTER_ALIGNMENT);
		backBtn.setFocusable(false);
		backBtn.addActionListener(actionListener);
		backBtn.setActionCommand("FROMHIGHSCORES");
		backBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));

		return backBtn;
	}

	/**
	 * pobieranie z pliku wynikow
	 */

	public void getHighscores() {

		Socket socket;
		int deaths;
		float time;
		String name;
		int nameDataLength;

		try {
			InetAddress IP;
			String host;

			IP = InetAddress.getLocalHost();
			host = IP.getHostName();
			socket = new Socket(host, 12129);
			DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
			ostream.writeInt(2);
			DataInputStream istream = new DataInputStream(socket.getInputStream());

			for (int i = 0; i < 10; i++) {
				time = istream.readFloat();
				deaths = istream.readInt();
				nameDataLength = istream.readInt();
				byte[] nameData = new byte[nameDataLength];
				istream.readFully(nameData);
				name = new String(nameData, "UTF-8");
				scoresList.add(new PlayersScore(deaths, time, name));

			}
		} catch (Exception e) {
			isOffline = true;

		}

	}

	/**
	 * rysowanie tła panelu
	 * @param g
	 */

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		BufferedImage background = null;

		try {

			background = ImageIO.read(MenuWindow.class.getResource("images/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

	}

}


	




