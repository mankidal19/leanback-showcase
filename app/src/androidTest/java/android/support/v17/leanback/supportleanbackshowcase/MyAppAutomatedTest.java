package android.support.v17.leanback.supportleanbackshowcase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;
import nurulaiman.sony.activity.CheckDeviceActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)

public class MyAppAutomatedTest {

    private static final String PACKAGE_NAME = getApplicationContext().getResources().getString(R.string.content_provider);

    private static final String HOME_FRAGMENT = "Home Fragment";

    private static final String LIVE_TV_FRAGMENT = "Live TV Fragment";
    private static final String LIVE_TV_DETAIL_VIEW = "Detail View Live TV";
    private static final String LIVE_TV_PLAYER = "Live TV Player";



    private static final String NEWS_SPORTS_FRAGMENT = "News & Sports Fragment";

    private static final String MOVIES_FRAGMENT = "Movies Fragment";

    private static final String TV_SHOW_FRAGMENT = "TV Show Fragment";

    private static final String TV_SHOW_CONTENT = "After School Club";

    private static final String TV_SHOW = "TV SHOWS";

    private static final String NAV_MENU = "Navigation menu";

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

        mDevice.wait(Until.hasObject(By.desc(HOME_FRAGMENT)),LAUNCH_TIMEOUT);


    }



    @Test
    public void verifyOperatorAppLaunched(){
        //TC001
        //upon launching, should display HOME fragment

        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(HOME_FRAGMENT)), LAUNCH_TIMEOUT);


        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(HOME_FRAGMENT));
    }

    @Test
    public void testEnterVODDetailsWithNetwork() throws UiObjectNotFoundException {
        //TC005
        //test entering VOD Details Page with network

        //navigate to TV SHOWS
        navigateMenu(TV_SHOW);



    }

    //method to navigate and choose desired menu on left pane
    private void navigateMenu(String menuName) throws UiObjectNotFoundException {

        UiCollection list = new UiCollection(
                new UiSelector().description(NAV_MENU));

        String name = null;

        for (int i=0;i<list.getChildCount();i++){
            UiObject headerName = list.getChild( new UiSelector().index(i))
                    .getChild(new UiSelector().className(android.widget.TextView.class));
            name = headerName.getText();
            if(!name.equals(TV_SHOW)){
                mDevice.pressDPadDown();
            }
            else {
                mDevice.pressDPadCenter();
                break;
            }
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
