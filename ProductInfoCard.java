import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

/** ProductInfoCard.java
 * @author Christine Zhao
 * Info card displaying the product's image, name and price
 */
public class ProductInfoCard<E> {

  // class fields
  private E product;
  private int x,y;
  private int width, height;
  private Font productName;
  private Font productPrice;

  /** ProductInfoCard
   * Construct the info card
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   */
  public ProductInfoCard(int x, int y, int width, int height){
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
    this.productName = new Font ("Copperplate Gothic Bold", Font.BOLD, 30 + height / 50);
    this.productPrice =  new Font ("Cooper Black", Font.PLAIN, 25 + height / 50);
  } // end ProductInfoCard

  /** draw
   * Draw the product info
   * @param g The graphics object
   */
  public void draw(Graphics g){
    //draw the background
    g.setColor(Color.RED);
    g.fillRect(x,y, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(x + width/20, y + height/20, width * 18/20, height * 18/20);
    g.setColor(Color.BLACK);
    // draw the product info
    if (product instanceof FoodProduct) {
      g.setFont(productName);
      g.drawString(((FoodProduct) product).getName(), x + width / 10, y + height / 5);
      g.setFont(productPrice);
      g.drawString("Price: " + ((FoodProduct) product).getPrice().toString(), x + width / 7, y + height/2);
      ((FoodProduct)product).draw(g, x + (width * 4 / 7), y + (height / 4),width * 3 / 8, width * 3 / 8 );
    }
  } // end draw

  /** setProduct
   * Set the product for the info card
   * @param product The product
   */
  public void setProduct(E product){
    this.product = product;
  } // end setProduct

  /** setX
   * Set the x coordinate
   * @param x The coordinate to set to
   */
  public void setX(int x){
    this.x = x;
  } // end setX

  /** setY
   * Set the y coordinate
   * @param y The coordinate to set to
   */
  public void setY(int y){
    this.y = y;
  } // end setY
} // end ProductInfoCard
