package android.support.v17.leanback.supportleanbackshowcase.app.room.controller.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class SearchActivity extends FragmentActivity {

    private static final String TAG = "SearchActivity";
    private static final boolean DEBUG = false;
    private static final boolean USE_INTERNAL_SPEECH_RECOGNIZER = false;
    private static final int REQUEST_SPEECH = 1;

    // The fragment has to be retrived from the id which is the runtime information
    public SearchFragment mFragment;

    public SpeechRecognitionCallback mSpeechRecognitionCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment2);
        if (USE_INTERNAL_SPEECH_RECOGNIZER) {
            mSpeechRecognitionCallback = new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    if (DEBUG) {
                        Log.v(TAG, "recognizeSpeech");
                    }
                    startActivityForResult(mFragment.getRecognizerIntent(), REQUEST_SPEECH);
                }
            };
            mFragment.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (DEBUG){
            Log.v(TAG, "onActivityResult requestCode="
                    + requestCode +
                    " resultCode="
                    + resultCode +
                    " data="
                    + data);
        }
        if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
            mFragment.setSearchQuery(data, true);
        }
    }

    /*@Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        KeyCode = event.getKeyCode();
        Intent intent = new Intent(this, LiveActivity.class);

        Log.i("KeyEvent",KeyCode + " button pressed");



        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Log.i("KeyEvent","Return button pressed");
            handled=true;
            intent = new Intent(this, MainActivity.class);

            onDestroy();
            startActivity(intent);
        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE){
            Log.i("KeyEvent","Exit button pressed");
            handled=true;
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }



        if(handled){


            return handled;
        }

        else{

            return super.onKeyDown(KeyCode, event);
        }
        //return handled;
    }
*/
}
