package nurulaiman.sony;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.media.VideoConsumptionExampleFragment;
import android.support.v4.os.BuildCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VideoPlaybackActivity extends AppCompatActivity {

    public static final String TAG = "VideoExampleActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playback);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.videoFragment, new VideoConsumptionExampleFragment(),
                    VideoConsumptionExampleFragment.TAG);
            ft.commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // This part is necessary to ensure that getIntent returns the latest intent when
        // VideoExampleActivity is started. By default, getIntent() returns the initial intent
        // that was set from another activity that started VideoExampleActivity. However, we need
        // to update this intent when for example, user clicks on another video when the currently
        // playing video is in PIP mode, and a new video needs to be started.
        setIntent(intent);
    }

    public static boolean supportsPictureInPicture(Context context) {
        return BuildCompat.isAtLeastN() &&
                context.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_PICTURE_IN_PICTURE);
    }
}
