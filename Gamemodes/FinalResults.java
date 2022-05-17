package Gamemodes;

import Components.Meal;
import Setup.SimpleLinkedList;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Calendar;

/** Gamemodes.FinalResults.java
 * @author Christine Zhao
 * The final game results screen
 */
public class FinalResults {

  // class fields
  private SimpleLinkedList<Meal> allMeals;
  private int x,y;
  private int width, height;
  private double finalPercentError;
  private double finalScore;
  private int timeLeft;
  private Font font;
  private Font scoreFont;
  private Font finalScoreFont;
  private Font mealsFont;
  private int difficulty;
  private String[][] scoreList;
  private int currentAttribute;
  private String[] difficultyNames = {"Easy", "Medium", "Hard"};

  /** Gamemodes.FinalResults
   * Construct the final results screen
   * @param x The x coordinate
   * @param y The y coordinate
   * @param w The screen width
   * @param h The screen height
   * @param timeLeft The player's time left
   * @param allMeals All attempted meals
   * @param totalPercentError The total percent error
   * @param gameDifficulty The game's difficulty
   */
  public FinalResults(int x, int y, int w, int h, int timeLeft, SimpleLinkedList<Meal> allMeals, double totalPercentError, int gameDifficulty){
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
    this.allMeals = allMeals;
    this.timeLeft = timeLeft;
    this.difficulty = gameDifficulty;
    finalPercentError = Math.round( totalPercentError / 500.0 * 10000) / 100.0; // num rounds * 100 / percent error
    calculateScore();
    // create the fonts according the screen size
    this.scoreFont = new Font("Arial", Font.BOLD, 9 + (height / 100 * 3 / 2) );
    this.font = new Font("Consolas", Font.BOLD, 15 + (height / 100 * 3 / 2));
    this.finalScoreFont = new Font("Consolas", Font.BOLD, 23 + (height / 70 * 3 / 2));
    this.mealsFont = new Font("Roboto", Font.BOLD, 10 + 30 / allMeals.size() + (height / 100 * 3 / 2));

  } // end Gamemodes.FinalResults

  /** draw
   * Draw the results screen
   * @param g The graphics object
   */
  public void draw(Graphics g){
    // the background
    g.setColor(Color.decode("#11D9F7"));
    g.fillRect(x,y,width,height);
    
    // The top scores list background
    g.setColor(Color.decode("#9DE5F5"));
    g.fillRect(280 * height /500,20 * height / 500,373 * height /500,450 * height / 500);

    // the current game's final results background 
    g.setColor(Color.WHITE);
    g.fillRect(20 * height / 500, 20 * height / 500, 260 * height /500, 450 * height / 500);
    
    // draw the final score
    g.setColor(Color.BLACK);
    g.setFont(finalScoreFont);
    g.drawString("Final Score: " + finalScore, 290* height / 500, 50* height / 500 );
    
    // this game's player results
    g.setFont(font);
    g.setColor(Color.decode("#0F272E"));
    g.drawString("Percent Error: " + finalPercentError,30 * height / 500, 70* height / 500);
    g.drawString("All meals attempted: ", 30* height / 500, 150* height / 500 );
    g.drawString("Time left: " + timeLeft, 30* height / 500, 100* height / 500);
    // draw all the attempted meals
    g.setFont(mealsFont);
    for (int i = 0; i < allMeals.size(); i++){
      g.drawString(allMeals.get(i).getName(), 40* height / 500, 180 * height / 500 + i*250 /allMeals.size() * height / 500 );
    }
    
    // the list of the top 14 scores
    g.setFont(scoreFont);
    if (scoreList.length >= 14) {
      g.drawString("Top 14 scores", 410 * height / 500, 85 * height / 500);
    } else {
      g.drawString("Top scores", 410 * height / 500, 85 * height / 500);
    }
    g.setColor(Color.decode("#1E4E5D"));
    g.drawString("Date", 300* height / 500, 110* height / 500);
    g.drawString("Score", 390* height / 500, 110* height / 500);
    g.drawString("% Error", 450* height / 500, 110* height / 500);
    g.drawString("Time left", 510* height / 500, 110* height / 500);
    g.drawString("Difficulty", 585* height / 500, 110* height / 500);

    if (scoreList.length < 14) { // if there are less than 14 total scores
      for (int i = 0; i < scoreList.length; i++) {
        g.drawString(scoreList[i][0], 300 * height / 500, (140 + i * 20) * height / 500);
        for (int j = 1; j < scoreList[i].length - 1; j++) {
          g.drawString(scoreList[i][j], (330 + j * 65) * height / 500, (140 + i * 20) * height / 500);
        }
        g.drawString(difficultyNames[Integer.parseInt(scoreList[i][4]) - 1] , (327 + 4 * 65) * height / 500, (140 + i * 20) * height / 500);

      }
    } else { // if there are more than 14 total scores, draw only the top 14
      for (int i = 0; i < 14; i++) { 
        g.drawString(scoreList[i][0], 300 * height / 500, (140 + i * 20) * height / 500);
        for (int j = 1; j < scoreList[i].length - 1; j++) {
          g.drawString(scoreList[i][j], (330 + j * 65) * height / 500, (140 + i * 20) * height / 500);
        }
        g.drawString(difficultyNames[Integer.parseInt(scoreList[i][4]) - 1] , (327 + 4 * 65) * height / 500, (140 + i * 20) * height / 500);

      }
    }
  } // end draw

  /** calculateScore
   * Calculate the player's final score
   */
  private void calculateScore() {
    if (timeLeft == 0) { // if the player did not complete a single day to the end
      finalScore = 100;
    } else{
      finalScore = Math.round(((timeLeft + (160 * difficulty) + 100) / ((finalPercentError * 3 / difficulty) + 100)) * 10000.0) / 100.0;
    }
    saveScore();
  } // end calculateScore

  /** saveScore
   * Save the game's results to the text file
   */
  private void saveScore(){
    int totalNumScores = 0;
    try {
      BufferedReader reader = new BufferedReader(new FileReader("Info/scores.txt"));
      String line;
      // get the number of total scores
      totalNumScores = 0;
      while ((line = reader.readLine()) != null) {
        totalNumScores++;
      }
      reader.close();
    }catch(IOException e) {
      System.out.println("error counting lines");
    }

    // create the array
    scoreList = new String[totalNumScores][5];  // 0. date 1. score 2. percent error 3. time left 4. difficulty
    
    String text = "";
    String line;
    
    // read the file add all the scores to the array
    try{
      BufferedReader reader = new BufferedReader(new FileReader("Info/scores.txt"));
      for (int i = 0; i < totalNumScores; i++){
        line = reader.readLine();
        text+= line;
        text+= "\n";
        scoreList[i] = line.split(" ");
      }
      // add the current date
      Calendar calendar = Calendar.getInstance();
      String date = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
      line = date + " " + finalScore + " " + finalPercentError + " " + timeLeft + " " + difficulty;
      text += line;
      scoreList[totalNumScores - 1] = line.split(" ");
      
      // save the list to the txt file adding the most recent scores
      PrintWriter printWriter = new PrintWriter("Info/scores.txt");
      printWriter.println(text);
      reader.close();
      printWriter.close();
      System.out.println("done score");

    } catch (IOException e){
      e.printStackTrace();
      System.out.println("error printwriting");
    }
    currentAttribute = 0; // starting in order of date

  } // end saveScore

  /** sortScore
   * Sort the score list based on a specific parameter
   * @param attribute The argument number to sort by
   */
  public void sortScore(int attribute){
    if (attribute != currentAttribute) { // if the list isnt already sorted by that attribute
      mergeSort(attribute);
      currentAttribute = attribute;
    }

  } // end sortScore


  /** mergeSort
   * Sort the score list using merge sort
   * @param attribute The attribute's argument number in the array
   */
  private void mergeSort(int attribute){
    mergeSort(attribute,new String[scoreList.length][scoreList[0].length], 0, scoreList.length - 1);
  } // end mergeSort

  private void mergeSort(int attribute, String[][] temp, int leftStart, int rightEnd){
    int middle = (leftStart + rightEnd) / 2;

    if (leftStart >= rightEnd){
      return;
    } else { // split the array in half
      mergeSort(attribute, temp, leftStart, middle);
      mergeSort(attribute, temp, middle + 1, rightEnd);
      mergeHalves(attribute, temp, leftStart, rightEnd);
    }
    return;
  } // end mergeSort

  private void mergeHalves(int attribute, String[][] temp, int leftStart, int rightEnd){
    int leftEnd = (rightEnd + leftStart) / 2;
    int rightStart = leftEnd + 1;
    int size = rightEnd - leftStart + 1;
    int left = leftStart;
    int right = rightStart;
    int index = leftStart;
    while (left <= leftEnd && right <= rightEnd){
      if (attribute == 1 || attribute == 3) { // sort them by attribute
        if (Double.parseDouble(scoreList[left][attribute]) >= Double.parseDouble(scoreList[right][attribute])) {
          temp[index] = scoreList[left];
          left++;
        } else {
          temp[index] = scoreList[right];
          right++;
        }
      } else { // sort them by attribute
        if (Double.parseDouble(scoreList[left][attribute]) <= Double.parseDouble(scoreList[right][attribute])) {
          temp[index] = scoreList[left];
          left++;
        } else {
          temp[index] = scoreList[right];
          right++;
        }
      }
      index++;

    }
    // copy the temp arrays into the real array
    System.arraycopy(scoreList, left, temp, index, leftEnd - left + 1);
    System.arraycopy(scoreList,right, temp,index, rightEnd - right + 1);
    System.arraycopy(temp, leftStart, scoreList, leftStart,size);
  } // end mergeHalves

} // end Gamemodes.FinalResults
