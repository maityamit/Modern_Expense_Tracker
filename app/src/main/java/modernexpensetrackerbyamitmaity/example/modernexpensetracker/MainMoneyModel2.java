package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

public class MainMoneyModel2 {
    private  String TripName;

    public MainMoneyModel2 ()
    {

    }

    public MainMoneyModel2 (String TRIPNAME) {
        this.TripName  = TRIPNAME;

    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }
}
