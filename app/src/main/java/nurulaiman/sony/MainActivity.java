package nurulaiman.sony;

import android.app.Activity;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_browse);
    }
}
