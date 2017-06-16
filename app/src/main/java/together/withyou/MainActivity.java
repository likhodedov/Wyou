package together.withyou;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.amplitude.api.Amplitude;
import java.util.Date;
import java.util.Random;
import java.util.logging.Handler;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {
    private static final String TAG ="" ;
    int mYear, mMonth, mDay;
    Thread b;
    private PendingIntent pendingIntent;
    TextView txtDate;
    public static final int PICK_IMAGE_GIRL=1;
    public static final int PICK_IMAGE_BOY=2;
    ImageView first;
    TogetherTimeManager Manager;
    SharedPreferences  mPrefs;
    CircleProgressView mCircleView;
    ImageView second;
    Calendar calendar;
    int days;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    InputStream imageStream = null;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Amplitude.getInstance().initialize(this, "17ed6ff2f639b8331a2f96e321630521").enableForegroundTracking(getApplication());
            if (getIntent().getExtras().getBoolean("NOTIFY")) {
                Toast.makeText(MainActivity.this, "OPENED FROM NOTIFY", Toast.LENGTH_SHORT).show();
            }


        Gson gson = new Gson();
        Amplitude.getInstance().logEvent("APP_LAUNHED");
        SharedPreferences  mPrefss = getPreferences(MODE_PRIVATE);
      String json = mPrefss.getString("MyCheckedDate", "");
              TogetherTimeManager obj = gson.fromJson(json, TogetherTimeManager.class);
        if (obj!=null) {
            days=obj.daysBetween();
                        String lala=Integer.toString(days);
                                }
        Intent myIntent = new Intent(MainActivity.this, AlarmService.class);

        pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);

        txtDate = (TextView) findViewById(R.id.textView);
        TextView txtMate = (TextView) findViewById(R.id.tex2tView);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "i_love_what_you_doo.ttf");
        txtDate.setTypeface(typeFace); txtMate.setTypeface(typeFace);
        txtDate.setText(R.string.left_title);txtMate.setText(R.string.right_title);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        first=(CircleImageView)  findViewById(R.id.profile_image);
        second=(CircleImageView)  findViewById(R.id.profile_image2);
        File path=getPathToImages();
        final File man = new File(path.getPath() + File.separator+"BOY.jpg");
        final File woman=new File(path.getPath() + File.separator+"GIRL.jpg");
        if (man.exists()) {
           final Bitmap SECOND_BITMAP=BitmapFactory.decodeFile(man.getAbsolutePath());
           second.setImageBitmap(SECOND_BITMAP);
        }
        if (woman.exists()) {

                   final Bitmap FIRST_BITMAP=BitmapFactory.decodeFile(woman.getAbsolutePath());
                   first.setImageBitmap(FIRST_BITMAP);
        }

        final TypedArray imgs = getResources().obtainTypedArray(R.array.apptour);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(imgs.length());
        final int resID = imgs.getResourceId(rndInt, 0);
        imageView.setImageResource(resID);
        final Button SetDATA = (Button) findViewById(R.id.SetDataButton);
        SetDATA.setTypeface(typeFace);
        first.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verifyStoragePermissions(MainActivity.this);
                Amplitude.getInstance().logEvent("CHANGE_LEFT_IMAGE");
                //Toast.makeText(MainActivity.this, "GEGE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_GIRL);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                Amplitude.getInstance().logEvent("CHANGE_RIGHT_IMAGE");
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_BOY);
                //Toast.makeText(MainActivity.this, "BEBE", Toast.LENGTH_SHORT).show();
            }
        });

//       runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                SetDATA.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Process to get Current Date

                      AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



                        final Calendar calendar2 = Calendar.getInstance();

                        calendar2.setTimeInMillis(System.currentTimeMillis());

                        calendar2.add(Calendar.SECOND, 10);

                       // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);


                      //  NotifyManager not=new NotifyManager("Привет","Тестовое сообщение",getApplicationContext());
                    //    not.addNotification();

                        Toast.makeText(MainActivity.this, "Start Alarm", Toast.LENGTH_LONG).show();

//addNotification();





                        final Calendar c = Calendar.getInstance();
                                                mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        Amplitude.getInstance().logEvent("CHANGE_DATE");
                        // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox
                                        //String blabla = ;
//
                                        //String pattern = ;
                                        calendar = Calendar.getInstance();
                                        try {
                                            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dayOfMonth + "."
                                                    + (monthOfYear + 1) + "." + year);
                                            calendar.setTime(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
//                                        first.post(new Runnable() {
//                                            public void run() {
//                                                first.setImageBitmap(yourSelectedImage);
//                                                storeImage(yourSelectedImage, PICK_IMAGE_GIRL);}
//                                        });
//                                    }
//                                }).start();
//                                        new Thread(new Runnable() {
//                                            public void run() {
//                                        mCircleView.post(new Runnable() {
//                                            @Override
//                                            public void run() {
                                        //NotifyManager.sendNotif();
                                        Manager = new TogetherTimeManager(calendar);
                                        if (Manager.compareDates() < 0) {
                                            mPrefs = getPreferences(MODE_PRIVATE);
                                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                            Gson gson = new Gson();
                                            String json = gson.toJson(Manager);
                                            prefsEditor.putString("MyCheckedDate", json);
                                            prefsEditor.commit();
                                            days = Manager.daysBetween();
                                            //mCircleView.setValue(days);
                                            mCircleView.setValueAnimated(ConverterTime.preparedays(days));

                                            //String lala =
                                            mCircleView.setTextMode(TextMode.TEXT);
                                            mCircleView.setText(Integer.toString(days));

                                           // Toast.makeText(MainActivity.this, lala, Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(MainActivity.this, "It's a future date", Toast.LENGTH_LONG).show();
                                            }
//                                        });
//
//                                        }
//                                    }).start();}
                                }, mYear, mMonth, mDay);
                        dpd.show();
                        //System.gc();



                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),10000/*AlarmManager.INTERVAL_FIFTEEN_MINUTES*/,pendingIntent);


                    }
                });
   //         }});

}
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "On Start .....");

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setValueAnimated(ConverterTime.preparedays(days));
        String lala=Integer.toString(days);
        mCircleView.setTextMode(TextMode.TEXT);
        mCircleView.setText(lala);
    }






    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
       // Bitmap yourSelectedImage;

        if (requestCode == PICK_IMAGE_GIRL) {
                    if (resultCode == RESULT_OK) {
                                Uri selectedImage = imageReturnedIntent.getData();
                                //InputStream imageStream = null;
                                try {
                                    imageStream = getContentResolver().openInputStream(selectedImage);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }


//                       final Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                        first.setImageBitmap(yourSelectedImage);

                        new Thread(new Runnable() {
                            public void run() {
                                final Bitmap yourSelectedImage=BitmapFactory.decodeStream(imageStream);;
                                first.post(new Runnable() {
                                    public void run() {
                                        first.setImageBitmap(yourSelectedImage);
                                        storeImage(yourSelectedImage, PICK_IMAGE_GIRL);}
                                });
                            }
                        }).start();

                        //Toast.makeText(MainActivity.this, "BITMAP", Toast.LENGTH_LONG).show();

                    }}
              else  if (requestCode == PICK_IMAGE_BOY) {
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        //InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

//                        final Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                        second.setImageBitmap(yourSelectedImage);

                        new Thread(new Runnable() {
                            public void run() {
                                final Bitmap yourSelectedImage=BitmapFactory.decodeStream(imageStream);;
                                second.post(new Runnable() {
                                    public void run() {
                                        second.setImageBitmap(yourSelectedImage);
                                        storeImage(yourSelectedImage, PICK_IMAGE_BOY);}
                                });
                            }
                        }).start();
                        //Toast.makeText(MainActivity.this, "BITMAP", Toast.LENGTH_LONG).show();
                    }
                }
            }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
    }}
    private void storeImage(final Bitmap image, final int TYPE_IMAGE) {
       b = new Thread(new Runnable() {
          public void run() {
                File pictureFile = getOutputMediaFile(TYPE_IMAGE);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());

        }
            }
        });
b.start();

    }


    @Nullable
    private  File getOutputMediaFile(int TYPE_IMAGE){
        File mediaStorageDir=getPathToImages();
         if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        File mediaFile;
        String mImageName=new String();
        if (TYPE_IMAGE==1)
        mImageName="GIRL.jpg";
        else if (TYPE_IMAGE==2)  mImageName="BOY.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    public File getPathToImages() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + getApplicationContext().getPackageName()
            + "/Files");
return  mediaStorageDir;
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
    int FM_NOTIFICATION_ID=3;

    private void addNotification() {



        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(FM_NOTIFICATION_ID, builder.build());
    }

    // Remove notification
    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(FM_NOTIFICATION_ID);
    }}





