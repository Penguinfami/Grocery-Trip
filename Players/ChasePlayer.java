package Players;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/** Players.ChasePlayer.java
 * @author Christine Zhao
 * The player for the store chase
 */
public class ChasePlayer  extends Player {

  // class fields
  private int xdirection,ydirection;
  private BufferedImage[][] sprites;
  private int[][] spriteHeights;
  private int[][] spriteWidths;
  private int currentSpriteRow;
  private int currentSpriteCol;
  private int updateSpriteRow, updateSpriteCol;
  private int speed;

  /** Players.ChasePlayer
   * Construct a chase player
   * @param screenWidth The width of the screen
   * @param screenHeight The height of the screen
   * @param compareWidth The width to base the player's width on
   * @param compareHeight The height to base the player's height on
   */
  public ChasePlayer(int screenWidth, int screenHeight, int compareWidth, int compareHeight) {
    super(0,300,80,50);
    loadSprite(screenWidth,screenHeight, compareWidth, compareHeight);
    this.xdirection=0;
    this.ydirection=0;
    this.speed = 50;
  } // end Players.ChasePlayer

  /** loadSprite
   * Load the different player sprites
   * @param screenWidth The screen's width
   * @param screenHeight The screen's height
   * @param compareWidth The width to base the player's width on
   * @param compareHeight The height to base the player's height on
   */
  public void loadSprite(int screenWidth, int screenHeight, int compareWidth, int compareHeight) {
    int rows = 4; // the number of sprite rows
    int cols = 8; // the number of sprite columns
    sprites = new BufferedImage[rows][cols]; // create the array
    spriteWidths = new int[rows][cols]; // The array of the sprite widths
    spriteHeights = new int[rows][cols]; // The array of the sprite widths
    
    // Get the spritesheets for all the directions
    try {
      BufferedImage spriteSheet = ImageIO.read(new File("grocery_images/horizontalWalkRight.png"));

        for (int j = 0; j < 8; j++){
          sprites[0][j] = spriteSheet.getSubimage(j * 70, 0 * 205, 70, 205);
          spriteWidths[0][j] = (int)(screenWidth/750.0 * 53 / compareWidth * 5);
          spriteHeights[0][j] = (int)(screenHeight/ 750.0 * 100 / compareHeight * 5);
        }

      spriteSheet = ImageIO.read(new File("grocery_images/horizontalWalkLeft.png"));
        for (int j = 0; j < 8; j++){
          sprites[1][j] = spriteSheet.getSubimage(j * 70, 0 * 205, 70, 205);
          spriteWidths[1][j] = (int)(screenWidth/750.0 * 53 / compareWidth * 5);
          spriteHeights[1][j] = (int)(screenHeight/ 750.0 * 100 / compareHeight * 5);
        }

      spriteSheet = ImageIO.read(new File("grocery_images/verticalWalkDown.png"));
        for (int j = 0; j < 8; j++){
          sprites[2][j] = spriteSheet.getSubimage(j * 70, 0 * 205, 70, 205);
          spriteWidths[2][j] = (int)(screenWidth/750.0 * 53 / compareWidth * 5);
          spriteHeights[2][j] = (int)(screenHeight/ 750.0 * 100 / compareHeight * 5);
        }

      spriteSheet = ImageIO.read(new File("grocery_images/verticalWalkUp.png"));
        for (int j = 0; j < 8; j++){
          sprites[3][j] = spriteSheet.getSubimage(j * 70, 0 * 205, 70, 205);
          spriteWidths[3][j] = (int)(screenWidth/750.0 * 53 / compareWidth * 5);
          spriteHeights[3][j] = (int)(screenHeight/ 750.0 * 100 / compareHeight * 5);
        }

    } catch(Exception e) {
      System.out.println(e);
      System.out.println("error loading horizontal sprite");
    }

    // Start walking right
    currentSpriteRow = 0;
    currentSpriteCol = 0;
    updateSpriteRow = 0;
    updateSpriteCol = 0;
  } // end loadSprite

  /** draw
   * Draw the player
   * @param g The graphics object
   */
  public void draw(Graphics g) {
    g.drawImage(sprites[currentSpriteRow][currentSpriteCol],getX(), getY(), getWidth()
            , getHeight(),null); // draw the player image
    g.setColor(Color.BLACK);
    Rectangle box = getCollisionArea();
    g.drawRect(box.x, box.y,box.width, box.height); // draw the collision area rectangle
  } // end draw

  /** update
   * Update the player's position
   * @param elapsedTime The time since previous update
   */
  public void update(double elapsedTime) {
    currentSpriteRow = updateSpriteRow; // update the sprite row

    currentSpriteCol = updateSpriteCol; // update the sprite column


    setX((int) (getX() + (Math.sqrt(Math.pow((elapsedTime* speed), 2) - Math.pow(this.ydirection*elapsedTime* speed, 2)) * xdirection))); // set the new x and y positions according the directions 
    setY((int) (getY() +(Math.sqrt(Math.pow((elapsedTime* speed), 2) - Math.pow(this.xdirection*elapsedTime* speed, 2)) * ydirection)));
    
   // update the width/height
    setWidth(spriteWidths[currentSpriteRow][currentSpriteCol]);
    setHeight(spriteHeights[currentSpriteRow][currentSpriteCol]);
  } // end update

  /** getDisplacementX
   * Get the x displacement
   * @param elapsedTime The time since previous update
   * @return The displacement
   */
  public int getDisplacementX(double elapsedTime){
    return (int)(getX()+ (Math.sqrt(Math.pow((elapsedTime* speed), 2) - Math.pow(this.ydirection*elapsedTime* speed, 2))* xdirection)) ;
  } // end getDisplacementX

  /** getDisplacementY
   * Get the y displacement
   * @param elapsedTime The time since previous update
   * @return The displacement
   */
  public int getDisplacementY(double elapsedTime){
    return (int)(getY()+ (Math.sqrt(Math.pow((elapsedTime* speed), 2) - Math.pow(this.xdirection*elapsedTime* speed, 2))* ydirection)) ;
  } // end getDisplacementY

  /** setXDirection
   * Set the horizontal direction
   * @param n The positive/negative integer
   */
  public void setXDirection(int n){
    xdirection = n;
  } // end setXDirection

  /** setYDirection
   * Set the vertical direction
   * @param n The positive/negative integer
   */
  public void setYDirection(int n){
    ydirection = n;
  } // end setYDirection

  /** moveLeft
   * Move the player left
   */
  public void moveLeft(){
    updateSpriteRow = 1;
    xdirection= -1;
    if (updateSpriteCol != 7){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    }
  } // end moveLeft

  /** moveRight
   * Move the player right
   */
  public void moveRight(){
    updateSpriteRow = 0;
    xdirection= 1;
    // update the sprite
    if (updateSpriteCol != 7){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    }
  } // end moveRight

  /** moveUp
   * Move the player up
   */
  public void moveUp(){
    updateSpriteRow = 3;
    ydirection= -1;
    // update the sprite
    if (updateSpriteCol != 7){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    }    
  }  // end moveUp

  /** moveDown
   * Move the player down
   */
  public void moveDown(){
    updateSpriteRow = 2;
    ydirection= 1;
    // update the sprite
    if (updateSpriteCol != 7){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    }    
  } // end moveDown


  /** stopMoveLeft
   * Stop the player moving left
   */
  public void stopMoveLeft(){
    xdirection = 0;
  } // end stopMoveLeft

  /** stopMoveRight
   * Stop the player moving Right
   */
  public void stopMoveRight(){
    xdirection= 0;
  } // end stopMoveRight

  /** stopMoveUp
   * Stop the player moving up
   */
  public void stopMoveUp(){
    ydirection=0;
  } // end stopMoveUp

  /** stopMoveDown
   * Stop the player moving down
   */
  public void stopMoveDown(){
    ydirection= 0;
  } // end stopMoveDown

  /** getCollisionArea
   * Get the player's current collision area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return new Rectangle(getX(),getY(),spriteWidths[currentSpriteRow][currentSpriteCol],
            spriteHeights[currentSpriteRow][currentSpriteCol]);
  } // end getCollisionArea

  /** getFutureCollisionArea
   * Get the player's collision area after the next update
   * @param elapsedTime The time since previous update
   * @return The collision area
   */
  public Rectangle getFutureCollisionArea(double elapsedTime){
    return new Rectangle(getDisplacementX(elapsedTime),getDisplacementY(elapsedTime),spriteWidths[updateSpriteRow][updateSpriteCol],
            spriteHeights[updateSpriteRow][updateSpriteCol]);
  } // end getFutureCollisionArea

  /** setSpeed
   * Set the player's walking speed
   * @param s The speed
   */
  public void setSpeed (int s){
    this.speed = s;
  } // end setSpeed

  /** setCurrentSpriteRow
   * Set the current player's sprite row number
   * @param num The row number
   */
  public void setCurrentSpriteRow(int num){
    currentSpriteRow = num;
    updateSpriteRow = num;
  } // end setCurrentSpriteRow

  /** setCurrentSpriteCol
   * Set the current player's sprite column number
   * @param num The col number
   */
  public void setCurrentSpriteCol(int num){
    currentSpriteCol = num;
    updateSpriteCol = num;
  } // end setCUrrentSpriteCol
  
 
}  // end Players.ChasePlayer

