import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;


/** PayAtCashier.java
 * @author Christine Zhao
 * A cash selecting game
 */
public class PayAtCashier extends Minigame {

  // class fields
  private SimpleLinkedList<Cash> cashList;
  private SimpleLinkedList<Integer> fallSpeed;
  private int x, y;
  private int width, height;
  private double currentBalance;
  private double balanceNeeded;
  private Font font = new Font("Arial", Font.BOLD, 30);
  private boolean isFinished;
  private boolean overBalanceMessage;
  private GameTimer timer;
  private int gameDifficulty;

  /** PayAtCashier
   * Construct the cashier game
   * @param total The total balance needed
   * @param x The screen starting x coordinate
   * @param y The screen y  starting coordinate
   * @param w The screen width
   * @param h The screen height
   * @param difficulty The game difficulty
   */
  public PayAtCashier(double total, int x, int y, int w, int h, int difficulty){
    this.balanceNeeded = total;
    this.gameDifficulty = difficulty;
    currentBalance = 0;
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
    isFinished = false;
    cashList = new SimpleLinkedList<Cash>();
    fallSpeed = new SimpleLinkedList<Integer>(); // each cash object has a different fall speed
    overBalanceMessage = false;
    setup();
  } // end PayAtCashier

  /** isFinished
   * Checks if the game is finished or not
   * @return True or false if it is finished
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished

  /** update
   * Update the cashier game
   * @param elapsedTime The elapsed time since previous update
   */
  public void update(double elapsedTime){
    int y;
    Cash currentCash;

    if (currentBalance == balanceNeeded){ // if the player has gone over the required balance
      isFinished = true;
    } else if (currentBalance > balanceNeeded){
      overBalanceMessage = true;
      timer = new GameTimer(this, "too much money", 3000);
      currentBalance = 0; // reset it to 0
    }
    for (int i = 0; i < cashList.size(); i++){
      currentCash = cashList.get(i);
      y = currentCash.getY();
      if (y > height){ // if it falls past the screen, delete it
        cashList.delete(i);
        fallSpeed.delete(i);
        cashList.add(generateCash()); // generate a new one to take its place
        fallSpeed.add(generateSpeed()); // generate a random speed
      } else {
        currentCash.setY((int)(y + fallSpeed.get(i) * elapsedTime *100));
      }
    }
    
  } // end update

  /** draw
   * Draw the cashier game
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.setColor(Color.CYAN);
    g.fillRect(x,y,width,height);
    for (int i = 0; i < cashList.size(); i++){
      cashList.get(i).draw(g);
    }
    g.setColor(Color.BLUE);
    g.setFont(font);
    g.drawString("Balanced owed: " + balanceNeeded, width - 400,50);
    g.drawString("Current balance: " + currentBalance, width - 400, 100 );
    if (overBalanceMessage){ // if the player has gotten over the balance, display the message
      g.drawString("That's too much money! Resetting to 0...", 40, height - 100);
    }
  } // end draw

  /** setup
   * Set up the cash objects
   */
  private void setup(){
    // start with 10 coins on the screen
    for (int i = 0; i < 10; i++){
      cashList.add(generateCash());
      fallSpeed.add(generateSpeed()); // generate a random speed
    }
  } // end setup

  /** generateCash
   * Generate a random cash object
   * @return The cash object
   */
  private Cash generateCash(){
    int random = (int) (Math.random() * 9 + 1);
    int x = (int)(Math.random() * (width - 10));
    int y = -200;
    switch (random){ // create new cash based on the random number generate
      case 1:
        return new Coin(0.01,x,y, height / 10, height / 10 );
      case 2:
        return new Coin(0.05,x,y, height / 9, height /  9 );
      case 3:
        return new Coin(0.1,x,y, height / 13, height /  13 );
      case 4:
        return new Coin(0.25,x,y, height / 8, height /  8 );
      case 5:
        return new Coin(1,x,y, height / 5, height /  5);
      case 6:
        return new Coin(2,x,y, height / 5, height /  5);
      case 7:
        return new Bill(5,x,y, height * 300/750 , height* 150 /750 );
      case 8:
        return new Bill(10,x,y, height* 300 /750 ,  height * 150/750 );
      default:
        return new Bill(20,x,y, height * 300/750 ,  height * 150  /750);

    }
  } // end generateCash

  /** selectCoin
   * Check if the mouseclick hovers over any of the coins and remove it from the screen
   * @param clickX The x coordinate of the mouseclick
   * @param clickY The y coordinate of the mouseclick
   * @return The cash that was selected
   */
  public Cash selectCoin(int clickX, int clickY){
    Cash currentCash;
    Rectangle rectangle;
    Circle circle;
    int radius;
    Point point = new Point(clickX, clickY);
    for (int i = cashList.size() - 1; i >= 0; i--){ // going in reverse order because it is the order they are last displayed
      currentCash = cashList.get(i);
      if (currentCash instanceof Bill){
        rectangle = new Rectangle(currentCash.getX(), currentCash.getY(), currentCash.getWidth(), currentCash.getHeight());
        if (rectangle.contains(point)){
          currentBalance+= currentCash.getValue();
          currentBalance = Math.round(currentBalance*100.0) / 100.0;
          cashList.delete(i);
          fallSpeed.delete(i);
          cashList.add(generateCash()); // generate a new one to take its place
          fallSpeed.add(generateSpeed()); // generate a random speed
          return currentCash; // only select 1 at a time
        }
      } else if (currentCash instanceof Coin){
        radius = currentCash.getWidth() / 2;
        circle = new Circle(currentCash.getX() + radius , currentCash.getY() + radius , radius ); // get the circle area
        if (circle.contains(point)){
          currentBalance+= currentCash.getValue();
          currentBalance = Math.round(currentBalance*100.0) / 100.0;
          cashList.delete(i);
          fallSpeed.delete(i);
          cashList.add(generateCash()); // generate a new one to take its place
          fallSpeed.add(generateSpeed()); // generate a random speed          
          return currentCash; // select only 1 at a time
        }
      }
    }
    return null;
  } // end selectCoin

  /** generateSpeed
   * Generate a random cash fall speed
   * @return The speed
   */
  private int generateSpeed(){
    return height * ((int)(Math.random() * 10) + gameDifficulty) / 500;
  } // end generateSpeed

  /** removeOverBalanceMessage
   * Remove the too much money message
   */
  public void removeOverBalanceMessage(){
    overBalanceMessage = false;
  } // end removeOverBalanceMessage
} // end PayAtCashier
