import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import java.awt.Rectangle;

/** ParkingCar
 * @author Christine Zhao
 * A parking car minigame
 */
public class ParkingCar extends Minigame{

  // class fields
  private CarPlayer carPlayer;
  private ShopPlayer shopPlayer;
  private SimpleLinkedList<Car> cars = new SimpleLinkedList<Car>();
  private TileMap map;
  private boolean isFinished;
  private boolean leftCar;
  private int gameWidth, gameHeight;
  public boolean isPulledOver;
  public GameTimer gameTimer;
  public GameTimer policeTimer;
  private int gameDifficulty;
  private int carWidth;
  private int carHeight;
  private String warningMessage;
  private BufferedImage pulledOverImage;
  private BufferedImage carControls,shopControls;
  private BufferedImage shopImage;
  private ParkingSpace currentHighlightedSpace;
  private boolean parkTimerOn;
  private Rectangle storeArea;

  /** ParkingCar
   * Construct the parking car game
   * @param width The screen width
   * @param height The screen height
   * @param difficulty The game difficulty
   */
  public ParkingCar(int width, int height, int difficulty){
    this.gameDifficulty = difficulty;
    this.gameWidth = width;
    this.gameHeight = height;
    this.isFinished = false;
    this.map = new TileMap("parkingLot.txt",  width * 19/20, height, 1, 1 );// create the parking lot
    
    this.carWidth =(int)( width * 19/20 / (map.getNumWidthTiles() + 2.5)); // get the car dimensions
    this.carHeight = height / (map.getNumHeightTiles() + 1);
    
    this.carPlayer = new CarPlayer(map.getStartingTile().x, map.getStartingTile().y, carWidth, carHeight, null,
            null, "grocery_images/playercar.png"); // create the car player
    this.carPlayer.setSpeed(0);
    this.carPlayer.setGasSpeed((height * (50 + 20 * difficulty) )/ 500);
    this.carPlayer.setTurnSpeed(50 + 40);
    this.carPlayer.setAnchorPoint(width / 2, height / 2);
    this.carPlayer.setMove(true);
    
    this.isPulledOver = false;
    this.leftCar = false;
    this.parkTimerOn = false;
    this.currentHighlightedSpace = null;

    this.shopPlayer = new ShopPlayer(map.getDisplayWidth(), map.getDisplayLength(), map.getNumWidthTiles(), map.getNumWidthTiles()); // create tge shopping cart player
    shopPlayer.setSpeed(150 * gameHeight / 500);
    shopPlayer.setMove(true); // the player moves
    
    this.storeArea = new Rectangle(((width * 19 / 20) / map.getNumWidthTiles()) * map.getWidth(), 0,gameWidth - ((width * 19 / 20) / map.getNumWidthTiles()) * map.getWidth(), height); // set the area of the store
    
    try{ // get the pulled over image
      pulledOverImage = ImageIO.read(new File("grocery_images/policescreen.png"));
    }catch (Exception e){
      System.out.println("Error loading pulled over image");
    }
    
    try{ // get the pulled over image
      shopImage = ImageIO.read(new File("grocery_images/sideStore.png"));
    }catch (Exception e){
      System.out.println("Error loading shop image");
    }
    try{
      carControls = ImageIO.read(new File("grocery_images/parkingcontrols.png"));
      shopControls = ImageIO.read(new File("grocery_images/shopplayercontrols.png"));
    } catch(Exception e){
      System.out.println("Error reading controls  image");
    }
  } // end ParkingCar

  /** draw
   * Draw the game
   * @param g The graphics object
   */
  public void draw(Graphics g){
    
    if (!isPulledOver){ // if the player isnt pulled over, draw the map, store and all the cars
      map.draw(g);
      g.setColor(Color.GREEN);

      g.drawImage(shopImage,storeArea.x,storeArea.y, storeArea.width, storeArea.height,null);
      for (int i = 0; i < cars.size(); i++){
        cars.get(i).draw(g);
      }
      carPlayer.draw(g);
      if (leftCar){ // if the player has succesfully parked, draw the shop player, and the appropriate controls image
        shopPlayer.draw(g);
        g.drawImage(shopControls, 390 * gameHeight / 500, 310 * gameHeight / 500 , 260 * gameHeight / 500,180 * gameHeight / 500, null);
      } else {
        g.drawImage(carControls, 390 * gameHeight / 500, 340 * gameHeight / 500 , 270 * gameHeight / 500,150 * gameHeight / 500, null);
      }
    } else { // otherwise, draw the pulled over screen
      g.drawImage(pulledOverImage,0,0, gameWidth, gameHeight, null);
      g.setColor(Color.BLACK);
      g.drawString("You have been pulled over for", 80, 100);
      g.setColor(Color.RED);
      g.drawString(warningMessage, 90, 150);
      g.setColor(Color.BLACK);
      g.drawString("Wait 5 seconds...", 80, 200);
    }
    carPlayer.draw(g); // draw the car player

  } // end draw

  /** update
   * Update the game
   * @param elapsedTime The elapsed time since previous update
   */
  public void update(double elapsedTime){

    checkIsParked();
    if (!isPulledOver) { // if the car is not pulled over
      if ((!leftCar) &&(carPlayer.getScreenX() < 0) ||( carPlayer.getScreenY() > gameHeight) || (carPlayer.getScreenY() < 0)){ // if the car has driven off the map
        warningMessage = "Driving off the road";
        isPulledOver = true;
        getPulledOver();
      } else if ((!leftCar) && carPlayer.intersects(storeArea)) { // if the car has driven into the store area
        warningMessage = "Crashing into the store";
        isPulledOver = true;
        getPulledOver();
      }else { 
        if (!leftCar) { // if the player is controlling the car
          for (int i = 0; i < cars.size(); i++) {// check if the player has collided with another car
            if (carPlayer.intersects(cars.get(i).getCollisionArea())) { // a collision with the car, pull over the player
              warningMessage = "Collision with another car";
              System.out.println("Collision");
              isPulledOver = true;
              getPulledOver();
            }
          }
        } else {  // player has left the car and is now controlling the shop player
         
          // otherwise, check if the shop player's movement is valid
          int xDirection = shopPlayer.getDisplacementX(elapsedTime);
          int yDirection = shopPlayer.getDisplacementY(elapsedTime);

          // if the player's movement is valid / won't collide with objects
          Rectangle currentArea = shopPlayer.getFutureCollisionArea();
          Rectangle futureArea = new Rectangle ( xDirection,  yDirection,currentArea.width, currentArea.height);

          shopPlayer.setMove(true);

          for (int i = 0; i < cars.size(); i++) { // if the player is about to collide with a car, stop the player from moving
            if ((futureArea.intersects(cars.get(i).getCollisionArea())) ||
            (futureArea.x < 0 )||(futureArea.y < 0) ||
                    (futureArea.y + shopPlayer.getHeight() > gameHeight) || (carPlayer.intersects(futureArea))) {
              shopPlayer.setMove(false);
              shopPlayer.setXDirection(0);
              shopPlayer.setYDirection(0);
            }
          }
          if (shopPlayer.getX() + shopPlayer.getWidth() - 20> gameWidth * 19 /20){ // if the player has entered the store ,edn the minigame
            isFinished = true;
            cars.clear();

          }
        }
      }


      if (!leftCar) {
        carPlayer.update(elapsedTime);
      } else {
        shopPlayer.update(elapsedTime);
      }
    }
  } // end update

  /** isFinished
   * Is the game finished or not
   * @return True or false if the game is finished
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished

  /** placeCars
   * Create and place the cars in the occupied parking space tiles
   */
  public void placeCars(){
    cars.clear();
    Tile currentTile;
    char[][] charMap = map.getCharMap();
    for (int i = 0; i < map.getNumHeightTiles();i++){
      for (int j = 0; j < map.getNumWidthTiles(); j++){
        currentTile = map.getTile(i,j);
        if (charMap[i][j] == 'P'){ // if the current tile is an occupied parknig space, generate a car inside at that position
          cars.add(new Car(currentTile.getXPosMap() + (int)(Math.random() * (currentTile.getWidth() - carWidth)),
                  currentTile.getYPosMap() +(int)(Math.random() * (currentTile.getHeight() - carHeight)), carWidth, carHeight, "grocery_images/cars.png" ));
        }
      }
    }
  } // end placeCars

  /** checkStayParked
   * Check if the car player is still fully inside a parking tile after a few seconds. If it is, set the shop player coordinates.
   */
  public void checkStayParked(){
    Tile parkedTile = map.getTile(0,0);
    if (!isFinished && parkTimerOn){ // if the park timer is still on, it means the player is still parked
      Tile currentTile;
      char[][] charMap = map.getCharMap();
      // find if the parking space the player is parked in
      for (int i = 0; i < map.getNumHeightTiles();i++) {
        for (int j = 0; j < map.getNumWidthTiles(); j++) {
          currentTile = map.getTile(i, j);
          if (charMap[i][j] == 'p') {
            if (carPlayer.isContainedInside(((ParkingSpace) currentTile).getParkingSpace())) {
              parkedTile = currentTile;
            }
          }
        }
      }
      boolean leaveFromTop = true;
      boolean leaveFromBottom = true;
      Rectangle leaveTopArea = new Rectangle(parkedTile.getXPosMap(), parkedTile.getYPosMap() - shopPlayer.getHeight(), shopPlayer.getWidth(), shopPlayer.getHeight());
      Rectangle leaveBottomArea = new Rectangle(parkedTile.getXPosMap(), parkedTile.getYPosMap() + parkedTile.getHeight(), shopPlayer.getWidth(), shopPlayer.getHeight());
     
      for (int i = 0; i < cars.size(); i++) { // check if leaving the car from above or below wont collidle with other cars
        if (leaveTopArea.intersects(cars.get(i).getCollisionArea())) {
          leaveFromTop = false;
        }
        if (leaveBottomArea.intersects(cars.get(i).getCollisionArea())){
          leaveFromBottom = false;
        }
      }
      // if leaving from the direction will be off the map
      if ((leaveTopArea.y < 0) || (leaveTopArea.y + leaveTopArea.height > gameHeight)){
        leaveFromTop = false;
      }
      if ((leaveBottomArea.y < 0) || (leaveBottomArea.y + leaveBottomArea.height > gameHeight)){
        leaveFromBottom = false;
      }
      
      // set the shop player's position to what's valid
      if (leaveFromTop){
        shopPlayer.setUpdateX(leaveTopArea.x);
        shopPlayer.setY(leaveTopArea.y);
      } else if (leaveFromBottom){
        shopPlayer.setUpdateX(leaveBottomArea.x);
        shopPlayer.setY(leaveBottomArea.y);
      }
      
      leftCar = true;
    }

  } // end checkStayParked

  /** checkIsParked
   * Check if the car player is still parked. If it is, highlight the parking tile
   * @return True or false if it is parked or not
   */
  public boolean checkIsParked(){
    Tile currentTile;
    char[][] charMap = map.getCharMap();
    
    // check if any empty parking space contains the car player
    for (int i = 0; i < map.getNumHeightTiles();i++){
      for (int j = 0; j < map.getNumWidthTiles(); j++){
        currentTile = map.getTile(i,j);
        if (charMap[i][j] == 'p'){ // if the tile is a parking space
          if (carPlayer.isContainedInside(((ParkingSpace)currentTile).getParkingSpace())) {
            if (currentHighlightedSpace == null){
              ((ParkingSpace) currentTile).highlight(); // highlight the space if the player has succesfully gotten inside
              currentHighlightedSpace = (ParkingSpace)currentTile;
             } else if (currentHighlightedSpace != currentTile) {
              currentHighlightedSpace.unHighlight();
              ((ParkingSpace) currentTile).highlight();
              currentHighlightedSpace = (ParkingSpace)currentTile;
            }
            if (!parkTimerOn) { // start a new timer if this is a new park
              gameTimer = new GameTimer ( this,"successful park", 3000);
              parkTimerOn = true;
            }
            return true;
          }
        }
      }
    }
    // if the player is not parked anymore
    if (currentHighlightedSpace != null){
      gameTimer.cancel(); // cancel the park timer and unhighlight the parking space
      parkTimerOn = false;
      currentHighlightedSpace.unHighlight();
      currentHighlightedSpace = null;
    }
    return false;
  } // end checkIsParked

  /** getPulledOver
   * Switch to pulled over screen for 5 seconds
   */
  public void getPulledOver(){
    isPulledOver = true;
    parkTimerOn = false;
    if (currentHighlightedSpace != null){
      currentHighlightedSpace.unHighlight();
      currentHighlightedSpace = null;
    }
    policeTimer = new GameTimer(this, "failed park", 5000);


  } // end getPulledOver


  /** endPulledOver
   * End the pulled over screen and move player back to starting point
   */
  public void endPulledOver(){
    carPlayer.setAngle(0);
    carPlayer.setScreenX(map.getStartingTile().x);
    carPlayer.setScreenY( map.getStartingTile().y);
    carPlayer.stopMoveUp();
    carPlayer.setDrive();
    carPlayer.stopTurnLeft();
    isPulledOver = false;
  } // end endPulledOver

  /** reset
   * Reset the game and car positions
   */
  public void reset(){
    isFinished = false;
    placeCars();
    if (currentHighlightedSpace != null){
      currentHighlightedSpace.unHighlight();
      currentHighlightedSpace = null;
    }
    carPlayer.stopMoveUp();
    carPlayer.stopTurnLeft();
    carPlayer.setScreenX(map.getStartingTile().x);
    carPlayer.setScreenY( map.getStartingTile().y);
    carPlayer.setAngle(0);
    carPlayer.setDrive();
    shopPlayer.setXDirection(0);
    shopPlayer.setYDirection(0);
    shopPlayer.setX(0);
    leftCar = false;
    parkTimerOn = false;
  } // end reset

  /** reverseCar
   * Set carplayer gear to reverse
   */
  public void reverseCar(){
    if (!leftCar) {
      carPlayer.setReverse();
    }
  } // end reverseCar

  /** driveCar
   * set car player gear to drive
   */
  public void driveCar(){
      if (!leftCar) {
        carPlayer.setDrive();
      }
  } // end driveCar

  /** playerMoveUp
   * Move the player up
   */
  public void playerMoveUp(){
    if (!leftCar) {
      carPlayer.moveUp();
    } else {
      shopPlayer.moveUp();
    }
  } // end playerMoveUp

  /** playerMoveDown
   * Move the player down
   */
  public void playerMoveDown(){
    if (!leftCar) {
      carPlayer.stopMoveUp();
    }else {
      shopPlayer.moveDown();
    }
  } // end playerMoveDown

  /** playerMoveLeft
   * Move the player left
   */
  public void playerMoveLeft(){
    if (!leftCar) {
      carPlayer.turnLeft();
    }else {
      shopPlayer.moveLeft();
    }
  } // end playerMoveLeft

  /** playerMoveRight
   * Move the player right
   */
  public void playerMoveRight(){
    if (!leftCar) {
      carPlayer.turnRight();
    }else {
      shopPlayer.moveRight();
    }
  } // end playerMoveRight

  /** playerStopMoveUp
   * Stop the player moving up
   */
  public void playerStopMoveUp(){
    if (leftCar) {
      shopPlayer.stopMoveUp();
      //System.out.println("stop move up");
    }
  } // end playerStopMoveUp

  /** playerStopMoveDown
   * Stop the player moving down
   */
  public void playerStopMoveDown(){
    if (!leftCar) {
      carPlayer.stopMoveDown();
    }else {
      shopPlayer.stopMoveDown();
    }
  } // end playerStopMoveDown

  /** playerStopMoveLeft
   * Stop the player moving left
   */
  public void playerStopMoveLeft(){
    if (!leftCar) {
      carPlayer.stopTurnLeft();
    }else {
      shopPlayer.stopMoveLeft();
    }
  } // end playerStopMoveLeft

  /** playerStopMoveRight
   * Stop the player moving right
   */
  public void playerStopMoveRight(){
    if (!leftCar) {
      carPlayer.stopTurnRight();
    }else {
      shopPlayer.stopMoveRight();
    }
  } // end playerStopMoveRight

} // end ParkingCar
