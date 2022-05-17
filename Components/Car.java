package Components;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;

/** Items.Car
 * @author Christine Zhao
 * A car object
 */
public class Car {

  // class fields
  private BufferedImage image;
  private int x, y;
  private int width, height;
  private Rectangle collisionArea;

  /** Items.Car
   * Construct a car
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The car's width
   * @param height The car's height
   * @param filepath The path to the car's image
   */
  public Car(int x, int y, int width, int height, String filepath){
    loadSprite(filepath);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.collisionArea = new Rectangle(x,y,width, height);
  } // end Items.Car

  /** loadSprite
   * Load the car's display image
   * @param filepath The file's path
   */
  public void loadSprite(String filepath){
    try{
      BufferedImage sheet = ImageIO.read(new File(filepath));
      int randNum = (int)(Math.random() * 5);
      image = sheet.getSubimage(randNum*50, 0,50,100 );
    } catch (Exception e){
      System.out.println("Error loading car player image");
    }
  } // end loadSprite

  /** update
   * Update the car's position
   * @param xMovement The difference in x coordinate
   * @param yMovement The difference in y coodinate
   */
  public void update(int xMovement, int yMovement){
    this.x = this.x + xMovement;
    collisionArea.x = x;
    this.y = y + yMovement;
    collisionArea.y = y;
  } // end update

  /** draw
   *  Draw the car
   * @param g The Graphics object
   */
  public void draw(Graphics g){
    g.drawImage(image, x, y, width, height, null);
  } // end draw

  /** getCollisionArea
   * Get the car's collision area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return collisionArea;
  } // end getCollisionArea

  /** getX
   * Get the x position
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
  } // end y

  /** setX
   * Set the x coodinate
   * @param x The coordinate to set to
   */
  public void setX(int x){
    this.x = x;
    collisionArea.x = x;
  } // end setX

  /** setY
   * Set the y coordinate
   * @param y The coordinate to set to
   */
  public void setY(int y){
    this.y = y;
    collisionArea.y = y;
  } // end setY

  /** getHeight
   * Get the car's height
   * @return The height
   */
  public int getHeight(){
    return height;
  } // end getHeight

  /** getWidth
   * Get the car;s width
   * @return The width
   */
  public int getWidth(){
    return width;
  } // end getWidth


}
