/**
 * klasa zdarze� zwi�zanych ze zderzeniami na planszy

 */
public class CollisionAction {

	private String actionName;
	private Boolean superPower;
	CollisionAction(String action, Boolean power)
	{
		this.actionName=action;
		this.superPower = power;
	}
	
	/**
	 *informacja o polu tekstowym z nazwą zdarzenia
	 */
	public String getActionName()
	{
		return(actionName);
	}

	/**
	 * informacja czy w chwilii zajścia zdarzenia kulka posiadała dodatkową zdolność
	 * @return
	 */
}
