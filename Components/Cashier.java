package Components;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

/** Items.Cashier.java
 * @author Christine Zhao
 * A cashier tile object
 */
public class Cashier extends Tile implements Highlightable {

  // class fields
  private BufferedImage image;
  private Rectangle collisionArea;
  private BufferedImage highlightedImage;
  private BufferedImage currentImage;

  /** Items.Cashier
   * Construct a cashier
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The width of the tile
   * @param h The height of the tile
   * @param filepath The filepath of the tile's image
   * @param filepath2 The filepath of the tile's highlighted image
   * @param orientation The tile's orientation
   */
  public Cashier(int x, int y, int w, int h, String filepath, String filepath2, int orientation){
    super(x,y,w,h, true);
    collisionArea = new Rectangle(x,y,w,h);
    try{
      image =  ImageIO.read(new File(filepath)).getSubimage((orientation - 1) * 300, 0, 300,300);
      highlightedImage = ImageIO.read(new File(filepath)).getSubimage((orientation - 1) * 300, 300, 300,300);
    } catch (Exception e){
      System.out.println("can't get tile image");
    }
    currentImage = image;
  } // end Items.Cashier

  /** draw
   * Draw the cashier tile
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(currentImage,  getXPosMap(), getYPosMap(), getWidth(), getHeight(), null);
  } // end draw

  /** getCollisionArea
   * Get the tile's collision area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return collisionArea;
  } // end getCollisionArea

  /** highlight
   * Set the image to the highlighted image
   */
  public void highlight(){
    currentImage = highlightedImage;
  } // end highlight

  /** unHighlight
   * Set the image to the normal image
   */
  public void unHighlight(){
    currentImage = image;
  } // end unhighlight
}
