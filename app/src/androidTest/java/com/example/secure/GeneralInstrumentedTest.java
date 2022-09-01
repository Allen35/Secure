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

    @Test
    public void testCheck1()
    {
        /***
         * different password
         * password doesn't respect nor specification or length
         * file doesn't exist
         */
        String psw1 = "passwor", psw2 = "pass123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/NON-EXISTENT.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(dirPath + "/Secure/Encrypted/" + "/NON-EXISTENT.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck2()
    {
        /***
         * different password
         * password doesn't respect nor format or length
         * file doesn't exist
         */
        String psw1 = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword", psw2 = "pass123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/NON-EXISTANT.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(dirPath + "/Secure/Encrypted/" + "/NON-EXISTENT.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck3()
    {
        /***
         * different password
         * password doesn't respect format
         * file doesn't exist
         */
        String psw1 = "password", psw2 = "pass123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/NON-EXISTENT.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(dirPath + "/Secure/Encrypted/" + "/NON-EXISTENT.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck4()
    {
        /***
         * different password
         * password respect format and length
         * file doesn't exist
         */
        String psw1 = "password123", psw2 = "password";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/NON-EXISTENT.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(dirPath + "/Secure/Encrypted/" + "/NON-EXISTENT.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck5()
    {
        /***
         * equal password
         * password respect format and length
         * file doesn't exist
         */
        String psw1 = "password123", psw2 = "password123";

        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        EventDriver eventDriver = EventDriver.newInstance();
        eventDriver.starter(activityRule.getActivity().context, psw1, psw2, dirPath + "/NON-EXISTENT.png", true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(dirPath + "/Secure/Encrypted/" + "/NON-EXISTENT.png.aes");

        System.out.println("Esiste: " + file.exists());
        System.out.println("Encr path: " + file.toString());
        System.out.println("Path: " + dirPath + "/test.png");
        assertEquals(false, file.exists());// check if the encrypted file exist
    }

    @Test
    public void testCheck6()
    {
        /***
         * equal password
         * password respect format and length
         * file exist
         */
        String psw1 = "password123", psw2 = "password123";

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
        assertEquals(true, file.exists());// check if the encrypted file exist
    }
}