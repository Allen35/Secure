package com.example.secure;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.secure", appContext.getPackageName());
        PasswordCheck pswCheck = new PasswordCheck();

        /***
         * case different password
         */
        String psw1 = "password91", psw2 = "password9";
        int result = pswCheck.check(psw1, psw2);

        assertEquals(1, result);

        /***
         * case password with letters and numbers but insufficient length
         */
        psw1 = "pass123"; psw2 = "pass123";
        result = pswCheck.check(psw1, psw2);

        assertEquals(2, result);

        /***
         * case password with letters and numbers but insufficient length
         */
        psw1 = "pass123"; psw2 = "pass123";
        result = pswCheck.check(psw1, psw2);

        assertEquals(2, result);

        /***
         * case password without letters
         */
        psw1 = "01234567"; psw2 = "01234567";
        result = pswCheck.check(psw1, psw2);

        assertEquals(3, result);

        /***
         * case password without numbers
         */
        psw1 = "password"; psw2 = "password";
        result = pswCheck.check(psw1, psw2);

        assertEquals(4, result);

        /***
         * case correct password
         */
        psw1 = "password123"; psw2 = "password123";
        result = pswCheck.check(psw1, psw2);

        assertEquals(0, result);
    }
}
