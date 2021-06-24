import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Graphics;

/** EndlessPictureMap
 * @author  Christine Zhao
 *  An endless picture map
 */
public class EndlessPictureMap extends Map {

  // class fields
  private SimpleLinkedList<PictureMap> mapPictures = new SimpleLinkedList<PictureMap>();
  private int x,y;
  private int width, height;
  private int visibleWidth, visibleHeight;
  private BufferedImage mapImage;


  /** EndlessPictureMap
   * Construct and endless picture map
   * @param filepath The path to the map's looping image
   * @param x The starting x coordinate
   * @param y The starting y coordinate
   * @param width The full map's width
   * @param height The full map's height
   * @param screenWidth The screen's visible width
   * @param screenHeight The screen's visible height
   */
  public EndlessPictureMap(String filepath, int x, int y, int width, int height, int screenWidth, int screenHeight){
    this.visibleWidth = screenWidth;
    this.visibleHeight = screenHeight;
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
    loadMap(filepath,x,y);

  } // end EndlessPictureMap

  /** loadMap
   * Load the map images
   * @param filepath The filepath of the image
   * @param x The starting x coordinate
   * @param y The starting y coordinate
   */
  public void loadMap(String filepath, int x, int y){
    try{
      mapImage = ImageIO.read(new File(filepath)); // the image that will loop continuously
    } catch(Exception e){
      System.out.println("error loading endless map pic");
    }

    int numImages = visibleHeight / height + 2; 
    mapPictures.clear();

    // add all the images composing of the endless map, through multiple smaller picture maps
    for (int i = 0; i < numImages; i++){
      mapPictures.add(new PictureMap(filepath, x, height * i + y, width, height, visibleWidth, visibleHeight, true)); // add to the list
    }

  } // end loadMap

  /** draw
   * Draw the map
   * @param g The graphics object
   */
  public void draw(Graphics g){
    PictureMap currentMap; 
    for (int i = 0; i < mapPictures.size();i++){
      currentMap = mapPictures.get(i);
      currentMap.draw(g);
    }
  } // end draw

  /** update
   * Update the map
   * @param xMovement The x movement since last update
   * @param yMovement The y movement since last update
   */
  public void update(int xMovement, int yMovement){
    PictureMap currentMap;

    int listSize = mapPictures.size();
    
    // update the positions of all the smaller picture maps
    for (int i = 0; i < listSize;i++){
      currentMap = mapPictures.get(i);
      currentMap.update(xMovement, yMovement);
     // if the picture map is way off the screen, send it to the opposite end of the screen
      if ((yMovement > 0) && (currentMap.getY() > visibleHeight)){
         currentMap.setY(currentMap.getY() - currentMap.getHeight() * listSize + 50);
      }  else if ((yMovement < 0) && (currentMap.getY() + currentMap.getHeight() < 0)){
        currentMap.setY(currentMap.getY() + currentMap.getHeight()*listSize - 50);
      }
    }
  } // end update

  /** setX
   * Set the x coordinate
   * @param x The x coordinate to set to
   */
  public void setX(int x){
    PictureMap currentMap;
    for (int i = 0; i < mapPictures.size(); i++){
      currentMap = mapPictures.get(i);
      currentMap.setX(x);
    }
  } // end setX
} // end EndlessPictureMap
