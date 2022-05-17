package Setup;


import Gamemodes.*;
import Setup.GameButtonListener;
import Setup.GameTimer;
import Setup.StartingFrameOOP;

import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/** Setup.GameManager.java
 *@author  Christine ZHao
 * The game manager that manages all the minigames and the controls
 */
public class GameManager {

  // java swing components
  private JPanel gamePanel;
  private JButton startButton;
  private JButton menuButton;
  private JButton nextButton;
  private JButton quitButton;
  private JButton settingsButton;
  private JButton giveUpButton;
  private JButton sortTime, sortPercent, sortScore;
  private GameButtonListener gameListener;

  // game  dimensions
  private int screenHeight, screenWidth;

  //number of rounds
  private int currentDay;
  private String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
  private Font dayFont = new Font("Merriweather", Font.BOLD,24);
  private enum GameMode{
    SHOPPING,
    RESULTS_SCREEN,
    MAIN_MENU,
    CASHIER,
    STORE_CHASE,
    DRIVING,
    PARKING,
    TIMES_UP,  
    FINISHED  
  }
  private GameMode gameMode;
  
  // game results
  private ShoppingResultsCard resultsCard;
  private FinalResults finalResults;
  private BufferedImage menuImage;
  private double totalPercentError = 0;

  // the different minigames
  private GroceryShopping shoppingGame;
  private PayAtCashier cashierGame;
  private DrivingToStore drivingGame;
  private StoreChase storeChase;
  private ParkingCar parkingGame;

  // timer
  private GameTimer timer;
  private Font timeFont;
  private Font timesUpFont, timesUpFont2;
  private int timeLeft;
  private int totalTimeLeft;
  private BufferedImage timesUpImage;

  private int gameDifficulty;


  /** Setup.GameManager.java
   * Construct the game manager
   * @param mainFrame The main JFrame
   * @param gamePanel The main JPanel the game is displayed on
   * @param settings The settings JPanel
   * @param width The screen width
   * @param height The screen height
   * @param difficulty The game difficulty
   */
  public GameManager(JFrame mainFrame, JPanel gamePanel, StartingFrameOOP settings, int width, int height, int difficulty){
    this.screenWidth = width;
    this.screenHeight = height;
    this.gamePanel = gamePanel;
    this.gameMode = GameMode.MAIN_MENU; // start at the menu
    this.gameListener = new GameButtonListener(settings, mainFrame, this); // create the button listener
    createItems(); // create the swing items
    this.gameDifficulty = difficulty; 
    this.drivingGame = new DrivingToStore(screenWidth, screenHeight, gameDifficulty); // create the driving game
    this.shoppingGame = new GroceryShopping(screenWidth,screenHeight,screenHeight,screenHeight, daysOfWeek[currentDay], gameDifficulty); // create the shopping game
    this.parkingGame = new ParkingCar(screenWidth,screenHeight, gameDifficulty); // create the parking car game
    this.totalTimeLeft = 0;
    // create the fonts for times up according the screen size
    this.timeFont = new Font("Times New Roman", Font.BOLD,10 + height/50);
    this.timesUpFont = new Font("Times New Roman", Font.BOLD,40 + height / 50 );
    this.timesUpFont2 = new Font("Times New Roman", Font.BOLD,10 + height / 50 );

  } // end Setup.GameManager

  /** createItems
   * Create the different JButtons with properties and dimensions
   */
  public void createItems(){
    // create the start button
    startButton = new JButton ("Start");
    startButton.setBounds(screenWidth * 350 / 1000, screenHeight  * 500 / 750,screenWidth* 300 / 1000, screenHeight* 180 / 750 );
    startButton.addActionListener(gameListener);
    try{
      BufferedImage startImage = ImageIO.read(new File("grocery_images/StartButton1.png"));
      BufferedImage startImagePressed = ImageIO.read(new File("grocery_images/StartButtonPressed.png"));
      startButton.setIcon(new ImageIcon(startImage.getScaledInstance(screenWidth* 320 / 1000 , screenHeight* 200 / 750, Image.SCALE_SMOOTH)));
      startButton.setPressedIcon(new ImageIcon(startImagePressed.getScaledInstance(screenWidth* 320 / 1000 , screenHeight* 200 / 750, Image.SCALE_SMOOTH)));
    } catch (Exception e){
      System.out.println("Error loading startbutton image");
    }
    startButton.setFocusable(false);
    
    // create the menu button
    menuButton = new JButton ("Menu");
    menuButton.setBounds(screenWidth  * 650/ 1000, screenHeight  * 655 / 750,screenWidth* 100 / 1000 , screenHeight * 50/ 750 );
    menuButton.addActionListener(gameListener);
    menuButton.setFocusable(false);
    menuButton.setBackground(Color.decode("#51D9F5"));
    menuButton.setForeground(Color.decode("#04363E"));
    menuButton.setFont(new Font("Arial", Font.BOLD, 10 + screenHeight/ 70));
    
    //create the settings button
    settingsButton = new JButton ("Change Settings");
    settingsButton.setBounds(screenWidth  * 120/ 1000, screenHeight  * 655 / 750,screenWidth* 200 / 1000 , screenHeight * 50/ 750 );
    settingsButton.addActionListener(gameListener);
    settingsButton.setBackground(Color.decode("#FFC894"));
    settingsButton.setFocusable(false);
    
    //create the next button
    nextButton = new JButton("Next");
    nextButton.setBounds(screenWidth * 700 / 1000, screenHeight  * 615 / 750,screenWidth* 250 / 1000, screenHeight* 100 / 750 );
    nextButton.addActionListener(gameListener);
    nextButton.setFocusable(false);
    nextButton.setBackground(Color.decode("#0FE01A"));
    nextButton.setFont(new Font("Roboto", Font.BOLD, 40));
    
    //create the quit button
    quitButton = new JButton("Quit");
    quitButton.setFont(new Font("Arial", Font.BOLD, 10 + screenHeight/ 70));
    quitButton.addActionListener(gameListener);
    
    // add these buttons to the main menu game panel
    gamePanel.add(settingsButton);
    gamePanel.add(startButton);
    gamePanel.add(quitButton);
    
    // create the sort score buttons for the final results screen
    Font sortButtonFont = new Font("Times New Roman", Font.BOLD+Font.ITALIC, 5 + (screenHeight / 100));
    // sort by score
    sortScore = new JButton("<HTML>Sort By Score</HTML>");
    sortScore.setBounds((screenWidth * (450 + (0 * 100)))/ 1000, screenHeight  * 625 / 750,screenWidth* 165 / 1000, screenHeight* 50 / 750 );
    sortScore.addActionListener(gameListener);
    sortScore.setFocusable(false);
    sortScore.setBackground(Color.decode("#8ECFDE"));
    sortScore.setForeground(Color.decode("#04363E"));
    sortScore.setFont(sortButtonFont);
    // sort by percent error
    sortPercent = new JButton("<HTML>Sort By % Error</HTML>");
    sortPercent.setBounds((screenWidth * (450 + (1 * 170)))/ 1000, screenHeight  * 625 / 750,screenWidth* 165 / 1000, screenHeight* 50 / 750 );
    sortPercent.addActionListener(gameListener);
    sortPercent.setFocusable(false);
    sortPercent.setBackground(Color.decode("#8ECFDE"));
    sortPercent.setForeground(Color.decode("#04363E"));
    sortPercent.setFont(sortButtonFont);
    // sort by time left
    sortTime = new JButton("<HTML>Sort By Time</HTML>");
    sortTime.setBounds((screenWidth * (450 + 2 * 170))/ 1000, screenHeight  * 625 / 750,screenWidth* 165 / 1000, screenHeight* 50 / 750 );
    sortTime.addActionListener(gameListener);
    sortTime.setFocusable(false);
    sortTime.setBackground(Color.decode("#8ECFDE"));
    sortTime.setForeground(Color.decode("#04363E"));
    sortTime.setFont(sortButtonFont);

    //create the give up button
    giveUpButton = new JButton("Give Up");
    giveUpButton.setBackground(Color.LIGHT_GRAY);
    giveUpButton.setFont(new Font("Arial", Font.PLAIN, screenHeight / 70));
    giveUpButton.setBounds(screenWidth * 900 / 1000, screenHeight  * 710 / 750,screenWidth* 100 / 1000, screenHeight* 40 / 750 );
    giveUpButton.addActionListener(gameListener);
    giveUpButton.setFocusable(false);

    try{
      menuImage = ImageIO.read(new File("grocery_images/Supermarket.png")); // get the main menu background image
      timesUpImage = ImageIO.read(new File("grocery_images/closed.png")); // get the times up image
    } catch (Exception e){
      System.out.println("Error loading times up / menu image");
    }
  } // end createItems

  /** update
   * Update the game every frame
   * @param elapsedTime The elapsed time since the previous update
   */
  public void update(double elapsedTime){

    // if time has run out
    if ((timeLeft <= 0) && (gameMode != GameMode.TIMES_UP) && (gameMode != GameMode.FINISHED) && (gameMode != GameMode.MAIN_MENU)){
      setTimesUp();
    }
    
    // update whichever game/screen is currently displayed
    switch(gameMode){
      case DRIVING:
        if (!drivingGame.isFinished()){
          drivingGame.update(elapsedTime);
        } else {
          System.out.println("driving finished");
          parkingGame.reset();
          gameMode = GameMode.PARKING;
        }
        break;
      case PARKING:
        if (!parkingGame.isFinished()){
          parkingGame.update(elapsedTime);
        } else {
          System.out.println("parking finished");
          gameMode = GameMode.SHOPPING;
        }
        break;
      case SHOPPING:
        if (!shoppingGame.isFinished()) {
          shoppingGame.update(elapsedTime);
        } else{
          System.out.println("shopping finished");
          storeChase = new StoreChase("grocery_images/emptyparkinglot.png", 0,0, screenWidth, screenHeight,gameDifficulty); // create a new store chase game
          gameMode = GameMode.STORE_CHASE;
        }
        break;
      case CASHIER:
        if (cashierGame.isFinished()){
          System.out.println("cashier finished");
          checkResults();
          cashierGame = null;
        } else {
          storeChase = null;
          cashierGame.update(elapsedTime);
        }
        break;
      case STORE_CHASE:
        if (storeChase.isFinished()){
          System.out.println("chase finished");
          cashierGame = new PayAtCashier(shoppingGame.getShoppingCart().getPriceTotal(), 0,0, screenWidth, screenHeight, gameDifficulty); // create a new cashier game
         gameMode = GameMode.CASHIER;
        } else {
          storeChase.update(elapsedTime);
        }
        break;
    }
  } // end update

  /** updateTime
   * Count down the timer
   */
  public void updateTime(){
    timeLeft--;
  } // end updateTime

  /** draw
   *  Draw the game graphics
   * @param g The Graphics to draw all the objects
   */
  public void draw(Graphics g){
    // current game mode
    switch(gameMode){
      case DRIVING:
        drivingGame.draw(g);
        break;
      case PARKING:
        parkingGame.draw(g);
        break;
      case SHOPPING:
        shoppingGame.draw(g);
        break;
      case RESULTS_SCREEN:
        resultsCard.draw(g);
        break;
      case FINISHED:
        finalResults.draw(g);
        break;
      case CASHIER:
        cashierGame.draw(g);
        break;
      case STORE_CHASE:
        storeChase.draw(g);
        break;
      case TIMES_UP:
        // draw the times up screen
        g.setColor(Color.ORANGE);
        g.fillRect(0,0, screenWidth, screenHeight);
        g.drawImage(timesUpImage,0,0,screenWidth,screenHeight, null);
        g.setColor(Color.BLACK);
        g.setFont(timesUpFont);
        g.drawString("TIME'S UP", screenWidth * 380 / 666, 100);
        g.setFont(timesUpFont2);
        g.drawString("Proceeding to next day...", screenWidth * 390 / 666, 125);
        break;
      case MAIN_MENU:
        g.drawImage(menuImage, 0, 0, screenWidth, screenHeight, null);
        break;
    }
    // draw the timer
    if ((timeLeft > 0) && (gameMode != GameMode.MAIN_MENU) && (gameMode != GameMode.TIMES_UP) && (gameMode != GameMode.FINISHED)) {
      g.setColor(Color.BLACK);
      g.setFont(timeFont);
      g.drawString("Time left: " + timeLeft, 300 * screenHeight / 500, 710 * screenHeight / 750);
      g.setFont(dayFont);
      g.drawString("Day " + currentDay, screenWidth / 3,50 * screenHeight / 750); // draw the current day
    }
  } // end draw

  /** newDay
   * Start a new day from the beginning, if 5 days have already passed, move to the Final results screen
   */
  public void newDay(){
     if (currentDay >= 5){ // if 5 days have passed
       // set up the final results screen
       finalResults = new FinalResults(0, 0, screenWidth , screenHeight , totalTimeLeft, shoppingGame.getAllPastMeals(), totalPercentError, gameDifficulty);
       gameMode = GameMode.FINISHED;
       
       // add the Jbuttons to the panel
       quitButton.setBounds(screenHeight  * 50 / 500, screenHeight * 450 / 500, screenHeight * 100 / 500, screenHeight * 40 / 500);
       quitButton.setBackground(Color.decode("#51D9F5"));
       menuButton.setBounds(screenHeight * 170 / 500, screenHeight * 450 / 500, screenHeight * 100 / 500, screenHeight * 40 / 500);
       gamePanel.add(quitButton);
       gamePanel.add(menuButton);
       gamePanel.add(sortScore);
       gamePanel.add(sortTime);
       gamePanel.add(sortPercent);
       
     }  else { // if there are still more days left
       currentDay++; // next day
       gamePanel.add(giveUpButton); // add the give up button
       
       // reset the driving and shopping games
       drivingGame.reset(); 
       shoppingGame.setCurrentDay(daysOfWeek[currentDay - 1]);
       shoppingGame.newRound();
       
       // reset the time left
       timeLeft = 200;
       timer = new GameTimer(this,"time left", 0, 1000);
       
       gameMode = GameMode.DRIVING; // start back at the driving game

    }
  } // end newDay

  /** mouseClick
   * Mouse clicked
   * @param x The x coordinate
   * @param y The y coordinate
   */
  public void mouseClick(int x, int y){
    switch(gameMode){
      case CASHIER:
        cashierGame.selectCoin(x,y);
        break;
    }
  } // end mouseClick

  /** startButton
   *  Start a new day and reset the accumulative variables
   */
  public void startButton(){
    switch(gameMode){
      case MAIN_MENU:
        gamePanel.remove(startButton);
        gamePanel.remove(settingsButton);
        gamePanel.add(giveUpButton);
        currentDay = 0;
        totalPercentError = 0;
        totalTimeLeft = 0;
        shoppingGame.clear();
        newDay();
        break;
    }
  } // end startButton

  /** menuButton
   *  Set the game mode to MAIN_MENU and remove previous buttons
   */
  public void menuButton(){
    gamePanel.remove(menuButton);
    gamePanel.remove(quitButton);
    gamePanel.add(startButton);
    gamePanel.add(settingsButton);
    gamePanel.remove(sortScore);
    gamePanel.remove(sortTime);
    gamePanel.remove(sortPercent);
    gamePanel.remove(giveUpButton);

    gameMode = GameMode.MAIN_MENU;
  } // end menuGameButton

  /** nextButton
   * Remove the buttons and start a new day
   */
  public void nextButton(){
    switch(gameMode){
      case RESULTS_SCREEN:
        gamePanel.remove(nextButton);
        gamePanel.remove(quitButton);
        newDay();
        break;
    }
  } // end nextButton

  /** spaceKey
   * Check out item from an aisle
   */
  public void spaceKey(){
    switch(gameMode){
      case SHOPPING:
        shoppingGame.checkOutItem();
        break;
    }
  } // end spaceKey

  /** returnItem
   * Remove an item from the shopping cart
   */
  public void returnItem(){
    switch(gameMode){
      case SHOPPING:
        shoppingGame.returnItem();
        break;
    }
  } // end returnItem

  /** setTimesUp
   * Set game mode to times up screen for 5 seconds
   */
  public void setTimesUp(){
    gameMode = GameMode.TIMES_UP;
    totalPercentError += 100;
    timer.cancel();
    gamePanel.remove(giveUpButton);
    new GameTimer(this, "times up", 5000);
  } // end setTimesUp

  /** checkResults
   * Create and set up a shopping results screen
   */
  public void checkResults(){
    resultsCard = new ShoppingResultsCard(0, 0, screenWidth, screenHeight,
            shoppingGame.getShoppingCart(), shoppingGame.getRoundMealProducts()); // create a shopping results card

    totalPercentError+= resultsCard.getPercentError(); // add the percent error to the total

    // add the jbuttons
    quitButton.setBounds(screenWidth  * 50/ 1000, screenHeight  * 640 / 750,screenWidth* 170 / 1000 , screenHeight * 90/ 750 );
    quitButton.setBackground(Color.decode("#0FE01A")); // update the quit button
    gamePanel.add(nextButton);
    gamePanel.add(quitButton);
    quitButton.setVisible(true);
    nextButton.setVisible(true);
    gamePanel.remove(giveUpButton);
    
    // add to the total time left
    totalTimeLeft+= timeLeft;
    timer.cancel();
    
    gameMode = GameMode.RESULTS_SCREEN; // set the game mode to results screen
  } // end checkResults

  /** sortScore
   * Sort the score
   * @param attribute The parameter used to sort
   */
  public void sortScore(int attribute){
    finalResults.sortScore(attribute);
  } // end sortScore

  /** setDrive
   * Set car gear to drive
   */
  public void setDrive(){
    if (gameMode == GameMode.PARKING){
      parkingGame.driveCar();
    }
  } // end setDrive

  /** setReverse
   *  Set car gear to reverse
   */
  public void setReverse(){
    if (gameMode == GameMode.PARKING){
      parkingGame.reverseCar();
    }
  } // end setReverse

  // keyboard arrow movements

  /**
   * Move the player up
   */
  public void playerMoveUp(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerMoveUp();
        break;
      case STORE_CHASE:
        storeChase.playerMoveUp();
        break;
      case DRIVING:
        drivingGame.playerMoveUp();
        break;
      case PARKING:
        parkingGame.playerMoveUp();
        break;
    }
  } // end playerMoveUp

  /**
   * Move the player down
   */
  public void playerMoveDown(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerMoveDown();
        break;
      case STORE_CHASE:
        storeChase.playerMoveDown();
        break;
      case DRIVING:
        drivingGame.playerMoveDown();
        break; 
      case PARKING:
        parkingGame.playerMoveDown();
        break;   
    }
  } // end playerMoveDown

  /**
   * Move the player left
   */
  public void playerMoveLeft(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerMoveLeft();
        break;
      case STORE_CHASE:
        storeChase.playerMoveLeft();
        break;
      case DRIVING:
        drivingGame.playerMoveLeft();
        break;   
      case PARKING:
        parkingGame.playerMoveLeft();
        break;        
    }
  } // end palyerMoveLeft

  /**
   * Move the player right
   */
  public void playerMoveRight(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerMoveRight();
        break;
      case STORE_CHASE:
        storeChase.playerMoveRight();
        break;
      case DRIVING:
        drivingGame.playerMoveRight();
        break;        
      case PARKING:
        parkingGame.playerMoveRight();
        break;
    }
  } // end playerMoveRight

  /**
   * Stop the player from moving up
   */
  public void playerStopMoveUp(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerStopMoveUp();
        break;
      case STORE_CHASE:
        storeChase.playerStopMoveUp();
        break;
      case DRIVING:
        drivingGame.playerStopMoveUp();
        break;
      case PARKING:
        parkingGame.playerStopMoveUp();
        break;
    }
  } // end playerStopMoveUp

  /**
   * Stop the player from moving down
   */
  public void playerStopMoveDown(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerStopMoveDown();
        break;
      case STORE_CHASE:
        storeChase.playerStopMoveDown();
        break;
      case DRIVING:
        drivingGame.playerStopMoveDown();
        break; 
      case PARKING:
        parkingGame.playerStopMoveDown();
        break;        
        
    }
  } // end playerStopMoveDown

  /**
   * Stop the player from moving left
   */
  public void playerStopMoveLeft(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerStopMoveLeft();
        break;
      case STORE_CHASE:
        storeChase.playerStopMoveLeft();
        break;
       case DRIVING:
        drivingGame.playerStopMoveLeft();
        break;    
      case PARKING:
        parkingGame.playerStopMoveLeft();
        break;   
    }
  } // end playerStopMoveLeft

  /**
   * Stop the player from moving right
   */
  public void playerStopMoveRight(){
    switch (gameMode){
      case SHOPPING:
        shoppingGame.playerStopMoveRight();
        break;
      case STORE_CHASE:
        storeChase.playerStopMoveRight();
        break;
       case DRIVING:
        drivingGame.playerStopMoveRight();
        break;   
       case PARKING:
        parkingGame.playerStopMoveRight();
        break;  
    }
  } // end playerStopMoveRight

} // end Setup.GameManager
