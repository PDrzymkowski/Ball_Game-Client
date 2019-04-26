import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * klasa opisująca panel, w którym uzytkownik zmienia parametry gry
 */
public class OptionsPanel extends JPanel{


	private ArrayList<GameActionObserverInterface> gameObservers;
     private String actionName;

    /**
     * kontruktor
     * @param width
     * @param height
     * @param action
     */
    public OptionsPanel(int width, int height, ActionListener action){
    	gameObservers = new ArrayList <GameActionObserverInterface>();
        setPreferredSize(new Dimension(width,height));
        JButton back = makeBackButton(action);
        add(back);
        addInterface();
        setVisible(true);
    }

    /**
     * rysowanie tła
     * @param g
     */
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        BufferedImage background = null;

        try{

            background = ImageIO.read(MenuWindow.class.getResource("images/background.jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }
        g.drawImage(background,0,0,getWidth(),getHeight(), this);

    }

    /**
     * tworzenie przycisku powrotu do menu
     * @param action
     * @return
     */
    private JButton makeBackButton(ActionListener action) {

        JButton backBtn = new JButton("BACK");
        backBtn.setAlignmentX(CENTER_ALIGNMENT);
        backBtn.setFocusable(false);
        backBtn.addActionListener(action);
        backBtn.setActionCommand("FROMOPTIONS");
        backBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));

        return backBtn;
    }

    /**
     * tworzenie intrefejsu pozwalającego na zmiany parametrów gry
     */
    private void addInterface(){

        add(Box.createVerticalStrut(50));
        JLabel sound= new JLabel("SOUND:");
        sound.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        sound.setAlignmentX(CENTER_ALIGNMENT);
        sound.setForeground(Color.WHITE);


        JLabel difficulty= new JLabel("DIFFICULTY:");
        difficulty.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        difficulty.setAlignmentX(CENTER_ALIGNMENT);
        difficulty.setForeground(Color.WHITE);


        JLabel color= new JLabel("BALL COLOR:");
        color.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        color.setAlignmentX(CENTER_ALIGNMENT);
        color.setForeground(Color.WHITE);

        JButton soundBtn = addSoundButton();
        JButton difficultyBtn = addDifficultyButton();
        JButton colorBtn = addColorButton();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(sound);
        add(soundBtn);
        add(Box.createVerticalStrut(30));
        add(difficulty);
        add(difficultyBtn);
        add(Box.createVerticalStrut(30));
        add(color);
        add(colorBtn);
    }

    /**
     * przycisk pozwalający na wyłączenie dźwięku w grze
     * @return
     */
    private JButton addSoundButton(){

        ImageIcon onSound = new ImageIcon("./src/resources/images/on.png");
        JButton sounds = new JButton(onSound);
        sounds.setAlignmentX(CENTER_ALIGNMENT);

        sounds.addActionListener((ActionEvent event)->{

          if(sounds.getIcon() == onSound)  {
              sounds.setIcon(new ImageIcon("./src/resources/images/off.png"));
              actionName = "SOUNDOFF";
          }
          else {
              sounds.setIcon(onSound);
              actionName = "SOUNDON";
          }
          notifyObserver();
        });

        return sounds;
    }

    /**
     * przysick pozwalający wybrać poziom trudności gry
     * @return
     */
    private JButton addDifficultyButton(){

        JButton difficulty = new JButton("EASY");
        difficulty.setForeground(Color.GREEN);
        difficulty.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        difficulty.setAlignmentX(CENTER_ALIGNMENT);

        difficulty.addActionListener((ActionEvent event)->{

            if(difficulty.getText() == "EASY")  {
                difficulty.setText("MEDIUM");
                difficulty.setForeground(Color.ORANGE);
                }

            else if(difficulty.getText() == "MEDIUM")  {
                difficulty.setText("HARD");
                difficulty.setForeground(Color.RED);
            }

            else {
                difficulty.setText("EASY");
                difficulty.setForeground(Color.GREEN);
            }

            actionName = difficulty.getText();
            notifyObserver();
        });

        return difficulty;
    }

    /**
     * informowanie o zmianach w opcjach
     */
    private void notifyObserver() {
    	 GameAction gameEvent = new GameAction(actionName);
    	 for(GameActionObserverInterface observer : gameObservers){
    	        observer.gameActionPerformed(gameEvent);

    	    }
	}

    /**
     * tworzenie przycisku pozwalającego na zmianę koloru kulki
     * @return
     */
	private JButton addColorButton(){

        JButton color = new JButton("RED");
        color.setForeground(Color.RED);
        color.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        color.setAlignmentX(CENTER_ALIGNMENT);

        color.addActionListener((ActionEvent event)->{

            if(color.getText() == "RED")  {
                color.setText("ORANGE");
                color.setForeground(Color.ORANGE);
            }

            else if(color.getText() == "ORANGE")  {
                color.setText("BLUE");
                color.setForeground(Color.BLUE);
            }

            else {
                color.setText("RED");
                color.setForeground(Color.RED);
            }

            actionName = color.getText();
            notifyObserver();
        });

        return color;
    }

    /**
     * usuwanie obserwatorów zdarzeń
     * @param observer
     */
	public void deleteObserver(GameActionObserverInterface observer) {

		gameObservers.remove(gameObservers.indexOf(observer));
	}

    /**
     * dodawanie nowych słuchaczy zdarzeń
     * @param observer
     */
	public void addObserver(GameActionObserverInterface observer) {

        gameObservers.add(observer);
	}
}
