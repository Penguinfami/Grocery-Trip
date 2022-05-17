package Players;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Point;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/** Players.CarPlayer
 * @author Christine Zhao
 * A controllable car
 */
public class CarPlayer extends Player {

  // class fields
  private double angle;
  private double turnSpeed;
  private BufferedImage image;
  private Rectangle collisionArea;
  private Point anchorPoint;
  private int rotateDirection;
  private Double minRotation, maxRotation;
  private int yDirection;
  private int driveSpeed;
  private int gasDriveSpeed;
  private AffineTransform transform = new AffineTransform();
  private String imageFile;
  private int carGear;
  private double rotatedX, rotatedY;
  private double screenX, screenY; // The car's coordinates on the screen, compared to the car's true transformed coordinates
  private boolean move;

  /** Players.CarPlayer
   * Construct a car player
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The player's width
   * @param height The player's height
   * @param minRotation The minimum angle the car can rotate
   * @param maxRotation The maximum angle the car can rotate
   * @param filepath The filepath of the car's image
   */
  public CarPlayer(int x, int y, int width, int height, Double minRotation, Double maxRotation, String filepath) {
    super(x,y,width,height);
    rotatedX = x;
    rotatedY = y;
    loadSprite(filepath);
    screenX = getX() + getWidth() / 2; // The center x point
    screenY = getY() + getHeight() /2; // the center y point
    this.anchorPoint = new Point (getX() + getWidth()/2, getY() + getHeight() /2); // Get the centre of the car
    this.angle = 0d;
    this.minRotation = minRotation;
    this.maxRotation = maxRotation;
    this.collisionArea = new Rectangle(getX(),getY(),getWidth(),getHeight()); // the collision area
    this.turnSpeed = 80;
    this.driveSpeed = 200;
    this.gasDriveSpeed = 300;
    this.imageFile = filepath;
    this.carGear = 1;
  } // end Players.CarPlayer

  /** loadSprite
   * Load the car image
   * @param filepath The filepath
   */
  public void loadSprite(String filepath){
    try{
      image = ImageIO.read(new File(filepath));
    } catch (Exception e){
      System.out.println("Error loading car player image");
    }
  } // end loadSprite

  /** draw
   * Draw the car
   * @param g The Graphics object
   */
  public void draw(Graphics g){
    if (image != null){ 
      Graphics2D g2d = (Graphics2D)g.create();
      g2d.transform(transform); // rotate the graphics
      g2d.drawImage(image, getX(), getY(), getWidth(), getHeight(), null); // draw the car image
    }
  } // end draw

  /** update
   * Update the car's position and angle
   * @param elapsedTime The elapsed time since the previous update
   */
  public void update(double elapsedTime){

    if (move){

      double pedalTravel;

      if (yDirection > 0){ // if the car is moving forward
        pedalTravel = yDirection * elapsedTime * gasDriveSpeed;
      } else { // if the car is braking
        pedalTravel = yDirection * elapsedTime * gasDriveSpeed / 3;
      }
      double xDisplacement;
      double yDisplacement;
      xDisplacement = ((Math.sin(Math.toRadians(angle))) * (pedalTravel))  * carGear; // The horizontal movement
      yDisplacement = ((Math.cos(Math.toRadians(angle))) * (pedalTravel))  * carGear; // The vertical movement
      
      rotatedY -= (pedalTravel * 1.0 * carGear); // The car drives forward 
      
      setY((int)rotatedY); // set the new y position
      
      screenY -= yDisplacement; // Items.Car moves up the screen
      screenX += xDisplacement; // Add the x displacement to the car's position

    }  
    anchorPoint.x = (int)(rotatedX + getWidth() / 2); // new anchor/centre point
    anchorPoint.y = (int)(rotatedY + getHeight() / 2); 
    double futureAngle = angle + (rotateDirection * elapsedTime * turnSpeed);
    
    if (futureAngle != angle) { // if the car has a difference in angle
      if (minRotation == null && maxRotation == null){ // if there is no max/min rotation angle
        double rads = Math.toRadians(futureAngle - angle);
        transform.rotate(rads, anchorPoint.x,  anchorPoint.y); // rotate the affine transform
        angle = (futureAngle - angle) + angle; // add to the total angle
      } else {
        if (futureAngle - angle + angle > minRotation && futureAngle - angle + angle < maxRotation) { // if the angle is within the min and max
          double rads = Math.toRadians(futureAngle - angle);
          transform.rotate(rads, anchorPoint.x, anchorPoint.y); // rotate the affine transform
          angle = (futureAngle - angle) + angle; // add to the total angle
        }
      }
    }
    
  } // end update

  /** update
   * Update the car's position and angle
   * @param elapsedTime The elapsed time since previous update
   * @param xMovement The horizontal displacement
   * @param yMovement the vertical displacement
   */
  public void update(double elapsedTime, int xMovement, int yMovement){

    double futureAngle = angle + (rotateDirection * elapsedTime * turnSpeed);
    if (futureAngle != angle) { // if the car has a difference in angle
      if ((minRotation == null) && (maxRotation == null)){
        double rads = Math.toRadians(futureAngle - angle);
        transform.rotate(rads, anchorPoint.x, anchorPoint.y);
        angle = (futureAngle - angle) + angle;
      } else {
        if ((futureAngle - angle + angle > minRotation) && (futureAngle - angle + angle < maxRotation)) {
          double rads = Math.toRadians(futureAngle - angle);

          transform.rotate(rads, anchorPoint.x, anchorPoint.y);
          angle = (futureAngle - angle) + angle;
        }
      }
    }
    setX(getX() + xMovement);// set the new x,y coordinates
    setY(getY() - yMovement);
  } // end update

  /** setAngle
   * Set the car's angle
   * @param a The angle
   */
  public void setAngle(double a){
    this.angle = a;
    transform = new AffineTransform(); // reset the affine transform
    loadSprite(imageFile);
  } // end setAngle

  /** getDisplacementX
   * Get the x displacement since previous update
   * @param elapsedTime The time since previous update
   * @return The displacement
   */
  public int getDisplacementX(double elapsedTime){
    double displacement = driveSpeed * elapsedTime;
    double pedalTravel;
    if (yDirection > 0){ // if the car is moving forward
      pedalTravel = yDirection * elapsedTime * gasDriveSpeed;
    } else {
      pedalTravel = yDirection * elapsedTime * gasDriveSpeed / 3; // if the car is moving backward
    }  
    int xDisplacement;
    xDisplacement = (int)((Math.sin(Math.toRadians(angle))) * (displacement + pedalTravel))  * carGear; // calculate horizontal displacement
    return xDisplacement;
  } // end getDisplacementX

  /** getDisplacementY
   * Get the y displacement since previous update
   * @param elapsedTime The time since previous update
   * @return The displacement
   */
  public int getDisplacementY(double elapsedTime){
    double displacement = driveSpeed * elapsedTime;
    double pedalTravel;


    if (yDirection > 0){ // if the car is moving forward
      pedalTravel = yDirection * elapsedTime * gasDriveSpeed;
    } else { // if the car is moving backward
      pedalTravel = yDirection * elapsedTime * gasDriveSpeed / 3;
    }

    int yDisplacement;       

    yDisplacement = (int)((Math.cos(Math.toRadians(angle))) * (displacement + pedalTravel) * carGear); // calculate horizontal displacement

    return yDisplacement;
  } // end yDisplacement

  /** setReverse
   * Set the car gear to reverse/backward
   */
  public void setReverse(){
    carGear = -1;
  } // end setReverse

  /** setDrive
   *  Set the car gear/direction to drive/forward
   */
  public void setDrive(){
    carGear = 1;
  } // end setDrive

  /** getCollisionArea
   * Get the car's collision area
   * @return The collision area
   */
  public Rectangle getCollisionArea(){
    return this.collisionArea;
  } // end getCollisionArea

  /** intersects
   * Checks if any of the car's corners are contained inside a rectangle
   * @param rect The rectangle to compare to
   * @return True or false if the corners are contained inside the rectangle
   */
  public boolean intersects(Rectangle rect){
    //double hyp = Math.sqrt(Math.pow(getWidth(),2) + Math.pow(getHeight(),2));
    //double halfHyp = hyp / 2;
    //double rectHypAngle = Math.toDegrees(Math.atan(1.0 * getWidth()/getHeight()));
    //double hypAngle = angle + rectHypAngle;
     /*Point bottomLeft = new Point((int)(centerX - Math.sin(Math.toRadians(hypAngle)) * halfHyp), (int)(centerY + Math.cos(Math.toRadians( hypAngle)) * halfHyp));
    Point topRight = new Point((int)(centerX +  Math.cos(Math.toRadians(90 - hypAngle)) * halfHyp), (int)(centerY - Math.sin(Math.toRadians(90 - hypAngle)) * halfHyp));
    Point topLeft = new Point((int)(bottomLeft.x + Math.cos(Math.toRadians(90 - angle)) *getHeight()), (int)(bottomLeft.y - Math.sin(Math.toRadians(90 - angle)) * getHeight()));
    Point bottomRight = new Point((int)(bottomLeft.x + Math.cos(Math.toRadians(90 - (rectHypAngle + (90 - hypAngle)))) * getWidth()), (int)(bottomLeft.y + Math.sin(Math.toRadians(90-( rectHypAngle + (90 - hypAngle)))) * getWidth()));*/

    // Calculate the 4 corner postions
    int centerX = (int)screenX;
    int centerY = (int)screenY;
    double halfWidth = getWidth() / 2.0;
    double halfHeight = getHeight() / 2.0;
    double cos = Math.cos(Math.toRadians(angle));
    double sin = Math.sin(Math.toRadians(angle));
    Point bottomLeft = new Point((int)(centerX - halfWidth * cos + halfHeight * sin ), (int)(centerY - halfWidth * sin - halfHeight * cos));
    Point topRight = new Point((int)(centerX + (halfWidth * cos) - halfHeight * sin ), (int)(centerY + halfWidth * sin + halfHeight * cos));
    Point topLeft = new Point((int)(centerX - halfWidth * cos - halfHeight * sin), (int)(centerY - halfWidth * sin + halfHeight * cos));
    Point bottomRight = new Point((int)(centerX + halfWidth * cos + halfHeight * sin), (int)(centerY + halfWidth * sin - halfHeight * cos));

    if ((rect.contains(bottomLeft)) ||(rect.contains(bottomRight)) || (rect.contains(topLeft)) || (rect.contains(topRight))){ // if the rectangle contains any of the points, return true
      return true;
    }  else {
      return false;
    }  
  } // end intersects


  /** isContainedInside
   * Check if all 4 corners of the car are contained inside the rectangle
   * @param rect The rectangle to contain the car
   * @return True or false if all corners are inside the rectangle
   */
  public boolean isContainedInside(Rectangle rect){
    int centerX = (int)screenX;
    int centerY = (int)screenY;
    double halfWidth = getWidth() / 2.0;
    double halfHeight = getHeight() / 2.0;
    double cos = Math.cos(Math.toRadians(angle));
    double sin = Math.sin(Math.toRadians(angle));
    // get the coordinates of the 4 corners
    Point bottomLeft = new Point((int)(centerX - halfWidth * cos + halfHeight * sin ), (int)(centerY - halfWidth * sin - halfHeight * cos));
    Point topRight = new Point((int)(centerX + (halfWidth * cos) - halfHeight * sin ), (int)(centerY + halfWidth * sin + halfHeight * cos));
    Point topLeft = new Point((int)(centerX - halfWidth * cos - halfHeight * sin), (int)(centerY - halfWidth * sin + halfHeight * cos));
    Point bottomRight = new Point((int)(centerX + halfWidth * cos + halfHeight * sin), (int)(centerY + halfWidth * sin - halfHeight * cos));

    if (rect.contains(bottomLeft) && rect.contains(bottomRight) && rect.contains(topLeft) && rect.contains(topRight)){ // if the rectangle contains ALL the corners, return true
      return true;
    }  else {
      return false;
    }
  } // end isContainedInside

  /** getGear
   * Get the car's gear
   * @return The car gear integer
   */
  public int getGear(){
    return carGear;
  } // end getGear

  /** setScreenY
   * Set the car's y position on the screen
   * @param y The y coordinate
   */
  public void setScreenY(int y){
    screenY = y + getHeight() /2.0;
    rotatedY = y;
    setY(y);
  } // end setScreenY

  /** setScreenX
   * Set the car's x position on the screen
   * @param x The x coordinate
   */
  public void setScreenX(int x){
    setX(x);
    rotatedX = x;
    screenX = x + getWidth() /2.0;
  } // end setScreenX

  /** getScreenY
   * Get the car's y coordinate relative to the screen
   * @return The y coordinate
   */
  public double getScreenY(){
    return screenY;
  } // end getScreenY

  /** getScreenX
   * Get the car's x coordinate relative to the screen
   * @return The x coordinate
   */
  public double getScreenX(){
    return screenX;
  } // end getScreenX

  /** setMove
   * Let the car move forward or backward or not
   * @param b The boolean value
   */
  public void setMove(boolean b){
    this.move = b;
  } // end setMove

  /** setAnchorPoint
   * Set the car's anchor point to rotate around
   * @param x The x coordinate
   * @param y The y coordinate
   */
  public void setAnchorPoint(int x, int y){
    anchorPoint = new Point(x,y);
  } // end setAnchorPoint

  /** setTurnSpeed
   * Set the car's turning angle speed
   * @param s The speed number
   */
  public void setTurnSpeed(double s){
    this.turnSpeed = s;
  } // end setTurnSPeed

  /** setSpeed
   * Set the car's general speed
   * @param s The speed
   */
  public void setSpeed(int s){
    this.driveSpeed = s;
  } // end setSPeed

  /** setGasSpeed
   * Set the car's speed when moving up / gas pedal
   * @param s The gas speed
   */
  public void setGasSpeed(int s){
    this.gasDriveSpeed = s;
  } // end setGasSpeed

  /** turnLeft
   * Rotate the car left
   */
  public void turnLeft(){
    if ((!move) || (yDirection != 0)) {
      rotateDirection = -1 * carGear;
    }
  } // end turnLeft

  /** turnRight
   * Rotate the car right
   */
  public void turnRight(){
    if ((!move) || (yDirection != 0)) {
      rotateDirection = 1 * carGear;
    }
  } // end turnRight

  /** moveUp
   * Move the car up / gas pedal
   */
  public void moveUp(){
    yDirection = 1;
  } // end moveUp

  /** moveDown
   * Move the car down / brake pedal
   */
  public void moveDown(){
    yDirection = -1;
  } // end moveDown

  /** stopTurnLeft
   * Stop the car turning left
   */
  public void stopTurnLeft(){
    rotateDirection = 0;
  }  // end stopturnLeft

  /** stopTurnRight
   * Stop the car turning right
   */
  public void stopTurnRight(){
    rotateDirection = 0;
  } // end stopTurnRight

  /** stopMoveUp
   * Stop the car moving up
   */
  public void stopMoveUp(){
    yDirection=0;
  }

  /** stopMoveDown
   * Stop the car moving down
   */
  public void stopMoveDown(){
    yDirection= 0;
  } // end stopMoveDown

} // end  Players.CarPlayer
