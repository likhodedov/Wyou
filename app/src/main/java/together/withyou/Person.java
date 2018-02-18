package together.withyou;

import android.net.Uri;
import android.util.Log;

import together.withyou.Events.EventListener;

public class Person implements EventListener {
    public final static int YOU = 1;
    public final static int MY_LOVE = 0;

    private Uri selectedImage;
    private String name;
    private int peronality;




    public Person() {}

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void setPeronality(int peronality) {
        this.peronality = peronality;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSelectedImage(){

    }

    @Override
    public void update(String eventType) {
        Log.d("PERSON:","I catch " + eventType + "event");
    }
}
