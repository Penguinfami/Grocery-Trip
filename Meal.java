/** Meal.java
 * @author Christine Zhao
 * A meal object composed of ingredients
 */
public class Meal {

  // class fields
  private String name;
  private SimpleLinkedList<FoodProduct> ingredients;

  /** Meal
   * Construct a meal
   * @param name The name of the meal
   * @param ingredients The list of food product ingredients
   */
  public Meal(String name, SimpleLinkedList<FoodProduct> ingredients){
    this.name = name;
    this.ingredients = ingredients;
  } // end Meal

  /** getName
   * Get the name of the meal
   * @return The meal's name
   */
  public String getName(){
    return name;
  } // end getName

  /** getIngredients
   * Get the list of ingredients
   * @return The list of ingredients
   */
  public SimpleLinkedList<FoodProduct> getIngredients() {
    return ingredients;
  } // end getIngredients
}
