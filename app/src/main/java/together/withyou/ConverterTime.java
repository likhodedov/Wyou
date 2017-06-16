package together.withyou;

/**
 * Created by d.lihodedov on 15.02.2017.
 */
public class ConverterTime {



    public static int preparedays(int count){
if (count/365>0) return count%365;
       return count;
    }
}
