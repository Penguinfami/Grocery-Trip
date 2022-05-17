package Components;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;

/** Items.Aisle.java
 * @author Christine Zhao
 * @param <F> The objects to hold
 * An highlightable aisle that contains an item
 */
public class Aisle<F> extends Tile implements Highlightable {

  // class fields
  private F item;
  private BufferedImage image;
  private BufferedImage highlightedImage;
  private BufferedImage currentImage;
  private Rectangle collisionArea;


  /**
   * Create an aisle
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The aisle width
   * @param h The aisle height
   * @param filepath The path to the aisle's normal image
   * @param filepath2 The path to the aisle's highlighted image
   * @param orientation The orientation of the aisle
   */
  public Aisle( int x, int y, int w, int h, String filepath, String filepath2, int orientation){

    super(x, y, w, h, true);
    collisionArea = new Rectangle(x,y,w,h);
    try{
      image = ImageIO.read(new File(filepath)).getSubimage((orientation - 1) * 300, 0, 300,300);
      this.highlightedImage = ImageIO.read(new File(filepath2)).getSubimage((orientation - 1) * 300, 300, 300,300);;
    } catch (Exception e){
      System.out.println("can't get tile image");
    }
    this.currentImage = image;
  } // end Items.Aisle
  
  // construct an aisle with an item inside
  /**
   * Create an aisle
   * @param item The Item inside the aisle
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The aisle width
   * @param h The aisle height
   * @param filepath The path to the aisle's normal image
   * @param filepath2 The path to the aisle's highlighted image
   */
  public Aisle(F item, int x, int y, int w, int h,String filepath, String filepath2){
    super(x,y,w,h, true);
    this.item = item;
    collisionArea = new Rectangle(x,y,w,h);
    try{
      image = ImageIO.read(getClass().getResource(filepath));
      this.highlightedImage = ImageIO.read(getClass().getResource(filepath2));
    } catch (Exception e){
      System.out.println("can't get tile image");
    }
    this.currentImage = image;
  } // end Items.Aisle

  /** setItem
   * Set a new item inside the aisle
   * @param item The item to add
   */
  public void setItem(F item){
    this.item = item;
  }// end setItem

  /** getItem
   * Get the item inside the aisle
   * @return The item
   */
  public F getItem(){
    return item;
  } // end getItem

  /** draw
   * Draw the aisle
   * @param g The Graphics to draw
   */
  public void draw(Graphics g){
    g.drawImage(currentImage,  getXPosMap(), getYPosMap(), getWidth(), getHeight(),null);
  } // end draw

  /** highlight
   * Highlight the aisle's image
   */
  public void highlight(){
    currentImage = highlightedImage;
  } // end highlight

  /** unHighlight
   * Unhighlight the aisle's image
   */
  public void unHighlight(){
    currentImage = image;
  } // end unhighlight


  /** getCollisionArea
   * Get the aisle's collision/intersection area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return collisionArea;
  } // end getCollisionArea
} // end Items.Aisle

