package Gamemodes;

import Components.Car;
import Components.EndlessPictureMap;
import Players.CarPlayer;
import Setup.GameTimer;
import Setup.SimpleLinkedList;
import Components.DrivingProgressBar;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/** Gamemodes.DrivingToStore.java
 * @author Christine ZHao
 * A driving game where one dodges other cars
 */
public class DrivingToStore extends Minigame {

  // class fields
  private CarPlayer player;
  private String warningMessage;
  private SimpleLinkedList<Car> cars = new SimpleLinkedList<Car>();
  private EndlessPictureMap map;
  private boolean isFinished;
  private int gameWidth, gameHeight;
  private int roadStartX, roadEndX;
  private int numRoads;
  private int roadWidth;
  private int playerCarX;
  private int mapStartingX;
  public boolean isPulledOver;
  public GameTimer gameTimer;
  private int mapSpeed;
  private int otherCarsSpeed;
  private DrivingProgressBar progressBar;
  private int gameDifficulty;
  private int carWidth;
  private int carHeight;
  private BufferedImage pulledOverImage;
  private BufferedImage controls;

  /** Gamemodes.DrivingToStore
   * Construct the game
   * @param width The screen width
   * @param height The screen height
   * @param difficulty The game's difficulty
   */
  public DrivingToStore(int width, int height, int difficulty){
    this.gameDifficulty = difficulty;
    this.gameWidth = width;
    this.gameHeight = height;
    this.carWidth = height * 50 / 500;
    this.carHeight = height * 100/500;
    this.isFinished = false;
    this.playerCarX = width /2 - (carWidth / 2);
    this.mapStartingX = -width / 2;
    this.player = new CarPlayer(playerCarX, height - 200, height * 50 / 500, height * 100 / 500, -90.0, 90.0, "grocery_images/playercar.png"); // create a car player
    this.map = new EndlessPictureMap("grocery_images/road.png", mapStartingX, 0, width *2, height * 2, width, height ); // create the road map
    this.roadStartX = 140 * (gameWidth*2) / 500 + mapStartingX; // set the starting curb
    this.roadEndX = 360 * (gameWidth * 2) / 500 + mapStartingX; // set the ending curb
    this.numRoads = 4; // there are 4 roads in total
    this.roadWidth = 55 * (gameWidth *2) / 500;
    this.mapSpeed = 0;
    this.otherCarsSpeed = height * 180 / 500; // set the speed of the other cars
    this.player.setSpeed(height * 180 / 500); // set the player speed
    this.player.setGasSpeed(height * 300 / 500); // set the speed when pressing the gas
    this.player.setMove(false); // The player does not move, instead the map does
    this.isPulledOver = false; 
    // create a progress bar outlining the car's distance travelled
    this.progressBar = new DrivingProgressBar(20 * gameWidth / 666,40 * gameHeight / 500, gameHeight * 30 / 500, gameHeight * 430 / 500, "grocery_images/minicar.png", gameHeight * 20);
    try{
      pulledOverImage = ImageIO.read(new File("grocery_images/policescreen.png"));
    }catch (Exception e){
      System.out.println("Error loading pulled over image");  
    }
    try{
      controls = ImageIO.read(new File("grocery_images/drivingcontrols.png"));
    } catch(Exception e){
      System.out.println("Error reading controls image");
    }
  } // end Gamemodes.DrivingToStore

  /** draw
   * Draw the game
   * @param g The graphics object
   */
  public void draw(Graphics g){
    if (!isPulledOver){ // if the car hasnt been pulled over
      map.draw(g); // draw  the map
      for (int i = 0; i < cars.size(); i++){
        cars.get(i).draw(g); // draw the other cars
      }
      player.draw(g); // draw the player
      g.drawImage(controls, 430 * gameHeight / 500, 350 * gameHeight / 500 , 200 * gameHeight / 500,150 * gameHeight / 500, null); // draw the controls
      progressBar.draw(g); // draw the progress bar
    } else { // draw the pulled over screen
      g.drawImage(pulledOverImage,0,0, gameWidth, gameHeight, null);
      g.setColor(Color.BLACK);
      g.drawString("You have been pulled over for", 80, 100);
      g.setColor(Color.RED);
      g.drawString(warningMessage, 90, 150);
      g.setColor(Color.BLACK);
      g.drawString("Wait 5 seconds...", 80, 200);
    }

  } // end draw

  /** generateCar
   * Create a new car on the road
   */
  private void generateCar(){
    int randomX;
    boolean isEmptyRoad;
    do{
      randomX = (int)(Math.random() * numRoads) * roadWidth + roadStartX + (int)(Math.random() * (roadWidth - carWidth)); // generate a random x position
      isEmptyRoad  = true;
      // check if the car will occupy a space without hitting other cars
      for (int i = 0; i < cars.size(); i ++){
        if ((cars.get(i).getCollisionArea().intersects(new Rectangle(randomX, -200, carWidth + 20,carHeight))) || (player.getCollisionArea().intersects(new Rectangle(randomX, -200, carWidth,carHeight)))){
          isEmptyRoad = false;
        }  
      }
    }   while (!isEmptyRoad); 
    Car newCar = new Car(randomX , -200, carWidth, carHeight, "grocery_images/cars.png");
    cars.add(newCar); // add the car to the list
  } // end generateCar

  /** generateCar
   * Create a new car on the road
   * @param x The car's x position
   * @param y The car's y position
   */
  private void generateCar(int x, int y){
    Car newCar = new Car(x, y, carWidth, carHeight, "grocery_images/cars.png");
    if (!player.getCollisionArea().intersects(newCar.getCollisionArea())){      
      cars.add(newCar); // add the car to the list
    }
  }  // end generateCar

  /** generateCar
   * Create a new car on the road
   * @param y The car's y position
   */
  private void generateCar( int y){
    int randomX;
    boolean isEmptyRoad;
    int numTries = 1;
    do{
      if (numTries % 20== 0){ // if after 20 tries, can't find a valid positon
        y -= 100 ; // decrease y by 100
      }
      randomX = (int)(Math.random() * numRoads) * roadWidth + roadStartX + (int)(Math.random() * (roadWidth - carWidth));
      isEmptyRoad  = true;
        // check if the car will occupy a space without hitting other cars
      for (int i = 0; i < cars.size(); i ++){ 
        if ((cars.get(i).getCollisionArea().intersects(new Rectangle(randomX - 20, y - 20, carWidth + 40,carHeight + 40))) || (player.intersects(new Rectangle(randomX - 20, y - 20, carWidth + 40,carHeight + 40)))){
          isEmptyRoad = false;
          numTries++;
        }
      } 

    }   while (!isEmptyRoad); 
    Car newCar = new Car(randomX, y, carWidth, carHeight, "grocery_images/cars.png");
    cars.add(newCar); // add the new car to the list
  } // end generateCar

  /** isFinished
   * Is the car finished
   * @return True or false
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished

  /** update
   * Update the game
   * @param elapsedTime The elapsed time since previous update
   */
  public void update(double elapsedTime){
    int xDisplacement = player.getDisplacementX(elapsedTime);
    int yDisplacement = player.getDisplacementY(elapsedTime);
    
    if (progressBar.isFinished()){ // the player has reached the end
      isFinished = true;
      cars.clear();
      
    }  else {
      
      if (!isPulledOver) {
        // is the car on the road
        if ((playerCarX < roadStartX) || (playerCarX + player.getWidth() > roadEndX)) { // if the player has driven past the road curbs, get pulled over
          warningMessage = "Driving off the road";
          System.out.println("Off the road");
          getPulledOver();
        } else {
          
          for (int i = 0; i < cars.size(); i++) {
        
            if (player.intersects(cars.get(i).getCollisionArea())){ // the player has collided with another car
              warningMessage = "Collision with another car";
              cars.delete(i); // remove that car from the road and generate a new one
              generateCar((int)(Math.random() * 1200 - 600));
              isPulledOver = true;
             getPulledOver();  
            }
           
          }
          map.update(-xDisplacement , (yDisplacement + mapSpeed)); // update the road map
          roadStartX-= xDisplacement; // update the curb
          roadEndX -= xDisplacement; // update the curb
          
          
          for (int i = 0; i < cars.size(); i++) {
            // update the car's position based on the map and player's movement
            cars.get(i).update(-xDisplacement, (yDisplacement) - (int)(otherCarsSpeed * elapsedTime));
            // if a car is way off the map, remove it and generate a new one
            if (cars.get(i).getY() > gameHeight + 600){ 
              generateCar(gameHeight + 600 - cars.get(i).getY() - 500);
              cars.delete(i);
            }  else if (cars.get(i).getY() < -600){
              generateCar(-600 - cars.get(i).getY() + gameHeight + 500);
              cars.delete(i);
            }  
          }
          
        }
        progressBar.update(yDisplacement); // update progress bar with the player's displacement
      }
      player.update(elapsedTime); // update the player
    }
  } // end update

  /** reset
   * Reset the driving game
   */
  public void reset(){
    cars.clear(); // remove all the cars
    for (int i = 0; i < 8 + (500 / (gameHeight / (6 + gameDifficulty))) *2; i++){ // generate the first cars
      generateCar(i  % 4 * roadWidth + roadStartX + (int)(Math.random() * (roadWidth - carWidth)), (i - 8) * gameHeight / (6 + gameDifficulty)  );
    }  
    resetRoad();
    isFinished = false;
    progressBar.reset();
  } // end reset

  /** getPulledOver
   * Stop the game for 5 seconds to pull over the player
   */
  public void getPulledOver(){
    isPulledOver = true;
    gameTimer = new GameTimer(this, "pulled over", 5000);
  } // end getPulledOver

  /** endPulledOver
   * End the pulled over screen
   */
  public void endPulledOver(){
    resetRoad();
    isPulledOver = false;
  } // end endPulledOver

  /** resetRoad
   * Reset the road to its original position and re-place the player's car
   */
  public void resetRoad(){
    int newRoadStartX = 140 * (gameWidth*2) / 500 + mapStartingX;
    int newEndStartX = 360 * (gameWidth * 2) / 500 + mapStartingX;
    Car currentCar;
    // reset the player's angle and position
    player.setAngle(0);
    player.stopMoveUp();// if the player's y direction is not 0, set it to 0
    player.stopTurnLeft();
    int size = cars.size();
    // set the car's to their original positions according the curb positions
    for (int i = 0; i < size ;i++){
      currentCar = cars.get(i);
      currentCar.setX(currentCar.getX() + (newRoadStartX - roadStartX));
      if (player.intersects(currentCar.getCollisionArea())){ // if the car intersects with the player, move it
        currentCar.setY(gameHeight + 1000);
      }  
    }
    this.roadStartX = newRoadStartX;
    this.roadEndX = newEndStartX;
    map.setX(mapStartingX);
  } // end resetRoad

  /** playerMoveUp
   * Move the player up the road
   */
  public void playerMoveUp(){
    player.moveUp();
  } // end playerMoveUp

  /** playerMoveUp
   * Move the player down the road
   */
  public void playerMoveDown(){
    player.moveDown();
  } // end playerMoveDown

  /** playerMoveLeft
   * Turn the player's car left
   */
  public void playerMoveLeft(){
    player.turnLeft();
  } // end playerMoveLeft

  /** playerMoveRight
   * Turn the player's car right
   */
  public void playerMoveRight(){
    player.turnRight();
  } // end playerMoveRight

  /** playerStopMoveUp
   * Stop the player moving up the road
   */
  public void playerStopMoveUp(){
    player.stopMoveUp();
  } // end playerStopMoveUp

  /** playerStopMoveDown
   * Stop the player moving down the road
   */
  public void playerStopMoveDown(){
    player.stopMoveDown();
  } // end playerStopMoveDown

  /** playerStopMoveLeft
   * Stop the player turning left
   */
  public void playerStopMoveLeft(){
    player.stopTurnLeft();
  }  // end playerStopMoveLeft

  /** playerStopMoveRight
   * Stop the player turning right
   */
  public void playerStopMoveRight(){
    player.stopTurnRight();
  } // end playerStopMoveRight
} // end Gamemodes.DrivingToStore
