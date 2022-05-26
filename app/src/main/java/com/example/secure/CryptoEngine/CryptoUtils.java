package com.example.secure.CryptoEngine;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class CryptoUtils {

    private static int IVsize = 16, saltSize = 18, passwordSize = 20;
    private static int parameterSize = IVsize + saltSize + passwordSize;
    private static int cipherByte = 16;

    public static long cryptedFileSize(int buffer_size, int threads_number,  File inputFile)
    {
        long fileSize = inputFile.length();
        int i = 0;
        if(fileSize % buffer_size == 0)
        {
            return (fileSize + (threads_number *(parameterSize + cipherByte)));
        }
        else
        {
            int last_buffer = (int) (fileSize % buffer_size);
            for(i = 1; i < 16; i++)
            {
                if((last_buffer + i) % 16 == 0)
                    break;
            }
        }
        //System.out.println("i: "+i);
        return (fileSize + i + (threads_number * parameterSize) + ((threads_number - 1) * cipherByte));
    }

    public static String decryptedFileSize(File inputFile)
    {
        RandomAccessFile raf;
        String fileSize = null;
        try {
            raf = new RandomAccessFile(inputFile, "r");
            raf.seek(raf.length() - 21);//21 being max length of added string of a long plus ()
            byte[] b = new byte[21];
            raf.read(b);
            String s = new String(b);
            fileSize = s.substring(s.lastIndexOf("(") + 1, s.lastIndexOf(")"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSize;
    }

    private static String removeStringGarbage(String string)
    {
        String newString = null;
        try{
            newString = string.substring(string.lastIndexOf("(")+1);
        }catch(Exception e) {
            //e.printStackTrace();
            return string;
        }
        return newString;
    }

    private static boolean unclosedParentesis(String string)
    {
        for(int i = 0; i < string.length(); i++)
        {
            if(Character.compare(string.charAt(i), "(".charAt(0)) == 0)
            {
                return true;
            }
        }
        return false;
    }

    public static String[] getFileParameters(File inputFile)
    {
        RandomAccessFile raf;
        String[] placeHolder = null, parameters = null, cleanedParams = null;
        try {
            raf = new RandomAccessFile(inputFile, "r");
            raf.seek(raf.length() - (21 + 12 + 7 + 7));//21 being max length of a long plus () 12 max length of an int plus() and 7 is max length of boolean converted to string plus ()
            byte[] parametersByte = new byte[47];
            raf.read(parametersByte);
            raf.close();

            //System.out.println(new String(parametersByte));
            placeHolder = StringUtils.substringsBetween(new String(parametersByte), "(", ")");
            parameters = Arrays.copyOfRange(placeHolder, placeHolder.length - 4, placeHolder.length);
            if(unclosedParentesis(parameters[0]))
            {
                cleanedParams = parameters;
                cleanedParams[0] = removeStringGarbage(cleanedParams[0]);
                return cleanedParams;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }
}
