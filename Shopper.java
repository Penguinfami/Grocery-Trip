import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Graphics;

/** Shopper.java
 * @author Christine Zhao
 * A shopper class that can move thorugh different targets
 */
public class Shopper {

  // class fields
  private int x,y;
  private int width, height;
  private int targetMapX, targetMapY;
  private BufferedImage sprite;
  private int runningDistance;
  private int speed;
  private boolean reachedTarget;
  private int targetX, targetY;

  /** Shopper
   * Create a shopper
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The shopper width
   * @param height The shopper height
   * @param runningDistance The running distance to each target
   */
  public Shopper(int x, int y, int width, int height, int runningDistance){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.speed = 200;
    this.runningDistance = runningDistance;
    loadSprite(); // load the sprite
  } // end Shopper

  /** loadSprite
   * Load the shopper's sprite image
   */
  public void loadSprite(){
    try {
      if (this instanceof StoreThief) {
        sprite = ImageIO.read(new File("grocery_images/randomPerson.png"));
      } else {
        sprite = ImageIO.read(new File("grocery_images/Coins.png"));
      }
    } catch (Exception e){
      System.out.println(e);
    }
  } // end loadSprite

  /** update
   * Update the shopper's position based on the elapsed time and map postion
   * @param elapsedTime The elapsed time since previous update
   * @param map The map the shopper is on
   */
  public void update(double elapsedTime, Map map){
    if (!reachedTarget){
      int xDistance;
      int yDistance;

      // cast to the correct map class and get the x and y distances
      if (map instanceof TileMap){
        xDistance = targetX - x +((TileMap)map).getX() - targetMapX;
        yDistance = targetY - y +((TileMap)map).getY() - targetMapY;
      } else {
        xDistance = targetX - x +((PictureMap)map).getX() - targetMapX;
        yDistance = targetY - y +((PictureMap)map).getY() - targetMapY;
      }

      // Calculate the displacement according the the angle
      double rads = Math.atan(Math.abs(yDistance * 1.0/ xDistance));
      double displacement =  ( speed * elapsedTime);
      double yMovement =  Math.sin(rads) * displacement;
      double xMovement = Math.cos(rads) * displacement;

      // if the shopper is basically at its target
      if ((xDistance < 5) && (xDistance > -5 ) && (yDistance < 5) && (yDistance > -5)){
        reachedTarget = true;
      }

      // move the shopper
      if (xDistance> 0){
        x = (int)(x + xMovement);
      } else if (xDistance < 0){
        x = (int)( x - xMovement);
      }
      if (yDistance > 0){
        y = (int)(y + yMovement);
      } else if (yDistance < 0){
        y = (int)(y - yMovement);
      }

    }
  } // end update

  /** draw
   * Draw the shopper
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(sprite, x,y,width,height,null);
  } // end draw

  /** updateTarget
   * Update the shopper's target position
   * @param m The map on which the shopper is located
   */
  public void updateTarget(Map m){
    setReachedTarget(false);

    if (m instanceof PictureMap) {
      PictureMap map = (PictureMap)m;
      boolean foundTarget = false;
      double randomPointX;
      double randomPointY;
      int numTries = 1;
      int[] posNeg = {-1, 1};

      while (!foundTarget) {
        while (numTries % 50 != 0) {
          randomPointX = x + (int) (Math.random() * runningDistance * posNeg[(int) (Math.random() * 2)]); // negative vs positive
          randomPointY = y + Math.sqrt(Math.pow(runningDistance, 2) - Math.pow(randomPointX - x, 2)) * posNeg[(int) (Math.random() * 2)];
          if (map.contains((int) (randomPointX), (int) (randomPointY),getWidth())) { // if the point is on the map
            setTargetX((int) (randomPointX)); // set it as the thief's new target
            setTargetY((int) (randomPointY));
            setTargetMapX(map.getX()); // the map's current position
            setTargetMapY(map.getY());
            foundTarget = true;
          }
          numTries++;
        }
        numTries++;
        if (!foundTarget) {
          runningDistance -= 50;
        }
        if (runningDistance < 50) {
          foundTarget = true;
          System.out.println("Couldn't find target");
        }
      }
    } else if (m instanceof TileMap){

      TileMap map = (TileMap)m;
      boolean foundTarget = false;
      double randomPointX;
      double randomPointY;
      int numTries = 1;
      int[] posNeg = {-1, 1};
      Rectangle futureArea;
      Tile currentTile;
      int distanceX;
      int distanceY;
      while (!foundTarget) {

        while (numTries % 20 != 0 && !foundTarget) {

          foundTarget = true;

          // generate a random x/y coordinate
          randomPointX = x + (int) (Math.random() * runningDistance * posNeg[(int) (Math.random() * 2)]); // negative vs positive
          randomPointY = y + Math.sqrt(Math.pow(runningDistance, 2) - Math.pow(randomPointX - x, 2)) * posNeg[(int) (Math.random() * 2)];

          // move it to its future position
          if ((randomPointX > 0) && (randomPointX < map.getDisplayWidth() - width && randomPointY > 0) && (randomPointY < map.getDisplayLength() - height)) {
            if (randomPointX > x){
              distanceX = (int)randomPointX - x + width;
            }  else {
              distanceX = (int)randomPointY - x;
            }  
            if (randomPointY > y){
              distanceY = (int)randomPointY - y + height;
            }  else {
              distanceY = (int)randomPointY - y;
            }  
            futureArea = new Rectangle(x,y,distanceX  , distanceY);

            // check if  the path collides with any collidable tiles
            for (int i = map.getRoomStartRow(); i < map.getNumWidthTiles(); i++) {
              for (int j = map.getRoomStartCol(); j < map.getNumHeightTiles(); j++) {
                currentTile = map.getTile(i, j);
                if (new Rectangle(currentTile.getXPosMap(), currentTile.getYPosMap(), currentTile.getWidth(), currentTile.getHeight()).intersects(futureArea)) {
                  if (currentTile.isCollidable()) {
                    foundTarget = false;
                  }
                }

              }
            }
            if (foundTarget) {
              setTargetX((int) (randomPointX)); // set it as the thief's new target
              setTargetY((int) (randomPointY));
            }
            numTries++;
          } else {
            foundTarget = false;
          }  
        }
        numTries++;

        // if no target was found after many tries, decrease the running distance
        if (!foundTarget) {
          runningDistance -= 50;
        }

        // if still can't find a target
        if (runningDistance < 50) {
          foundTarget = true;
        }
      }
    }
  } // end updateTarget

  /** setRunningDistance
   * Set the shopper's running distance per target update
   * @param d The distance
   */
  public void setRunningDistance(int d){
    this.runningDistance = d;
  } // end setRunningDistance

  /** getRunningDistance
   * Get the running distance
   * @return The running distance
   */
  public int getRunningDistance(){
    return runningDistance;
  } // end getX

  /** setReachedTarget
   * Set whether or not the shopper has reached its target
   * @param b The boolean value
   */
  public void setReachedTarget(boolean b){
    this.reachedTarget = b;
  } // end setReachedTarget

  /** getX
   * Get the x coordinate
   * @return The x coordinate
   */
  public int getX(){
    return x;
  } // end getX

  /** getY
   * Get the y coordinate
   * @return The y coordinate
   */
  public int getY(){
    return y;
  } // end getY

  /** setY
   * Set the y coordinate
   * @param y The coordinate to set to
   */
  public void setY(int y){
    this.y = y;
  } // end setY

  /** setX
   * Set the x coordinate
   * @param x The coordinate to set to
   */
  public void setX(int x){
    this.x = x;
  } // end setX

  /** getWidth
   * Get the shopper's width
   * @return The width
   */
  public int getWidth(){
    return width;
  } // end getWidth

  /** getHeight
   * Get the shopper's height
   * @return The height
   */
  public int getHeight(){
    return height;
  } // end getHeight

  /** setWidth
   * Set the shopper's width
   * @param w The width to set to
   */
  public void setWidth(int w){
    this.width = w;
  } // end setWidth

  /** setHeight
   * Set the shopper's height
   * @param h The height to set to
   */
  public void setHeight(int h){
    this.height = h;
  } // end setHeight

  /** setTargetX
   * Set the shopper's target x coordinate
   * @param x The x coordinate to set to
   */
  public void setTargetX(int x){
    this.targetX = x;
  } // end setTargetX

  /** setTargetY
   * Set the shopper's target y coordinate
   * @param y The y coordinate to set to
   */
  public void setTargetY(int y){
    this.targetY = y;
  } // end setTargetY

  /** setTargetMapX
   * Set the map's x coordinate relative to the target x coordinate
   * @param x The coordinate to set to
   */
  public void setTargetMapX(int x){
    this.targetMapX = x;
  } // end setTargetMapX

  /** setTargetMapY
   * Set the map's y coordinate relative to the target y coordinate
   * @param y The coordinate to set to
   */
  public void setTargetMapY(int y){
    this.targetMapY = y;
  } // end setTargetMapY

  /** getCollisionArea
   * Get the collision area
   * @return The area rectangle
   */
  public Rectangle getCollisionArea(){
    return new Rectangle(x, y, width, height);
  } // end getCollisionArea

  /** getSpeed
   * Get the shopper's speed
   * @return The speed
   */
  public int getSpeed(){
    return speed;
  } // end getSpeed

  /** setSpeed
   * Set the shopper's speed
   * @param s The speed to set to
   */
  public void setSpeed(int s){
    this.speed = s;
  } // end setSpeed

} // end Shopper
