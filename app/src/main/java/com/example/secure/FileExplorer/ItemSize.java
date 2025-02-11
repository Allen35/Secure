package com.example.secure.FileExplorer;

import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

public class ItemSize extends Thread{

    private TextView textView;
    private Model item;
    private FileExplorerMain mainContext;

    public ItemSize(TextView textView, Model item, FileExplorerMain mainContext)
    {
        this.textView = textView;
        this.item = item;
        this.mainContext = mainContext;
    }

    public void run()
    {
        long folderSize = getFolderSizeTreeMap(new File(item.getItemPath()));
        String size = readableFileSize(folderSize);
        Cache.Instance.gettMap().put(item.getItemPath(), folderSize);

        mainContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(size);
            }
        });
    }

    private String readableFileSize(long size) {
        if(size <= 0)
            return "0 B";

        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private long getFolderSizeTreeMap(File dir)
    {
        if(dir.isFile())
            return dir.length();
        else {
            long length = 0;
            long place;
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        Cache.Instance.getLock().writeLock().lock();
                        Cache.Instance.gettMap().put(file.toString(), file.length());
                        Cache.Instance.getLock().writeLock().unlock();

                        length += file.length();
                    } else {
                        place = getFolderSizeTreeMap(file);
                        Cache.Instance.getLock().writeLock().lock();
                        Cache.Instance.gettMap().put(file.toString(), place);
                        Cache.Instance.getLock().writeLock().unlock();

                        length += place;
                    }
                }
            }
            return length;
        }
    }
}
