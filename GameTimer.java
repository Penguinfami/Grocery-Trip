import java.util.Timer;
import java.util.TimerTask;

/** GameTimer.java
 *@author Christine Zhao
 * A timer and timertask associated with the game that runs the appropriate methods
 */
public class GameTimer {

  // class fields
  private Object game;
  private String task;
  private Timer timer = new Timer();

  /** TimerTask
   * The task that contains all possible task strings
   */
  private TimerTask timerTask = new TimerTask(){
    public void run(){
      
      // run the appropriate method and cast to the appropriate class according the the task string
      if (task.equals("time left")) {
        ((GameManager)game).updateTime();
      } else if (task.equals("don't have this item")){
        ((GroceryShopping)game).removeInvalidMessage();
      } else if (task.equals("too much money")){
        ((PayAtCashier)game).removeOverBalanceMessage();
      } else if (task.equals("thief running")){
        ((StoreChase)game).updateThiefTarget();
      } else if (task.equals("pulled over")){
        ((DrivingToStore)game).endPulledOver();
      }  else if (task.equals("times up")){
        ((GameManager)game).newDay();
      } else if (task.equals("successful park")){
        ((ParkingCar)game).checkStayParked();
        cancel();
        purge();
      }else if (task.equals("failed park")){
        ((ParkingCar)game).endPulledOver();
      } 
    }
  };

  /** GameTimer
   * Construct the game timer
   * @param game The current game object / minigame
   * @param task The task to perform
   * @param time The time delay
   */
  public GameTimer(Object game, String task, int time){
    this.game = game;
    this.task = task;
    timer.schedule(timerTask, time);
  } // end GameTimer

  /** GameTimer
   * Construct the game timer
   * @param game The current game object / minigame
   * @param task The task to perform
   * @param time The time delay
   * @param period The period between tasks
   */
  public GameTimer(Object game, String task, int time, int period){
    this.game = game;
    this.task = task;
    timer.scheduleAtFixedRate(timerTask, time, period);
  } // end GameTimer

  /** reschedule
   * Schedule the timer
   * @param task The task to perform
   * @param time The time delay
   * @param period The period between tasks
   */
  public void reschedule(String task, int time, int period){
    this.task = task;
    timer.scheduleAtFixedRate(timerTask, time, period);
  } // end reschedule

  /** reschedule
   * Schedule the timer
   * @param task The task to perform
   * @param time The time delay
   */
  public void reschedule(String task, int time){
    this.task = task;
    timer.schedule(timerTask, time);
  } // end reschedule

  /** cancel
   * Cancel the timer's task
   */
  public void cancel(){
    timer.cancel();
  } // end cancel

  /** purge
   * Purge all cancelled tasks
   */
  public void purge(){
    timer.purge();
  } // end purge

} // end GameTimer
