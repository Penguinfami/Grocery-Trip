import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;

/** Ground.java
 * @author Christine Zhao
 * A ground tile
 */
public class Ground extends Tile{

  private BufferedImage image;
  
  /** Ground
   * Construct a ground tile
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The tile width
   * @param h The tile height
   * @param filepath The image filepath
   */
  public Ground(int x, int y, int w, int h, String filepath){
    super(x,y,w,h, false);
    try{
      image =  ImageIO.read(new File(filepath));
    } catch (Exception e){
      System.out.println("can't get tile image");
    }
  } // end Ground

  /** draw
   * Draw the tile
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.drawImage(image, getXPosMap(), getYPosMap(), getWidth(), getHeight(), null);
  } // end draw

} // end Ground
