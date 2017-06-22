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
    ImageView first_image;
    TogetherTimeManager Manager;
    SharedPreferences  mPrefs;
    CircleProgressView mCircleView;
    ImageView second_image;
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
        Amplitude.getInstance().logEvent("APP_LAUNCHED");
        final Calendar calendar2 = Calendar.getInstance();
        try {
            if (getIntent().getExtras().getInt("NOTIFY")==35) {
                Amplitude.getInstance().logEvent("APP_LAUNCHED_NOTIFY"); }}
        catch (NullPointerException e){
                 Log.e("ERROR", "NULLPOINTER");}

        Gson gson = new Gson();
        SharedPreferences  mPrefss = getPreferences(MODE_PRIVATE);
        String json = mPrefss.getString("MyCheckedDate", "");
        final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
              TogetherTimeManager obj = gson.fromJson(json, TogetherTimeManager.class);
        if (obj!=null) {days=obj.daysBetween();
            calendar2.setTimeInMillis(System.currentTimeMillis());
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),10000/*AlarmManager.INTERVAL_FIFTEEN_MINUTES*/,pendingIntent);

        }

        Intent myIntent = new Intent(MainActivity.this, AlarmService.class);
        pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);

        txtDate = (TextView) findViewById(R.id.textView);
        TextView txtMate = (TextView) findViewById(R.id.tex2tView);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "i_love_what_you_doo.ttf");
        txtDate.setTypeface(typeFace); txtMate.setTypeface(typeFace);
        txtDate.setText(R.string.left_title);txtMate.setText(R.string.right_title);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        first_image =(CircleImageView)  findViewById(R.id.profile_image);
        second_image =(CircleImageView)  findViewById(R.id.profile_image2);

        File path=getPathToImages();
        final File man = new File(path.getPath() + File.separator+"BOY.jpg");
        final File woman=new File(path.getPath() + File.separator+"GIRL.jpg");

            if (man.exists()) {
                     final Bitmap SECOND_BITMAP=BitmapFactory.decodeFile(man.getAbsolutePath());
                     second_image.setImageBitmap(SECOND_BITMAP);}
            if (woman.exists()) {
                     final Bitmap FIRST_BITMAP=BitmapFactory.decodeFile(woman.getAbsolutePath());
                     first_image.setImageBitmap(FIRST_BITMAP);}

        final TypedArray imgs = getResources().obtainTypedArray(R.array.apptour);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(imgs.length());
        final int resID = imgs.getResourceId(rndInt, 0);
        imageView.setImageResource(resID);
        final Button setdataButton = (Button) findViewById(R.id.SetDataButton);
        setdataButton.setTypeface(typeFace);


        first_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verifyStoragePermissions(MainActivity.this);
                Amplitude.getInstance().logEvent("CHANGE_LEFT_IMAGE");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_GIRL);
            }
        });

        second_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                Amplitude.getInstance().logEvent("CHANGE_RIGHT_IMAGE");
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_BOY);
                            }
        });
                setdataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                         calendar2.setTimeInMillis(System.currentTimeMillis());
                         final Calendar c = Calendar.getInstance();
                         mYear = c.get(Calendar.YEAR);
                         mMonth = c.get(Calendar.MONTH);
                         mDay = c.get(Calendar.DAY_OF_MONTH);
                        Amplitude.getInstance().logEvent("CHANGE_DATE");

                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                      calendar = Calendar.getInstance();
                                            try {
                                                    Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dayOfMonth + "."
                                                    + (monthOfYear + 1) + "." + year);
                                                    calendar.setTime(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
//
                                        Manager = new TogetherTimeManager(calendar);
                                                 if (Manager.compareDates() < 0) {
                                                        mPrefs = getPreferences(MODE_PRIVATE);
                                                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                                        Gson gson = new Gson();
                                                        String json = gson.toJson(Manager);
                                                        prefsEditor.putString("MyCheckedDate", json);
                                                        prefsEditor.apply();
                                                        days = Manager.daysBetween();
                                                        mCircleView.setValueAnimated(ConverterTime.preparedays(days));
                                                        mCircleView.setTextMode(TextMode.TEXT);
                                                        mCircleView.setText(Integer.toString(days));
                                                 } else
                                                        Toast.makeText(MainActivity.this, "It's a future date", Toast.LENGTH_LONG).show();
                                            }
//                                        });
//
//                                        }
//                                    }).start();}
                                }, mYear, mMonth, mDay);
                        dpd.show();

                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),10000/*AlarmManager.INTERVAL_FIFTEEN_MINUTES*/,pendingIntent);
                    }
                });
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

                        new Thread(new Runnable() {
                            public void run() {
                                final Bitmap yourSelectedImage=BitmapFactory.decodeStream(imageStream);;
                                first_image.post(new Runnable() {
                                    public void run() {
                                        first_image.setImageBitmap(yourSelectedImage);
                                        storeImage(yourSelectedImage, PICK_IMAGE_GIRL);}
                                });
                            }
                        }).start();

                    }}
              else  if (requestCode == PICK_IMAGE_BOY) {
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();

                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        new Thread(new Runnable() {
                            public void run() {
                                final Bitmap yourSelectedImage=BitmapFactory.decodeStream(imageStream);;
                                second_image.post(new Runnable() {
                                    public void run() {
                                        second_image.setImageBitmap(yourSelectedImage);
                                        storeImage(yourSelectedImage, PICK_IMAGE_BOY);}
                                });
                            }
                        }).start();
                    }
                }
            }
    public static void verifyStoragePermissions(Activity activity) {
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
            if (! mediaStorageDir.mkdirs()){return null;}
        }
        File mediaFile;
        String mImageName= "";
        if (TYPE_IMAGE==1)
        mImageName="GIRL.jpg";
        else if (TYPE_IMAGE==2)  mImageName="BOY.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    public File getPathToImages() {
        return new File(Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + getApplicationContext().getPackageName()
            + "/Files");
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

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(FM_NOTIFICATION_ID, builder.build());
    }

    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(FM_NOTIFICATION_ID);
    }}





