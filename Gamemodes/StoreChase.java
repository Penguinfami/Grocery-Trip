package Gamemodes;

import Players.ChasePlayer;
import Players.StoreThief;
import Setup.GameTimer;
import Components.PictureMap;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Font;
import javax.imageio.ImageIO;

/** Gamemodes.StoreChase.java
 * @author Christine Zhao
 * A chasing game around the grocery store
 **/
public class StoreChase extends Minigame {

  // class fields
  private ChasePlayer player;
  private PictureMap map;
  private StoreThief thief;
  private boolean isFinished;
  private int gameHeight, gameWidth;
  private GameTimer thiefTimer;

  // the corner guide box
  private int guideBoxWidth;
  private int guideBoxHeight;
  private int guideBoxX;
  private int guideBoxY;

  private int gameDifficulty;
  private boolean walletStolen;
  private BufferedImage cashierImage;
  private BufferedImage controls;
  private Font font;

  /** Gamemodes.StoreChase
   * Create the store chase game
   * @param mapFile The picture map image file path
   * @param gameX The starting x coordinate
   * @param gameY The starting y coordinate
   * @param gameWidth The game screen width
   * @param gameHeight The game screen height
   * @param difficulty The game difficulty
   */
  public StoreChase(String mapFile, int gameX, int gameY,  int gameWidth, int gameHeight, int difficulty){
    this.gameDifficulty = difficulty;
    this.gameHeight = gameHeight;
    this.gameWidth = gameWidth;
    this.player = new ChasePlayer(gameWidth, gameHeight, 5,5);
    this.map = new PictureMap(mapFile,0,0,gameWidth * 3 / 2, gameHeight * 3 /2,  gameWidth, gameHeight, false);
    this.thief = new StoreThief(-100, gameHeight * 200/ 500, 180 * gameHeight / 500,300 * gameHeight / 500,map.getWidth() / 4 );
    this.thief.setSpeed(gameHeight * 550 / 500);
    this.isFinished = false;
    player.setX(0);
    player.setY(0);
    player.setSpeed(gameHeight * 150 / 500);
    guideBoxWidth = gameWidth / 6;
    guideBoxHeight =  guideBoxWidth * map.getHeight() / map.getWidth();
    guideBoxX = gameWidth * 5 / 6 - 20;
    guideBoxY = 0;
    this.walletStolen = false;
    this.font = new Font("Georgia", Font.BOLD, 15 + gameHeight/40);
    try {
      cashierImage = ImageIO.read(new File("grocery_images/cashierScreen.png"));
    } catch (Exception e){
      System.out.println("Error loading cashier image");
    }
    try {
      controls = ImageIO.read(new File("grocery_images/shopplayercontrols.png"));
    } catch (Exception e){
      System.out.println("Error loading chase controls image");
    }

  } // end Gamemodes.StoreChase


  /** draw
   * Draw the game
   * @param g The graphics object
   */
  public void draw(Graphics g){
    if (walletStolen) {
      map.draw(g);
      // draw the guide box
      g.setColor(Color.BLACK);
      g.drawRect(guideBoxX, guideBoxY, guideBoxWidth, guideBoxHeight);
      g.setColor(Color.RED);
      g.fillRect(guideBoxX + guideBoxWidth * (thief.getX() - map.getX()) / map.getWidth(), guideBoxY + guideBoxHeight * (thief.getY() - map.getY()) / map.getHeight(), 5, 5);
      g.setColor(Color.BLUE);
      g.fillRect(guideBoxX + guideBoxWidth * (player.getX() - map.getX()) / map.getWidth(), guideBoxY + guideBoxHeight * (player.getY() - map.getY()) / map.getHeight(), 5, 5);
      player.draw(g);
      g.setColor(Color.BLACK);
      g.drawRect(thief.getX(), thief.getY(), thief.getWidth(), thief.getHeight()); // the thief's collision box
      thief.draw(g);
      g.setColor(Color.RED);
      g.setFont(font);
      g.drawString("A thief stole your wallet. Catch them!", 50 * gameHeight / 500, 50 * gameHeight /500);
      g.drawImage(controls, 430 * gameHeight / 500, 350 * gameHeight / 500 , 200 * gameHeight / 500,150 * gameHeight / 500, null);
    }else {
      g.drawImage(cashierImage, 0,0, gameWidth, gameHeight,null);
      g.setColor(Color.BLUE);
      thief.draw(g);
    }
  } // end draw

  /** update
   * Update the game, player and thief positions
   * @param elapsedTime The elapsed time since previous update
   */
  public void update(double elapsedTime){
    if (walletStolen) {
      int xPosition = player.getDisplacementX(elapsedTime);
      int yPosition = player.getDisplacementY(elapsedTime);
      int xDisplacement = xPosition - player.getX();
      int yDisplacement = yPosition - player.getY();
      // if the player's movement is valid / won't collide with objects
      Rectangle futureArea = player.getFutureCollisionArea(elapsedTime);
      if (!isValidPosition(futureArea)) {     // if the player is about to move off the map
        player.setXDirection(0);
        player.setYDirection(0);
      } else {

        int xMapMovement;
        if (xDisplacement != 0) {
          // calculate the map x movements relative to the player's position
          if (xDisplacement > 0) {
            xMapMovement = (map.getWidth() - (gameWidth - map.getX())) * xDisplacement / (gameWidth - player.getWidth() - player.getX());
          } else {
            xMapMovement = (Math.abs(map.getX())) * xDisplacement / (player.getX());
          }
        } else {
          xMapMovement = 0;
        }

        int yMapMovement;
        if (yDisplacement != 0) {
          // calculate the map y movements relative to the player's position
          if (yDisplacement > 0) {
            yMapMovement = (map.getHeight() - (gameHeight - map.getY())) * yDisplacement / (gameHeight - player.getHeight() - player.getY());
          } else {
            yMapMovement = (Math.abs(map.getY())) * yDisplacement / (player.getY());
          }
        } else {
          yMapMovement = 0;
        }

        map.update(-xMapMovement, -yMapMovement);
        if (map.hasXMovement()) { // if the map moved horiztonally
          thief.setX(thief.getX() - xMapMovement);
        }


        if (map.hasYMovement()) { // if the map moved vertically

          thief.setY(thief.getY()- yMapMovement); // move the thief
        }

        player.update(elapsedTime);
      }

      if (player.getCollisionArea().intersects(thief.getCollisionArea())) { // if the player has caught the thief
        isFinished = true;
        thiefTimer.cancel();
        thiefTimer.purge();
      }

      thief.update(elapsedTime, map);

    } else {
      if (thief.getX() > gameWidth + 100){ // if the thief has run past the screen, start the chase
        walletStolen = true;
        thief = new StoreThief(-100, gameHeight * 200/ 500, 60 * gameHeight / 500,100 * gameHeight / 500,map.getWidth() / 4 );
        thief.setSpeed(gameHeight * 550 / 500);
        thiefTimer = new GameTimer(this, "thief running", 1000, 1300 - (gameDifficulty* 200));
        thief.setTargetX(500);
        thief.setTargetY(500);
      } else { // show the thief running at the cashier to steal the waller
        thief.setX(thief.getX() + (int)(4*gameHeight/500 * elapsedTime * 100));
      }
    }

  } // end update

  /** updateThiefTarget
   * Update the thief's movement target according to the player and the map position
   */
  public void updateThiefTarget(){
    thief.updateTarget(map, player);
  } // end updateThiefTarget

  /** isFinished
   * Check if the game is finished
   * @return True or false is the game is finished
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished

  /** isValidPosition
   * Check if the future position area is within the map/screen area
   * @param boxAfterMovement The area the check for
   * @return True or false if it is contained within the map
   */
  public boolean isValidPosition(Rectangle boxAfterMovement){
    if ((boxAfterMovement.x < 0 )|| (boxAfterMovement.y < 0) ||
            (boxAfterMovement.x + boxAfterMovement.width > gameWidth) || (boxAfterMovement.y + boxAfterMovement.height > gameHeight)){
      return false;
    }
    return true;
  } // end isValidPosition

  /** playerMoveUp
   * Move the player up
   */
  public void playerMoveUp(){ 
    player.moveUp();
  } // end playerMoveUp

  /** playerMoveDown
   * Move the player down
   */
  public void playerMoveDown(){
    player.moveDown();
  } // end playeMoveDown

  /** playerMoveLeft
   * Move the player left
   */
  public void playerMoveLeft(){
    player.moveLeft();
  } // end playerMoveLeft

  /** playerMoveRight
   * Move the player right
   */
  public void playerMoveRight(){
    player.moveRight();
  }

  /** playerStopMoveUp
   * Stop moving the player up
   */
  public void playerStopMoveUp(){
    player.stopMoveUp();
  }

  /** playerStopMoveDown
   * Stop moving the player down
   */
  public void playerStopMoveDown(){
    player.stopMoveDown();
  }

  /** playerStopMoveLeft
   * Stop moving the player left
   */
  public void playerStopMoveLeft(){
    player.stopMoveLeft();
  }

  /** playerStopMoveRight
   * Stop moving the player right
   */
  public void playerStopMoveRight(){
    player.stopMoveRight();
  }

}
