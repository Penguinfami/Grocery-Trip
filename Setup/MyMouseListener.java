package Setup;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/** Setup.MyMouseListener.java
 * @author Christine Zhao
 *  Keeps track of mouse actions
 */
public class MyMouseListener implements MouseListener {

  // class fields
  private GameManager game;

  /** Setup.MyMouseListener
   * Construct a mouse listener
   * @param game The game manager
   */
  public MyMouseListener(GameManager game){
    this.game = game;
  } // end MyMouselistener

  public void mouseClicked(MouseEvent e) {

  }

  /** mousePressed
   * The mouse pressed
   * @param e The event action
   */
  public void mousePressed(MouseEvent e) {

    game.mouseClick(e.getX(), e.getY());
  } // end mousePressed

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }
} //end of mouselistener


