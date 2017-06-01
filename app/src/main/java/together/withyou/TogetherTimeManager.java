package together.withyou;

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

    Calendar Fixed_day;

    TogetherTimeManager(Calendar date_with_you){
        Fixed_day=date_with_you;   }

    public int daysBetween(){
        Calendar dayOne = (Calendar) Fixed_day.clone(),
                dayTwo=(Calendar.getInstance());

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    public int compareDates(){
        Calendar dayOne = (Calendar) Fixed_day.clone(),
                dayTwo=(Calendar.getInstance());
                return dayOne.compareTo(dayTwo);
    }


}
