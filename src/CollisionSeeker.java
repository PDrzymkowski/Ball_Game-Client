import java.util.ArrayList;
/**
 * 
 * klasa  wyszukuj�ca kolizji, kt�re mog� nast�pi� przy ruchu kulka
 *
 */
public class CollisionSeeker {

	private ArrayList<CollisionObserverInterface> collisionObservers;

	private String actionName;
	private Boolean isSuperPower = false;

	private ArrayList<GameActionObserverInterface> gameObservers;


	/**
	 * konstruktor tworz�cy tablic� z obserwatorami zdarzeniami
	 */
	CollisionSeeker() {
		collisionObservers = new ArrayList<>();
		gameObservers = new ArrayList<>();


	}

	/**
	 * sprawdzanie czy nast�pi�a jaka� kolizja, informowanie obserwator�w o nich
	 *
	 * @param ball
	 * @param finish
	 * @param obstList
	 * @param superpoint
	 */
	public void check(Ball ball, FinishPoint finish, ArrayList<Obstacle> obstList, SuperPoint superpoint) {


		if (isCollision(ball, obstList)) {
			actionName = "COLLISION";
			notifyObserver();
		} else if (isOut(ball)) {
			actionName = "OUT";
			notifyObserver();
		} else if (isNextLevel(ball, finish)) {

			actionName = "NEXTLEVEL";
			notifyObserver();

		} else if (isSuperAbility(ball, superpoint)) {

			actionName = "SUPERABILITY";
			notifyObserver();
		}

		if (ball.invincibility) {
			isSuperPower = true;
		} else isSuperPower = false;
	}


	/**
	 * sprawdzanie czy nast�pi�a kolizja z przeszkod�
	 *
	 * @param ball
	 * @param obstList
	 * @return true- tak false- nie
	 */
	private Boolean isCollision(Ball ball, ArrayList<Obstacle> obstList) {
		for (int i = 0; i < (obstList.size()); i++) {
			if (((isCollisionUp(ball, obstList.get(i))) || (isCollisionDown(ball, obstList.get(i)))))
				return (true);
			else if ((isCollisionSide(ball, obstList.get(i))))
				return (true);
		}
		return (false);
	}

	/**
	 * sprawdzanie, czy kulka dotar�a do mety
	 *
	 * @param ball
	 * @param finish
	 * @return true- tak false- nie
	 */
	private Boolean isNextLevel(Ball ball, FinishPoint finish) {

		if (ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() <= (finish.xMiddle + finish.width / 2) / ball.gamePanel.getWidth()
				&& ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() >= (finish.xMiddle - finish.width / 2) / ball.gamePanel.getWidth()
				&& ball.yPos + 0.5 * ball.ballRadius / ball.gamePanel.getHeight() >= (finish.yMiddle - finish.height / 2) / ball.gamePanel.getHeight()
				&& ball.yPos + 0.5 * ball.ballRadius / ball.gamePanel.getHeight() <= (finish.yMiddle + finish.height / 2) / ball.gamePanel.getHeight()) {


			return (true);
		} else {
			return (false);
		}
	}

	/**
	 * sprawdzanie, czy kulka dotkn�a g�rnej cz�ci przeszkody
	 *
	 * @param ball
	 * @param obst
	 * @return
	 */
	private Boolean isCollisionUp(Ball ball, Obstacle obst) {
		if (ball.yPos <= obst.getY() + obst.getHeight()
				&& ball.yPos >= obst.getY()
				&& ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() <= obst.getX() + obst.getWidth()
				&& ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() >= obst.getX()) {
			return (true);
		} else {
			return (false);
		}
	}

	/**
	 * sprawdzanie, czy kulka dotkn�a dolnej cz�ci przeszkody
	 *
	 * @param ball
	 * @param obst
	 * @return
	 */
	private Boolean isCollisionDown(Ball ball, Obstacle obst) {
		if (ball.yPos + ball.ballRadius / ball.gamePanel.getHeight() >= obst.getY()
				&& ball.yPos + ball.ballRadius / ball.gamePanel.getHeight() <= obst.getY() + obst.getHeight()
				&& ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() >= obst.getX()
				&& ball.xPos + 0.5 * ball.ballRadius / ball.gamePanel.getWidth() <= obst.getX() + obst.getWidth()) {

			return (true);
		} else
			return (false);
	}


	/**
	 * sprawdzanie, czy kulka dotkn�a boku przeszkody
	 *
	 * @param ball
	 * @param obst
	 * @return
	 */
	private Boolean isCollisionSide(Ball ball, Obstacle obst) {
		if (ball.yPos + 0.5 * ball.ballRadius / ball.gamePanel.getHeight() >= obst.getY()
				&& ball.yPos + 0.5 * ball.ballRadius / ball.gamePanel.getHeight() <= obst.getY() + obst.getHeight()
				&& ball.xPos <= obst.getX() + obst.getWidth()
				&& ball.xPos + ball.ballRadius / ball.gamePanel.getWidth() >= obst.getX()) {

			return (true);
		} else
			return (false);
	}


	/**
	 * sprawdzanie, czy kulka kulka wypad�a poza okno gry
	 *
	 * @param ball
	 * @return
	 */
	private Boolean isOut(Ball ball) {

		if (ball.xPos < ball.normBallRadius) {

			return (true);
		} else if (ball.xPos > (1 - ball.ballRadius / ball.gamePanel.getWidth())) {

			return (true);
		} else if (ball.yPos < ( ball.normBallRadius +0.0001)) {
			return (true);
		} else if (ball.yPos > ( 1 - ball.ballRadius / ball.gamePanel.getHeight())) {


			return (true);
		} else return (false);
	}

	/**
	 * sprawdzanie, czy kulka zderzyla si� z punktem z chwilow� nie�miertelno�ci�
	 *
	 * @param ball
	 * @param superpoint
	 * @return
	 */
	private Boolean isSuperAbility(Ball ball, SuperPoint superpoint) {

		if (superpoint.yPos >= ball.yPos
				&& superpoint.yPos <= ball.yPos + ball.ballRadius / ball.gamePanel.getHeight()
				&& superpoint.xPos >= ball.xPos
				&& superpoint.xPos <= ball.xPos + ball.ballRadius / ball.gamePanel.getWidth()) {

			superpoint.xPos = 2;
			superpoint.yPos = 2;
			return true;
		} else
			return false;
	}

	/**
	 * dodawanie obserwatora kolizji
	 *
	 * @param observer
	 */
	public void addObserver(CollisionObserverInterface observer) {
		collisionObservers.add(observer);
	}

	public void addObserver(GameActionObserverInterface observer) {
		gameObservers.add(observer);
	}


	/**
	 * usuwanie obserwatora kolizji
	 *
	 * @param observer
	 */
	public void deleteObserver(CollisionObserverInterface observer) {
		collisionObservers.remove(collisionObservers.indexOf(observer));
	}

	public void deleteObserver(GameActionObserverInterface observer) {
		gameObservers.remove(gameObservers.indexOf(observer));
	}


	/**
	 * informowanie obserwatorow o kolizji
	 */
	public void notifyObserver() {
		CollisionAction actionEvent = new CollisionAction(actionName, isSuperPower);

		for (CollisionObserverInterface observer : collisionObservers) {
			observer.collisionActionPerformed(actionEvent);


		}
		for (GameActionObserverInterface observer : gameObservers) {

			if ((actionName.equals("OUT") || actionName.equals("COLLISION")) && !isSuperPower) {

				GameAction gameEvent = new GameAction("DEATH");
				observer.gameActionPerformed(gameEvent);

			} else {
				GameAction gameEvent = new GameAction(actionName);
				observer.gameActionPerformed(gameEvent);
			}

		}
	}
}


