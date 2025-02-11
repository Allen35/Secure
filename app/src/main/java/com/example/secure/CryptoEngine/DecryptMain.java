package com.example.secure.CryptoEngine;

import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.secure.R;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class DecryptMain implements AsyncZip.CallBack2, MultiThreadAES.DecryptCallBack{

    private Enable CryptoActivity;
    private CryptoEngineMain cryptoMain;

    private FileUtils f = new FileUtils();
    private String StoragePath;
    private String filePath;
    private String fileWithoutExtension;
    private String fileName;
    private String[] originalName;

    private ProgressBar progressbar1, progressbar2;
    private TextView textOperation1, textOperation2;
    private TextView textIndicator1, textIndicator2;

    private String secretKey /*= "boooooooooom!!!!"*/;
    private boolean decrypt = true, lastUnzip = true;

    private int buffer_size = 2048*2;

    static boolean unzip;

    interface Enable
    {
        void enableButton();
    }

    public DecryptMain(String filePath, String secretKey, CryptoEngineMain activity)
    {
        this.CryptoActivity = activity;
        this.cryptoMain = activity;

        textOperation1 = activity.findViewById(R.id.firstOperation);
        textOperation2 = activity.findViewById(R.id.secondOperation);
        textIndicator1 = activity.findViewById(R.id.firstIndicator);
        textIndicator2 = activity.findViewById(R.id.secondIndicator);
        progressbar1 = activity.findViewById(R.id.firstOperationBar);
        progressbar2 = activity.findViewById(R.id.secondOperationBar);

        this.filePath = filePath;
        this.secretKey = secretKey;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            StoragePath = String.valueOf(Environment.getExternalStorageDirectory());
        }
        StartDecrypt();
        DecryptDriver();
    }

    public void StartDecrypt()
    {
        String[] folders = new String[]{
                StoragePath + "/Secure/Decrypted/",
                StoragePath + "/Secure/Decrypted/Temp/"
        };
        FileUtils.CreateFolder(folders);
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(new File(StoragePath + "/Secure/Decrypted/Temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DecryptDriver()
    {
        if(decrypt)
        {
            decrypt = false;

            String destDir = StoragePath + "/Secure/Decrypted/Temp";
            fileName = new String(new FileUtils().getFileName(filePath));
            File destPath = new File(StoragePath+"/Secure/Decrypted/Temp/"+ StringUtils.left(fileName, fileName.length() - 4));
            System.out.println("Destpath: "+destPath);

            DecryptMain activity = this;
            new Thread(){
                public void run()
                {
                    new MultiThreadAES().AesDecryption(secretKey, new File(filePath), destPath, buffer_size, cryptoMain, activity, progressbar1, textIndicator1);
                }
            }.start();
            return;
        }
        else if(unzip && lastUnzip)//caso in cui è una cartella
        {
            lastUnzip = false;
            System.out.println("Cartella rilevata");
            //System.out.println("OriginalName: " + StringUtils.left(fileName, fileName.length() - 8));

            short mode = (short) 3;
            AsyncZip c = new AsyncZip(StoragePath+"/Secure/Decrypted/Temp/"+ StringUtils.left(fileName, fileName.length() - 4), StoragePath+"/Secure/Decrypted/", mode, progressbar2, textOperation2, textIndicator2, this);
            c.execute();
            return;
        }
        else if(! unzip)//caso in cui è un file
        {
            File from = new File(StoragePath+"/Secure/Decrypted/Temp/"+ StringUtils.left(fileName, fileName.length() - 4));
            File to = new File(StoragePath+"/Secure/Decrypted/"+ StringUtils.left(fileName, fileName.length() - 4));

            /*try {
                Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Directory moved successfully.");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }*/
            from.renameTo(to);

            progressbar2.setProgress(100);
            cryptoMain.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textIndicator2.setText("100%");
                }
            });
        }
        FileUtils.deleteFiles(StoragePath+"/Secure/Decrypted/Temp/");

        cryptoMain.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CryptoActivity.enableButton();
            }
        });
        return;
    }
}
