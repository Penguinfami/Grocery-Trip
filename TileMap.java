
import java.awt.Graphics;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;

/** TileMap.java
 * @author Christine ZHao
 * A map composed of an array of tiles
 */
public class TileMap extends Map{

  // class fields
  private Tile[][] map;
  private char[][] charMap;
  private char[][] orientationsMap;
  private int boxWidth;
  private int boxHeight;
  private SimpleLinkedList<Tile> collidableTiles = new SimpleLinkedList<Tile>();
  private int roomStartRow, roomStartCol;
  private int numWidthTiles, numHeightTiles;
  private int gameWidth, gameHeight;
  private int x, y;
  private Rectangle startingTile;
  private int numRoomsWidth, numRoomsHeight;

  /** TileMap
   * Construct a tile map
   * @param filepath The txt file containing the tile position information
   * @param gameWidth The screen width
   * @param gameHeight The screen height
   * @param numRoomsWidth The number of rooms per row
   * @param numRoomHeight The number of rooms per column
   */
  public TileMap(String filepath, int gameWidth, int gameHeight, int numRoomsWidth, int numRoomHeight){
    System.out.print("new map");
    this.x = 0;
    this.y = 0;
    this.gameWidth = gameWidth;
    this.gameHeight = gameHeight;
    this.numRoomsHeight = numRoomHeight;
    this.numRoomsWidth = numRoomsWidth;
    createCharMap(filepath);
    this.roomStartCol = 0;
    this.roomStartRow = 0;
    createTileMap();
  }


  /** createCharMap
   * Create the character map representing the tiles
   * @param filepath The text file path with all the characters
   */
  private void createCharMap(String filepath) {

    int width = 0;
    int length = 0;
    String line;

    // Get the length and width of the map
    try{
      BufferedReader reader = new BufferedReader(new FileReader(filepath));
      while ((line = reader.readLine()) != null){
        width = line.split(" ").length * 2;
        length++;
      }
      
      reader.close();
    }catch(IOException e){
      System.out.println("Can't read map");
    }

    // create the arrays of tile characters and orientations
    char[][] charArray = new char[length][width/2];
    char[][] orientationsArray = new char[length][width/2];

    char[] row;

    try{
      BufferedReader reader = new BufferedReader(new FileReader(filepath));
      int lineNum = 0;
      // Add the characters to the array
      while ((line = reader.readLine())!= null){
        row = line.replace(" ", "").toCharArray();
        for (int i = 0; i < width; i++){
          if (i % 2 == 0){ // even index
            charArray[lineNum][i/2] = row[i];
          } else { // odd index
            orientationsArray[lineNum][i/2] = row[i];
          }  
        }
        lineNum++;
      } 
      reader.close();

    }catch(IOException e){
      System.out.println("Can't read map");
    }
    charMap = charArray;
    orientationsMap = orientationsArray;

  } // end createCharMap

  /** getCharMap
   * Get the character map
   * @return The character map
   */
  public char[][] getCharMap(){
    return charMap;
  } // end getCharMap

  /** createTileMap
   * Create all the appropriate tiles for the map
   */
  private void createTileMap(){
    // create a tile array
    map = new Tile[charMap.length][charMap[1].length];
    
    // set the tile/room sizes
    this.numHeightTiles = map.length / numRoomsHeight;
    this.numWidthTiles = map[0].length /numRoomsWidth;
    this.boxWidth = gameWidth/numWidthTiles;
    this.boxHeight = gameHeight/numHeightTiles;
    
    
    int orientation;

    for (int i = 0; i < map.length; i++){
      for (int j = 0; j < map[0].length;j++){
        orientation = "v>^<".indexOf(orientationsMap[i][j]) + 1;
        // create the tile according to the character and orientations
        switch(charMap[i][j]){
          case 'o': // empty space
            map[i][j] = new Ground(j % numWidthTiles *boxWidth + x, i % numHeightTiles*boxHeight + y,boxWidth,boxHeight,
                    "grocery_images/floor.png");
            break;
          case 'O': // starting tile empty space
            map[i][j] = new Ground(j % numWidthTiles *boxWidth + x, i % numHeightTiles*boxHeight + y ,boxWidth,boxHeight,
                    "grocery_images/startingfloor.png");
            startingTile = new Rectangle (j % numWidthTiles *boxWidth + x, i % numHeightTiles*boxHeight ,boxWidth,boxHeight);
            break;  
          case 'B':  // bread aisle
            map[i][j] = new Aisle<FoodProduct>(j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth, boxHeight,
                    "grocery_images/breadAisle.png", "grocery_images/breadAisle.png", orientation);
            break;
          case 'b':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth, boxHeight,
                    "grocery_images/breadAisle.png", orientation);
            break;
          case 'M': // meats aisle
            map[i][j] = new Aisle<FoodProduct>( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y ,
                    boxWidth, boxHeight, "grocery_images/meatAisle.png", "grocery_images/meatAisle.png", orientation);
            break;
          case 'm':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth,
                    boxHeight, "grocery_images/meatAisle.png", orientation);
            break;
          case 'V': //vegetable aisle
            map[i][j] = new Aisle<FoodProduct>( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y,
                    boxWidth, boxHeight, "grocery_images/vegetableAisle.png", "grocery_images/vegetableAisle.png", orientation);
            break;  
          case 'v':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth,
                    boxHeight, "grocery_images/vegetableAisle.png", orientation);
            break;  
          case 'S': // sauces/condiments
            map[i][j] = new Aisle<FoodProduct>( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y ,
                    boxWidth, boxHeight, "grocery_images/sauceAisle.png", "grocery_images/sauceAisle.png", orientation);
            break;
          case 's':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth,
                    boxHeight, "grocery_images/sauceAisle.png", orientation);
            break;
          case 'D': // dairy aisle
            map[i][j] = new Aisle<FoodProduct>( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y ,
                    boxWidth, boxHeight, "grocery_images/dairyAisle.png", "grocery_images/dairyAisle.png", orientation);
            break;
          case 'd':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth,
                    boxHeight, "grocery_images/dairyAisle.png", orientation);
            break;
          case 'R': // other/extras aisle
            map[i][j] = new Aisle<FoodProduct>( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y ,
                    boxWidth, boxHeight, "grocery_images/otherAisle.png", "grocery_images/otherAisle.png", orientation);
            break;
          case 'r':
            map[i][j] = new EmptyAisle( j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight  + y, boxWidth,
                    boxHeight, "grocery_images/otherAisle.png", orientation);
            break;
          case 'C': // cashier
            map[i][j] = new Cashier(j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y , boxWidth,
                    boxHeight, "grocery_images/cashierTile.png", "grocery_images/cashierTile.png", orientation);
            break;
          case 'p': // parking space
          case 'P':
            map[i][j] = new ParkingSpace(j % numWidthTiles * boxWidth + x, i % numHeightTiles*boxHeight + y ,
                    boxWidth, boxHeight, "grocery_images/parkingspace" + orientation + ".png",  "grocery_images/parkingspacehl" + orientation + ".png");
            break;
          case 'X': // empty road starting tile
            startingTile = new Rectangle (j % numWidthTiles *boxWidth + x, i % numHeightTiles*boxHeight + y ,boxWidth,boxHeight);
          case 'x': // empty road
            map[i][j] = new Ground(j % numWidthTiles *boxWidth + x, i % numHeightTiles*boxHeight + y ,boxWidth,boxHeight,
                    "grocery_images/emptyroad.png");
            break;

        }
        // if the tile is collidable, add to the list of collidable tiles
        if (map[i][j].isCollidable()){
          collidableTiles.add(map[i][j]);
        }
      }
    }
  } // end createTileMap

  /** draw
   * Draw the tile map
   * @param g The graphics object
   */
  public void draw (Graphics g){
    BasicStroke borderStroke = new BasicStroke(2); // create a new stroke
    Stroke previousStroke = ((Graphics2D)g).getStroke();
    ((Graphics2D)g).setStroke(borderStroke);
    for (int i = roomStartRow; i < roomStartRow + numHeightTiles; i++){
      for (int j = roomStartCol; j < roomStartCol + numWidthTiles; j++){
        map[i][j].draw(g);
        if (map[i][j] instanceof Aisle){ // surround the aisle tiles with a yellow border to indicate they are aisles
          g.setColor(Color.YELLOW);
          g.drawRect(map[i][j].getXPosMap(),map[i][j].getYPosMap(), map[i][j].getWidth(),map[i][j].getHeight());
        }
      }
    }
    ((Graphics2D)g).setStroke(previousStroke); // set the stroke back to the original
  }

  /** getTile
   * Get a specific tile
   * @param row The tile's row number
   * @param col The tile's column number
   * @return
   */
  public Tile getTile(int row, int col){
    return map[row][col];
  } // end getTile

  /** getX
   * Get the starting x coordinate
   * @return The x coordinate
   */
  public int getX(){
    return x;
  }

  /** getY
   * Get the starting y coordinate
   * @return The y coordinate
   */
  public int getY(){
    return y;
  }

  /** getLength
   * Get the number of tile rows
   * @return The number of tile rows
   */
  public int getLength(){
    return map.length;
  }

  /** getWidth
   * Get the number of tile Columns
   * @return The number of tile columns
   */
  public int getWidth(){
    return map[0].length;
  }

  /** getDisplayLength
   * Get the display height on the screen
   * @return The display height
   */
  public int getDisplayLength(){
    return gameHeight;
  }

  /** getDisplayWidth
   * Get the display width on the screen
   * @return The display width
   */
  public int getDisplayWidth(){
    return gameWidth;
  }

  /** getCollidableTiles
   * Get the list of all collidable tiles in the map
   * @return The list of collidable tiles
   */
  public SimpleLinkedList<Tile> getCollidableTiles(){
    return collidableTiles;
  }

  /** setRoomStartRow
   * Set the tile row number to begin displaying
   * @param num The row number
   */
  public void setRoomStartRow(int num){
    this.roomStartRow = num;
  }

  /** setRoomStartCol
   * Set the tile column number to begin displaying
   * @param num The column number
   */
  public void setRoomStartCol(int num){
    this.roomStartCol = num;
  }

  /** setNumWidthTiles
   * Set the number of width tiles
   * @param num The number of width tiles displayed
   */
  public void setNumWidthTiles(int num){
    this.numWidthTiles = num;
  }

  /** setNumHeightTiles
   * Set the number of height tiles
   * @param num The number of height tiles displayed
   */
  public void setNumHeightTiles(int num){
    this.numHeightTiles = num;
  }

  /** getRoomStartRow
   * Get the starting row when displaying
   * @return The starting row displayed
   */
  public int getRoomStartRow(){
    return roomStartRow;
  }

  /** getRoomStartCol
   * Get the starting col when displaying
   * @return The starting row displayed
   */
  public int getRoomStartCol(){
    return roomStartCol;
  }

  /** getNumWidthTiles
   * Get the number of tiles that make up the width of the screen
   * @return The number of width tiles
   */
  public int getNumWidthTiles(){
    return numWidthTiles;
  }

  /** getNumHeightTiles
   * Get the number of tiles that make up the width of the screen
   * @return The number of width tiles
   */
  public int getNumHeightTiles(){
    return numHeightTiles;
  }

  /** getStartingTile
   * Get the map's allocated starting tile area
   * @return The rectangle area of the starting tile
   */
  public Rectangle getStartingTile(){
    return startingTile;
  }  

}  
    
