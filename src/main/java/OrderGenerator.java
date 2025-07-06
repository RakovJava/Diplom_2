import java.util.ArrayList;
import java.util.Random;

public class OrderGenerator {
    private ArrayList<String> data = new ArrayList<>();

    public ArrayList <String> createRandomOrder(GetIngredientsResponse ingredients){
        final int maxCount = 10;
        Random random = new Random();
        int orderSize = random.nextInt(maxCount) + 1;
        for (int i = 0; i < orderSize; i++) {
            int index = random.nextInt(maxCount);
            data.add(ingredients.getData().get(index).get_id());
        }
        return data;
    }
}
