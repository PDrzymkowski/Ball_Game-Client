import java.awt.*;
import java.awt.geom.Ellipse2D;
/**
 * 
 * klasa opisuj¹ca punkt z dodatkow¹ zdolnoœci¹
 *
 */
public class SuperPoint {

    public double height, width, xPos, yPos;
   
    private double normBallRadius=0.0007/2;
    public double ballRadius;

    public SuperPoint(double x, double y){

       // xPos = app.xPosSuperPoint;
      // yPos = app.yPosSuperPoint;
        xPos = x;
        yPos = y;
    }

    public void drawSuperPoint(Graphics2D g2d,int panelSize, GamePanel pan){

        double ballSize=2*normBallRadius*panelSize;
        ballRadius = 2*Math.sqrt(ballSize/3.141);

        Ellipse2D ball2d = new Ellipse2D.Double(this.xPos*pan.getWidth(),this.yPos*pan.getHeight(),ballRadius,ballRadius);
        g2d.setColor(new Color(0, 221, 71));
        g2d.fillOval((int)ball2d.getX(),(int)ball2d.getY(),(int)ball2d.getWidth(),(int)ball2d.getHeight());
        g2d.setColor(new Color(0,0,0));
        float thickness = 3;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.draw(ball2d);
    }

}
