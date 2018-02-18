package together.withyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import together.withyou.Events.EventManager;


@EFragment (R.layout.youselector_fragment)
public class YouSelectorFragment extends Fragment {
    @ViewById
    TextView countDaysTextView;

    @ViewById
    DatePicker datePicker;

    TogetherTimeManager togetherTimeManager;
    @AfterViews
    public void inject() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                togetherTimeManager = new TogetherTimeManager(year, month, dayOfMonth);
                int b = togetherTimeManager.daysBetween();
                countDaysTextView.setText(Integer.toString(b));
            }
        });
    }
}
