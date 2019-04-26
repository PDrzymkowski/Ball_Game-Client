import java.awt.BasicStroke;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * klasa opisujaca przeszkody na drodze pilki
 */
public class Obstacle {

	//private GamePanel gamepanel;
	private float xPos;
	 private float yPos;
	private float height;
	 private float width;
	public Obstacle (float x, float y, float h, float w) {
		xPos=x;
		  yPos=y;
		  height=h;
		  width=w;
	}

	public float getX()
	{return xPos;}
	public float getY()
	{return(yPos);}
	public float getHeight()
	{return(height);}
	public float getWidth()
	{return(width);}
	/**

	 * rysowanie przeszkod
	 * @param g kontekst graficzny
	 * @param pan referencja na GamePane umozliwiajaca dostep do ogolnych parametrow
	 */

	public void draw(Graphics g, GamePanel pan) {

		paintComponent(g, pan);
	}

	/**
	 * szczególowe rysowanie przeszkód
	 * @param g
	 * @param pan
	 */
	private void paintComponent(Graphics g, GamePanel pan) {
		//super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		g2d.setColor(new Color(0,0,255));
		Rectangle2D rect = new Rectangle2D.Double(this.xPos*pan.getWidth(),this.yPos*pan.getHeight(),
				this.width*pan.getWidth(),this.height*pan.getHeight());
		g2d.fillRect((int)rect.getX(),(int)rect.getY(),(int)rect.getWidth(),(int)rect.getHeight());
		g2d.setColor(new Color(0,0,0));
		float thickness = 3;
		g2d.setStroke(new BasicStroke(thickness));
		g2d.draw(rect);

		}
}
