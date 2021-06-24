
//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyListener implements KeyListener {

  //reference to items affected by keyboard press
  private GameManager game;

  /** PlayerKeyListener
   * Construct a key listener
   * @param g The game manager
   */
  public PlayerKeyListener(GameManager g) {
    game = g;
  }


  public void keyTyped(KeyEvent e) { 

  }

  /** keyPressed
   * Perform the corresponding action to the key press
   * @param e The key event
   */
  public void keyPressed(KeyEvent e) {
      int keyNum = e.getKeyCode();
      switch(keyNum){        
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
          game.playerMoveLeft();
          break;
        case  KeyEvent.VK_W:
        case  KeyEvent.VK_UP:
          game.playerMoveUp();
          break;
        case  KeyEvent.VK_S:  
        case  KeyEvent.VK_DOWN:
          game.playerMoveDown();
          break;
        case  KeyEvent.VK_D:    
        case  KeyEvent.VK_RIGHT:
          game.playerMoveRight();
          break;  
      }  
  } // end keyPressed

  /** keyReleased
   * Perform the corresponding action to the key release
   * @param e The key event
   */
  public void keyReleased(KeyEvent e) {

      int keyNum = e.getKeyCode();
      switch(keyNum){   
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
          game.playerStopMoveLeft();
          break;
        case  KeyEvent.VK_W:  
        case  KeyEvent.VK_UP:
          game.playerStopMoveUp();
          break;
        case  KeyEvent.VK_S:  
        case  KeyEvent.VK_DOWN:
          game.playerStopMoveDown();
          break;
        case  KeyEvent.VK_D:    
        case  KeyEvent.VK_RIGHT:
          game.playerStopMoveRight();
          break;
        case KeyEvent.VK_SPACE:
          game.spaceKey();
          break;
         case KeyEvent.VK_Q:
          game.returnItem();
          break;
        case KeyEvent.VK_R:
          game.setReverse();
          break;
        case KeyEvent.VK_E:
          game.setDrive();
          break;
      }
        
  } // end keyReleased
} //end of keyboard listener
  
