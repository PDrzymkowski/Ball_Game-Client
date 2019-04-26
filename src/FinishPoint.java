
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.event.*;
import javax.swing.Timer;
/**
 * klasa opisujaca punkt koncowy gry
 *
 */
public class FinishPoint implements ActionListener{
	private float xPos,yPos;
	 public int width,height;
	 private GamePanel gamepanel;
	public float xMiddle;
	public float yMiddle;
	public float xStart,yStart;
	 private BufferedImage finish;
	 private Timer time;
	 private double angle = 0.0;

	/**
	 * kontruktor klasy z parametrami pola końcowego
	 * @param x położenie na osi X
	 * @param y położenie na osi Y
	 * @param pan odwołanie do panelu gry
	 */
	 public FinishPoint(float x, float y, GamePanel pan)
	 
	 {
		  xStart=x;
		  yStart=y;
			gamepanel = pan;
		 time = new Timer(30,this);
		 try {
			 finish = ImageIO.read(new File("./src/resources/images/finish.png"));
		 } catch (IOException e) {
		 	System.out.println("Could not read the finishline pic!");
		 }
		time.start();
	 };
	 /**
	  * rysowanie celu
	  * @param g kontekst graficzny
	  * @param pan referencja do klasy z parametrami panelu
	  */

		public void paintComponent(Graphics g, GamePanel pan)
		{
			xPos = xStart*gamepanel.getWidth();
			yPos = yStart*gamepanel.getHeight();
			xMiddle=xPos+width/2;
			yMiddle=yPos+height/2;
			Graphics2D g2 = (Graphics2D) g;
			width = pan.getWidth()/12;
			height = pan.getHeight()/10;
			g2.rotate(angle, (xPos+width/2), yPos+height/2);
			g2.drawImage(finish, (int)xPos,(int)yPos,width, height,null);

			}
/**
 * zdarzenie zwi�zane z ruchem punktu ko�cowego wok� w�asnej osi
 */
			public void actionPerformed(ActionEvent e){

				angle += Math.toRadians(5);
				while (angle > 2 * Math.PI)
					angle -= 2 * Math.PI;
			}
}
