package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

public class FreindModel {

    private  String Name,Image;

    public FreindModel ()
    {

    }

    public FreindModel (String NAME,String IMAGE) {
        this.Name  = NAME;
        this.Image=IMAGE;

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
