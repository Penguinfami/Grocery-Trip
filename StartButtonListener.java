
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** StartButtonListener.java
 * @author Christine Zhao
 * Listens for the start button that starts the entire game
 */
public class StartButtonListener implements ActionListener{

  private StartingFrameOOP parentFrame;

  /** StartButtonListener
   * Construct a start button listner
   * @param parent The parent starting frame
   */
  public StartButtonListener(StartingFrameOOP parent) {
    this.parentFrame = parent;
  }

  /** actionPerformed
   * The start button pressed and start the game
   * @param event The action event
   */
  public void actionPerformed(ActionEvent event) {
    String button = event.getActionCommand();
    if (button.equals("START")){
      System.out.println("Starting new Game");
      parentFrame.start();
    }
  } // end actionPerformed
} // end StartButtonListener