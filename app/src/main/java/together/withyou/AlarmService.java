package together.withyou;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AlarmService extends Service {
    //public static SharedPreferences preferences;

    @Override

    public void onCreate() {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();

    }



    @Override

    public IBinder onBind(Intent intent) {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

        return null;

    }



    @Override

    public void onDestroy() {

// TODO Auto-generated method stub

        super.onDestroy();

        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

    }



    @Override

    public void onStart(Intent intent, int startId) {
        android.os.Debug.waitForDebugger();  // this line is key

// TODO Auto-generated method stub

        super.onStart(intent, startId);
        Gson gson = new Gson();
        String lala= "";
//        NotifyManager not=new NotifyManager("Привет","Тестовое сообщение",getApplicationContext());
//           not.addNotification();
        SharedPreferences mPrefss=getSharedPreferences("MainActivity",MODE_PRIVATE);
        String json = mPrefss.getString("MyCheckedDate", "");
        TogetherTimeManager obj = gson.fromJson(json, TogetherTimeManager.class);
        if (obj!=null) {
            int days=obj.daysBetween();
            lala=Integer.toString(days);

        }
        Toast.makeText(this, "MyAlarmService.onStart()"+lala, Toast.LENGTH_LONG).show();

        stopSelf();



    }



    @Override

    public boolean onUnbind(Intent intent) {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

        return super.onUnbind(intent);

    }
}
