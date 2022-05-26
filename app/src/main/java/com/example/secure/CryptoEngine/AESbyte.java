package com.example.secure.CryptoEngine;

import com.google.common.io.CountingInputStream;
import com.google.common.io.CountingOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class AESbyte {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(File inputFile, File outputFile, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            doCrypto(cipher, inputFile, outputFile);
            /*CipherInputStream in = new CipherInputStream(new FileInputStream(inputFile), cipher);
            CountingInputStream cis = new CountingInputStream(in);
            System.out.println("Inputfile: "+inputFile.getAbsolutePath());

            FileOutputStream out = new FileOutputStream(outputFile);
            CountingOutputStream cos = new CountingOutputStream(out);

            byte[] buffer = new byte[512];
            double val = inputFile.length()/100;
            int count;
            while((count = cis.read(buffer)) > 0)
            {
                cos.write(buffer, 0, count);
                cos.flush();
                //System.out.println("Percentuale: "+cos.getCount()/val+" %");
            }
            cis.close();
            cos.close();*/
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
    }

    public static void decrypt(File inputFile, File outputFile, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            doCrypto(cipher, inputFile, outputFile);
            /*CipherInputStream in = new CipherInputStream(new FileInputStream(inputFile), cipher);
            CountingInputStream cis = new CountingInputStream(in);

            FileOutputStream out = new FileOutputStream(outputFile);
            CountingOutputStream cos = new CountingOutputStream(out);

            int count;
            double val = (inputFile.length()/100.0);
            byte[] buffer = new byte[512];
            while((count = cis.read(buffer)) != -1)
            {
                cos.write(buffer, 0, count);
                cos.flush();
                //System.out.println("Percentuale: "+cos.getCount()/val+"%");
            }
            cis.close();
            cos.close();*/
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
    }

    private static void doCrypto(Cipher cipher, File inputFile, File outputFile) throws Exception
    {
        CipherInputStream in = new CipherInputStream(new FileInputStream(inputFile), cipher);
        CountingInputStream cis = new CountingInputStream(in);

        FileOutputStream out = new FileOutputStream(outputFile);
        CountingOutputStream cos = new CountingOutputStream(out);

        int count;
        double val = (inputFile.length()/100.0);
        byte[] buffer = new byte[512];
        while((count = cis.read(buffer)) != -1)
        {
            cos.write(buffer, 0, count);
            cos.flush();
            //System.out.println("Percentuale: "+cos.getCount()/val+"%");
        }
        cis.close();
        cos.close();
    }
}