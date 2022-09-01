package com.example.secure;

import static org.junit.Assert.assertEquals;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MultiThreadAESTest {

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
	public void testAES1() {

	    String secretKey = "password123";

	    String baseFile = dirPath + "/test.png";
		String encrFile = dirPath + "/Secure/Encrypted/" + "test.png.aes";
		String decrFile = dirPath + "/Secure/Decrypted/" + "test.png";

	    boolean isFile = true;

		Intent intent = new Intent();
		activityRule.launchActivity(intent);

		System.out.println("Cifratura");

		EventDriver eventDriver = EventDriver.newInstance();
		eventDriver.starter(activityRule.getActivity().context, secretKey, secretKey, baseFile, true);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		System.out.println("Decifratura");

		EventDriver eventDriver2 = EventDriver.newInstance();
		eventDriver2.starter(activityRule.getActivity().context, secretKey, secretKey, encrFile, false);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String hash1 = new ToHash().hash(baseFile);
		String hash2 = new ToHash().hash(decrFile);
		
		System.out.println("Hash: " + hash1);
		System.out.println("Hash: " + hash2);

		assertEquals(true, hash1.equals(hash2));
	}

}
