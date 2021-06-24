

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;


/** FoodProduct.java
 *@author  Christine ZHao
 * A food product object
 */
public class FoodProduct{

  // class fields
  private Double price; // is this in this object or in a product object?
  private String name;
  private BufferedImage sprite;

  /** FoodProduct
   * Construct a food product
   * @param str The product's name
   * @param price The product's price
   * @param filepath The product's display image
   */
  public FoodProduct(String str, Double price, String filepath){
    this.name = str;
    this.price = price;
    loadSprite(filepath);

  } // end FoodProduct

  /** loadSprite
   * Load the display image
   * @param file The image's filepath
   */
  private void loadSprite(String file){
    try{
      sprite = ImageIO.read(new File(file));
    } catch (Exception e){
      System.out.println("error loading food sprite");
    }
  } // end loadSprite

  /** getName
   * Get the product's name
   * @return The product name
   */
  public String getName(){
    return name;
  } // end getName

  /** getPrice
   * Get the product's price
   * @return The product price
   */
  public Double getPrice(){
    return price;
  } // end getPrice

  /** draw
   * Draw the food product
   * @param g The graphics object
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   */
  public void draw(Graphics g, int x, int y, int width, int height){
    g.drawImage(sprite, x,y,width,height, null);
  } // end draw

} // end FoodProduct

