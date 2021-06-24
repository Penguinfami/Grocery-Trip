import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/** ShopPlayer.java
 * @author Christine Zhao
 * The player controlled during the grocery game
 */
public class ShopPlayer extends Player{

  // class fields
  private int updateX, updateY;
  private int xdirection,ydirection;
  private BufferedImage[][] sprites;
  private int[][] spriteHeights;
  private int[][] spriteWidths;
  private int currentSpriteRow;
  private int currentSpriteCol;
  private int updateSpriteRow, updateSpriteCol;
  private int speed;
  private boolean move;

  /** ShopPlayer
   * Construct a shopping player
   * @param screenWidth The screen width
   * @param screenHeight The screen height
   * @param compareWidth The width to base the player width on
   * @param compareHeight The height to base the player height on
   */
  public ShopPlayer(int screenWidth, int screenHeight, int compareWidth, int compareHeight) {
    super (0,300,80,50);
    loadSprite(screenWidth,screenHeight, compareWidth, compareHeight);
    this.updateX = 0;
    this.xdirection=0;
    this.ydirection=0;
    this.move = true;
  } // end ShopPlayer

  /** loadSprite
   * Load the player sprite images and calculate the heights and widths for all the sprites
   * @param screenWidth The screen width
   * @param screenHeight The screen height
   * @param compareWidth The width to base the player width on
   * @param compareHeight The height to base the player height on
   */
  public void loadSprite(int screenWidth, int screenHeight, int compareWidth, int compareHeight) {
    int rows = 4;
    int cols = 5;
    sprites = new BufferedImage[rows][cols];

    // Since the vertical/horizontal sprites have different proportions, use a compare width and height to set the dimensions
    spriteWidths = new int[rows][cols];
    spriteHeights = new int[rows][cols];
    try {
      BufferedImage spriteSheet = ImageIO.read(new File("grocery_images/horizontalWalk.png"));
      for (int i = 0; i < 2; i++){
        for (int j = 0; j < 5; j++){
          sprites[i][j] = spriteSheet.getSubimage(j * 112, i * 100, 112, 100);
          spriteWidths[i][j] = (int)(screenWidth/750.0 * 112 / compareWidth * 5);
          spriteHeights[i][j] = (int)(screenHeight/ 750.0 * 100 / compareHeight * 5);
        }  
      }  
    } catch(Exception e) {
      System.out.println(e);
      System.out.println("error loading horizontal sprite");
    };
    try{
      BufferedImage spriteSheet = ImageIO.read(new File("grocery_images/verticalWalk.png"));
      for (int i = 2; i < 4; i++){
        for (int j = 0; j < 5; j++){
          sprites[i][j] = spriteSheet.getSubimage(j * 53, (i-2) * 100, 53,100);
          spriteWidths[i][j] = (int) (screenWidth/750.0 * 53 / compareWidth * 5);
          spriteHeights[i][j] = (int) (screenHeight/ 750.0 * 100 / compareHeight * 5);
        }  
      }  
    } catch(Exception e) {
      System.out.println(e);
      System.out.println("error loading vertical sprite");
    };
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

    g.drawImage(sprites[currentSpriteRow][currentSpriteCol],getX(), getY(), spriteWidths[currentSpriteRow][currentSpriteCol]
                  , spriteHeights[currentSpriteRow][currentSpriteCol],null);
    //g.setColor(Color.BLACK);
    //Rectangle box = getCollisionArea();
    //g.drawRect(box.x, box.y,box.width, box.height);
  } // end draw

  /** update
   * Update the player's current sprite and position
   * @param elapsedTime The elapsed time between updates
   */
  public void update(double elapsedTime) {
    if (move){
      currentSpriteRow = updateSpriteRow;
      setX(updateX);
    } 
    currentSpriteCol = updateSpriteCol;
    setX((int) (getX()+ (this.xdirection * elapsedTime * speed)));
    setY((int) (getY() + this.ydirection * elapsedTime* speed));
    updateX = getX();
    setHeight(spriteHeights[currentSpriteRow][currentSpriteCol]);
    setWidth(spriteWidths[currentSpriteRow][currentSpriteCol]);

  } // end update

  /** setMove
   * Set whether or not the player should be moving in the next update
   * @param b Boolean value if it is moving or not
   */
  public void setMove(boolean b){
    this.move = b;
  } // end setMove

  /** getDisplacementX
   * Get the player's travelled distance after the next update
   * @param elapsedTime The elapsed time between updates
   * @return The horizontal displacement
   */
  public int getDisplacementX(double elapsedTime){
    return (int)(updateX+ (this.xdirection*elapsedTime* speed));
  } // end getDisplacementX

  /** getDisplacementY
   * Get the player's travelled distance after the next update
   * @param elapsedTime The elapsed time between updates
   * @return The vertical displacement
   */
  public int getDisplacementY(double elapsedTime){
    return (int)(getY()+ (this.ydirection*elapsedTime* speed));
  } // end getDisplacementY

  /** setXDirection
   * Set the player's horizontal direction
   * @param n The positive/negative direction
   */
  public void setXDirection(int n){
    xdirection = n;
  } // end setXDirection

  /** setYDirection
   * Set the player's vertical direction
   * @param n The positive/negative direction
   */
  public void setYDirection(int n){
    ydirection = n;
  } // end setYDirection

  /** getUpdateX
   * Get the player's x value after the next update, relative to the current sprite
   * @return The next updated x
   */
  public int getUpdateX(){
    return updateX;
  } // end getUpdateX

  /** moveLeft
   * Move the player left and update the sprite
   */
  public void moveLeft(){
    updateSpriteRow = 1;
    if (currentSpriteRow >= 2){
      updateX = getX() - (spriteWidths[0][0] - spriteWidths[2][0]);
    }  else {
      updateX = getX();
    }  
    xdirection= -1;
    if (updateSpriteCol != 4){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    }   
  } // end moveLeft

  /** moveRight
   * Move the player right and update the sprite
   */
  public void moveRight(){
    updateSpriteRow = 0;
    if (currentSpriteRow >= 2){
      updateX = getX() - 5;
    }  else {
      updateX = getX();
    }  
    xdirection= 1;
    if (updateSpriteCol != 4){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    } 
  } // end moveRight

  /** moveUp
   * Move the player up and update the sprite
   */
  public void moveUp(){
    updateSpriteRow = 3;
    if (currentSpriteRow == 0){
      updateX = getX() + (spriteWidths[0][0] - spriteWidths[2][0]);
    }   else {
      updateX = getX();
    }  
    if (updateSpriteCol != 4){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    } 
    ydirection= -1;

  } // end moveUp

  /** moveDown
   * Move the player down and update the sprite
   */
  public void moveDown(){
    updateSpriteRow = 2;
     if (currentSpriteRow == 0){
      updateX = getX() + (spriteWidths[0][0] - spriteWidths[2][0]);
    }   else {
      updateX = getX();
    }  
    if (updateSpriteCol != 4){
      updateSpriteCol++;
    }  else {
      updateSpriteCol = 0;
    } 
    ydirection= 1;
  } // end moveDown


  /** stopMoveLeft
   * Stop moving the player left
   */
  public void stopMoveLeft(){
    xdirection = 0;
  }

  /** stopMoveRight
   * Stop moving the player right
   */
  public void stopMoveRight(){
    xdirection= 0;
  }

  /** stopMoveUp
   * Stop moving the player up
   */
  public void stopMoveUp(){
    ydirection=0;
  }

  /** stopMoveDown
   * Stop moving the player down
   */
  public void stopMoveDown(){
    ydirection= 0;
  }

  /** getCollisionArea
   * Get the player's current collision area
   */
  public Rectangle getCollisionArea(){
    return new Rectangle(getX(),getY(),spriteWidths[currentSpriteRow][currentSpriteCol],
                         spriteHeights[currentSpriteRow][currentSpriteCol]);
  }

  /** getFutureCollisionArea
   * Get the player's collision area after the next x coordinate is updated
   */
   public Rectangle getFutureCollisionArea(){
      return new Rectangle(updateX,getY() ,spriteWidths[updateSpriteRow][updateSpriteCol],
                         spriteHeights[updateSpriteRow][updateSpriteCol]);
  }

  /** setUpdateX
   * Since the sprites have different widths, the x coordinate is adjusted when switching between them to smooth the movement
   * @param x The x coordinate to set to
   */
  public void setUpdateX(int x){
    this.updateX = x;
  }

  /** setSpeed
   * Set the player's speed
   * @param s The speed
   */
  public void setSpeed (int s){
    this.speed = s;
  }

  /** setCurrentSpriteRow
   * Set the current sprite's row in the sprite array
   * @param num The row number
   */
  public void setCurrentSpriteRow(int num){
    currentSpriteRow = num;
    updateSpriteRow = num;
  }

  /** setCurrentSpriteCol
   * Set the current sprite's column in the sprite array
   * @param num The column number
   */
 public void setCurrentSpriteCol(int num){
    currentSpriteCol = num;
    updateSpriteCol = num;
  }  
}
   