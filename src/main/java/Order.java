import java.util.ArrayList;

public class Order {
    private ArrayList<String> ingredients;

    public Order(){}

    public Order(ArrayList ingredients){
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
