package com.example.secure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secure.CryptoEngine.CryptoMain;
import com.example.secure.CustomDialog.CustomDialog;

import org.w3c.dom.Text;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private boolean toEncrypt;
    private String pathName;
    public final int EXTERNAL_REQUEST = 138;

    private TextView textOperation,textFileName;

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            return;*/


        ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE/*,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,*/
                }, EXTERNAL_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("permission accepted");
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();

        textOperation = findViewById(R.id.textOperation);
        textFileName = findViewById(R.id.fileName);
    }

    public void FileEncrypt(View v)
    {
        Intent i = new Intent();
        i.setClass(MainActivity.this, com.example.secure.FileExplorer.MainActivity.class);
        i.putExtra("toEncrypt", true);
        startActivityForResult(i, 0);
    }

    public void FileDecrypt(View v)
    {
        Intent i = new Intent();
        i.setClass(MainActivity.this, com.example.secure.FileExplorer.MainActivity.class);
        i.putExtra("toEncrypt", false);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("onActivityResult code: "+resultCode);

        if (data == null)
        {
            System.out.println("Nessun dato passato per intent");
            return;
        }

        Bundle b = data.getExtras();
        //Toast.makeText(getApplicationContext(), "Visto: " + data.getStringExtra("SELECTED_FILE"), Toast.LENGTH_LONG).show();

        if (resultCode == 0)//CIFRA UN FILE
        {
            final String path = b.getString("SELECTED_FILE");//getStringExtra("SELECTED_FILE");
            toEncrypt = true;
            pathName = path;
            textOperation.setText("Cifratura");
            textFileName.setText(new File(path).getName());
            //CallEnrypt(path, false);
        }
        else if (resultCode == 1)//DECIFRA UN FILE
        {
            final String path = b.getString("SELECTED_FILE");//getStringExtra("SELECTED_FILE");
            toEncrypt = false;
            pathName = path;
            textOperation.setText("Decifratura");
            textFileName.setText(new File(path).getName());
            //CallDecrypt(path, false);
        }
        else if (resultCode == 2)//CIFRA UNA CARTELLA
        {
            final String path = b.getString("SELECTED_FILE");//getStringExtra("SELECTED_FILE");
            toEncrypt = true;
            pathName = path;
            textOperation.setText("Cifratura");
            textFileName.setText(new File(path).getName());
            //CallEnrypt(path, true);
        }
        else if (resultCode == 100)//PASSWORD ERRATA
        {
            System.out.println("Resultcode: "+resultCode);
            String message = b.getString("ERROR");
            showPopUp(message);
        }
    }

    public void CallEnrypt(String filepath, boolean isFolder)
    {
        /*FragmentManager fm = getSupportFragmentManager();
        CryptoMain editNameDialogFragment = CryptoMain.newInstance(filepath, true, isFolder);
        editNameDialogFragment.show(fm, "fragment_edit_name");*/
        Intent i = new Intent();
        i.setClass(this, com.example.secure.CryptoEngine.CryptoMain.class);
        i.putExtra("toEncrypt", true);
        i.putExtra("isFolder", isFolder);
        i.putExtra("filePath", filepath);
        startActivity(i);
    }

    public void CallDecrypt(String filepath, boolean isFolder)
    {
        /*FragmentManager fm = getSupportFragmentManager();
        CryptoMain editNameDialogFragment = CryptoMain.newInstance(filepath, false, isFolder);
        editNameDialogFragment.show(fm, "fragment_edit_name");*/
        Intent i = new Intent();
        i.setClass(this, com.example.secure.CryptoEngine.CryptoMain.class);
        i.putExtra("toEncrypt", false);
        i.putExtra("isFolder", isFolder);
        i.putExtra("filePath", filepath);
        startActivityForResult(i, 99);
    }

    public void process(View v)
    {
        EditText edtText1 = findViewById(R.id.setPass1);
        EditText edtText2 = findViewById(R.id.setPass2);

        String txt1 = edtText1.getText().toString(), txt2 = edtText2.getText().toString();
        String numbers = ".*[0-9].*";
        String letters = ".*[A-Za-z].*";

        if(txt1.equals(txt2) && txt1.length() >= 8 && txt1.matches(numbers) && txt1.matches(letters))
        {
            //call cryptoengine main and start execution
            Intent i = new Intent();
            i.setClass(this, com.example.secure.CryptoEngine.CryptoMain.class);
            i.putExtra("toEncrypt", toEncrypt);
            i.putExtra("isFolder", new File(pathName).isDirectory());
            i.putExtra("filePath", pathName);
            i.putExtra("key", txt1);
            startActivityForResult(i, 99);
            Toast.makeText(getApplicationContext(), "LA PASSWORD RISPETTA LE SPECIFICHE", Toast.LENGTH_SHORT).show();
        }
        else if(!txt1.equals(txt2))
            Toast.makeText(getApplicationContext(), "LE PASSWORD INSERITE NON CORRISPONDONO", Toast.LENGTH_SHORT).show();

        else if(txt1.length() < 8)
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 8 CARATTERI", Toast.LENGTH_SHORT).show();

        else if(!txt1.matches(letters))
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 1 CARATTERE ALFABETICO", Toast.LENGTH_SHORT).show();

        else if(!txt1.matches(numbers))
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 1 CARATTERE NUMERICO", Toast.LENGTH_SHORT).show();
    }

    public void showPopUp(String message)
    {
        CustomDialog customDialog = new CustomDialog(message);
        customDialog.show(getSupportFragmentManager(), "customDialog");
    }
}
