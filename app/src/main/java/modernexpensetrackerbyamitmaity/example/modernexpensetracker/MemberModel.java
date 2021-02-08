package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

public class MemberModel {

    private  String Person;

    public MemberModel ()
    {

    }

    public MemberModel (String PERSON) {
        this.Person  = PERSON;

    }

    public String getPerson() {
        return Person;
    }

    public void setPerson(String person) {
        Person = person;
    }
}
