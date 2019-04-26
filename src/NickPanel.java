import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * panel pobierający informację o nazwie gracza
 */
public class NickPanel extends JPanel{


    private String nick;
    private JButton OKButton;
    private JTextField nickField;
    private MenuWindow menuwindow;
    private JLabel label;

    /**
     * kontruktor z parametrami początkowymi
     * @param width szerokość okna
     * @param height wysokość okna
     * @param menu odwołanie do menu
     */

    public NickPanel(int width, int height, MenuWindow menu){

        setPreferredSize(new Dimension(width,height));
        menuwindow = menu;

        createInterface();
        setVisible(true);

    }

    /**
     * rysowanie tła
     * @param g
     */
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        super.paintComponent(g);
        BufferedImage background = null;
        BufferedImage title = null;



        try{

            background = ImageIO.read(MenuWindow.class.getResource("images/background.jpg"));
            title = ImageIO.read(MenuWindow.class.getResource("images/letsPlay.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        g.drawImage(background,0,0,getWidth(),getHeight(), this);
        g.drawImage(title,(int)(getWidth()*0.37),(int)(getHeight()*0.35),(int)(getWidth()*0.25),(int)(getHeight()*0.3), this);

    }

    /**
     * tworzenie interfejsu pozwalającego na wprowadzanie danych
     */
    private void createInterface(){

        add(Box.createVerticalStrut(100));
        OKButton = new JButton("OK");
        OKButton.setAlignmentX(CENTER_ALIGNMENT);
        OKButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));

        nickField = new JTextField("New Player");
        nickField.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        nickField.setAlignmentX(CENTER_ALIGNMENT);
        nickField.setSize(new Dimension(70,20));

        label = new JLabel("                        Insert player's name...:                         ");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setForeground(Color.WHITE);

        OKButton.addActionListener((ActionEvent event)->{

           if(menuwindow.isSound){ menuwindow.loadClickSound();}
            nick = nickField.getText();
            setVisible(false);
            menuwindow.setGameWindow(nick);

        });

        setLayout(new FlowLayout());

        add(label);
        add(nickField);
        add(Box.createVerticalStrut(30));
        add(OKButton);

    }

    /**
     * informacje o nazwie gracza
     * @return
     */
    public String getNick(){

        return nick;
    }
}
