import java.util.ArrayList;

public class GetIngredientsResponse {
    private boolean success;
    private ArrayList<IngredientsData> data;

    public GetIngredientsResponse(){
    };

    public GetIngredientsResponse(boolean success, ArrayList<IngredientsData> data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<IngredientsData> getData() {
        return data;
    }

    public void setData(ArrayList<IngredientsData> data) {
        this.data = data;
    }
}
