package Setup;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

/** Setup.FrameRate
 * Get the framerate and display it
 */
public class FrameRate {

  // class fields
  private String frameRate;
  private long lastTimeChecked;
  private long deltaTime;
  private int frameCount;

  /** Setup.FrameRate
   * Construct a framerate object
   */
  public FrameRate(){
    lastTimeChecked = System.currentTimeMillis();
    frameCount = 0;
    frameRate = "0 fps";
  } // end Setup.FrameRate

  /** update
   * Update the frameRate
   */
  public void update(){
    long currentTime = System.currentTimeMillis();
    deltaTime += currentTime - lastTimeChecked;
    lastTimeChecked = currentTime;
    frameCount++;
    if (deltaTime >= 1000){
      frameRate = frameCount + " fps";
      frameCount = 0;
      deltaTime = 0;
    } 
  } // end update

  /** draw
   * Draw the frameRate string
   * @param g The graphics object
   * @param font The font to use
   * @param colour The colour to use
   * @param x The x coordinate
   * @param y The y coordinate
   */
  public void draw(Graphics g, Font font, Color colour,int x, int y){
    g.setFont(font);
    g.setColor(colour);
    g.drawString(frameRate, x,y);
  } // end draw

  /** draw
   * Draw the frameRate string
   * @param g The graphics object
   * @param x The x coordinate
   * @param y The y coordinate
   */
  public void draw(Graphics g, int x, int y){
    g.drawString(frameRate,x,y);
    
  }   // end draw
} // end Setup.FrameRate
