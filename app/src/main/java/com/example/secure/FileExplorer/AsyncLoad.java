package com.example.secure.FileExplorer;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AsyncLoad extends AsyncTask<Void, Void, Void> {

    private ImageView image;
    private TextView textView;
    private Model item;
    private TreeMap<String, Long> tMap;
    private ReentrantReadWriteLock lock;

    private int mode;
    private long folderSize;

    //METODO CARICAMENTO IMMAGINI SU MULTITHREAD ALTERNATIVO A GLIDE
    public AsyncLoad(ImageView img, Model t)
    {
        this.image = img;
        this.item = t;

        this.mode = 1;
    }

    public AsyncLoad(TextView txt, Model t)
    {
        this.textView = txt;
        this.item = t;

        this.mode = 2;
    }

    @Override
    protected Void doInBackground(Void... integers)
    {
        if(mode == 1)
        {
            return null;
        }
        else if(mode == 2)
        {
            double start = System.currentTimeMillis();
            folderSize = getFolderSizeTreeMap(new File(item.getItemPath()), tMap, lock);
            //folderSize = folderSizeMTTreeMap(new File(item.getItemPath()));
            double end = System.currentTimeMillis();
            System.out.println("Item: "+item.getItemName()+" Time: "+(end - start)+" ms");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void g)
    {
        if(mode == 1)
            image.setImageResource(item.getImage());
        else if(mode == 2) {
            Cache.Instance.gettMap().put(item.getItemPath(), folderSize);
            textView.setText(readableFileSize(folderSize));
        }
    }

    private String readableFileSize(long size) {
        if(size <= 0)
            return "0 B";

        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private long getFolderSizeTreeMap(File dir, TreeMap<String, Long> tMap, ReentrantReadWriteLock lock)
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
                        place = getFolderSizeTreeMap(file, tMap, lock);
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

    private long size = 0;
    /*public long folderSizeMTTreeMap(File directory) {
        if (directory.isFile())
            return directory.length();

        int pos = 0;
        RecursiveTreeSearch[] t = new RecursiveTreeSearch[numberOfFolders(directory.listFiles())];
        System.out.println("Number of files: "+directory.listFiles().length);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (File file : directory.listFiles())
        {
            if(file.isDirectory() && !file.isFile())
            {
                //System.out.println("Folder Name: "+file.getName());
                t[pos] = new RecursiveTreeSearch(file);
                executor.execute(t[pos]);
                pos++;
            } else {
                Cache.Instance.getLock().writeLock().lock();
                Cache.Instance.gettMap().put(file.toString(), file.length());
                Cache.Instance.getLock().writeLock().unlock();

                size += file.length();
            }
        }

        executor.shutdown();
        while(!executor.isTerminated()) {}

        for(int i = 0; i < numberOfFolders(directory.listFiles()); i++)
        {
            try {
                t[i].join();
                Cache.Instance.getLock().writeLock().lock();
                Cache.Instance.gettMap().put(t[i].getDirectory().toString(), t[i].getFolderSize());
                Cache.Instance.getLock().writeLock().unlock();
                size += t[i].getFolderSize();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    private int numberOfFolders(File[] files)
    {
        int folders = 0;
        for(int i = 0; i < files.length; i++)
        {
            if(files[i].isDirectory())
            {
                //System.out.println("folder: "+files[i].getName());
                folders++;
            }
        }
        //System.out.println("Folders: "+folders);
        return folders;
    }*/
}
