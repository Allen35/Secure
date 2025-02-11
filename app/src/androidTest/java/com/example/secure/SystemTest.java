package com.example.secure;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.example.secure.FileExplorer.FileExplorerMain;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SystemTest {

    private String stringToBeTyped = "Password99";
    private String fileToEncrypt = "Vvv.pdf";
    private String fileToDecrypt = "Vvv.pdf.aes";

    private String folder = "Download";

    @Rule
    public ActivityScenarioRule<MainActivity> MainActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Rule
    public ActivityScenarioRule<FileExplorerMain> RecycleRule = new ActivityScenarioRule<>(
            FileExplorerMain.class);

    @Test
    public void encryptionTest()
    {
        //Flow Home
        onView(withId(R.id.setPass1)).perform(typeText(stringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.setPass2)).perform(typeText(stringToBeTyped), closeSoftKeyboard());

        onView(withId(R.id.FileEncrypt)).perform(click());

        //Flow File Manager
        //Scroll to Download folder, click on it then click on the indicated file
        onView(withId(R.id.recycleList)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Download"))));
        onView(withText(folder)).perform(click());

        onView(withText(fileToEncrypt)).perform(click());
        //onView(withId(R.id.recycleList)).perform(RecyclerViewActions.actionOnItemAtPosition(13, click())); //scroll and click the 13 position

        //Flow return Home
        onView(withId(R.id.executeBtn)).perform(click());
    }

    @Test
    public void decryptionTest()
    {
        //Flow Home
        onView(withId(R.id.setPass1)).perform(typeText(stringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.setPass2)).perform(typeText(stringToBeTyped), closeSoftKeyboard());

        onView(withId(R.id.FileDecrypt)).perform(click());

        //Flow File Manager
        //Click on the file to decrypt
        onView(withText(fileToDecrypt)).perform(click());

        //Flow return Home
        onView(withId(R.id.executeBtn)).perform(click());
    }
}