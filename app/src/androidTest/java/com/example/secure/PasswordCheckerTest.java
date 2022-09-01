package com.example.secure;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PasswordCheckerTest {

	@Test
	public void testCheck1()
	{
		PasswordCheck pswCheck = new PasswordCheck();
		
		/***
		 * case different password
		 */
		String psw1 = "password91", psw2 = "password9";
		int result = pswCheck.check(psw1, psw2);
		 
		assertEquals(1, result);
		
	}
	
	@Test
	public void testCheck2()
	{
		PasswordCheck pswCheck = new PasswordCheck();

		/***
		 * case password with letters and numbers but insufficient length
		 */
		String psw1 = "pass123", psw2 = "pass123";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(2, result);
	}

	@Test
	public void testCheck3()
	{
		PasswordCheck pswCheck = new PasswordCheck();

		/***
		 * case password excessive length
		 */
		String psw1 = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword", psw2 = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(3, result);
	}
	
	@Test
	public void testCheck4()
	{
		PasswordCheck pswCheck = new PasswordCheck();

		/***
		 * case password without numbers
		 */
		String psw1 = "password", psw2 = "password";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(5, result);
	}
	
	@Test
	public void testCheck5()
	{
		PasswordCheck pswCheck = new PasswordCheck();

		/***
		 * case password without letters
		 */
		String psw1 = "01234567", psw2 = "01234567";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(4, result);
	}
	
	@Test
	public void testCheck6()
	{
		PasswordCheck pswCheck = new PasswordCheck();

		/***
		 * case password without letters
		 */
		String psw1 = "PASSWORD", psw2 = "PASSWORD";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(6, result);
	}

	@Test
	public void testCheck7()
	{
		PasswordCheck pswCheck = new PasswordCheck();
		
		/***
		 * case correct password
		 */
		String psw1 = "Password123", psw2 = "Password123";
		int result = pswCheck.check(psw1, psw2);
		
		assertEquals(0, result);
	}
}
