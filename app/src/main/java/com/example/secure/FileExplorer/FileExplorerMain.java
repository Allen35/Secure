package com.example.secure.FileExplorer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secure.R;

import java.io.File;
import java.util.LinkedList;

public class FileExplorerMain extends AppCompatActivity /*implements RecycleList.updateCallBack*/ {

    public RecyclerView rList;
    public TextView textPath;
    public LinkedList<Model> array = new LinkedList<>();
    public String dirPath;
    public File dir;
    public RecycleList recycleList;
    private boolean decrypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_explorer_activity_main);

        rList = findViewById(R.id.recycleList);
        textPath = findViewById(R.id.textPath);

        Intent i = getIntent();

        setDirPath();
        if(! i.getBooleanExtra("toEncrypt", true))//If called to decrypt
        {
            dirPath = dirPath + "/Secure/Encrypted/";
            decrypt = true;
        }
        else
            decrypt = false;

        refreshList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if(backCheck(dirPath) || decrypt)
            {
                finish();
                return true;
            }
            else {
                String str = dirPath;
                String remove = str.substring(str.lastIndexOf("/"));
                dirPath = str.substring(0, str.lastIndexOf(remove));

                RefreshListView();
                recycleList.notifyDataSetChanged();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean backCheck(String dir)
    {
        if(dir.equals(String.valueOf(Environment.getExternalStorageDirectory())))
            return true;

        return false;
    }

    private void refreshList()
    {
        RefreshAdapter();
        RefreshListView();
    }

    private void setTextPath()
    {
        textPath.setText(dirPath);
    }

    private void setDirPath()
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            dirPath = String.valueOf(Environment.getExternalStorageDirectory());
    }

    private boolean fileCheck(String path)
    {
        File pathName = new File(path);
        if(pathName.isFile())
            return true;
        return false;
    }

    private void RefreshAdapter()
    {
        recycleList = new RecycleList(array, new RecycleList.Updater() {
            @Override
            public void updateCallBack(String pathName)
            {
                if(fileCheck(dirPath + "/" + pathName))
                {
                    Intent returnIntent = new Intent();
                    if(decrypt)
                    {
                        returnIntent.putExtra("SELECTED_FILE", dirPath + "/" + pathName);
                        setResult(1, returnIntent);
                        finish();
                    }
                    else
                    {
                        returnIntent.putExtra("SELECTED_FILE", dirPath + "/" + pathName);
                        if(new File(dirPath + "/" + pathName).isFile())
                        {
                            System.out.println("Pressione semplice file");
                            setResult(0, returnIntent);
                        }
                        finish();
                    }
                }

                dirPath = dirPath + "/" + pathName;
                RefreshListView();
                recycleList.notifyDataSetChanged();
            }

            @Override
            public void longClick(String pathName)
            {
                Intent returnIntent = new Intent();
                if(!decrypt)
                {
                    returnIntent.putExtra("SELECTED_FILE", dirPath + "/" + pathName);
                    if(new File(dirPath + "/" + pathName).isFile())
                    {
                        System.out.println("Pressione prolungata file");
                        setResult(0, returnIntent);
                    }
                    else
                    {
                        System.out.println("Pressione prolungata cartella");
                        setResult(2, returnIntent);
                    }
                    finish();
                }
            }
        }, this);
        rList.setLayoutManager(new LinearLayoutManager(this));
        rList.setAdapter(recycleList);
    }

    private void setReturnIntent(String pathName)
    {
        Intent data = new Intent();
        data.putExtra("SELECTED_FILE", pathName);
        setResult(RESULT_OK, data);
    }

    private void  RefreshListView()
    {
        setTextPath();
        try
        {
            if(fileCheck(dirPath))
                return;

            dir = new File(dirPath);

            File[] fileList = dir.listFiles();

            array.clear();

            int flLstLength = fileList.length;
            for(int i = 0; i < flLstLength; i++)
            {
                if(fileList[i].isDirectory()) {
                    array.add(new Model(fileList[i], R.drawable.folder_image_directory, true));
                }
                else if(fileList[i].isFile()) {
                    array.add(new Model(fileList[i], R.drawable.file, false));
                }
            }
            RecyclerListOrganize.Organize(array);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}