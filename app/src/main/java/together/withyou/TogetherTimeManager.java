package together.withyou;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by d.lihodedov on 26.12.2016.
 */
public class TogetherTimeManager  {
    private final Calendar Fixed_day;

    TogetherTimeManager(Calendar date_with_you){
        Fixed_day=date_with_you;   }

    TogetherTimeManager(int year, int month, int dayOfMonth){
        String sday = Integer.toString(dayOfMonth).length()==1?
                "0" + Integer.toString(dayOfMonth):Integer.toString(dayOfMonth);
        String smonth = Integer.toString(month+1).length()==1?
                "0" + Integer.toString(month+1):Integer.toString(month+1);
        String s = Integer.toString(year)+smonth+sday;
        String pattern = "yyyyMMdd";
        Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Fixed_day = Calendar.getInstance();
        Fixed_day.setTime(date);
    }

        public int daysBetween(){
        Calendar dayOne = (Calendar) Fixed_day.clone(),
        dayTwo=Calendar.getInstance();
        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        }
        else {
            int extraDays = 0;
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;}

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);
            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                    dayOne.add(Calendar.YEAR, -1);
                    extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);}
            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    public int compareDates(){
        Calendar dayOne = (Calendar) Fixed_day.clone(),
                dayTwo = (Calendar.getInstance());
                return dayOne.compareTo(dayTwo);
    }
    public int GetCountMonthsBetween(){

        int monthdiffer;
        Calendar today = Calendar.getInstance();
            if (((Fixed_day.get(Calendar.DAY_OF_MONTH))- today.get(Calendar.DAY_OF_MONTH))==1) {
                int yearsInBetween = today.get(Calendar.YEAR)- Fixed_day.get(Calendar.YEAR);
                monthdiffer = yearsInBetween*12 + (today.get(Calendar.MONTH) - Fixed_day.get(Calendar.MONTH));
                Log.d("TAG","MONTH between "+monthdiffer);
            }
            else monthdiffer=0;
       return monthdiffer;
    }



}
