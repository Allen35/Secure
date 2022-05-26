package com.example.secure.CryptoEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class FileUtils {

    void writeName(String name, File file, boolean isDir)
    { 
        try { 
  
            // Initialize a pointer 
            // in file using OutputStream 
            OutputStream  os = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            // Starts writing the bytes in the file 
            bw.write(name);
            bw.newLine();
            if(isDir)
            	bw.write("q");
            else
				bw.write("a");

            System.out.println("Successfully byte inserted");
  
            // Close the file 
            bw.close();
        } 
  
        catch (Exception e) { 
            System.out.println("Exception: " + e); 
        } 
    }
    
	String[] readName(File file)
	{
		Scanner fis;
		// Creating a byte array using the length of the file
		// file.length returns long which is cast to int
		String[] sArray = new String[2];
		try
		{
			fis = new Scanner(file);
		    sArray[0] = fis.nextLine();
			sArray[1] = fis.nextLine();
		    fis.close();
		}catch(IOException ioExp)
		{
			ioExp.printStackTrace();
		}
		return sArray;
	}
	
	public byte[] getFileName(String absolutePath)
	{
		return absolutePath.substring(absolutePath.lastIndexOf("/")+1, absolutePath.length()).getBytes();
	}

	public static void deleteFiles(String path) {
		File file = new File(path);
		if (file.exists())
		{
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void CreateFolder(String[] folders)
	{
		int size = folders.length;
		for(int i = 0; i < size; i++)
		{
			File directory = new File(folders[i]);
			if (!directory.exists())
				directory.mkdir();
		}
	}
}
