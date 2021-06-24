
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

/** GrocerySideBar.java
 * @author Christine Zhao
 * Displays the items in the cart and the current day's meals as well as price
 */
public class GrocerySideBar {

  // class fields
  private int x,y;
  private int width, height;
  private ShoppingCart shoppingCart;
  private Font mealsFont;
  private Font mealsFont2;
  private Font mealsFont3;
  private Font todaysMealFont;
  private Font andFont;
  private SimpleLinkedList<Meal> meals;
  private String currentDay;
  private int[][] paddings;

  /** GrocerySideBar
   * Construct the grocery side bar
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The bar width
   * @param height The bar height
   * @param cart The shopping cart
   * @param list The list of meals
   * @param currentDay The current day
   */
  public GrocerySideBar(int x, int y, int width, int height,  ShoppingCart cart, SimpleLinkedList<Meal> list, String currentDay){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.shoppingCart = cart;
    // create the fonts
    this.mealsFont = new Font ("Times New Roman", Font.BOLD, 17 + (height / 300) * 3);
    this.mealsFont2 = new Font ("Times New Roman", Font.BOLD, 15 + (height / 300) * 3);
    this.mealsFont3 = new Font ("Times New Roman", Font.BOLD, 12 + (height / 300) * 3);
    this.todaysMealFont = new Font ("Arial", Font.BOLD, 25 + (height / 300) * 3);
    this.andFont = new Font ("Arial", Font.BOLD, 13 + (height / 300) * 3);
    
    this.meals = list;
    this.currentDay = currentDay;
    
    paddings = new int[meals.size()][2]; // create font paddings for the text
    setMealLabelsPaddings();
  } // end GrocerySideBar

  /** setMealLabelsPaddings
   * Set meal label paddings
   */
  public void setMealLabelsPaddings(){
    for (int i = 0; i < paddings.length; i++){
      paddings[i][0] = width / 20;
      paddings[i][1] = height / 250;
    }
  } // end setMealLabelsPaddings

  /** draw
   * Draw the side bar
   * @param g The Graphics object
   */
  public void draw (Graphics g){
    // the overall area
    g.setColor(Color.decode("#FCE25D"));
    g.fillRect(x,y,width,height);
    
    // write today's meals
    g.setColor(Color.BLACK);
    g.setFont(todaysMealFont);
    g.drawString(currentDay+ "'s", x + 10 , y + (height  /15));
    g.drawString("Meals", x + 10 , y + (height  /9));
    // use the font based on the length of the meal's name so it all fits on the screen
    for (int i = 0; i < meals.size(); i++){
      g.setColor(Color.decode("#2448FF"));
      if (meals.get(i).getName().length() <= 12){
        g.setFont(mealsFont);
      } else if (meals.get(i).getName().length() <= 15){
        g.setFont(mealsFont2);
      } else {
        g.setFont(mealsFont3);
      }
      g.drawString(meals.get(i).getName(),x + paddings[i][0], (int)(y + i * height/12.0 + 50 + (height * 2 /15) ));
    }
    
    // write "and" between each meal
    g.setColor(Color.RED);
    g.setFont(andFont);
    for (int i = 0; i < meals.size() - 1; i++){
      g.drawString("and",x + paddings[i][0],(int)( y + i * height/12.0  + 68 + (height * 2 /15)  ) );
    }
    
    // items in cart and total price of the items
    g.drawString("Total price: " + shoppingCart.getPriceTotal(),x + paddings[0][0], (int)(y  + height *345 / 750.0 ));
    g.setColor(Color.decode("#E61534"));
    g.drawString("Items in Cart" ,x + paddings[0][0], (int)(y +  + paddings[0][1] + height *380 / 750.0 ));
    // draw all the items in the cart
    for (int i = 0; i < shoppingCart.numItems(); i++){
      shoppingCart.get(i).draw(g,x + 7 + (i % 4 * (width / 4 - 7)), (int)(y + (i / 4 * (width / 4 - 7)) + height * 410 / 750.0), (width - 2 * 7) / 4,(width - 2 * 7) / 4 );
    }
  } // end draw

  /** setMealProducts
   * The set current meal products
   * @param mealProducts The meal products for the current round
   */
  public void setMealProducts(SimpleLinkedList<Meal> mealProducts){
    this.meals = mealProducts;
  } // end setMealProducts

  /** setCurrentDay
   * Set the currentDay
   * @param str The current day
   */
  public void setCurrentDay(String str){
    this.currentDay = str;
  } // end setCurrentDay
} // end GrocerySideBar
