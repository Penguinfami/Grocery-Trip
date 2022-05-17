package Players;

import Components.Map;
import Components.PictureMap;

/** Players.StoreThief.java
 * @author Christine Zhao
 * A store thief that moves in increments relative to the player's position
 **/
public class StoreThief extends Shopper {

  /** Players.StoreThief
   * Construct the store thief
   * @param x The thief's x coordinate
   * @param y The thief's y coordinate
   * @param width The thief's width
   * @param height The thief's height
   * @param distance The thief's running distance each update
   */
  public StoreThief(int x, int y, int width, int height, int distance){
    super(x,y,width,height, distance);
  }

  /** updateTarget
   * Update the thief's target judging by the current map and player's position to be the best distance from the player
   * @param m The map the thief runs on
   * @param chaser The player chasing the thief
   */

  public void updateTarget(Map m, Player chaser){
    setReachedTarget(false);

    int chaserX = chaser.getX();
    int chaserY = chaser.getY();
    int thiefX = getX();
    int thiefY = getY();
    int mapWidth;

    if (m instanceof PictureMap) {

      PictureMap map = (PictureMap)m;
      mapWidth =  map.getWidth();

      double runningDistance = (double) getRunningDistance(); // the distance to move to from the thief's current position
      double futureChaserDistance;
      boolean foundTarget = false;
      double randomPointX;
      double randomPointY;
      int numTries = 1;
      int[] posNeg = {-1, 1};
      double currentChaserDistance = Math.sqrt((Math.pow(chaserX - thiefX, 2) + Math.pow(chaserY - thiefY, 2))); // distance between the chaser and the thief
      double largestDistance = 0;

      while (!foundTarget) {
        while (numTries % 50 != 0) {
          randomPointX = thiefX + (int) (Math.random() * runningDistance * posNeg[(int) (Math.random() * 2)]); // negative vs positive
          randomPointY = thiefY + Math.sqrt(Math.pow(runningDistance, 2) - Math.pow(randomPointX - thiefX, 2)) * posNeg[(int) (Math.random() * 2)];
          if (map.contains((int) (randomPointX), (int) (randomPointY),getWidth())) { // if the point is on the map
            futureChaserDistance = (Math.sqrt(Math.pow(randomPointX - (chaserX), 2) + Math.pow(randomPointY - (chaserY), 2)));// hypothetical distance between the chaser and the thief
            if ((futureChaserDistance > runningDistance) && (futureChaserDistance > currentChaserDistance)) { // if the thief is closer to that point than the chaser
              if (largestDistance < futureChaserDistance) {
                setTargetX((int) (randomPointX)); // set it as the thief's new target
                setTargetY((int) (randomPointY));
                setTargetMapX(map.getX());
                setTargetMapY(map.getY());
                largestDistance = futureChaserDistance;
                foundTarget = true;
              }
            }
          }
          numTries++;
        }
        numTries++;
        // if haven't found a good position after many tries, decrease the running distance
        if (!foundTarget) {
          runningDistance -= 50;
        }
        // automatically move on
        if (runningDistance < 50) {
          foundTarget = true;
        }
      }
    }

  } // end updateTarget

} // end Players.StoreThief
