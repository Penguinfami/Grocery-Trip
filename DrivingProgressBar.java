import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;

/** DrivingProgressBar.java
 * @author Christine Zhao
 * The progress bar highlighting the car player's travelled distance
 */
public class DrivingProgressBar {

  // class fields
  private int currentDistance;
  private int x,y;
  private int width, height;
  private BufferedImage miniCar;
  private boolean isFinished;
  private int totalDistance;
  private int currentBarLength;

  /** DrivingProgressBar
   * Construct the DrivingProgressBar
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The progress bar width
   * @param h The progress bar height
   * @param filepath The filepath of the current position's image
   * @param totalDistance The total distance needed to travel
   */
  public DrivingProgressBar(int x, int y, int w, int h, String filepath, int totalDistance){
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
    this.isFinished = false;
    this.totalDistance = totalDistance; // total distance needed to drive
    this.currentDistance = 0;
    this.currentBarLength = 0;
    loadSprite(filepath);
  } // end DrivingProgressBar

  /** loadSprite
   *  Load the minicar
   * @param filepath The minicar's filepath
   */
  private void loadSprite(String filepath){
    try{
      miniCar = ImageIO.read(new File(filepath));
    }catch (Exception e){
      System.out.println("Error loading mini car");
    }
  } // end loadSprite

  /** draw
   * Draw the progress bar
   * @param g The graphics object
   */
  public void draw(Graphics g){
    g.setColor(Color.ORANGE);
    g.fillRect(x - 3, y - 3 - width / 2, width + 6, height + 6 + width / 2); // draw the border
    g.setColor(Color.RED);
    g.fillRect(x,y + height - currentBarLength,width,currentBarLength);
    g.drawImage(miniCar, x - 4, y - width/2 + height - currentBarLength, width + 8,width,null); // draw the red progress bar
  } // end draw

  /** update
   * Update the progress bar
   * @param distanceTravelled The distance travelled since previous update
   */
  public void update(int distanceTravelled){
    if (currentDistance >= totalDistance){
      isFinished = true;
    } else{
      currentDistance+= distanceTravelled; // add to the distance travelled
    }
    currentBarLength = currentDistance * height / totalDistance; // update the length according to the distance tranvelled
  } // end update

  /** reset
   * Reset the progress
   */
  public void reset(){
    currentDistance = 0;
    isFinished = false;
  } // end reset

  /** isFnished
   * Is the progress finsihed
   * @return True or false
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished
} // end DrivingProgressBar
