import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JFrame;

/** GameAreaPanelOOP.java
 * Christine Zhao
 * The game panel that displays all the minigames
 */
public class GameAreaPanelOOP extends JPanel {

  // class fields
  private JFrame parent;
  private Clock clock;
  private FrameRate frameRate;
  private GameManager interactionsPane;
  private Font fps = new Font("Arial", Font.BOLD, 30);
  private StartingFrameOOP settings;

  /** GameAreaPanelOOP
   * Construct the panel
   * @param parent The parent JFrame
   * @param settings The settings JPanel
   * @param width The screen width
   * @param height The screen height
   * @param difficulty The game's difficulty
   */
  public GameAreaPanelOOP(JFrame parent, StartingFrameOOP settings, int width, int height, int difficulty){
    this.parent = parent;
    this.settings = settings;
    this.setLayout(null);

    //initialize the game manager
    interactionsPane = new GameManager(parent, this, settings, width, height, difficulty);


    // time / frame rate
    clock = new Clock();
    frameRate = new FrameRate();

    //Listeners
    PlayerKeyListener keyListener = new PlayerKeyListener(interactionsPane);
    this.addKeyListener(keyListener);

    MyMouseListener mouseListener = new MyMouseListener(interactionsPane);
    this.addMouseListener(mouseListener);

    //JPanel Stuff
    this.setFocusable(true);
    this.requestFocusInWindow();
    

    //Start the game loop in a separate thread (allows simple frame rate control)
    Thread t = new Thread(new Runnable() {
      public void run() {
        animate();
      }
    }); //start the gameLoop 
    t.start();

  } // end GameAreaPanelOOP

  //The main gameloop - this is where the game state is updated
  /** animate
    * Update the clock and game
   */
  public void animate() {

    while(true){

      //update game content
      interactionsPane.update(clock.getElapsedTime()); 

      clock.update(); // frame rate
      frameRate.update();

      //delay
      try{
        Thread.sleep(10);
      } catch (Exception exc){
        System.out.println("Thread Error");
      }

      //repaint request
      this.revalidate();
      this.repaint();
    }
  } // end animate

  // paintComponent Runs everytime the screen gets refreshed
  
  /** paintComponent
    * Draw all the game components
   */ 
  public void paintComponent(Graphics g) {
    super.paintComponent(g); //required
    setDoubleBuffered(true);

    //screen is being refreshed - draw all objects
    interactionsPane.draw(g);
    frameRate.draw(g, fps, Color.BLUE, 0,30);


  } // end paintComponent

} // end GameAreaPanelOOP


