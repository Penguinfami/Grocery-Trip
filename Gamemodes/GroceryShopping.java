package Gamemodes;

import Components.*;
import Players.ShopPlayer;
import Setup.GameTimer;
import Setup.SimpleLinkedList;
import Setup.ShoppingCart;

import java.awt.Graphics;
import java.awt.Color;
import java.io.FileReader;
import java.io.BufferedReader;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Font;

/** Gamemodes.GroceryShopping.java
 * @author Christine Zhao
 * A shopping game with a shopping cart and aisles
 */
public class GroceryShopping extends Minigame {

  // class fields
  private ShopPlayer player;
  private TileMap map;
  private int detectiveFieldLength;
  private Tile currentHighlightedTile;
  private ShoppingCart shoppingCart;
  private int roomNumber;
  private int screenHeight, screenWidth;
  private int shopScreenHeight, shopScreenWidth;
  private GrocerySideBar sideBar;
  private SimpleLinkedList<Meal> roundMealProducts;
  // the lists of the separate food groups
  private SimpleLinkedList<FoodProduct> meats = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<FoodProduct> breads = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<FoodProduct> dairy = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<FoodProduct> otherProducts = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<FoodProduct> vegetables = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<FoodProduct> sauceCondiments = new SimpleLinkedList<FoodProduct>();
  private SimpleLinkedList<SimpleLinkedList<FoodProduct>> foodGroups = new SimpleLinkedList<SimpleLinkedList<FoodProduct>>();
  
  private Meal[] allPossibleMeals = new Meal[31]; // The list of all possible meals
  private SimpleLinkedList<Meal> allPastMeals;
  // product info
  private ProductInfoCard<FoodProduct> productInfo;
  private boolean productInfoVisible;
  
  private boolean invalidMessage;
  private Font invalidMsgFont;
  
  // general variables
  private String dayString;
  private boolean isFinished;
  private int gameDifficulty;
  private BufferedImage controls;


  /** Gamemodes.GroceryShopping
   * Construct a grocery shopping game
   * @param screenWidth The screen width
   * @param screenHeight The screen height
   * @param shopScreenWidth The width of the grocery store area
   * @param shopScreenHeight The height of the grocery store area
   * @param day The current day
   * @param difficulty The game difficulty
   */
  public GroceryShopping(int screenWidth,int screenHeight, int shopScreenWidth, int shopScreenHeight,String day, int difficulty){
    this.gameDifficulty = difficulty;
    this.dayString = day;
    this.detectiveFieldLength = 30 * screenHeight / 750;
    this.shoppingCart = new ShoppingCart(); // create a shopping cart
    this.screenHeight = screenHeight;
    this.screenWidth = screenWidth;
    this.roomNumber = 1;
    this.shopScreenHeight = screenHeight;
    this.shopScreenWidth = screenHeight; // shop screen is a square
    this.map = new TileMap("Info/map.txt", shopScreenWidth, shopScreenHeight, 2, 2);
    this.roundMealProducts = new SimpleLinkedList<Meal>();
    this.allPastMeals = new SimpleLinkedList<Meal>(); 
    this.player = new ShopPlayer(shopScreenWidth, shopScreenHeight, map.getNumWidthTiles(),map.getNumHeightTiles()); // create the player
    player.setSpeed(screenHeight * 400 / 800);
    this.productInfo = new ProductInfoCard<FoodProduct>(shopScreenWidth / 20, shopScreenHeight /20, shopScreenWidth * 8 / 10, shopScreenHeight * 210 / 500 ); // create an info cart
    this.isFinished = false;
    this.invalidMsgFont = new Font("Rock Salt", Font.BOLD, 13 + (screenHeight/100)*3);
    try{
      controls = ImageIO.read(new File("grocery_images/grocerycontrols.png"));
    } catch(Exception e){
      System.out.println("Error reading controls image");
    }
    createFoodProducts();
    distributeProducts();

  } // end Gamemodes.GroceryShopping

  /** isFinished
   * Is the game finished
   * @return True or false if the game is finished
   */
  public boolean isFinished(){
    return isFinished;
  } // end isFinished

  /** update
   * Update the game
   * @param elapsedTime The elapsed time since previous update
   */
  public void update(double elapsedTime){
    int xDirection = player.getDisplacementX(elapsedTime);
    int yDirection = player.getDisplacementY(elapsedTime);

    // if the player's movement is valid / won't collide with objects
    Rectangle currentArea = player.getFutureCollisionArea();
    if (isValidPosition(new Rectangle ( xDirection,  yDirection,currentArea.width, currentArea.height) )){ // if the player will occupy a free space, update the position
      if (player.getX() < -30 || player.getX() > shopScreenWidth || player.getY() < -30 || player.getY() > shopScreenHeight){
        changeRooms();
        player.setMove(false);
      } else {
        player.setMove(true);
      }
    } else { // otherwise, dont move the player
      player.setMove(false);
      player.setXDirection(0);
      player.setYDirection(0);
    }

    player.update(elapsedTime);
    seeNearestInteractiveTile(); // highlight the nearest highlightable tile to the player
    
  } // end update

  /** draw
   * Draw the game
   * @param g The graphics object
   */
  public void draw(Graphics g){
    map.draw(g);
    player.draw(g);
    sideBar.draw(g);
    g.drawImage(controls, 3 * shopScreenHeight / 500, -10 * screenHeight / 500 , 250 * screenHeight / 500,180 * screenHeight / 500, null);
    if (productInfoVisible){
      productInfo.draw(g);
    }
    
    // if the invalid message is shown, draw the string with a black border around the letters
    if (invalidMessage){
      g.setColor(Color.BLACK);
      g.setFont(invalidMsgFont);
      // the border
      g.drawString("You don't have any of this product to return", 30 * screenHeight/500 + 2, 50* screenHeight/500 - 2);
      g.drawString("You don't have any of this product to return", 30* screenHeight/500 + 2, 50* screenHeight/500 + 2);
      g.drawString("You don't have any of this product to return", 30* screenHeight/500 - 2, 50* screenHeight/500 - 2);
      g.drawString("You don't have any of this product to return", 30* screenHeight/500 - 2, 50* screenHeight/500 + 2);

      g.setColor(Color.RED);
      g.drawString("You don't have any of this product to return", 30* screenHeight/500, 50* screenHeight/500);

    }
  } // end draw

  /** isValidPosition
   * Checks if the a rectangle intersects any collidable map tiles or moves outside the screen
   * @param boxAfterMovement The player's collision area after next update
   * @return True if there is no collision, false if there is
   */
  private boolean isValidPosition( Rectangle boxAfterMovement){
    Tile currentTile;
    Rectangle tileBox;
    // check all the current tiles that are displayed and if they collide with the player area rectangle
    for (int i = map.getRoomStartRow(); i < map.getRoomStartRow() + map.getNumHeightTiles(); i++){
      for (int j = map.getRoomStartCol(); j < map.getRoomStartCol() + map.getNumWidthTiles(); j++) {
        currentTile = map.getTile(i,j);
        if (currentTile.isCollidable()) { // if the tile is collidable, check if it intersects with the rectangle area
          if (currentTile instanceof Aisle) {
            tileBox = ((Aisle<FoodProduct>) currentTile).getCollisionArea();
          } else if (currentTile instanceof EmptyAisle) {
            tileBox = ((EmptyAisle) currentTile).getCollisionArea();
          } else {
            tileBox = ((Cashier)currentTile).getCollisionArea();
          }
          if (tileBox.intersects(boxAfterMovement)) {
            return false;
          }
        }
      }
    }
    // if the player is about to move off the screen
    if (roomNumber == 1 && (boxAfterMovement.x < 0 || boxAfterMovement.y < 0)){
      return false;
    } else if (roomNumber == 2 && (boxAfterMovement.x + boxAfterMovement.width > shopScreenWidth || boxAfterMovement.y < 0)){
      return false;
    } else if (roomNumber == 3 &&(boxAfterMovement.x < 0 || boxAfterMovement.y + boxAfterMovement.height > shopScreenHeight)){
      return false;
    } else if (roomNumber == 4 && (boxAfterMovement.x + boxAfterMovement.width > shopScreenWidth || boxAfterMovement.y + boxAfterMovement.height > shopScreenHeight)){
      return false;
    }
    return true;
  } // end isValidPosition

  /** seeNearestInteractiveTile
   * If there are any highlightable tiles within the detective field length, highlight the nearest one
   */
  public void seeNearestInteractiveTile(){
    // create a bounding/detection box that increases in size depending on the detective field length
    Rectangle playerBox = new Rectangle (player.getX() - detectiveFieldLength, player.getY() - detectiveFieldLength, player.getWidth() + 2*detectiveFieldLength, player.getHeight() + 2*detectiveFieldLength);
    Rectangle currentIntersection = new Rectangle();
    Tile currentTile;
    Tile closestTile = null;
    int largestIntersectionArea = 0;
    int currentIntersectionArea;

     // check all the current tiles that are displayed 
    for (int i = map.getRoomStartRow(); i < map.getRoomStartRow() + map.getNumHeightTiles(); i++){
      for (int j = map.getRoomStartCol(); j < map.getRoomStartCol() + map.getNumWidthTiles(); j++){
        currentTile = map.getTile(i,j);
        // get the total intersection area
        if (currentTile instanceof Aisle ){
          currentIntersection = playerBox.intersection(((Aisle<FoodProduct>)currentTile).getCollisionArea());
        }  else   if (currentTile instanceof Cashier){
          currentIntersection = playerBox.intersection(((Cashier)currentTile).getCollisionArea());
        }
        if (!currentIntersection.isEmpty()){ // if there is an intersection
          currentIntersectionArea = currentIntersection.height * currentIntersection.width;
          if (currentIntersectionArea > largestIntersectionArea){// if the intersection area is larger,
            largestIntersectionArea = currentIntersectionArea;
            closestTile = currentTile; //set is as the current closest tile
          }
        }
      }
    }
    
    // if there is a new closest highlightable tile, unhihglight the previoys highlighted tile
    if (closestTile != currentHighlightedTile){
      if (currentHighlightedTile != null) {
        if (currentHighlightedTile instanceof Aisle){
          ((Aisle) currentHighlightedTile).unHighlight(); // un-highlight the current highlighted tile
        }else if (currentHighlightedTile instanceof Cashier){
          ((Cashier) currentHighlightedTile).unHighlight(); // un-highlight the current highlighted tile
        }
      }
      
      // if the nearest highlighted tile is an aisle, update the display product info card and highlight the tile
      if (closestTile != null){
        if (closestTile instanceof Aisle){
          ((Aisle<FoodProduct>)closestTile).highlight(); // highlight the tile to show the player it is interactive
          productInfo.setProduct((FoodProduct)(((Aisle) closestTile).getItem()));
          if (player.getY() < shopScreenHeight / 2){ // make sure the info card does not cover the player
            productInfo.setY(shopScreenHeight * 260 / 500);
          } else {
            productInfo.setY(shopScreenHeight * 40/ 500);
          }
          productInfoVisible = true;
        } else if (closestTile instanceof Cashier){
          productInfoVisible = false;
          ((Cashier)closestTile).highlight(); // highlight the tile to show the player it is interactive
        }
      }   else {
        productInfoVisible = false;
      }
    }

    currentHighlightedTile = closestTile; // set the current highlighted tile as the closest one within the detection field
  } // end seeNearestInteractiveTile

  /** checkOutItem
   * Add the highlighted aisle's, if exists, item to the shopping cart
   */
  public void checkOutItem(){
    
    if (currentHighlightedTile != null){ // if there is a current highlighted tile
      if (currentHighlightedTile instanceof Aisle){ // if the player is at an aisle, add the item to cart
        shoppingCart.add((FoodProduct)((Aisle)currentHighlightedTile).getItem());
      } else if (currentHighlightedTile instanceof Cashier){ // if the player is at the cashier, proceed to the next minigame
        System.out.print("Items.Cashier time");
        isFinished = true;
      }     
    }  else {
      System.out.println("No item to get");
    }
  } // end checkOutItem

  /** returnItem
   * Return the item to the highlighted tile and remove from cart
   */
  public void returnItem(){
    if (currentHighlightedTile != null){
      FoodProduct itemInAisle = (FoodProduct)(((Aisle)currentHighlightedTile).getItem());
      if (shoppingCart.contains(itemInAisle)){ // if the shopping cart contains the item, remove it from the cart
        shoppingCart.removeLatest(itemInAisle);
        invalidMessage = false;
      }  else { // otherwise, display the error message
        new GameTimer(this, "don't have this item", 5000);
        invalidMessage = true;
        System.out.println("We don't have " + itemInAisle);
      }
    }
  } // end returnItem

  /** removeInvalidMessage
   * Remove the invalid message
   */
  public void removeInvalidMessage(){
    invalidMessage = false;
  } // end removeInvalidMessage

  /** changeRooms
   * Change the map's current room based on the player's position
   */
  public void changeRooms(){
    int numWidthTiles = map.getNumWidthTiles();
    int numHeightTiles = map.getNumHeightTiles();

    // change rooms according to the current room and the player's location
    if (player.getX() < -30 ){
      if (roomNumber == 2){
        roomNumber = 1;
        map.setRoomStartCol(0);
        map.setRoomStartRow(0);
        player.setX(shopScreenWidth - player.getWidth());
      } else if (roomNumber == 4){
        roomNumber = 3;
        map.setRoomStartCol(0);
        map.setRoomStartRow(numHeightTiles);
        player.setX(shopScreenWidth - player.getWidth());
      }
    } else if (player.getX() > shopScreenWidth){
      if (roomNumber == 1){
        roomNumber = 2;
        map.setRoomStartCol(numWidthTiles);
        map.setRoomStartRow(0);
        player.setX(0);
      } else if (roomNumber == 3){
        roomNumber = 4;
        map.setRoomStartCol(numWidthTiles);
        map.setRoomStartRow(numHeightTiles);
        player.setX(0);
      }
    } else if (player.getY() > shopScreenHeight){
      if (roomNumber == 1){
        roomNumber = 3;
        map.setRoomStartCol(0);
        map.setRoomStartRow(numHeightTiles);
        player.setY(0);
      } else if (roomNumber == 2){
        roomNumber = 4;
        map.setRoomStartCol(numWidthTiles);
        map.setRoomStartRow(numHeightTiles);
        player.setY(0);
      }
    }else if (player.getY() < -30){
      if (roomNumber == 3){
        roomNumber = 1;
        map.setRoomStartCol(0);
        map.setRoomStartRow(0);
        player.setY(shopScreenHeight - 100);
      } else if (roomNumber == 4){
        roomNumber = 2;
        map.setRoomStartCol(numWidthTiles);
        map.setRoomStartRow(0);
        player.setY(shopScreenHeight - player.getHeight());
      }
    }

  } // end changeRooms

  /** newRound
   * Start a new round
   */
  public void newRound(){
    map.setRoomStartCol(0);
    map.setRoomStartRow(0);
    player.setXDirection(0); // stop the player from moving if it currently is
    player.setYDirection(0);
   // proceed to next day
    distributeProducts(); // redistribute the food products randomly in the aisles
    roundMealProducts.clear();
    shoppingCart.empty();
    for (int i = 0; i < gameDifficulty; i++){
      roundMealProducts.add(allPossibleMeals[(int) (Math.random() * 31)]); // randomize the meals for the round
    }

    for (int i = 0; i < roundMealProducts.size(); i++){
      allPastMeals.add(roundMealProducts.get(i)); // add them to the list of all attempted meals
    }
    this.sideBar = new GrocerySideBar(shopScreenWidth,0 , screenWidth - shopScreenWidth, shopScreenHeight,
            shoppingCart,roundMealProducts, dayString); // create a new side bar
    
    // set the player's starting position
    Rectangle startingTile = map.getStartingTile();
    player.setX(startingTile.x);
    player.setY(startingTile.y);
    player.setCurrentSpriteRow(0);

    roomNumber = 1; // start at room no 1
    isFinished = false;
  } // end newRound



  /** clear
   * Clear the current round's used food products and meals
   */
  public void clear(){
    roundMealProducts.clear();
    allPastMeals.clear();
    shoppingCart.empty();
  } // end clear

  /** createFoodProducts
   * Create all the food products to use
   */
  public void createFoodProducts(){
    try{
      BufferedReader reader = new BufferedReader(new FileReader ("Info/foodProducts.txt"));
      String line;
      String name;
      
      // add all the food group lists
      foodGroups.add(otherProducts);
      foodGroups.add(breads);
      foodGroups.add(dairy);
      foodGroups.add(vegetables);
      foodGroups.add(meats);
      foodGroups.add(sauceCondiments);

      // get all the food products, organized by food group
      for (int i = 0; i < foodGroups.size(); i++){
        line = reader.readLine();
        String[] items = line.split(",");
        for (int item = 0; item < items.length; item++){
          name = items[item].trim(); // get the name of the ingredient
          foodGroups.get(i).add(new FoodProduct(name,  (double)(Math.round(Math.random() * (10 + 3.3*gameDifficulty) * 100) / 100.0), "food/"+name+".png"));
        }
      }
      
      reader.close();
    } catch (Exception e){
      System.out.println("error reading food products");
    }
    try{
      BufferedReader reader = new BufferedReader(new FileReader ("Info/meals.txt"));
      String line;
      int index = 0;
      while ((line = reader.readLine()) != null){
        
        SimpleLinkedList<FoodProduct> ingredients = new SimpleLinkedList<FoodProduct>();
        
        String mealName = line.substring(0, line.indexOf("-")).trim(); // get the name of the meal

        line = line.substring(line.indexOf("-") + 1).trim(); // remove meal name from string
        String[] ingredientsStrings = line.split(","); // split into the different ingredients
        
        // get all the ingredients for all the meals meals
        for (int i = 0; i < ingredientsStrings.length; i++){
          String str = ingredientsStrings[i].trim();
          for (int foodGroup = 0; foodGroup < foodGroups.size(); foodGroup++){
            for (int product = 0; product < foodGroups.get(foodGroup).size(); product++){
              if (foodGroups.get(foodGroup).get(product).getName().equalsIgnoreCase(str)){ // if the meal's ingredient matches the name in the list, add it to the ingredients list
                ingredients.add(foodGroups.get(foodGroup).get(product));
              }
            }
          }
        }
        allPossibleMeals[index] = new Meal(mealName, ingredients); // create the new meal with the ingredients and the name
        index++;
      }
      
      reader.close();
    } catch(Exception e){
      System.out.println("error reading meals");
    }
  } // end createFoodProducts

  /** distributeProducts
   * Distribute all the food products to the correct aisles randomly
   */
  public void distributeProducts(){
    char[][] charTileMap = map.getCharMap();
    SimpleLinkedList<SimpleLinkedList<Integer>> indexes = new SimpleLinkedList<SimpleLinkedList<Integer>>();
    
    // get a list of total number of products for each food group
    for (int foodGroup = 0; foodGroup < foodGroups.size(); foodGroup++){
      indexes.add(new SimpleLinkedList<Integer>());
      for (int product = 0; product < foodGroups.get(foodGroup).size(); product++){
        indexes.get(foodGroup).add(product);
      }
    }


    int index;
    int foodGroupNumber = 0;

    // randomly distribute by getting a random integer from the indexes linked list
    for (int i = 0; i < charTileMap.length; i++){
      for (int j = 0; j < charTileMap[0].length; j++){
        if ("RBDVMS".indexOf(charTileMap[i][j]) != -1) {
          foodGroupNumber = "RBDVMS".indexOf(charTileMap[i][j]); // find the group number
          index = (int)(Math.random() * indexes.get(foodGroupNumber).size()); // get a random index

          FoodProduct item = foodGroups.get(foodGroupNumber).get(indexes.get(foodGroupNumber).get(index));
          ((Aisle) map.getTile(i, j)).setItem(item); // add the corresponding product to the aisle
          indexes.get(foodGroupNumber).delete(index); // remove from the list of possible indexes
        }
      }
    }
  } // end distrubuteproducts

  /** getShoppingCard
   * Get the shopping get
   * @return The shopping cart
   */
  public ShoppingCart getShoppingCart(){
    return shoppingCart;
  } // end getShoppingCart

  /** getRoundMealProducts
   * Get the current round's meals' products
   * @return The current round's meals
   */
  public SimpleLinkedList<Meal> getRoundMealProducts(){
    return roundMealProducts;
  } // end getRoundMealProducts

  /** getAllPastMeals
   * Get all attempted meals
   * @return All past meals
   */
  public SimpleLinkedList<Meal> getAllPastMeals(){
    return allPastMeals;
  } // end getAllPAstMeals

  /** playerMoveUp
   * Move the player up
   */
  public void playerMoveUp(){
    player.moveUp();
  } // end playerMoveUp

  /** playerMoveDown
   * Move the player down
   */
  public void playerMoveDown(){
    player.moveDown();
  } // end playerMoveDown

  /** playerMoveLeft
   * Move the player left
   */
  public void playerMoveLeft(){
    player.moveLeft();
  } // end playerMoveLeft

  /** playerMoveRight
   * Move the player right
   */
  public void playerMoveRight(){
    player.moveRight();
  } // end playerMoveRight

  /** playerStopMoveUp
   * Stop the player moving up
   */
  public void playerStopMoveUp(){
    player.stopMoveUp();
  } // end playerStopMoveUp

  /** playerStopMoveDown
   * Stop the player moving down
   */
  public void playerStopMoveDown(){
    player.stopMoveDown();
  } // end playerStopMoveDown

  /** playerStopMoveLeft
   * Stop the player moving left
   */
  public void playerStopMoveLeft(){
    player.stopMoveLeft();
  } // end playerStopMoveLeft

  /** playerStopMoveRight
   * Stop the player moving right
   */
  public void playerStopMoveRight(){
    player.stopMoveRight();
  } // end playerStopMoveRight

  /** setCurrentDay
   * Set the currentDay
   * @param str The current day
   */
  public void setCurrentDay(String str){
    this.dayString = str;
  } // end setCurrentDay
} // end Gamemodes.GroceryShopping
