package com.example.secure.CryptoEngine;

import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.secure.R;

import java.io.File;
import java.io.IOException;

public class EncryptMain implements MultiThreadAES.EncryptCallBack, AsyncZip.CallBack1{

    private FileUtils f = new FileUtils();
    private String StoragePath;
    private String filePath;
    private String fileWithoutExtension;
    private File file;

    private ProgressBar progressbar1, progressbar2;

    private TextView textOperation1, textOperation2;
    private TextView textIndicator1, textIndicator2;

    private String secretKey /*= "boooooooooom!!!!"*/;
    private String fileName;
    private boolean isADirectory, firstTime = true, encrypt = true;

    private Enable CallBack;
    private EncryptMain encryptMain = this;
    private CryptoMain cryptoMain;

    private int buffer_size = 2048*2;
    private int thread_num = 8;

    public interface Enable
    {
        void enableButton();
    }

    public EncryptMain(String filePath, String secretKey, CryptoMain activity)
    {
        this.CallBack = activity;
        this.cryptoMain = activity;

        textOperation1 = activity.findViewById(R.id.firstOperation);
        textIndicator1 = activity.findViewById(R.id.firstIndicator);
        progressbar1 = activity.findViewById(R.id.firstOperationBar);

        textOperation2 = activity.findViewById(R.id.secondOperation);
        textIndicator2 = activity.findViewById(R.id.secondIndicator);
        progressbar2 = activity.findViewById(R.id.secondOperationBar);

        this.filePath = filePath;
        this.secretKey = secretKey;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            StoragePath = String.valueOf(Environment.getExternalStorageDirectory());
        }
        StartEncrypt();
        EncryptDriver();
    }

    private void doEncryptFile(String fileName, String secretKey)
    {
        //System.out.println("FileName: "+fileName+"\n  FilePath: "+filePath);
        new Thread(){
            public void run()
            {
                //new MultiThreadAESMemoryMappedFile().doMultiThreadAES(cryptoMain, encryptMain, progressbarCrypto, 4, 512, new File(filePath), StoragePath + "/Secure/Encrypted/" + fileWithoutExtension + ".aes", secretKey);
                new MultiThreadAES().doMultiThreadAES(thread_num, buffer_size, new File(filePath), StoragePath + "/Secure/Encrypted/" + fileName + ".aes", secretKey, cryptoMain, encryptMain, progressbar2, textIndicator2, true);
            }
        }.start();
    }

    private void doEncryptFolder(String fileName, String secretKey) {
        //System.out.println("FileName: "+fileName+"\n  FilePath: "+filePath);
        //System.out.println("File: "+(StoragePath + "/Secure/Encrypted/Temp/" + fileName));
        new Thread(){
            public void run()
            {
                new MultiThreadAES().doMultiThreadAES(thread_num, buffer_size, new File(StoragePath + "/Secure/Encrypted/Temp/" + fileName), StoragePath + "/Secure/Encrypted/" + fileName + ".aes", secretKey, cryptoMain, encryptMain, progressbar2, textIndicator2, false);
            }
        }.start();
    }

    public void StartEncrypt() {
        file = new File(filePath);

        String[] folders = new String[]{
                StoragePath + "/Secure/",
                StoragePath + "/Secure/Encrypted/",
                StoragePath + "/Secure/Encrypted/Temp"
        };
        FileUtils.CreateFolder(folders);
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(new File(StoragePath + "/Secure/Encrypted/Temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileName = new String(f.getFileName(filePath));
        //System.out.println("String: "+fileName);
    }

    public void EncryptDriver()
    {
        file = new File(filePath);
        isADirectory = file.isDirectory();
        if (isADirectory && firstTime) {//if there is a folder in input must be compressed into a file
            firstTime = false;

            short mode = (short) 2;
            AsyncZip c = new AsyncZip(filePath, StoragePath + "/Secure/Encrypted/Temp/" + fileName + ".zip", mode, progressbar1, textOperation1, textIndicator1, this);
            c.execute();

            return;
        }
        if(encrypt)//now i have always a file to encrypt
        {
            encrypt = false;

            if(isADirectory) {
                fileWithoutExtension = fileName;
                doEncryptFolder(fileName + ".zip", secretKey);
            }else {
                System.out.println("filename: "+fileName);

                try {//caso in cui si cifra un file senza estensioni
                    fileWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
                }catch(Exception e){
                    fileWithoutExtension = fileName;
                }
                doEncryptFile(fileName, secretKey);
            }
            return;
        }

        FileUtils.deleteFiles(StoragePath + "/Secure/Encrypted/Temp");

        cryptoMain.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CallBack.enableButton();
            }
        });
    }
}