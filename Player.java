import java.awt.Graphics;

/** Player.java
 * @author Christine Zhao
 * Abstract player class with getters and setters
 */
public abstract class Player {

  // class fields
  private int x, y;
  private int width, height;

  /** Player
   * Construct a player
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The player width
   * @param height The player height
   */
  public Player(int x, int y, int width, int height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  } // end Player


  /** draw
   * Draw the player
   * @param g The graphics object
   */
  public abstract void draw(Graphics g);

  /** update
   * Update the player
   * @param elapsedTime The elapsed time between updates
   */
  public abstract void update(double elapsedTime);

  /** getX
   * Get the x coordinate
   * @return The x coordinate
   */
  public int getX(){
    return x;
  } // end getX

  /** getY
   * Get the Y coordinate
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
   * Get the player width
   * @return The width
   */
  public int getWidth(){
    return width;
  } // end getWidth

  /** getHeight
   * Get the player height
   * @return The height
   */
  public int getHeight(){
    return height;
  } // end getHeight

  /** setWidth
   * Set the player width
   * @param w The width to set to
   */
  public void setWidth(int w){
    this.width = w;
  } // end setWidth

  /** setHeight
   * Set the player height
   * @param h The height to set to
   */
  public void setHeight(int h){
    this.height = h;
  } // end setHeight
} // end Player
