package Setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JFrame;

/** Setup.GameButtonListener
 * @author Christine Zhao
 * The button action listeners
 */
public class GameButtonListener implements ActionListener {

  // class fields
  private GameManager game;
  private StartingFrameOOP settings;
  private JFrame mainFrame;

  /** Setup.GameButtonListener
   * Construct the jbutton action listener
   * @param settingsFrame The settings panel
   * @param mainFrame The main JFrame
   * @param game The game manager
   */
  public GameButtonListener(StartingFrameOOP settingsFrame, JFrame mainFrame, GameManager game){
    this.game = game;
    this.mainFrame = mainFrame;
    this.settings = settingsFrame;
  } // end Setup.GameButtonListener

  /** actionPerformed
   * Every time a button is pressed
   * @param event The action event
   */
  public void actionPerformed(ActionEvent event){
    String button = event.getActionCommand();
    
    // find which button was pressed and perform the correct action
    if (button.equals("Next")){
      game.nextButton();
    } else if (button.equals("Quit")){
      System.exit(0);
    }  else if (button.equals("Start")){
      System.out.println("Start");
      game.startButton();
    } else if (button.equals("Menu")){
      game.menuButton();
    } else if (button.equals("Change Settings")){
      System.out.println("settings");
      settings.setVisible(true);
      mainFrame.dispose();
    }  else if (button.equals("<HTML>Sort By Score</HTML>")){
      game.sortScore(1);
    }else if (button.equals("<HTML>Sort By Time</HTML>")){
      game.sortScore(3);
    }else if (button.equals("<HTML>Sort By % Error</HTML>")){
      game.sortScore(2);
    } else if (button.equals("Give Up")){
      game.setTimesUp();
    }
  } // end actionPerformed

} // end Setup.GameButtonListener
