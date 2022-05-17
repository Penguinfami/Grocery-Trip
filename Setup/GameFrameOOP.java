package Setup; /**
 * This template can be used as reference or a starting point
 * for your final summative project
 * @author Mangat
 **/

//Graphics &GUI imports
import javax.swing.JFrame;


class GameFrameOOP extends JFrame {

  //Game Screen
  static GameAreaPanelOOP gamePanel;
  static int gameScreenWidth;
  static int gameScreenHeight;
  static StartingFrameOOP settings;
  GameFrameOOP(StartingFrameOOP settings, int difficulty, int height) {
    super("My Game");
    this.settings = settings;
    gameScreenHeight = height;
    gameScreenWidth = height * 4 / 3;
    
    // Set the frame to full screen 
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(gameScreenWidth, gameScreenHeight + 30);
    this.setResizable(false);

    //Set up the game panel (where we put our graphics)
    gamePanel = new GameAreaPanelOOP(this, settings, gameScreenWidth, gameScreenHeight, difficulty);
    this.add(gamePanel);
    this.setFocusable(false);  //focus on the JPanel
    this.setVisible(true);
  }

}




