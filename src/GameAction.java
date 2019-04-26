/**
 * klasa opisująca zdarzenie zachodzące w grze
 */
public class GameAction {
	
	private String actionName;

	/**
	 * kontruktor przyjmujący parametr tekstowy
	 * @param action
	 */
	 public GameAction(String action)
	{
		this.actionName=action;
		
	}

	/**
	 * informacja o rodzaju zdarzaniu, które zaszło
	 * @return nazwa zdarzenia
	 */
	
	public String getActionName()
	{
		return(actionName);
	}
}
