package Components;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;

/** Items.EmptyAisle.java
 * @author Christine Zhao
 * An aisle tile without any items inside
 */
public class EmptyAisle extends Tile {

  // class fields
  private BufferedImage image;
  private Rectangle collisionArea;

  /** Items.EmptyAisle
   * Construct an empty aisle
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The tile width
   * @param h The tile height
   * @param filepath The filepath of the tile's image
   */
  public EmptyAisle( int x, int y, int w, int h, String filepath, int orientation){

    super(x, y, w, h, true);
    collisionArea = new Rectangle(x,y,w,h);
    try{
      image = ImageIO.read(new File(filepath)).getSubimage((orientation - 1) * 300, 0, 300,300);
    } catch (Exception e){
      System.out.println("can't get tile image");
    }

  } // end Items.EmptyAisle

  /** draw
   * Draw the tile
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(image,  getXPosMap(), getYPosMap(), getWidth(), getHeight(), null);
  } // end draw

  /** getCollisionArea
   * Get the tile's collision area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return collisionArea;
  } // end getCollisionArea
} // end Items.EmptyAisle
