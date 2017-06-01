package together.withyou;

/**
 * Created by d.lihodedov on 15.02.2017.
 */
public class ConverterTime {



    public static int preparedays(int count){
if (count/100>0) return count%100;
        else if (count/1000>0) return count%1000;
            else if (count/10000>0) return count%10000;
        return count;
    }
}
