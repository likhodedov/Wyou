package together.withyou;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AlarmService extends Service {

    @Override
    public void onCreate() {
    // TODO Auto-generated method stub
        //Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
       // Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
       // Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
//        android.os.Debug.waitForDebugger();  // ONLY FOR DEBUGGER
// TODO ADD
        super.onStart(intent, startId);
        Gson gson = new Gson();
        int betw=0;
        SharedPreferences mPrefss=getSharedPreferences("MainActivity",MODE_PRIVATE);
        String json = mPrefss.getString("MyCheckedDate", "");
        TogetherTimeManager obj = gson.fromJson(json, TogetherTimeManager.class);
        if (obj!=null) {
            int days=obj.daysBetween();
            betw=obj.GetCountMonthsBetween();
            Log.e("MONTHS"," "+ betw);
            if (betw!=0){
            if (betw%12==0){
                String variety;
                if (betw/12==1) variety="year"; else variety="years";
                NotifyManager notification=new NotifyManager("Don't forget, friend!","Tomorrow you will meet "+betw/12+" "+variety+"!",getApplicationContext());
                notification.addNotification();}
            else
            if ((betw/12==0)){
                String variety;
                if (betw==1) variety="month"; else variety="months";
                NotifyManager notification=new NotifyManager("Don't forget, friend!","Tomorrow you will meet "+betw+" "+variety+"!",getApplicationContext());
                notification.addNotification();}
            else if (betw/12>1)
            {
                String variety_year;
                String variety_month;
                if (betw%12==1) variety_month="month"; else variety_month="months";
                if (betw/12==1) variety_year="year"; else variety_year="years";
                NotifyManager notification=new NotifyManager("Don't forget, friend!","Tomorrow you will meet "+betw/12+" "+variety_year+" and "+betw%12+" "+variety_month+"!",getApplicationContext());
                notification.addNotification();}
            }
        }
        stopSelf();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}
