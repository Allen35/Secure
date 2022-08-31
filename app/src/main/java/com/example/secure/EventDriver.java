package com.example.secure;

import android.content.Intent;
import android.widget.Toast;

import java.io.File;

public class EventDriver {

    private static EventDriver self = null;

    private EventDriver() {}

    public static EventDriver newInstance()
    {
        if(self == null)
        {
            self = new EventDriver();
        }

        return self;
    }

    //CONTROLLA SE FUNZIONA E SE QUANDO RITORNa IL RESULTCODE LO RITORNA IN MAINACTIVITY O QUA
    public int starter(MainActivity context, String txt1, String txt2, String pathName, boolean toEncrypt)
    {
        PasswordCheck pswCheck = new PasswordCheck();
        int result = pswCheck.check(txt1, txt2);

        File file = new File(pathName);

        if(!file.isFile() && !file.isDirectory() && !file.exists())
            return 100;

        else if(result == 0)
        {
            //call cryptoengine main and start execution
            Intent i = new Intent();
            i.setClass(context, com.example.secure.CryptoEngine.CryptoMain.class);
            i.putExtra("toEncrypt", toEncrypt);
            i.putExtra("isFolder", new File(pathName).isDirectory());
            i.putExtra("filePath", pathName);
            i.putExtra("key", txt1);
            context.startActivityForResult(i, 99);

            return 0;//Password ok
        }
        else
            return result;//problems with password
    }
}