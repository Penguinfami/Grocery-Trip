package Components;

import java.awt.Point;

/** Items.Circle.java
 * @author Christine Zhao
 * A circle and its properties
 */
public class Circle {

  // class fields
  private int radius;
  private int x, y; // the  center point

  /** Items.Circle
   * Construct a circle
   * @param x The x coordinate
   * @param y The y coordinate
   * @param radius The radius length
   */
  public Circle(int x, int y, int radius){
    this.x = x;
    this.y = y;
    this.radius = radius;
  } // end Items.Circle

  /** contains
   * Check whether the circle contains a point
   * @param point The point to check
   * @return True of false if it contains the point
   */
  public boolean contains(Point point){
    double pointX = point.getX();
    double pointY = point.getY();
    double distanceFromCenter = Math.sqrt(Math.pow(pointX - x, 2) + Math.pow(pointY - y, 2)); // calculate the distance between the point and the center
    if (distanceFromCenter < radius){ // if the distance is smaller than the radius
      return true;
    } else { // if the distance is larger than the circle's area
      return false;
    }

  } // end  contains
  
} // end Items.Circle
