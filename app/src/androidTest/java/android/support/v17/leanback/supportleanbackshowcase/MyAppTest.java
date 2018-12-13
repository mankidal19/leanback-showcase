package android.support.v17.leanback.supportleanbackshowcase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.view.KeyEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Random;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.StaleObjectException;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MyAppTest {

    private static final String PACKAGE_NAME = getApplicationContext().getPackageName();

    private static final String HOME_FRAGMENT = "Home Fragment";

    private static final String LIVE_TV_FRAGMENT = "Live TV Fragment";
    private static final String LIVE_TV_DETAIL_VIEW = "Detail View Live TV";
    private static final String LIVE_TV_PLAYER = "Live TV Player";



    private static final String NEWS_SPORTS_FRAGMENT = "News & Sports Fragment";

    private static final String MOVIES_FRAGMENT = "Movies Fragment";

    private static final String TV_SHOW_FRAGMENT = "TV Show Fragment";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen(){
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);

    }

    @Test
    public void checkPreconditions() {
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void testCurrentOnHome(){
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(HOME_FRAGMENT)), LAUNCH_TIMEOUT);


        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(HOME_FRAGMENT));
    }

    @Test
    public void testRedButton(){
        //wait until home fragment is loaded
        boolean found = mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_RED);
        //UiObject2 uiObject = mDevice.wait(Until.findObject(By.res("container_list")),LAUNCH_TIMEOUT);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(LIVE_TV_FRAGMENT)),LAUNCH_TIMEOUT);

        if(!found){
            Assert.fail("home fragment not found");
        }

        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(LIVE_TV_FRAGMENT));
    }

    @Test
    public void testGreenButton(){
        //wait until home fragment is loaded
        boolean found = mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_GREEN);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(NEWS_SPORTS_FRAGMENT)),LAUNCH_TIMEOUT);

        if(!found){
            Assert.fail("home fragment not found");
        }


        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(NEWS_SPORTS_FRAGMENT));
    }

    @Test
    public void testYellowButton(){
        //wait until home fragment is loaded
        boolean found = mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_YELLOW);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(TV_SHOW_FRAGMENT)),LAUNCH_TIMEOUT);

        if(!found){
            Assert.fail("home fragment not found");
        }


        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(TV_SHOW_FRAGMENT));
    }

    @Test
    public void testBlueButton(){
        //wait until home fragment is loaded
        boolean found = mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_BLUE);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(MOVIES_FRAGMENT)),LAUNCH_TIMEOUT);

        if(!found){
            Assert.fail("home fragment not found");
        }


        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(MOVIES_FRAGMENT));
    }

    @Test
    public void multipleKeys(){
        ArrayList<String> fragmentsList = new ArrayList<String>();
        //wait until home fragment is loaded
        boolean found = mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);

        //for iteration other than first one
        if(!found){
            found = mDevice.wait(Until.hasObject(By.desc(MOVIES_FRAGMENT)),LAUNCH_TIMEOUT);
        }

        UiObject2 uiObject;

        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_RED);
        uiObject = mDevice.wait(Until.findObject(By.desc(LIVE_TV_FRAGMENT)),LAUNCH_TIMEOUT);
        fragmentsList.add(uiObject.getContentDescription());

        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_GREEN);
        uiObject = mDevice.wait(Until.findObject(By.desc(NEWS_SPORTS_FRAGMENT)),LAUNCH_TIMEOUT);
        fragmentsList.add(uiObject.getContentDescription());

        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_YELLOW);
        uiObject = mDevice.wait(Until.findObject(By.desc(TV_SHOW_FRAGMENT)),LAUNCH_TIMEOUT);
        fragmentsList.add(uiObject.getContentDescription());

        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_BLUE);
        uiObject = mDevice.wait(Until.findObject(By.desc(MOVIES_FRAGMENT)),LAUNCH_TIMEOUT);
        fragmentsList.add(uiObject.getContentDescription());



        if(!found){
            Assert.fail("fragment not found");
        }

        assertThat(fragmentsList.get(0),equalTo(LIVE_TV_FRAGMENT));
        assertThat(fragmentsList.get(1),equalTo(NEWS_SPORTS_FRAGMENT));
        assertThat(fragmentsList.get(2),equalTo(TV_SHOW_FRAGMENT));
        assertThat(fragmentsList.get(3),equalTo(MOVIES_FRAGMENT));

    }

    @Test
    public void multipleKeyLoopTest(){
        final int iteration = 50;

        for(int i=1;i<=iteration;i++){
            multipleKeys();
            System.out.println("multipleKeys() test loop #" + i + " out of " + iteration);

        }
    }

    @Test
    public void testChannelsButton() throws StaleObjectException, UiObjectNotFoundException, InterruptedException {
        Random r = new Random();
        //int iteration = r.nextInt(30);
        int iteration = 50;
        boolean found;
        //UiObject2 titleText;
        UiObject titleText;
        String channelPressed;
        String prevChannelTitle;
        String currentChannelTitle = null;


        //wait until home fragment is loaded
        mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_RED);
        mDevice.wait(Until.hasObject(By.desc(LIVE_TV_FRAGMENT)),LAUNCH_TIMEOUT);

        mDevice.pressDPadCenter();
        mDevice.wait(Until.hasObject(By.desc(LIVE_TV_DETAIL_VIEW)),LAUNCH_TIMEOUT);

        mDevice.pressDPadCenter();
        found = mDevice.wait(Until.hasObject(By.desc("channel title")),1000);
        titleText = mDevice.findObject(new UiSelector().description("channel title"));

        prevChannelTitle = titleText.getText();

        //if iteration less than 10, increase by the value
        if(iteration<10){
            iteration+=10;
        }

        for(int i=1;i<=iteration;i++){

            found = mDevice.wait(Until.hasObject(By.res("youTubePlayerDOM")),1000);
            //mDevice.wait(1000);
            if(r.nextBoolean()){
                mDevice.pressKeyCode(KeyEvent.KEYCODE_CHANNEL_UP);
                channelPressed = "UP";
            }
            else{
                mDevice.pressKeyCode(KeyEvent.KEYCODE_CHANNEL_DOWN);
                channelPressed = "DOWN";
            }

            try{
                currentChannelTitle = titleText.getText();
            }
            catch (StaleObjectException e){
                System.err.println("StaleObjectException!");
            }
            catch (UiObjectNotFoundException e){
                System.err.println("UiObjectNotFoundException!");
            }



            System.out.println("Channel Up/Down Random test loop #" + i + " of " + iteration);
            System.out.println("Button pressed: " + channelPressed);
            System.out.println("Previous channel: " + prevChannelTitle);
            System.out.println("Current channel: " + currentChannelTitle + "\n--------------------------------------");


            prevChannelTitle = currentChannelTitle;

            Assert.assertTrue("Youtube player view not found",found);


        }

    }

    @Test
    public void testNewsSportsContents() throws UiObjectNotFoundException{
        testGreenButton();

        //UiScrollable videos = new UiScrollable(new UiSelector()
         //       .descriptionContains(NEWS_SPORTS_FRAGMENT));

        UiScrollable videos = new UiScrollable(new UiSelector()
                .descriptionContains("News"));

        // Retrieve the number of videos in this collection:
        /*int count = videos.getChildCount(new UiSelector()
                .className("android.widget.FrameLayout"));*/
        int count = videos.getChildCount(new UiSelector()
                .className("android.widget.FrameLayout"));



        for(int i=0;i<count;i++){
            UiObject video = videos.getChildByInstance(new UiSelector()
                    .className("android.widget.FrameLayout"),i);

            System.out.println("Click " + video.toString() + " #" + i + " of " + count);

            video.click();
            mDevice.pressBack();

        }

    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
