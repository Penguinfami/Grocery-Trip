package Components;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

/** Components.ParkingSpace.java
 * @author Christine Zhao
 * Parking space tile
 */
public class ParkingSpace extends Tile implements Highlightable {

  // class fields
  private Rectangle parkingSpace;
  private BufferedImage image;
  private BufferedImage highlightedImage;
  private BufferedImage currentImage;

  /** Components.ParkingSpace
   * Construct a parking space tile
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The tile width
   * @param h The tile height
   * @param filepath The normal image filepath
   * @param highlightedFilepath The highlighted image filepath
   */
  public ParkingSpace(int x, int y, int w, int h, String filepath, String highlightedFilepath){
    super(x,y,w,h, false);
    this.parkingSpace = new Rectangle(x,y,w,h);
    try{
      image =  ImageIO.read(new File(filepath));
      highlightedImage =  ImageIO.read(new File(highlightedFilepath));
      currentImage = image;
    } catch (Exception e){
      System.out.println("can't get tile image");
    }
  } // end Components.ParkingSpace

  /** draw
   * Draw the tile
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(currentImage, getXPosMap(), getYPosMap(), getWidth(), getHeight(), null);
  } // end draw

  /** getParkingSpace
   * Get the collision / parking space area
   * @return The parking space area
   */
  public Rectangle getParkingSpace(){
    return parkingSpace;
  } // end getParkingSpace

  /** highlight
   * Highlight the tile image
   */
  public void highlight(){
    currentImage = highlightedImage;
  } // end highlight

  /** unHighlight
   * Unhighlight the tile image
   */
  public void unHighlight(){
    currentImage = image;
  } // end unHighlight
} // end Components.ParkingSpace
