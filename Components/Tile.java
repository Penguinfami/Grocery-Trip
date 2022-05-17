package Components;

import java.awt.Graphics;

/** Items.Tile.java
 *@author Christine Zhao
 * A tile of a tile map on the screen
 */
public abstract class Tile{

  // class fields
  private int xPosMap, yPosMap;
  private int width, height;
  private boolean isCollidable;

  /** Items.Tile
   * Construct a tile
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The tile width
   * @param h The tile height
   * @param collidable Whether or not the tile is collidable
   */
  public Tile(int x, int y, int w, int h, boolean collidable){
    xPosMap = x;
    yPosMap = y;
    width = w;
    height = h;
    isCollidable = collidable;
  } // end Items.Tile

  /** draw
   * Draw the tile
   * @param g The graphics object
   */
  public abstract void draw (Graphics g);

  /** isCollidable
   * Checks if the tile is collidable
   * @return True or false
   */
  public boolean isCollidable(){
    return isCollidable;
  } // end isCollidable

  /** getXPosMap
   * Get the tile's x coordinate
   * @return The x coordinate on the map
   */
  public int getXPosMap(){
    return xPosMap;
  } // end getXPosMap

  /** getYPosMap
   * Get the tile's y coordinate
   * @return The y coordinate on the map
   */
  public int getYPosMap(){
    return yPosMap;
  } // end getYPosMap

  /** getWidth
   * Get the tile width
   * @return The width
   */
  public int getWidth(){
    return width;
  } // end getWidth

  /** getHeight
   * Get the tile's height
   * @return The height
   */
  public int getHeight(){
    return height;
  } // end getHeight

} // end Items.Tile
