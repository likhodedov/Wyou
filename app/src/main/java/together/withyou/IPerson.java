package together.withyou;

import android.net.Uri;

import java.util.List;


public interface IPerson {

    void setYouImage(Uri uri);

    void setFriendImage(Uri uri);

    void setDateInfo (TogetherTimeManager... togetherTimeManagers);

    void setYouName(String name);

    void setFriendName(String name);

    int getDaysBetweenDate(String nameIdtimeManager, TogetherTimeManager togetherTimeManager);


}
