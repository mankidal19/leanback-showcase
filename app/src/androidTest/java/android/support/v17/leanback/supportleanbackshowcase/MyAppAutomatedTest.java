package android.support.v17.leanback.supportleanbackshowcase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
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
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
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

    private static final String NEWS_SPORTS = "NEWS & SPORTS";

    private static final String NEWS_SPORTS_CONTENT = "Best Shot";

    private static final String MOVIES_FRAGMENT = "Movies Fragment";

    private static final String TV_SHOW_FRAGMENT = "TV Show Fragment";

    private static final String TV_SHOW_CONTENT = "After School Club";

    private static final String TV_SHOW = "TV SHOWS";

    private static final String LIVE_TV = "LIVE TV CHANNELS";

    private static final String LIVE_TV_CONTENT = "Arirang TV World";

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
    public void verifyOperatorAppLaunched() throws InterruptedException {
        //TC001
        //upon launching, should display HOME fragment

        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(HOME_FRAGMENT)), LAUNCH_TIMEOUT);
        sleep(20000);

        assertThat(uiObject,notNullValue());
        assertThat(uiObject.getContentDescription(),equalTo(HOME_FRAGMENT));
    }

    @Test
    public void testEnterVODDetailsWithNetwork() throws UiObjectNotFoundException, InterruptedException {
        //TC005
        //test entering VOD Details Page with network

        String title = null;

        //navigate to TV SHOWS
        navigateMenu(TV_SHOW);

        //wait until TV SHOW fragment loaded
        mDevice.wait(Until.hasObject(By.desc(TV_SHOW_FRAGMENT)),LAUNCH_TIMEOUT);

        //select the content
        findInGrid(TV_SHOW_CONTENT);

        boolean loadDetailsPage = mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME,"details_root")),LAUNCH_TIMEOUT);

        title = mDevice.findObject(By.res(PACKAGE_NAME,"primary_text")).getText();

        if(title!=null){
            title = title.toLowerCase();
        }

        sleep(20000);


        assertTrue("details fragment not loaded!",loadDetailsPage);

        assertThat("incorrect details fragment loaded", title, containsString(TV_SHOW_CONTENT.toLowerCase()));
    }

    @Test
    public void testEnterLiveDetailsWithNetwork() throws UiObjectNotFoundException {
        //TC007
        //test entering Live Details Page with network

        String title = null;

        //navigate to TV SHOWS
        navigateMenu(LIVE_TV);

        //wait until TV SHOW fragment loaded
        mDevice.wait(Until.hasObject(By.desc(LIVE_TV_FRAGMENT)),LAUNCH_TIMEOUT);

        //select the content
        findInGrid(LIVE_TV_CONTENT);

        boolean loadDetailsPage = mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME,"details_root")),LAUNCH_TIMEOUT);

        title = mDevice.findObject(By.res(PACKAGE_NAME,"primary_text")).getText();

        if(title!=null){
            title = title.toLowerCase();
        }



        assertTrue("details fragment not loaded!",loadDetailsPage);

        assertThat("incorrect details fragment loaded", title, containsString(LIVE_TV_CONTENT.toLowerCase()));
    }

    @Test
    public void testRGYBonMainBrowser() throws InterruptedException {
        //TC0013, Red/Green/Yellow/Blue buttons on remote should act as shortcut in main browser UI
        testRedButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);
        sleep(20000);
        testGreenButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);
        sleep(20000);
        testYellowButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);
        sleep(20000);
        testBlueButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);
        sleep(20000);

    }

    @Test
    public void testRGYBonDetailsPage() throws UiObjectNotFoundException, InterruptedException {
        //TC0014, Red/Green/Yellow/Blue buttons on remote should act as shortcut in details page UI

        navigateMenu(NEWS_SPORTS);
        findInGrid(NEWS_SPORTS_CONTENT);
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);
        testRedButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);

        mDevice.pressDPadLeft();

        navigateMenu(NEWS_SPORTS);
        findInGrid(NEWS_SPORTS_CONTENT);

        testGreenButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);

        mDevice.pressDPadLeft();

        navigateMenu(NEWS_SPORTS);
        findInGrid(NEWS_SPORTS_CONTENT);

        testYellowButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);

        mDevice.pressDPadLeft();

        navigateMenu(NEWS_SPORTS);
        findInGrid(NEWS_SPORTS_CONTENT);

        testBlueButton();
        mDevice.waitForWindowUpdate(PACKAGE_NAME,500);

        mDevice.pressDPadLeft();
    }

    @Test
    public void testRedButton() throws InterruptedException {
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_RED);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(LIVE_TV_FRAGMENT)),LAUNCH_TIMEOUT);

        assertThat("Live TV fragment is not loaded!", uiObject,notNullValue());
        assertThat("Incorrect fragment obtained!", uiObject.getContentDescription(),equalTo(LIVE_TV_FRAGMENT));
    }

    @Test
    public void testGreenButton() throws InterruptedException {

        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_GREEN);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(NEWS_SPORTS_FRAGMENT)),LAUNCH_TIMEOUT);


        assertThat("News & Sports fragment is not loaded!",uiObject,notNullValue());
        assertThat("Incorrect fragment obtained!", uiObject.getContentDescription(),equalTo(NEWS_SPORTS_FRAGMENT));
    }

    @Test
    public void testYellowButton() throws InterruptedException {
        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_YELLOW);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(TV_SHOW_FRAGMENT)),LAUNCH_TIMEOUT);


        assertThat("TV Shows fragment is not loaded!", uiObject,notNullValue());
        assertThat("Incorrect fragment obtained!", uiObject.getContentDescription(),equalTo(TV_SHOW_FRAGMENT));
    }

    @Test
    public void testBlueButton() throws InterruptedException {

        //press the key
        mDevice.pressKeyCode(KeyEvent.KEYCODE_PROG_BLUE);
        UiObject2 uiObject = mDevice.wait(Until.findObject(By.desc(MOVIES_FRAGMENT)),LAUNCH_TIMEOUT);

        assertThat("Movies fragment is not loaded!", uiObject,notNullValue());
        assertThat("Incorrect fragment obtained!", uiObject.getContentDescription(),equalTo(MOVIES_FRAGMENT));
    }

    //method to navigate and choose desired menu on left pane
    private void navigateMenu(String menuName) throws UiObjectNotFoundException {

        UiCollection list = new UiCollection(
                new UiSelector().description(NAV_MENU));

        String name = null;

        UiObject current = null;
        UiObject previous = null;

        String currentName = null;
        String previousName = null;

        boolean endReached = false;

        /*for (int i=0;i<list.getChildCount();i++){
            UiObject headerName = list.getChild( new UiSelector().index(i))
                    .getChild(new UiSelector().className(android.widget.TextView.class));
            name = headerName.getText();
            if(!name.equals(menuName)){
                mDevice.pressDPadDown();
            }
            else {
                mDevice.pressDPadCenter();
                break;
            }
        }*/

        do{
            current = mDevice.findObject(new UiSelector().focused(true)
                    .childSelector(new UiSelector().className(android.widget.TextView.class)));
            currentName = current.getText();

            if(currentName.equals(menuName)){
                mDevice.pressDPadCenter();
            }

            else if(previous!=null){
                if(!previousName.equals(currentName)){
                    if(!endReached){
                        mDevice.pressDPadDown();
                    }
                    else{
                        mDevice.pressDPadUp();
                    }

                }
                else {
                    endReached = true;
                }
            }

            else {
                mDevice.pressDPadDown();
            }

            previous = current;
            previousName = currentName;

        }while(!currentName.equals(menuName));
    }


    private void findInGrid(String showName) throws UiObjectNotFoundException {

        UiSelector uiSelector = new UiSelector().className(android.widget.FrameLayout.class)
                .childSelector(new UiSelector().className(android.widget.TextView.class)
                        .text(showName));

        boolean isMovingRight = true;
        UiObject previous = null;
        UiObject current;

        boolean isEndReached = false;

        String targetShow = null;
        String previousTitle = null;
        String currentTitle = null;

        do{

            current = mDevice.findObject(new UiSelector().className(android.widget.FrameLayout.class).focused(true).
                    childSelector(new UiSelector().className(android.widget.TextView.class)));
            currentTitle = current.getText();
            if(mDevice.findObject(uiSelector).exists()){
                targetShow = mDevice.findObject(uiSelector).getText();
            }

            System.out.println("current title: " + current.getText());

            if(previous!=null){
                System.out.println("previous title: " + previousTitle);

                if(currentTitle.equals(previousTitle)){
                    mDevice.pressDPadDown();
                    mDevice.waitForWindowUpdate(null,500);

                    if(mDevice.hasObject(By.desc(LIVE_TV_FRAGMENT))){
                        isMovingRight = !isMovingRight;
                    }

                    current = mDevice.findObject(new UiSelector().className(android.widget.FrameLayout.class).focused(true).
                            childSelector(new UiSelector().className(android.widget.TextView.class)));
                    currentTitle = current.getText();

                    if(currentTitle.equals(previousTitle)){
                        if (isEndReached) {
                            throw new RuntimeException("Show was not found!");
                        } else {
                            isEndReached = true;
                        }
                    }
                }
            }

            previous = current;
            previousTitle = previous.getText();

            if(!currentTitle.equals(targetShow)){
                moveFocus(isMovingRight);
            }


            System.out.println("current: " + current.getText() + ", target: " + targetShow);
        }while(!currentTitle.equals(targetShow));

        mDevice.pressDPadCenter();

    }

    private void moveFocus(boolean movingRight){
        if(movingRight){
            mDevice.pressDPadRight();
        }
        else
        {
            mDevice.pressDPadLeft();
        }
        mDevice.waitForWindowUpdate(null,500);
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
