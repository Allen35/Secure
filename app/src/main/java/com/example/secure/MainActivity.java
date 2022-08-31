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
    public boolean operationConcluded = false;

    private String pathName;
    public final int EXTERNAL_REQUEST = 138;

    final public MainActivity context = this;

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

    public void FileManager(View v)
    {
        String fullName = v.getResources().getResourceName(v.getId());
        String name = fullName.substring(fullName.lastIndexOf("/") + 1);

        if(name.equals(("FileEncrypt")))
        {
            Intent i = new Intent();
            i.setClass(MainActivity.this, com.example.secure.FileExplorer.MainActivity.class);
            i.putExtra("toEncrypt", true);
            startActivityForResult(i, 0);
        }
        else
        {
            Intent i = new Intent();
            i.setClass(MainActivity.this, com.example.secure.FileExplorer.MainActivity.class);
            i.putExtra("toEncrypt", false);
            startActivityForResult(i, 1);
        }
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
        }
        else if (resultCode == 1)//DECIFRA UN FILE
        {
            final String path = b.getString("SELECTED_FILE");//getStringExtra("SELECTED_FILE");
            toEncrypt = false;
            pathName = path;
            textOperation.setText("Decifratura");
            textFileName.setText(new File(path).getName());
        }
        else if (resultCode == 2)//CIFRA UNA CARTELLA
        {
            final String path = b.getString("SELECTED_FILE");//getStringExtra("SELECTED_FILE");
            toEncrypt = true;
            pathName = path;
            textOperation.setText("Cifratura");
            textFileName.setText(new File(path).getName());
        }
        else if (resultCode == 100)//PASSWORD ERRATA DECIFRATURA
        {
            System.out.println("Resultcode: "+resultCode);
            String message = b.getString("ERROR");
            showPopUp(message);
        }
        else if (resultCode == 101)//TUTTO OK
        {
            System.out.println("Resultcode: "+resultCode);
            String message = b.getString("SUCCESS");
            operationConcluded = true;
        }
    }

    public void process(View v)
    {
        EditText edtText1 = findViewById(R.id.setPass1);
        EditText edtText2 = findViewById(R.id.setPass2);

        String txt1 = edtText1.getText().toString(), txt2 = edtText2.getText().toString();

        operationConcluded = false;

        EventDriver eventDriver = EventDriver.newInstance();
        int result = eventDriver.starter(this, txt1, txt2, pathName, toEncrypt);

        if(result == 0)
            Toast.makeText(getApplicationContext(), "LA PASSWORD RISPETTA LE SPECIFICHE", Toast.LENGTH_SHORT).show();
        else if(result == 1)
            Toast.makeText(getApplicationContext(), "LE PASSWORD INSERITE NON CORRISPONDONO", Toast.LENGTH_SHORT).show();
        else if(result == 2)
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 8 CARATTERI", Toast.LENGTH_SHORT).show();
        else if(result == 3)
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 1 CARATTERE ALFABETICO", Toast.LENGTH_SHORT).show();
        else if(result == 4)
            Toast.makeText(getApplicationContext(), "LA PASSWORD DEVE CONTENERE ALMENO 1 CARATTERE NUMERICO", Toast.LENGTH_SHORT).show();
    }

    public void showPopUp(String message)
    {
        CustomDialog customDialog = new CustomDialog(message);
        customDialog.show(getSupportFragmentManager(), "customDialog");
    }
}
