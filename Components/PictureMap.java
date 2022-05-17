package Components;

import Components.Map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Rectangle;

/** Components.PictureMap.java
 * @author Christine Zhao
 * A movable picture map
 */
public class PictureMap extends Map {

  // class fields
  private int x,y;
  private int xDirection,yDirection;
  private int startingX, startingY;
  private int width, height;
  private int visibleWidth, visibleHeight;
  private BufferedImage mapImage;
  private boolean mapXMovement;
  private boolean mapYMovement;
  private boolean offTheScreen;


  /** Components.PictureMap
   * Construct a picture map
   * @param filepath The filepath to the map's image
   * @param x The starting x coordinate
   * @param y The starting y coordinate
   * @param width The map width
   * @param height The map height
   * @param screenWidth The screen width
   * @param screenHeight The screen height
   * @param offTheScreen Whether or not the picture extends off the screen
   */
  public PictureMap(String filepath, int x, int y, int width, int height, int screenWidth, int screenHeight, boolean offTheScreen){
    this.x = x;
    this.y = y;
    this.startingX = x;
    this.startingY = y;
    this.width = width;
    this.height = height;
    this.visibleWidth = screenWidth;
    this.visibleHeight = screenHeight;
    this.offTheScreen = offTheScreen;
    loadMap(filepath);
  }

  /** loadMap
   * Load the map image
   * @param filepath The image's filepath
   */
  public void loadMap(String filepath){
    try{
      mapImage = ImageIO.read(new File(filepath));
    } catch(Exception e){
      System.out.println("error loading map pic");
    }
  }

  /** update
   * Update the map's position
   * @param xMovement The horizontal movement
   * @param yMovement The vertical movement
   */
  public void update(int xMovement, int yMovement){
    
    int xNewPosition = x + xMovement;

    //if (xMovement != 0){
      if ((xNewPosition + width >= visibleWidth)  && (xNewPosition <= startingX) || (offTheScreen )){ // if the player hasnt reached the end of the map
        mapXMovement = true;
        this.x = xNewPosition;
      } else {
        mapXMovement = false;
      }  
    //}
    int yNewPosition = y + yMovement;
    //if (yMovement != 0){
      if ((yNewPosition + height  >= visibleHeight) && (yNewPosition <= startingY  )||( offTheScreen)){ // if the player hasnt reached the end of the map
        mapYMovement = true;
        this.y = yNewPosition;
      } else {
        mapYMovement = false;
      }
  }


  /** draw
   * Draw the map
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(mapImage, x,y,width, height, null);
  }

  /** contains
   * Check whether or not the map contains a point
   * @param pointX The point x coordinate
   * @param pointY The point y coordinate
   * @param borderLength The length of the edge border
   * @return True or false if it contains the point
   */
  public boolean contains(int pointX, int pointY, int borderLength){
    Rectangle mapRect = new Rectangle(x + borderLength,y + borderLength,width - 2*borderLength, height - 2*borderLength);
    return mapRect.contains(pointX, pointY);
  }

  /** contains
   * Checks whether the map contains a rectangle
   * @param area The rectangle
   * @return True or false if the map contains the rectangle
   */
  public boolean contains(Rectangle area){
    Rectangle mapRect = new Rectangle(x ,y,width , height );
    return mapRect.contains(area);
  }

  /** contains
   * Checks whether the map contains a rectangle
   * @param area The rectangle
   * @param borderLength The added border length
   * @return True or false if the map contains the rectangle
   */
  public boolean contains(Rectangle area, int borderLength){
    Rectangle mapRect = new Rectangle(x + borderLength, y + borderLength,width - 2*borderLength, height - 2*borderLength);
    return mapRect.contains(area);
  }

  /** getWidth
   * Get the map width
   * @return The map width
   */
  public int getWidth(){
    return width;
  }

  /** getHeight
   * Get the map height
   * @return The map height
   */
  public int getHeight(){
    return height;
  }

  /** getX
   * Get the map x coordinate
   * @return The x coordinate
   */
  public int getX(){
    return x;
  }

  /** getY
   * Get the map y coordinate
   * @return The y coordinate
   */
  public int getY(){
    return y;
  }

  /** setX
   * Set the x coordinate
   * @param x The coordinate to set to
   */
  public void setX(int x){
    this.x = x;
  }

  /** setY
   * Set the y coordinate
   * @param y The coordinate to set to
   */
  public void setY(int y){
    this.y = y;
  }

  /** hasXMovement
   * Checks if the map moved horizontally in the previous update
   * @return True or false if there was movement
   */
  public boolean hasXMovement(){
    return mapXMovement;
  }

  /** hasYMovement
   * Checks if the map moved vertically  in the previous update
   * @return True or false if there was movement
   */
  public boolean hasYMovement(){
    return mapYMovement;
  }

  /** moveLeft
   * Move the map left
   */
  public void moveLeft(){
    xDirection = -1;
  }

  /** moveRight
   * Move the map right
   */
  public void moveRight(){
    xDirection = 1;
  }

  /** moveUp
   * Move the map up
   */
  public void moveUp(){

    yDirection = 1;

  }

  /** moveDown
   * Move the map down
   */
  public void moveDown(){
    yDirection = -1;

  }

  /** stopMoveLeft
   * Stop moving the map left
   */
  public void stopMoveLeft(){
    xDirection = 0;
  }

  /** stopMoveRight
   * Stop moving the map right
   */
  public void stopMoveRight(){
    xDirection= 0;
  }

  /** stopMoveUp
   * Stop moving the map up
   */
  public void stopMoveUp(){
    yDirection=0;
  }

  /** stopMoveDown
   * Stop moving the map down
   */
  public void stopMoveDown(){
    yDirection= 0;
  }
}
