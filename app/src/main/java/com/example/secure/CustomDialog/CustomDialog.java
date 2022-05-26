package com.example.secure.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.secure.R;

public class CustomDialog extends AppCompatDialogFragment {

    private TextView textView;
    private Button okBtn;

    private String message;

    public CustomDialog(String text)
    {
        this.message = text;
    }

    @Override
    public Dialog onCreateDialog(Bundle b)//https://android--examples.blogspot.com/2016/10/android-change-alertdialog-background.html
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_dialog, null);

        okBtn = v.findViewById(R.id.okButton);
        textView = v.findViewById(R.id.textV);
        textView.setText(message);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
                }
        });
        /*TextView tv = new TextView(getContext());//https://stackoverflow.com/questions/51380128/android-how-can-i-change-alertdialog-title-text-color-and-background-color-with
        tv.setText("Login");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(30F);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);*/

        builder.setView(v);
                /*.setCustomTitle(tv)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.setText(edtUsr.getText().toString(), edtPsw.getText().toString());
                    }
                });*/

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}