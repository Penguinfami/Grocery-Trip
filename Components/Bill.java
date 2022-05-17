package Components;

/** Items.Bill.java
 * @author Christine Zhao
 * A bill class
 */
public class Bill extends Cash{

  /** Items.Bill
   * Construct a bill
   * @param value The dollar value of the bill
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width of the bill
   * @param height The height of the Items.Bill
   */
  public Bill(double value, int x, int y, int width, int height){
    super(x,y,width,height, value);
  } // end Items.Bill
} // end Items.Bill
