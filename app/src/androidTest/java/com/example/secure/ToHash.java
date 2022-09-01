package com.example.secure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ToHash {

	private String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
	  FileInputStream fis = new FileInputStream(file);
	   
	  byte[] byteArray = new byte[1024];
	  int bytesCount = 0; 
	    
	  while ((bytesCount = fis.read(byteArray)) != -1) {
	    digest.update(byteArray, 0, bytesCount);
	  };
	   
	  fis.close();
	   
	  //Get the hash's bytes
	  byte[] bytes = digest.digest();

	  StringBuilder sb = new StringBuilder();
	  for(int i=0; i< bytes.length ;i++)
	  {
	    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	  }
	   
	  //return complete hash
	   return sb.toString();
	}
	
	public String hash(String fileName)
	{
		//Use MD5 algorithm
		MessageDigest SHADigest;
		try {
			SHADigest = MessageDigest.getInstance("SHA-512");

			//Get the checksum
			String checksum = getFileChecksum(SHADigest, new File(fileName));
			 
			return checksum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		 
	}
}