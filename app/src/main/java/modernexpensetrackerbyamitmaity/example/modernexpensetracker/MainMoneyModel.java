package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

public class MainMoneyModel {


    private  String Cost;

    public MainMoneyModel ()
    {

    }

    public MainMoneyModel (String COST) {
        this.Cost  = COST;

    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
