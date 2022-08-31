package com.example.secure.CryptoEngine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.secure.R;

public class CryptoMain extends Activity implements EncryptMain.Enable, DecryptMain.Enable {

    private String filePath;

    private ProgressBar progressbar1, progressbar2;
    private TextView textOperation1, textOperation2;
    private Button okButton;

    private String secretKey;

    public static CryptoMain context;

    public CryptoMain() {}

    /*//https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_dialog_layout);

        context = this;

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out);            }
        });

        // Fetch arguments from bundle
        Intent i = getIntent();
        secretKey = i.getStringExtra("key");
        filePath = i.getStringExtra("filePath");

        //IF A FILE IS BEING PROCESSED HIDE THE FIRST COMPRESSION PROGRESSBAR
        if(i.getBooleanExtra("toEncrypt", true)) {
            initializeFieldOperation2("Cifratura");

            if(i.getBooleanExtra("isFolder", true)) {
                initializeFieldOperation1("Compressione");
            } else {
                findViewById(R.id.lin1).setVisibility(View.GONE);
            }
            new EncryptMain(filePath, secretKey, this);
        } else {
            initializeFieldOperation1("Decifratura");
            initializeFieldOperation2("Decompressione");

            new DecryptMain(filePath, secretKey, this);
        }
    }

    public void initializeFieldOperation1(String operation)
    {
        textOperation1 = findViewById(R.id.firstOperation);
        textOperation1.setText(operation);
        progressbar1 = findViewById(R.id.firstOperationBar);
        progressbar1.setProgress(0);
    }

    public void initializeFieldOperation2(String operation)
    {
        textOperation2 = findViewById(R.id.secondOperation);
        textOperation2.setText(operation);
        progressbar2 = findViewById(R.id.secondOperationBar);
        progressbar2.setProgress(0);
    }

    @Override
    public void enableButton()
    {
        okButton.setTextColor(Color.WHITE);
        okButton.setEnabled(true);
    }

    public void forcedTermination(int code)
    {
        Intent i = new Intent();
        i.putExtra("ERROR", "Password non corretta");
        setResult(code, i);
        System.out.println("Forced Termination code: "+code);

        finish();
    }

    public void dismiss(View v)
    {
        int code = 101;

        Intent i = new Intent();
        i.putExtra("SUCCESS", "Everything ok");
        setResult(code, i);
        System.out.println("Forced Termination code: "+code);

        finish();
    }
}