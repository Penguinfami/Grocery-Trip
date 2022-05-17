package Gamemodes;

import java.awt.Graphics;

/** Gamemodes.Minigame.java
 * @author Christine Zhao
 * Abstract minigame class that can be drawn and updated
 */
public abstract class Minigame {

  // class fields
  public abstract void update(double elapsedTime);
  public abstract void draw (Graphics g);
  public abstract boolean isFinished();
} // end Gamemodes.Minigame
