

/** Coin.java
 * @author Christine Zhao
 * A coin object
 */
public class Coin extends Cash{

  /** Coin
   * Construct a coin
   * @param value The dollar value
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The coin's width
   * @param height The coin's height
   */
  public Coin(double value, int x, int y, int width, int height){
    super(x,y,width,height, value);

  } // end Coin
} // end Coin
