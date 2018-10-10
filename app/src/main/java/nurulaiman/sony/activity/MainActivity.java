package nurulaiman.sony.activity;

import android.app.Activity;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.os.Bundle;

import nurulaiman.sony.fragment.MainBrowseFragment;

public class MainActivity extends Activity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_browse);
        MainBrowseFragment fragment = MainBrowseFragment.newInstance();

        //implement voice interaction
        /*super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }  else if (!isVoiceInteraction()) {
            Log.e(TAG, "Not voice interaction");
            if (intent != null) {
                intent.setComponent(null);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            finish();
            return;
        }

        setContentView(R.layout.fragment_main_browse);*/
        //MainBrowseFragment fragment = MainBrowseFragment.newInstance();
        /*fragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();*/
    }



}
