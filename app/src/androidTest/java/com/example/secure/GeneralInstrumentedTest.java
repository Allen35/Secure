package com.example.secure;

import static org.junit.Assert.assertEquals;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GeneralInstrumentedTest {

    String dirPath = String.valueOf(Environment.getExternalStorageDirectory());

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    }

    //@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.secure", appContext.getPackageName());
    }

    @Test
    public void testCheck1()
    {
        /***
         * case different password
         * file exist
         */
        String psw1 = "password91", psw2 = "password9";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck2()
    {
        /***
         * case password with letters and numbers but insufficient length
         * file exist
         */
        String psw1 = "pass123", psw2 = "pass123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck3()
    {
        /***
         * case password excessive length
         * file exist
         */
        String psw1 = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword", psw2 = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck4()
    {
        /***
         * case password without numbers
         * file exist
         */
        String psw1 = "password", psw2 = "password";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck5()
    {
        /***
         * case password without letters
         * file exist
         */
        String psw1 = "01234567", psw2 = "01234567";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck6()
    {
        /***
         * case correct password
         * file doesn't exist
         */
        String psw1 = "password123", psw2 = "password123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/tes.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck7()
    {
        String psw1 = "password123", psw2 = "password123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/test.png", true);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*while(activityRule.getActivity().operationConcluded == false) {}*/

        File file = new File(dirPath + "/Secure/Encrypted/" + "test.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(true, file.exists());// check if the encrypted file exist
    }
}