import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * klasa odpowiedzialna za start gry
 */
public class AppStart {

	protected int frameWidth;
	protected int frameHeight;
	private String xmlFileSet = "./src/resources/settings.xml";
	protected ArrayList<Color> colorList;

	public MenuWindow mWindow;
	int maxLvl;

	/**
	 * konstruktor klasy uruchamiający okno startowe
	 */
	public AppStart() {
		this.getSettings();
		
		mWindow = new MenuWindow(this);
		mWindow.setVisible(true);
		
	}
	/**
	 * Pobranie ustawien ogolnych gry
	 */

	private void getSettings() {

		try {
			File xmlSetFile = new File(xmlFileSet);
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlSetFile);
			doc.getDocumentElement().normalize();
			frameWidth = Integer.parseInt(doc.getElementsByTagName("frameWidth").item(0).getTextContent());
			frameHeight = Integer.parseInt(doc.getElementsByTagName("frameHeight").item(0).getTextContent());
			maxLvl = Integer.parseInt(doc.getElementsByTagName("lvls").item(0).getTextContent());
			NodeList nodeList = doc.getElementsByTagName("Color");


			for (int p = 0; p < nodeList.getLength(); p++) {
				Node tmpNode = nodeList.item(p);

				if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) tmpNode;

					int r = Integer.parseInt(element.getElementsByTagName("xPos").item(0).getTextContent());
					int g = Integer.parseInt(element.getElementsByTagName("yPos").item(0).getTextContent());
					int b = Integer.parseInt(element.getElementsByTagName("hight").item(0).getTextContent());

					Color tmpobj = new Color(r, g, b);
					colorList.add(tmpobj);
				}
			}
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}