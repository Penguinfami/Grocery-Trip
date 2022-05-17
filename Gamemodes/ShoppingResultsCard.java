package Gamemodes;

import Components.FoodProduct;
import Components.Meal;
import Setup.ShopResultsList;
import Setup.ShoppingCart;
import Setup.SimpleLinkedList;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/* Gamemodes.ShoppingResultsCard.java
 * Christine Zhao
 * Tabulates the results for the round's grocery trip, and compares the ingredients pruchased to the ingredients needed
 */
public class ShoppingResultsCard {

  // class fields
  private int x,y;
  private int height, width;
  private ShoppingCart shoppingCart;
  private SimpleLinkedList<Meal> meals;
  // the ordered lists
  private ShopResultsList<Integer> quantityPurchased;
  private ShopResultsList<FoodProduct> orderedCorrectProducts;
  private ShopResultsList<Integer> orderedCorrectQuantity;
  private ShopResultsList<FoodProduct> itemsPurchased;
  // unordered lists
  private SimpleLinkedList<Integer> quantityPerCorrectItem;
  private SimpleLinkedList<FoodProduct> correctIngredients;


  private double percentError;

  // fonts
  private Font font1;
  private Font numFont;
  private Font titleFont;
  private String resultsMessage;

  /** Gamemodes.ShoppingResultsCard
   * Construct the results card
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   * @param cart The player's shopping card
   * @param meals The meals required for this round
   */
  public ShoppingResultsCard(int x, int y, int width, int height, ShoppingCart cart, SimpleLinkedList<Meal> meals){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.shoppingCart = cart;
    this.meals = meals;
    this.font1 = new Font("Times New Roman", Font.ITALIC, 5 + height / 100);
    this.numFont = new Font("Roboto", Font.BOLD, 20);
    this.titleFont = new Font("Times New Roman", Font.BOLD, 12 + height / 80);
    calculateResults();
  } // end Gamemodes.ShoppingResultsCard

  /** draw
   * Draw the results
   * @param g The graphics object
   */
  public void draw (Graphics g){
    g.setColor(Color.decode("#11F71D")); // the background
    g.fillRect(x, y, width, height);
    g.setColor(Color.BLACK); // draw the headings
    g.setFont(titleFont);
    g.drawString(resultsMessage, width / 10, height * 75 / 750);
    g.drawString("Items needed", 320 * width / 750, 100 * height / 750);
    for (int i = 0; i < orderedCorrectProducts.size();i++){ // draw the correct ingredients, 9 per row
      g.setFont(font1);
      g.drawString(orderedCorrectProducts.get(i).getName(), (60 + (i%9)*100) * height / 750,  (140 + i / 9 * 100) * height / 750 );
      orderedCorrectProducts.get(i).draw(g, (60  + i % 9 *100)* height / 750, (150 + i / 9 * 95) * height / 750, 85 * height / 750, 85 * height / 750);
      g.setFont(numFont);
      g.drawString(orderedCorrectQuantity.get(i).toString(), (60   + i % 9 *100 + 80) * height / 750, (160 + i / 9 * 100) * height / 750);

    }
    g.setFont(titleFont);
    g.drawString("Items purchased", 310 * width / 750, 360 * height / 750);
    for (int i = 0; i < itemsPurchased.size();i++){ // draw the purchased items, 9 per row
      g.setFont(font1);
      g.drawString(itemsPurchased.get(i).getName(), (60  +  i% 9*100)* height / 750, (380 + i / 9 * 100) * height / 750);
      itemsPurchased.get(i).draw(g, (60  + i % 9*100) * height / 750, (390 + i /9 * 95) * height / 750, 85 * height / 750, 85 * height / 750);
      g.setFont(numFont);
      g.drawString(quantityPurchased.get(i).toString(), (60   + i % 9*100 + 80)* height / 750, (400 + i / 9 * 100) * height / 750);

    }
    g.setFont(titleFont);
    g.drawString("Percent error: " + percentError, width * 450 / 666 , height / 11); // draw the percent error
  } // end draw

  /** calculateResults
   * Calculate the player's accuracy, percent error and sort the food products in the best order
   */
  public void calculateResults(){

    // get and calculate quantity of the correct ingredients
    quantityPerCorrectItem = new SimpleLinkedList<Integer>(); // create 2 associated linked lists for item + quantity
    correctIngredients = new SimpleLinkedList<FoodProduct>();
    SimpleLinkedList<FoodProduct> currentMealIngredients;
    FoodProduct currentProduct;
    Meal currentMeal;
    int index;
    int totalCorrectItems = 0;
    for (int i = 0; i < meals.size(); i++){
      currentMeal = meals.get(i);
      currentMealIngredients = currentMeal.getIngredients();
      for (int f = 0; f < currentMealIngredients.size(); f++){
        currentProduct = currentMealIngredients.get(f);
        System.out.print(currentProduct.getName());
        if (correctIngredients.contains(currentProduct)){ // if the food product is repeated
          index = correctIngredients.indexOf(currentProduct);
          totalCorrectItems++;
          quantityPerCorrectItem.set(quantityPerCorrectItem.get(index) + 1, index); // add 1 to the quantity
        } else { // if its the first instance of this food product
          correctIngredients.add(currentProduct);
          quantityPerCorrectItem.add(1);
          totalCorrectItems++;
        }
      }
    } 

    // sort it in a priority list by having the items with the largest quantity at the beginning

    orderedCorrectProducts = new ShopResultsList<FoodProduct>();
    orderedCorrectQuantity = new ShopResultsList<Integer>();


    for (int i = 0; i < correctIngredients.size(); i ++){

      orderedCorrectProducts.add(correctIngredients.get(i), quantityPerCorrectItem.get(i)); // the quantity of the product is the priority
      orderedCorrectQuantity.add(quantityPerCorrectItem.get(i), quantityPerCorrectItem.get(i));
    }

    SimpleLinkedList<Integer> quantityPerPurchasedItem = new SimpleLinkedList<Integer>(); // create 2 associated linked lists for item + quantity
    SimpleLinkedList<FoodProduct> itemsInCart = new SimpleLinkedList<FoodProduct>();
    int numInCart;

    while(shoppingCart.numItems() != 0){
      currentProduct = shoppingCart.get(0); // get the first item in the cart
      numInCart = shoppingCart.getQuantityOfProduct(currentProduct); // how many of the product in the cart
      itemsInCart.add(currentProduct);
      quantityPerPurchasedItem.add(numInCart);
      shoppingCart.removeAll(currentProduct); // remove from cart
    }


    // compare the correct ingredients with whats in the shopping cart
    int numMissing = 0;
    int numExtra = 0;
    Integer correctQuantity;
    int numCorrect = 0;
    Integer purchasedQuantity;
    int difference;
    itemsPurchased = new ShopResultsList<FoodProduct>();
    quantityPurchased =  new ShopResultsList<Integer>();
    for (int i = 0; i < itemsInCart.size(); i++){
      currentProduct = itemsInCart.get(i);
      correctQuantity = quantityPerCorrectItem.get(correctIngredients.indexOf(currentProduct));
      if (correctQuantity == null){
        correctQuantity = 0;
      }  
      purchasedQuantity = quantityPerPurchasedItem.get(i);
      if (purchasedQuantity == null){
        purchasedQuantity = 0;
      }  
      difference = correctQuantity - purchasedQuantity;
      if (difference == 0){ // get the difference
        numCorrect+= purchasedQuantity;
      } else if (difference < 0){ // too many
        numExtra+= Math.abs(difference);
        numCorrect+= correctQuantity;
      }  else if (difference > 0){ // not enough
        numCorrect+= purchasedQuantity;
      }  
      System.out.println(currentProduct.getName() + " : Correct: " + correctQuantity + " Purchased : " + purchasedQuantity);
      if (correctQuantity == 0){ // if the product shouldnt have been purchased
        itemsPurchased.add(currentProduct, orderedCorrectProducts.indexOf(currentProduct) ); // set the priority to -1
        quantityPurchased.add(purchasedQuantity, orderedCorrectProducts.indexOf(currentProduct));
      }  else {
        itemsPurchased.add(currentProduct, totalCorrectItems - orderedCorrectProducts.indexOf(currentProduct) ); // the closer it is to the front of the list, the higher the priority
        quantityPurchased.add(purchasedQuantity, totalCorrectItems - orderedCorrectProducts.indexOf(currentProduct));
      }

    }
    numMissing = totalCorrectItems - numCorrect;
    System.out.println("Num correct: " + numCorrect + " Num missing: " + numMissing + " Num extra: " + numExtra + " Total correct items : " + totalCorrectItems);

    percentError = Math.round(((numMissing + numExtra * 1.0 )/ (totalCorrectItems * 1.0) * 100.0)*100.0)/100.0;
    System.out.print("PercentError: " + percentError);

    if (percentError == 0){
      resultsMessage = "CONGRATULATIONS!";
      
    }  else if (percentError < 40){
      resultsMessage = "ALMOST!";
      
    }   else {
      resultsMessage = "FAILURE!";
      
    }  

  } // end calculateResults


  /** getPercentError
   * Get the round's percent error
   * @return The percent error
   */
  public double getPercentError(){
    return percentError;
  } // end getPercentError



} // end Gamemodes.ShoppingResultsCard
