package Setup;

//Imports

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

/** Setup.StartingFrameOOP.java
 * @author Christine Zhao
 * The starting frame and panel for adjusting game settings
 */
public class StartingFrameOOP extends JFrame {

  // class fields
  private int gameHeight = 500; // height automatically set to 500
  private int difficulty = 2; // difficulty automatically set to medium

  /** Setup.StartingFrameOOP
   *  Construct the starting frame
   */
  StartingFrameOOP() {
    super("Start Screen");

    //configure the window
    this.setSize(400,700);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setResizable (false);
    this.setLayout(new BorderLayout());

    //Create a Panel for stuff
    JPanel menu = new JPanel();
    menu.setLayout(new GridLayout(10,1));

    //Create a start JButton for the centerPanel
    JButton startButton = new JButton("START");
    
    //Create labels for the centerPanel
    JLabel startLabel = new JLabel("<HTML><H1>Welcome to a fun Game!</H1></HTML>",JLabel.CENTER);
    //startLabel.setBounds(0, 400, 400, 50);
    JLabel difficultyLabel = new JLabel("<HTML><H2>Set Difficulty</H2></HTML>", JLabel.CENTER);
    //difficultyLabel.setBounds(0,150,400,50);
    JLabel heightLabel = new JLabel("<HTML><H2>Set Gamescreen Height</H2></HTML>", JLabel.CENTER);
    //heightLabel.setBounds(0,300,400,50);

    //Add label items to the menu
    menu.add(startLabel);

    // add the panels to the frame
    this.add(menu, BorderLayout.NORTH);
    this.setVisible(true);
    this.requestFocusInWindow();
    StartButtonListener buttonListener = new StartButtonListener(this);
    menu.add(difficultyLabel);
     final JSlider difficultySlider = new JSlider(JSlider.HORIZONTAL,1,3, 2);
    difficultySlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        difficulty =  (difficultySlider.getValue() );
      }
    });

    // Adjust the difficulty slider and add labels
    difficultySlider.setMinorTickSpacing(1);
    difficultySlider.setMajorTickSpacing(1);
    difficultySlider.setPaintTicks(true);
    difficultySlider.setPaintLabels(true);  
    Hashtable<Integer, JLabel> difficultyLabels = new Hashtable<Integer, JLabel>();
    difficultyLabels.put(1, new JLabel("Easy"));
    difficultyLabels.put(2, new JLabel("Medium"));
    difficultyLabels.put(3, new JLabel("Hard"));
    difficultySlider.setLabelTable(difficultyLabels);   
    menu.add(difficultySlider);
    
    final JSlider heightSlider = new JSlider(JSlider.HORIZONTAL,500, 900, 500);
    heightSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        gameHeight =  (heightSlider.getValue() );
      }
    });

    // Adjust the game height slider
    heightSlider.setMinorTickSpacing(10);
    heightSlider.setMajorTickSpacing(50);
    heightSlider.setPaintTicks(true);
    heightSlider.setPaintLabels(true);
    menu.add(heightLabel);
    menu.add(heightSlider);

    // set visible
    heightSlider.setVisible(true);
    difficultySlider.setVisible(true);
    menu.setVisible(true);
    menu.revalidate();
    startButton.addActionListener(buttonListener);

    
    this.add(startButton, BorderLayout.CENTER);
  }

  /** start
   * Start the game and create the main game JFrame
   */
  public void start(){
    this.setVisible(false);
    new GameFrameOOP(this, difficulty, gameHeight); //create a new frame after removing the current one
  }

  //Start the application
  public static void main(String[] args) {
    new StartingFrameOOP();
  }


} // end Setup.StartingFrameOOP
