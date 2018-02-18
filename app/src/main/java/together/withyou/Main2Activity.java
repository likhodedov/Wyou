package together.withyou;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main2  )
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Person person = new Person();

        YouSelectorFragment youSelectorFragment = new YouSelectorFragment_();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.lin,youSelectorFragment);
        fragmentTransaction.commit();
       // fragmentTransaction.addToBackStack("fr1");
    }
}
