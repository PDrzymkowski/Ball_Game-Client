import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * klasa opisujaca glowny element gry - kulkę
 */
public class Ball implements CollisionObserverInterface {

 public float xPos,yPos;
 private double dY=0;
 private  double dt ;
 private static final double g = 0.98;
 private static final int v=2;
 private  ballThrowState state;
 public double normBallRadius=0.01/2;
 public double ballRadius;
 public GamePanel gamePanel;
 private  float startX, startY;
 public int counter=0;
 private int timerCounter=0;
 public boolean invincibility;
 private Timer invTimer;
private Color color;
private float  difficulty;

	/**
	 * stany kulki w trakcie ruchu
	 */
 private enum ballThrowState {
	 
	 LEFT,
	 RIGHT,
	 UP,
	 DOWN,
	 STOP
	 
 }

	/**
	 * konstrktor klasy
	 * @param xpos pozycja startowa
	 * @param ypos pozycja startowa
	 * @param color kolor kulki- określany w menu
	 * @param gamepanel odwołanie do panelu gry
	 */

public Ball(float xpos, float ypos, Color color, GamePanel gamepanel) {

xPos = xpos;
yPos = ypos;
startX=xpos;
startY=ypos;
gamePanel=gamepanel;
this.color = color;
invincibility = false;

String tmp = new String();
tmp =gamepanel.getDifficulty();

/**
 * okreslanie poziomu trodnosci na podstawie wyboru gracza- okresla przyspieszenie z jakim spada kulka
 */
	switch (tmp)
{ case ("EASY") :
	difficulty = (float)1.5;
	break;
case ("MEDIUM") :
	difficulty = (float) 2.1;
break;
	case ("HARD") :
	difficulty = (float)2.7;
	break;
default: 
 
break;


}

dt = 0.002*(double)difficulty;

}
/**
 * metoda odpowiedzialna za ruch kulki, zaimplementowanie fizyki w kierunku osi Y
 */
	 public void gravity(){
dY+= g*dt;
//yPos += (1/2)*g *dt*dt + dY * dt;
if (state == ballThrowState.UP) {

	yPos += (1/2)*g *dt*dt + (dY-v) * dt;
	dY=0;
}
else if (state == ballThrowState.DOWN) {

	yPos += (1/2)*g *dt*dt +(dY+v) * dt;
}
else {
		yPos += (1/2)*g *dt*dt + dY * dt;
	}
    moveX();
}


/**
 *  metoda odpowiedzialna za rzuty pi�ki w kierunku osi X
 */
	private void moveX() {
	if (state == ballThrowState.LEFT)
	{
		xPos += (-1)* v * dt;
	}
		
   if (state == ballThrowState.RIGHT) {
	   xPos += v * dt;
   }
}
/**
 * ustawienie kierunku ruchu kulki
 * @param throwState - parametr zwiazany ze zdarzeniem, wcisnieciem klawisza
 */

 private void setState (ballThrowState throwState) {

 	state= throwState;
 }
/**
 * obs�uga zdarze�- wci�ni�cia klawisza
 * @param evt - zdarzenie
 */
public void keyPressed(KeyEvent evt) {
	int key = evt.getKeyCode();

	
	if (key == KeyEvent.VK_LEFT)
	{
		this.setState(ballThrowState.LEFT);
	}
	
	if (key == KeyEvent.VK_RIGHT)
	{
		this.setState(ballThrowState.RIGHT);
		
	}
	if (key == KeyEvent.VK_UP)
	{
		this.setState(ballThrowState.UP);
		
	}
	if (key == KeyEvent.VK_DOWN)
	{
		this.setState(ballThrowState.DOWN);
	}
}


/**
 * obs�uga zdarzenia zwi�zanego z puszczeniem klawisza
 * @param evt
 */
public void keyReleased(KeyEvent evt) {

    this.setState(ballThrowState.STOP);
}
/**
 * restart ruchu pi�ki od punktu startowego
 */
	public void restart() {
	xPos=startX;
	yPos=startY;
	dY=0;
	counter++;
	}
/**
 * licznik
 * @return
 */
	public int getCounter(){
	return counter;
	}
/**
 * rysowanie kulki
 * @param g2d
 * @param panelSize
 * @param pan
 */
	public void drawBall(Graphics2D g2d, int panelSize, GamePanel pan){

	double ballSize=2*normBallRadius*panelSize;
	 ballRadius = 2*Math.sqrt(ballSize/3.141);
        Ellipse2D ball2d = new Ellipse2D.Double(this.xPos*pan.getWidth(),this.yPos*pan.getHeight(),ballRadius,ballRadius);
        g2d.setColor( color);
        g2d.fillOval((int)ball2d.getX(),(int)ball2d.getY(),(int)ball2d.getWidth(),(int)ball2d.getHeight());
        g2d.setColor(new Color (0,0,0));
        float thickness = 3;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.draw(ball2d);
    }

	@Override
	/**
	 * rozpoznanie zdarze� zwi�zanych z ruchem kulki na planszy
	 */
	public void collisionActionPerformed(CollisionAction action) {
		String actionName=action.getActionName();

		switch(actionName) {
		case "COLLISION" : 
			if(!invincibility)
				restart();
			else if(state==ballThrowState.DOWN) state = ballThrowState.UP;
			else if(state==ballThrowState.UP) state = ballThrowState.DOWN;
			else if(state==ballThrowState.RIGHT) state = ballThrowState.LEFT;
			else if(state==ballThrowState.LEFT) state = ballThrowState.RIGHT;
			else state = ballThrowState.UP;
			break;
		case "OUT" :
			if(!invincibility){
				state = ballThrowState.STOP;
				restart();}
			else if(state==ballThrowState.DOWN) state = ballThrowState.UP;
			else if(state==ballThrowState.UP) state = ballThrowState.DOWN;
			else if(state==ballThrowState.RIGHT) state = ballThrowState.LEFT;
			else if(state==ballThrowState.LEFT) state = ballThrowState.RIGHT;
			 else {
				state=ballThrowState.UP;
			}
			break;


			case "SUPERABILITY" :
				timer(5000, 1);
				break;
			case "NEXTLEVEL" :
				restart();
				break;
				
		default :break;
		}
	}
/**
 * minutnik odliczaj�cy czas ochronny kulki
 */
	public void timer(int period, int action){
	invTimer = new Timer();
	 Color tmpColor = new Color(color.getRGB());
	invTimer.schedule(new TimerTask() {
		@Override
		public void run() {

			invincibility = true;

			color = Color.GREEN;
			timerCounter++;
			if(timerCounter==2){
				if(action==1){
				invincibility = false;
				color = tmpColor;}
				invTimer.cancel();
			}
		}
	},0, period);



	}

}
