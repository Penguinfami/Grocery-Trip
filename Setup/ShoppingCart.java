package Setup;

import Components.FoodProduct;
import Setup.SimpleLinkedList;

/** Setup.ShoppingCart.java
 * @author Christine Zhao
 * A shopping cart that manages food objects
 */

public class ShoppingCart {

  // class fields
  private SimpleLinkedList<FoodProduct> foodItems = new SimpleLinkedList<FoodProduct>();


  /** add
   * Add an item to the cart
   * @param f The Components.FoodProduct to add
   */
  public void add (FoodProduct f){
    foodItems.add(f);
  } // end add

  /** removeAll
   * Remove all of a specific food from the cart
   * @param f The food to remove
   */
  public void removeAll (FoodProduct f){
    foodItems.deleteAll(f);
  } // end removeAll

  /** removeLatest
   * Remove the last instance of a food in the cart
   * @param f The food to remove
   */
  public void removeLatest(FoodProduct f){
    foodItems.deleteLast(f);
  } // end removeLatest

  /** empty
   * Clear the cart
   */
  public void empty (){
    foodItems.clear();
  } // end empty

  /** contains
   *  Checks if the cart is currently holding a specific food
   * @param f The food to check for
   * @return True or False if it contains the food or not
   */
  public boolean contains(FoodProduct f){
    return foodItems.contains(f);
  } // end contains

  /** numItems
   * Get the total number of (not unique) items in the cart
   * @return The number of items in the cart
   */
  public int numItems(){
    return foodItems.size();
  } // end size

  /** get
   * Get the Components.FoodProduct at a specific position in the cart
   * @param index The index position
   * @return The Components.FoodProduct at the index
   */
  public FoodProduct get(int index){
    return foodItems.get(index);
  }
  
   /** getPriceTotal
   * Get the total sum of the prices of all items in the cart
   * @return The total cost
   */ 
  public double getPriceTotal(){
    double total = 0;
    for (int i = 0; i < foodItems.size(); i++){
      total += ((FoodProduct)foodItems.get(i)).getPrice();
    }  
    return Math.round(total * 100.0) / 100.0 ;
  }

  /** getQuantityOfProduct
   * Get the number of occurences of a product
   * @param f The food product
   * @return The number of times in appears in the cart
   */
  public int getQuantityOfProduct(FoodProduct f){
    int currentIndex = 0;
    int numOccurences = 0;
    while (currentIndex < foodItems.size() && currentIndex != -1){
      currentIndex = foodItems.indexOf(f, currentIndex);
      if (currentIndex != -1){ // if it occurs in the list
        numOccurences++;
        currentIndex++;
      }
    }
    return numOccurences;
  }

} // end Setup.ShoppingCart

