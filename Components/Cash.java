package Components;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

/** Items.Cash.java
 *@author  Christine Zhao
 * A cash class
 */
public class Cash {

  // class fields
  private int x, y;
  private int width, height;
  private double value;
  private BufferedImage image;

  /** Items.Cash
   * Construct a cash object
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   * @param value The dollar value
   */
  public Cash(int x, int y, int width, int height, double value){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.value = value;
    try {
      BufferedImage sheet = ImageIO.read(new File("grocery_images/Coins.png"));
      BufferedImage sheet2 = ImageIO.read(new File("grocery_images/Bills.png"));

      // set the appropriate image according to the cash's value
      if (value == 20) {
        image = sheet2.getSubimage(0, 0, 200, 100);
      }else if (value == 10){
        image = sheet2.getSubimage(0, 100, 200, 100);
      }else if (value == 5){
        image = sheet2.getSubimage(0, 200, 200, 100);
      }else if (value == 2){
        image = sheet.getSubimage(0,0,42,42);
      } else if (value == 1){
        image = sheet.getSubimage(0,42,42,42);
      }else if (value == 0.25){
        image = sheet.getSubimage(0,84,42,42);
      }else if (value == 0.1){
        image = sheet.getSubimage(0,126,42,42);
      }else if (value == 0.05){
        image = sheet.getSubimage(0,168,42,42);
      }else{
        image = sheet.getSubimage(0,210,42,42);
      }
    } catch(Exception e){
      System.out.println("Error loading coin sprite");
    }
  } // end Items.Cash

  /** draw
   * Draw the cash
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(image, x, y, width, height, null);
  } // end draw

  /** getValue
   * Get the value of the cash
   * @return The dollar value
   */
  public double getValue(){
    return value;
  } // end getValue

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

  /** getWidth
   * Get the width
   * @return The width
   */
  public int getWidth(){
    return width;
  } // end getWidth

  /** getHeight
   * Get the height
   * @return The height
   */
  public int getHeight(){
    return height;
  } // end getHeight

  /** setX
   * Set the x coordinate
   * @param x The coordinate to set to
   */
  public void setX(int x){
    this.x = x;
  } // end setX

  /** setY
   * Set the y coordinate
   * @param y The coordinate to set to
   */
  public void setY(int y){
    this.y = y;
  } // end setY

} // end Items.Cash
