package Setup;

/** Setup.Clock.java
 * @author Christine Zhao
 * A clock
 */
public class Clock {

  // class fields
  private long elapsedTime;
  private long recentCheckedTime;

  /** Setup.Clock
   * Construct a clock
   */
  public Clock(){
    recentCheckedTime = System.nanoTime();
    elapsedTime = 0;
  } // end Setup.Clock

  /** update
   * Update the clock
   */
  public void update(){
    long currentTime = System.nanoTime();
    elapsedTime = currentTime - recentCheckedTime;
    recentCheckedTime = currentTime;
  } // end update

  /** getElapsedTine
   * Get the time between updates
   * @return The elapsed time
   */
  public double getElapsedTime(){
    return elapsedTime/1.0E9;
  } // end getElapsedTime

} // end Setup.Clock
